package com.cmg.lesson.data.dto.word;

import com.cmg.lesson.data.jdo.word.WordMappingPhonemes;

import java.util.List;

/**
 * Created by lantb on 2015-10-05.
 */
public class WordDTO {
    private String id;
    private String word;
    private String pronunciation;
    private String definition;
    private String mp3Path;
    private List<WordMappingPhonemes> phonemes;
    private String message;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getMp3Path() {
        return mp3Path;
    }

    public void setMp3Path(String mp3Path) {
        this.mp3Path = mp3Path;
    }

    public List<WordMappingPhonemes> getPhonemes() {
        return phonemes;
    }

    public void setPhonemes(List<WordMappingPhonemes> phonemes) {
        this.phonemes = phonemes;
    }
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
