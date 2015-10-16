package com.cmg.android.bbcaccent.data.dto;

import com.luhonghai.litedb.LiteEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

/**
 * Created by luhonghai on 09/10/2015.
 */
@LiteTable(name = "pronunciation")
public class PronunciationWord extends LiteEntity {
    @LiteColumn
    private String word;

    @LiteColumn
    private boolean beep;

    @LiteColumn
    private String pronunciation;

    public PronunciationWord() {

    }

    public PronunciationWord(String word, String pronunciation, boolean beep) {
        this.word = word;
        this.pronunciation = pronunciation;
        this.beep = beep;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isBeep() {
        return beep;
    }

    public void setBeep(boolean beep) {
        this.beep = beep;
    }

    public String getPronunciation() {
        if (pronunciation == null) return "";
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }
}
