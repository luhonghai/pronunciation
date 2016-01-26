package com.cmg.vrc.data.jdo;

/**
 * Created by CMGT400 on 1/19/2016.
 */
public class MailToUser {
    private String teacher;

    private String [] listmail;

    public String[] getListmail() {
        return listmail;
    }

    public void setListmail(String[] listmail) {
        this.listmail = listmail;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
