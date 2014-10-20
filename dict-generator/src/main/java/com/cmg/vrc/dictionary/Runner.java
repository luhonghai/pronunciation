package com.cmg.vrc.dictionary;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by luhonghai on 9/19/14.
 */
public class Runner {

    public static class Configuration {
        private static final String DEFAULT_SHEET_NAME = "Dictionary";
        private static final String DEFAULT_SHEET_EXCEPTION = "Exception";

        private static final String DEFAULT_ACOUSTIC_MODEL_PATH = "resource:/edu/cmu/sphinx/models/acoustic/wsj";
        private static final String DEFAULT_DICTIONARY_PATH = "resource:/edu/cmu/sphinx/models/acoustic/wsj/dict/cmudict.0.6d";
        private static final String DEFAULT_LANGUAGE_MODEL_PATH = "resource:/edu/cmu/sphinx/models/language/en-us.lm.dmp";

        private String sheetName;
        private String sheetException;
        private String audioDir;
        private String reportXlsx;
        private String wordList;

        private String acousticModelPath;
        private String dictionaryPath;
        private String languageModelPath;

        private String emailUsername;
        private String emailPassword;
        private String emailReceivers;

        public String getAudioDir() {
            return audioDir;
        }

        public void setAudioDir(String audioDir) {
            this.audioDir = audioDir;
        }

        public String getReportXlsx() {
            return reportXlsx;
        }

        public void setReportXlsx(String reportXlsx) {
            this.reportXlsx = reportXlsx;
        }

        public String getWordList() {
            return wordList;
        }

        public void setWordList(String wordList) {
            this.wordList = wordList;
        }

        public String getSheetName() {
            if (sheetName == null || sheetName.length() == 0) {
                sheetName = DEFAULT_SHEET_NAME;
            }
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }

        public String getSheetException() {
            if (sheetException == null || sheetException.length() == 0) {
                sheetException = DEFAULT_SHEET_EXCEPTION;
            }
            return sheetException;
        }

        public void setSheetException(String sheetException) {
            this.sheetException = sheetException;
        }

        public String getAcousticModelPath() {
            if (acousticModelPath == null || acousticModelPath.length() == 0) {
                acousticModelPath = DEFAULT_ACOUSTIC_MODEL_PATH;
            }
            return acousticModelPath;
        }

        public void setAcousticModelPath(String acousticModelPath) {
            this.acousticModelPath = acousticModelPath;
        }

        public String getDictionaryPath() {
            if (dictionaryPath == null || dictionaryPath.length() == 0) {
                dictionaryPath = DEFAULT_DICTIONARY_PATH;
            }
            return dictionaryPath;
        }

        public void setDictionaryPath(String dictionaryPath) {
            this.dictionaryPath = dictionaryPath;
        }

        public String getLanguageModelPath() {
            if (languageModelPath == null || languageModelPath.length() == 0) {
                languageModelPath = DEFAULT_LANGUAGE_MODEL_PATH;
            }
            return languageModelPath;
        }

        public void setLanguageModelPath(String languageModelPath) {
            this.languageModelPath = languageModelPath;
        }

        public String getEmailUsername() {
            if (emailUsername == null)
                emailUsername = "";
            return emailUsername;
        }

        public void setEmailUsername(String emailUsername) {
            this.emailUsername = emailUsername;
        }

        public String getEmailPassword() {
            if (emailPassword == null)
                emailPassword = "";
            return emailPassword;
        }

        public void setEmailPassword(String emailPassword) {
            this.emailPassword = emailPassword;
        }

        public String getEmailReceivers() {
            if (emailReceivers == null)
                emailReceivers = "";
            return emailReceivers;
        }

        public void setEmailReceivers(String emailReceivers) {
            this.emailReceivers = emailReceivers;
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            String configFile = args[0];
            File cFile = new File(configFile);
            if (cFile.exists() && !cFile.isDirectory()) {
                try {
                    Gson gson = new Gson();
                    String configSource = FileUtils.readFileToString(cFile);
                    Configuration configuration = gson.fromJson(configSource, Configuration.class);
                    DictionaryFetcher fetcher = new DictionaryFetcher(configuration);
                    if (fetcher.validate()) {
                        fetcher.execute();
                    } else  {
                        System.out.println("Invalid configuration");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("File not found or invalid: " + cFile);
                System.out.println("Int new default configuration to file " + cFile);
                saveDefaultConfig(cFile);
            }
        } else {
            System.out.println("Missing configuration file");
        }
    }

    private static void saveDefaultConfig(File file) {
        Gson gson = new Gson();
        Configuration conf = new Configuration();
        conf.setAudioDir("sample-data");
        conf.setReportXlsx("report.xlsx");
        conf.setWordList("words.txt");
        try {
            FileUtils.write(file, gson.toJson(conf), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
