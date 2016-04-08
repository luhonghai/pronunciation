package com.cmg.prediction.youtube;

import com.cmg.vrc.processor.CommandExecutor;
import com.cmg.vrc.util.AudioHelper;
import com.cmg.vrc.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 4/5/16.
 */
public class YoutubeDownloader {


    private static Logger logger = Logger.getLogger(YoutubeDownloader.class.getName());

    private String url;

    private File rootCache;

    private File tmpDir;

    private File outputWav;

    private File outputTranscription;

    private String videoId;

    private File logFile;

    public YoutubeDownloader(String url) throws MalformedURLException, UnsupportedEncodingException {
        this.url = url;
        validate();
        rootCache = new File(FileUtils.getTempDirectory(), "youtube_downloader");
        if (rootCache.exists()) {
            rootCache.mkdirs();
        }
        tmpDir = new File(rootCache, videoId);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        outputWav = new File(tmpDir, "audio.wav");
        outputTranscription = new File(tmpDir, "raw_transcription.txt");
        logFile = new File(tmpDir, "log.txt");

    }

    private boolean validate() throws MalformedURLException, UnsupportedEncodingException {
        Map<String, String> para = StringUtil.splitQuery(new URL(url));
        if (para != null && para.size() > 0 && para.containsKey("v")) {
            videoId = para.get("v");
            logger.info("Found video id " + videoId);
            return true;
        } else {
            logger.severe("No video id found");
            return false;
        }
    }

    private void appendLog(String message) {
        try {
            FileUtils.write(logFile, message + "\n", "UTF-8", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean download() {
        try {
            if (outputWav.exists()) {
                logger.info("wav file exist " + outputWav);
                return true;
            } else {
                if (logFile.exists()) {
                    FileUtils.forceDelete(logFile);
                }
                File tmpScript = new File(tmpDir, "script.sh");
                FileUtils.writeStringToFile(tmpScript, "youtube-dl " + url);
                CommandExecutor.execute(tmpDir, new CommandExecutor.CommandListener() {
                    @Override
                    public void onMessage(String message) {
                        logger.info(message);
                        appendLog(message);
                    }

                    @Override
                    public void onError(String message, Throwable e) {
                        logger.log(Level.SEVERE, message, e);
                        appendLog(message + ". Error: " + e.getMessage());
                    }
                }, "sh", tmpScript.getAbsolutePath());
                FileUtils.forceDelete(tmpScript);
                File[] files = tmpDir.listFiles();
                if (files != null && files.length > 1) {
                    for (File file : files) {
                        String name = file.getName();
                        if (!name.toLowerCase().endsWith(".txt") && !name.toLowerCase().endsWith(".json")) {
                            logger.info("found video file: " + name);
                            try {
                                return AudioHelper.convertToWav(file, outputWav);
                            } finally {
                                FileUtils.forceDelete(file);
                            }
                        }
                    }
                } else {
                    logger.severe("no downloaded file found");
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "could not download youtube link: " + url, e);
        }
        return false;
    }

    public VideoInformation fetchVideoInformation() {
        try {
            if (!outputTranscription.exists() && validate()) {
                FileUtils.copyURLToFile(new URL("http://video.google.com/timedtext?lang=en&v=" + videoId), outputTranscription);
            }
            if (outputTranscription.exists()) {
                Document document = Jsoup.parse(outputTranscription, "UTF-8");
                List<Element> elements = document.getElementsByTag("text");
                VideoInformation videoInformation = new VideoInformation();
                videoInformation.setId(videoId);
                if (elements != null && elements.size() > 0) {
                    for (Element element : elements) {
                        Sentence sentence = new Sentence();
                        sentence.setText(element.text());
                        sentence.setStart((long)(Double.parseDouble(element.attr("start")) * 1000));
                        sentence.setDuration((long)(Double.parseDouble(element.attr("dur")) * 1000));
                        sentence.setEnd(sentence.getStart() + sentence.getDuration());
                        videoInformation.getSentences().add(sentence);
                    }
                } else {
                    logger.severe("no sentences found");
                }
                return videoInformation;
            } else {
                logger.severe("no transcription found");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "could not download transcription youtube link: " + url, e);
        }
        return null;
    }

    public File getOutputWav() {
        return outputWav;
    }

    public void clean() {
        if (tmpDir.exists()) {
            try {
                FileUtils.forceDelete(tmpDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class VideoInformation {

        private String id;
        private double duration;
        private List<Sentence> sentences = new ArrayList<>();

        public double getDuration() {
            return duration;
        }

        public void setDuration(double duration) {
            this.duration = duration;
        }

        public List<Sentence> getSentences() {
            return sentences;
        }

        public void setSentences(List<Sentence> sentences) {
            this.sentences = sentences;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class Sentence {
        private String text;
        private long start;
        private long end;
        private long duration;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public long getStart() {
            return start;
        }

        public void setStart(long start) {
            this.start = start;
        }

        public long getEnd() {
            return end;
        }

        public void setEnd(long end) {
            this.end = end;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }
    }

    public static void main(String[] args) throws MalformedURLException, UnsupportedEncodingException {
        YoutubeDownloader youtubeDownloader = new YoutubeDownloader("https://www.youtube.com/watch?v=IKxiijbfwLE");
        try {
            youtubeDownloader.download();
            File output = youtubeDownloader.getOutputWav();
            logger.info("WAV file: " + output);
            VideoInformation information = youtubeDownloader.fetchVideoInformation();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            logger.info(gson.toJson(information));
        } finally {
            //youtubeDownloader.clean();
        }
    }
}
