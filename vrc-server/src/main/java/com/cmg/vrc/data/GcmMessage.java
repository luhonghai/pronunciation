package com.cmg.vrc.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cmg on 14/12/2015.
 */
public class GcmMessage {

    public static final int TYPE_DATABASE = 0;

    public GcmMessage(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public static class Language {
        private String id;
        private String message;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    private int type;

    private Date timestamp = new Date(System.currentTimeMillis());

    private List<Language> languages = new ArrayList<>();
}
