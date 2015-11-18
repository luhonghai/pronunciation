package com.cmg.lesson.data.jdo.word;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by lantb on 2015-10-01.
 */
@PersistenceCapable(table = "WORDMAPPINGPHONEMES", detachable = "true")
public class WordMappingPhonemes implements Mirrorable{

    @PrimaryKey
    private String id;

    @Persistent
    private String wordID;

    @Persistent
    private String phoneme;

    @Persistent
    private int index;

    @Persistent
    private boolean isDeleted;

    @Persistent
    private int version;

    @NotPersistent
    private String ipa;

    public String getIpa() {
        return ipa;
    }

    public void setIpa(String ipa) {
        this.ipa = ipa;
    }

    public WordMappingPhonemes(String wordID, String phoneme,int index, boolean isDeleted, int version){
        this.wordID = wordID;
        this.phoneme = phoneme;
        this.index = index;
        this.isDeleted = isDeleted;
        this.version = version;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }



    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
