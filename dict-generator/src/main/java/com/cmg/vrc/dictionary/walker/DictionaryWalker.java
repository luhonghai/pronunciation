package com.cmg.vrc.dictionary.walker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 9/19/14.
 */
public abstract class DictionaryWalker {

    public static class WalkerException extends Exception {

        protected  WalkerException(String message) {
            super(message);
        }
    }

    public static class Language {
        public static final Language ENGLISH = new Language("english");
        public static final Language SPANISH = new Language("spanish");
        public static final Language ARABIC = new Language("arabic");
        public static final Language FRENCH = new Language("french");
        public static final Language GERMAN = new Language("german");
        public static final Language ITALIAN = new Language("italian");

        private final String language;

        private Language(String language) {
            this.language = language;
        }

        @Override
        public String toString() {
            return language;
        }
    }

    protected final Logger logger;

    private final File targetDir;

    private DictionaryListener listener;

    public DictionaryWalker(File targetDir) {
        logger =  Logger.getLogger(this.getClass().toString());
        this.targetDir = targetDir;
    }

    public void execute(List<String> words) {
        if (words == null || words.size() == 0) {
            return;
        }
        for (String word: words) {
            try {
                execute(word);
            } catch (Exception ex) {
                onError(new DictionaryItem(word),"Could not execute word: " + word , ex);
            }
        }
    }

    protected void onDetectWord(DictionaryItem item) {
        if (listener != null) {
            listener.onDetectWord(item);
        }
    }

    protected void onError(DictionaryItem item, String message, Exception ex) {
        logger.log(Level.SEVERE, message ,ex);
        if (listener != null) {
            listener.onError(item ,ex);
        }
    }

    protected void onWordNotFound(DictionaryItem item, FileNotFoundException ex) {
        logger.log(Level.SEVERE, "Could not found word: " + item.getWord() ,ex);
        if (listener != null) {
            listener.onWordNotFound(item, ex);
        }
    }

    public abstract void execute(String word);

    public File getTargetDir() {
        return targetDir;
    }

    public void setListener(DictionaryListener listener) {
        this.listener = listener;
    }

}
