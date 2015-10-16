package com.cmg.lesson.data.jdo.word;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.*;

/**
 * Created by lantb on 2015-10-01.
 */
@PersistenceCapable(table = "WordCollection", detachable = "true")
public class WordCollection implements Mirrorable{

    @PrimaryKey
    private String id;

    @Persistent
    private String word;

    @Persistent
    private String pronunciation;

    @Persistent
    @Column(jdbcType="VARCHAR", length=10000)
    private String definition;

    @Persistent
    private boolean isDeleted;

    @Persistent
    private String mp3Path;

    @Persistent
    private int version;

    @NotPersistent
    private String arpabet;

    public WordCollection(){}

    public WordCollection(String word, String pronunciation, String definition, String mp3Path,boolean isDeleted, int version){
        this.word = word;
        this.pronunciation = pronunciation;
        this.definition = definition;
        this.mp3Path = mp3Path;
        this.isDeleted = isDeleted;
        this.version = version;
    }

    public WordCollection(String word, String pronunciation, String definition, String mp3Path,boolean isDeleted){
        this.word = word;
        this.pronunciation = pronunciation;
        this.definition = definition;
        this.mp3Path = mp3Path;
        this.isDeleted = isDeleted;
    }

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

    public String getArpabet() {
        return arpabet;
    }

    public void setArpabet(String arpabet) {
        this.arpabet = arpabet;
    }
}
