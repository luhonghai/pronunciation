package com.cmg.vrc.dictionary;


import com.cmg.vrc.service.MailService;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.mail.MessagingException;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luhonghai on 9/19/14.
 */
public class OxfordDictionaryWalker extends DictionaryWalker {

    private static final String ENCODE = "UTF-8";

    private Language language = Language.ENGLISH;

    private Gson gson;

    private static final String OXFORD_DICTIONARIES_ROOT = "http://www.oxforddictionaries.com/definition/";

    public OxfordDictionaryWalker(File targetDir) {
        super(targetDir);
        if (!targetDir.exists() || !targetDir.isDirectory()) {
            targetDir.mkdirs();
        }
        gson = new Gson();
    }

    public static DictionaryItem getExistingDictionary(String word) {
        Gson gson = new Gson();
        File wordData = new File(new File(FileHelper.getTmpSphinx4DataDir(), "dictionary"), word + ".json");
        if (wordData.exists()) {
            try {
                return gson.fromJson(FileUtils.readFileToString(wordData, "UTF-8"), DictionaryItem.class);
            } catch (Exception ex) {

            }
        }
        return null;
    }

    @Override
    public void execute(String word) {
        File wordData = new File(getTargetDir(), word +".json");
        if (wordData.exists()) {
            try {
                onDetectWord(gson.fromJson(FileUtils.readFileToString(wordData, "UTF-8"), DictionaryItem.class));
                return;
            } catch (Exception ex) {

            }
        }

        DictionaryItem item = new DictionaryItem(word);
        String url = "";
        try {
            url = OXFORD_DICTIONARIES_ROOT + language + "/" + URLEncoder.encode(word, ENCODE);
        } catch (UnsupportedEncodingException e) {
            url = OXFORD_DICTIONARIES_ROOT + language + "/" + word;
           // logger.log(Level.WARNING, "Could not encode URL", e);
        }
        File tmpSource = new File(FileUtils.getTempDirectory(), UUIDGenerator.generateUUID());
        try {
           // logger.log(Level.INFO, "Fetch word: " + word + ". URL: " + url);

            FileUtils.copyURLToFile(new URL(url), tmpSource);
            Document doc = Jsoup.parse(tmpSource, ENCODE);
            Element title = doc.select(".pageTitle").first();
            String mTitle = "";
            if (title != null && title.hasText() && (mTitle = title.text()).toLowerCase().contains(word.toLowerCase())) {
                // Find pronunciation
                try {
                    String pron = doc.select(".headpron").first().text();
                    Matcher matcher = Pattern.compile("/(.+?)/").matcher(pron);
                    if (matcher.find()) {
                        String tp = matcher.group(1).trim();
                        tp = tp.replace("\n"," ");
                        tp = tp.replace("\t", " ");
                        while (tp.contains("  ")) {
                            tp.replace("  ", " ");
                        }
                        tp = tp.trim();
                        if (tp.contains(","));
                        tp = tp.split(",")[0].trim();
                        item.setPronunciation(tp);
                    }
                } catch (Exception e) {

                }
                if (isFetchAudio()) {
                    // Find line breaks
                    //item.setLineBreaks(doc.select("span.linebreaks").first().text().trim());
                    // Find audio sound url. Type mp3
                    try {
                        String audioUrl = doc.select(".audio_play_button").first().attr("data-src-mp3");
                        if (audioUrl.length() > 0 && audioUrl.endsWith(".mp3")) {
                            item.setAudioUrl(audioUrl);
                            if (!getTargetDir().exists() || !getTargetDir().isDirectory()) {
                                getTargetDir().mkdirs();
                            }
                            File saveFile = new File(getTargetDir(), word + ".mp3");

                            if (saveFile.exists()) {

                            } else {
                              //  logger.info("Download URL " + item.getAudioUrl() + " to file: " + saveFile);
                                FileUtils.copyURLToFile(new URL(item.getAudioUrl()), saveFile);

                            }
                            item.setAudioFile(saveFile.getAbsolutePath());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                FileUtils.write(new File(getTargetDir(), word + ".json"), gson.toJson(item), "UTF-8");
                onDetectWord(item);
            } else {
                onWordNotFound(item,
                        new FileNotFoundException(
                                "Word title not matched. Found: "
                                        + mTitle
                                        + ". Actual: "
                                        + word));
            }

        } catch (FileNotFoundException fnex) {
            onWordNotFound(item, fnex);
        } catch (IOException e) {
            onError(item, "Could not download URL: " + url, e);
        } catch (Exception e) {
            onError(item, "Could not fetch word: " + url, e);
        } finally {
            try {
                if (tmpSource.exists())
                    FileUtils.forceDelete(tmpSource);
            } catch (IOException e) {
                //logger.log(Level.WARNING, "Could not delete temp file " + tmpSource, e);
            }
        }
    }

    public static void generateDictionary() throws IOException, MessagingException {
        File targetDir = new File(FileHelper.getTmpSphinx4DataDir(), "dictionary");
        DictionaryWalker walker = new OxfordDictionaryWalker(targetDir);
        walker.setFetchAudio(false);
        final File wordXml = new File(targetDir,"words.xml");
        if (wordXml.exists()) {
            FileUtils.forceDelete(wordXml);
        }
        FileUtils.writeStringToFile(wordXml, "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "\t<string-array name=\"words_list\">", true);
        walker.setListener(new DictionaryListener() {
            @Override
            public void onDetectWord(DictionaryItem item) {
                System.out.println("Write word " + item.getWord() + " to list");
                try {
                    FileUtils.writeStringToFile(wordXml, "\n\t\t<item>" + item.getWord() + "|" + item.getPronunciation() + "</item>", true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onWordNotFound(DictionaryItem item, FileNotFoundException ex) {
                System.out.println("ERROR: word " + item.getWord() + " not found");
            }

            @Override
            public void onError(DictionaryItem item, Exception ex) {
                System.out.println("ERROR: Could not fetch word " + item.getWord() + ". Message: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        File fWords = new File(targetDir, "brit-a-z.txt");
        if (!fWords.exists()) {
            AWSHelper awsHelper = new AWSHelper();
            awsHelper.download("sphinx-data/dict/brit-a-z.txt", fWords);
        }
        List<String> words = FileUtils.readLines(fWords, "UTF-8");
        walker.execute(words);
        FileUtils.writeStringToFile(wordXml, "\t</string-array>\n" +
                "</resources>", true);

        MailService mailService = new MailService();
        mailService.sendEmail(new String[] {
                "hai.lu@c-mg.com"
        }, "UK dictionary", "Please take a look at attachment", new String[] {
                wordXml.getAbsolutePath()
        });
    }

}
