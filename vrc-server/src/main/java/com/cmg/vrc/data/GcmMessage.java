package com.cmg.vrc.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cmg on 14/12/2015.
 */
public class GcmMessage {

    public static final int TYPE_DATABASE = 0;
    public static final int TYPE_INVITE = 1;
    public static final int TYPE_UPDATE_COURSE = 2;

    public GcmMessage(int type) {
        this.type = type;
        this.title = "New accenteasy message" ;
        if(type == TYPE_INVITE){
            this.content = "You have received a new invitation";
        }else if(type == TYPE_UPDATE_COURSE){
            this.content = "You have received a course update";
        }
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
        private String title;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    private int type;

    private Date timestamp = new Date(System.currentTimeMillis());

    private List<Language> languages = new ArrayList<>();

    private String title;

    private String content;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
