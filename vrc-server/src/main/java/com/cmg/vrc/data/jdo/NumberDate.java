package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by CMGT400 on 11/12/2015.
 */
@PersistenceCapable(table = "NUMBERDATE", detachable = "true")
public class NumberDate implements Mirrorable {
    @PrimaryKey
    private String id;

    @Persistent
    private String userName;

    @Persistent
    private Date createdDate;

    @Persistent
    private int  numberDate;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }
    public String getUserName(){
        if (userName != null) userName = userName.toLowerCase();
        return userName;
    }
    public  void setUserName(String userName){
        if (userName != null) userName = userName.toLowerCase();
        this.userName=userName;
    }
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getNumberDate() {
        return numberDate;
    }

    public void setNumberDate(int numberDate) {
        this.numberDate = numberDate;
    }

}
