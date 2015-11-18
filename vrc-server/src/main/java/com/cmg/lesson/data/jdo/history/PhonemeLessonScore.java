package com.cmg.lesson.data.jdo.history;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by lantb on 2015-10-16.
 */
@PersistenceCapable(table = "PHONEMELESSONSCORE", detachable = "true")
public class PhonemeLessonScore implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    private String idUserLessonHistory;

    @Persistent
    private String phoneme;

    @Persistent
    private int index;

    @Persistent
    private float totalScore;

    @Persistent
    private String idCountry;


    public String getIdUserLessonHistory() {
        return idUserLessonHistory;
    }

    public void setIdUserLessonHistory(String idUserLessonHistory) {
        this.idUserLessonHistory = idUserLessonHistory;
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

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public String getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(String idCountry) {
        this.idCountry = idCountry;
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
