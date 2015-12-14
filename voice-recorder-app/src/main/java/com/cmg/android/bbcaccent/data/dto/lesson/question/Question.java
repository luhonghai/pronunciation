package com.cmg.android.bbcaccent.data.dto.lesson.question;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.data.dto.lesson.word.WordCollection;
import com.cmg.android.bbcaccent.dictionary.DictionaryItem;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@LiteTable(allowedParent = BaseLessonEntity.class)
public class Question extends BaseLessonEntity {

    @LiteColumn
    private String name;

    @LiteColumn
    private Date timeCreated;

    @LiteColumn
    private String description;

    private boolean recorded;

    private boolean enabled;



    private String word;

    private int score;

    private List<Integer> scoreHistory = new ArrayList<>();

    private DictionaryItem dictionaryItem;

    private UserVoiceModel userVoiceModel;

    private List<WordCollection> wordCollections;

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isRecorded() {
        return recorded;
    }

    public void setRecorded(boolean recorded) {
        this.recorded = recorded;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public DictionaryItem getDictionaryItem() {
        return dictionaryItem;
    }

    public void setDictionaryItem(DictionaryItem dictionaryItem) {
        this.dictionaryItem = dictionaryItem;
    }

    public UserVoiceModel getUserVoiceModel() {
        return userVoiceModel;
    }

    public void setUserVoiceModel(UserVoiceModel userVoiceModel) {
        this.userVoiceModel = userVoiceModel;
    }

    public List<Integer> getScoreHistory() {
        return scoreHistory;
    }

    public void setScoreHistory(List<Integer> scoreHistory) {
        this.scoreHistory = scoreHistory;
    }

    public String getDescription() {
        if (description == null) return "";
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<WordCollection> getWordCollections() {
        return wordCollections;
    }

    public void setWordCollections(List<WordCollection> wordCollections) {
        this.wordCollections = wordCollections;
    }
}
