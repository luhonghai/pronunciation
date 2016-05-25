package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by CMGT400 on 6/8/2015.
 */
@PersistenceCapable(table = "ADMIN", detachable = "true")
public class Admin implements Mirrorable {
    @PrimaryKey
    private String id;

    @Persistent
    private String userName;

    @Persistent
    private String password;

    @Persistent
    private int role;

    @Persistent
    private String firstName;

    @Persistent
    private String lastName;

    @NotPersistent
    private String idCompany;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }
//    public String getUserName(){
//        if (userName != null) userName = userName.toLowerCase();
//        return userName;
//    }
//    public  void setUserName(String userName){
//        if (userName != null) userName = userName.toLowerCase();
//        this.userName=userName;
//    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        if(firstName==null){
            this.firstName="";
        }
        this.firstName =firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName){
        if(lastName==null){
            this.lastName="";
        }
        this.lastName =lastName;
    }
    public int getRole(){
        return role;
    }
    public  void setRole(int role){
        this.role=role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(String idCompany) {
        this.idCompany = idCompany;
    }
}
