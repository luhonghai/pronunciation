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


}
