package com.cmg.android.bbcaccent.data.dto.lesson.word;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

@LiteTable(allowedParent = BaseLessonEntity.class)
public class WordCollection extends BaseLessonEntity {

    @LiteColumn
    private String word;

    @LiteColumn
    private String pronunciation;

    @LiteColumn
    private String definition;

    @LiteColumn
    private String mp3Path;

    public WordCollection(){}

    public String getMp3Path() {
        return mp3Path;
    }

    public void setMp3Path(String mp3Path) {
        this.mp3Path = mp3Path;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
