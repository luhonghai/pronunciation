package com.cmg.android.bbcaccent.dictionary;

/**
 * Created by luhonghai on 9/19/14.
 */
public class DictionaryItem {

    private String word;

    private String pronunciation;

    private String lineBreaks;

    private String audioUrl;

    private String audioFile;

    private String definition;

    public DictionaryItem() {

    }

    public DictionaryItem(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public String getPronunciation() {
        if (pronunciation == null) pronunciation = "";
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getLineBreaks() {
        return lineBreaks;
    }

    public void setLineBreaks(String lineBreaks) {
        this.lineBreaks = lineBreaks;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getDefinition() {
        if (definition == null) return "";
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
