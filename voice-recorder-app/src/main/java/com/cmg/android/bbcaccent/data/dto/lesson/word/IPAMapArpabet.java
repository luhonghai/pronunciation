package com.cmg.android.bbcaccent.data.dto.lesson.word;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

/**
 * Created by luhonghai on 12/11/2015.
 */
@LiteTable(allowedParent = BaseLessonEntity.class)
public class IPAMapArpabet extends BaseLessonEntity {

    public String getMp3UrlShort() {
        return mp3UrlShort;
    }

    public void setMp3UrlShort(String mp3UrlShort) {
        this.mp3UrlShort = mp3UrlShort;
    }

    public enum IPAType {
        VOWEL("vowel"),
        CONSONANT("consonant")
        ;
        String name;
        IPAType(String name) {
            this.name = name;
        }

        public static IPAType fromName(String name) {
            for (IPAType type : values()) {
                if (type.name.equalsIgnoreCase(name)) return type;
            }
            return null;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @LiteColumn
    private String arpabet;

    @LiteColumn
    private String color;

    @LiteColumn
    private String description;

    @LiteColumn
    private String ipa;

    @LiteColumn
    private String mp3Url;

    @LiteColumn
    private String mp3UrlShort;

    @LiteColumn
    private String tip;

    @LiteColumn
    private String type;

    @LiteColumn
    private String words;

    public String getArpabet() {
        if (arpabet == null) return "";
        return arpabet;
    }

    public void setArpabet(String arpabet) {
        this.arpabet = arpabet;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIpa() {
        return ipa;
    }

    public void setIpa(String ipa) {
        this.ipa = ipa;
    }

    public String getMp3Url() {
        if (mp3Url == null) return "";
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}
