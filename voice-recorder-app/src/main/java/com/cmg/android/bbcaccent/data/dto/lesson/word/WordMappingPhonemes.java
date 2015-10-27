package com.cmg.android.bbcaccent.data.dto.lesson.word;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

@LiteTable
public class WordMappingPhonemes extends BaseLessonEntity {

    @LiteColumn
    private String wordID;

    @LiteColumn
    private String phoneme;

    @LiteColumn
    private int index;

    public WordMappingPhonemes(String wordID, String phoneme,int index, boolean isDeleted, int version){
        this.wordID = wordID;
        this.phoneme = phoneme;
        this.index = index;
        setIsDeleted(isDeleted);
        setVersion(version);
    }

    public String getWordID() {
        return wordID;
    }

    public void setWordID(String wordID) {
        this.wordID = wordID;
    }

    public String getPhoneme() {
        return phoneme;
    }

    public void setPhoneme(String phoneme) {
        this.phoneme = phoneme;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
