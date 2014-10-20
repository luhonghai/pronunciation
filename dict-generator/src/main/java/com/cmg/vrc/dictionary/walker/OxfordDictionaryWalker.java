package com.cmg.vrc.dictionary.walker;

import com.cmg.vrc.dictionary.util.UUIDGenerator;
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

    private static final String OXFORD_DICTIONAIES_ROOT = "http://www.oxforddictionaries.com/definition/";

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
        DictionaryItem item = new DictionaryItem(word);
        String url = "";
        try {
            url = OXFORD_DICTIONAIES_ROOT + language + "/" + URLEncoder.encode(word, ENCODE);
        } catch (UnsupportedEncodingException e) {
            url = OXFORD_DICTIONAIES_ROOT + language + "/" + word;
            logger.log(Level.WARNING, "Could not encode URL", e);
        }
        File tmpSource = new File(FileUtils.getTempDirectory(), UUIDGenerator.generateUUID());
        try {
            logger.log(Level.INFO, "Fetch word: " + word + ". URL: " + url);
            FileUtils.copyURLToFile(new URL(url), tmpSource);
            Document doc = Jsoup.parse(tmpSource, ENCODE);
            Element title = doc.select("h2.pageTitle").first();
            String mTitle = "";
            if (title.hasText() && (mTitle = title.text()).toLowerCase().contains(word.toLowerCase())) {
                // Find pronunciation
                String pron = doc.select("div.headpron").first().text();
                Matcher matcher = Pattern.compile("/(.+?)/").matcher(pron);
                if (matcher.find()) {
                    item.setPronunciation(matcher.group(1).trim());
                }
                // Find line breaks
                item.setLineBreaks(doc.select("span.linebreaks").first().text().trim());
                // Find audio sound url. Type mp3
                String audioUrl = doc.select("div.audio_play_button").first().attr("data-src-mp3");
                if (audioUrl.length() > 0 && audioUrl.endsWith(".mp3")) {
                    item.setAudioUrl(audioUrl);
                    String audioFile = audioUrl.substring(audioUrl.lastIndexOf("/") + 1, audioUrl.length());

                    if (!getTargetDir().exists() || !getTargetDir().isDirectory()) {
                        getTargetDir().mkdirs();
                    }
                    File saveFile = new File(getTargetDir(), audioFile);
                    logger.info("Download URL " + item.getAudioUrl() + " to file: " + saveFile);
                    FileUtils.copyURLToFile(new URL(item.getAudioUrl()), saveFile);
                    String audioWav = audioFile.substring(0, audioFile.lastIndexOf("mp3")) + "wav";
                    if (convertMp3ToWav(audioFile, audioWav)) {
                        item.setAudioFile(audioWav);
                    }
                    try {
                        logger.info("Delete cached file " + saveFile);
                        FileUtils.forceDelete(saveFile);
                    } catch (IOException iex) {
                        logger.log(Level.SEVERE, "Could not delete cached file " + saveFile, iex);
                    }
                }
                logger.info("Found: " + gson.toJson(item));
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

    private boolean convertMp3ToWav(String mp3, String wav) {
        File source = new File(getTargetDir(), mp3);
        if (source.exists()) {
            File dest = new File(getTargetDir(), wav);
            logger.info("Convert to WAV file " + dest);
            try {
                String command = "ffmpeg -y -i " + source + " -acodec pcm_s16le -ac 1 -ar 16000 " + dest + "";
                logger.info("Execute command: " + command);
                Process proc = Runtime.getRuntime().exec(command);
//                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//                BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
//                String line = null;
//                while ((line = stdInput.readLine()) != null) {
//                    logger.info(line);
//                }
//                while ((line = stdError.readLine()) != null) {
//                    logger.info(line);
//                }
                int code = proc.waitFor();
                logger.info("Exit code: " + code);
                return dest.exists();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
