package com.cmg.android.bbcaccent.data.dto.lesson.word;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

import java.util.ArrayList;
import java.util.List;

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

    public String getImgTongue() {
        return imgTongue;
    }

    public void setImgTongue(String imgTongue) {
        this.imgTongue = imgTongue;
    }

    public String getTextTongue() {
        return textTongue;
    }

    public void setTextTongue(String textTongue) {
        this.textTongue = textTongue;
    }

    public String getImgLip() {
        return imgLip;
    }

    public void setImgLip(String imgLip) {
        this.imgLip = imgLip;
    }

    public String getTextLip() {
        return textLip;
    }

    public void setTextLip(String textLip) {
        this.textLip = textLip;
    }

    public String getImgJaw() {
        return imgJaw;
    }

    public void setImgJaw(String imgJaw) {
        this.imgJaw = imgJaw;
    }

    public String getTextJaw() {
        return textJaw;
    }

    public void setTextJaw(String textJaw) {
        this.textJaw = textJaw;
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

    @LiteColumn
    private String imgTongue;

    @LiteColumn
    private String textTongue;

    @LiteColumn
    private String imgLip;

    @LiteColumn
    private String textLip;

    @LiteColumn
    private String imgJaw;

    @LiteColumn
    private String textJaw;

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

    public List<String> getWordList() {
        if (words != null && words.length() > 0) {
            List<String> list = new ArrayList<>();
            String[] data = words.split(",");
            if (data.length > 0) {
                for (String word : data) {
                    list.add(word.trim());
                }
            }
            return list;
        }
        return new ArrayList<>();
    }
}
