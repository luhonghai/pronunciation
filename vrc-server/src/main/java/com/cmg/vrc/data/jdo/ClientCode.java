package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by CMGT400 on 8/7/2015.
 */
@PersistenceCapable(table = "CLIENTCODE", detachable = "true")
public class ClientCode implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    private String companyName;

    @Persistent
    private String contactName;

    @Persistent
    private String email;

    @Persistent
    private boolean isDeleted;
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }
    public String getCompanyName(){
        return companyName;
    }
    public void setCompanyName(String companyName){
        this.companyName=companyName;
    }
    public String getContactName(){
        return contactName;
    }
    public void setContactName(String contactName){
        this.contactName=contactName;
    }
    public  String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
