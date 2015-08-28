package com.cmg.vrc.data.jdo;
import com.cmg.vrc.data.Mirrorable;

/**
 * Created by CMGT400 on 8/7/2015.
 */
public class ClientCode implements Mirrorable{
    private String id;
    private String companyName;
    private String contactName;
    private String email;
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
