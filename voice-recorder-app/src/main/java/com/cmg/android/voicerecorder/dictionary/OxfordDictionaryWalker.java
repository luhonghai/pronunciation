package com.cmg.android.voicerecorder.dictionary;

import com.cmg.android.voicerecorder.utils.UUIDGenerator;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
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
        gson = new Gson();
    }

    public OxfordDictionaryWalker(File targetDir, Language language) {
        this(targetDir);
        this.language = language;
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
            logger.log(Level.WARNING, "Could not encode URL", e);
        }
        File tmpSource = new File(FileUtils.getTempDirectory(), UUIDGenerator.generateUUID());
        try {
            logger.log(Level.INFO, "Fetch word: " + word + ". URL: " + url);

            FileUtils.copyURLToFile(new URL(url), tmpSource);
            Document doc = Jsoup.parse(tmpSource, ENCODE);
            Element title = doc.select(".pageTitle").first();
            String mTitle = "";
            if (title != null && title.hasText() && (mTitle = title.text()).toLowerCase().contains(word.toLowerCase())) {
                // Find pronunciation
                String pron = doc.select(".headpron").first().text();
                Matcher matcher = Pattern.compile("/(.+?)/").matcher(pron);
                if (matcher.find()) {
                    String tp = matcher.group(1).trim();
                    if (tp.contains(","));
                    tp = tp.split(",")[0].trim();
                    item.setPronunciation(tp);
                }
                // Find line breaks
                item.setLineBreaks(doc.select("span.linebreaks").first().text().trim());
                // Find audio sound url. Type mp3
                String audioUrl = doc.select("div.audio_play_button").first().attr("data-src-mp3");
                if (audioUrl.length() > 0 && audioUrl.endsWith(".mp3")) {
                    item.setAudioUrl(audioUrl);
                    if (!getTargetDir().exists() || !getTargetDir().isDirectory()) {
                        getTargetDir().mkdirs();
                    }
                    File saveFile = new File(getTargetDir(), word + ".mp3");

                    if (saveFile.exists()) {

                    } else {
                        logger.info("Download URL " + item.getAudioUrl() + " to file: " + saveFile);
                        FileUtils.copyURLToFile(new URL(item.getAudioUrl()), saveFile);

                    }
                    item.setAudioFile(saveFile.getAbsolutePath());
                }
                logger.info("Found: " + gson.toJson(item));
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
        } finally {
            try {
                if (tmpSource.exists())
                    FileUtils.forceDelete(tmpSource);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not delete temp file " + tmpSource, e);
            }
        }
    }

    public static void main(String[] args) {
        DictionaryWalker walker = new OxfordDictionaryWalker(new File("/Volumes/DATA/Development/test-zone"));
        walker.setListener(new DictionaryListener() {
            @Override
            public void onDetectWord(DictionaryItem item) {

            }

            @Override
            public void onWordNotFound(DictionaryItem item, FileNotFoundException ex) {

            }

            @Override
            public void onError(DictionaryItem item, Exception ex) {

            }
        });
        walker.execute("necessarily");
    }
}
