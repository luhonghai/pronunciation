package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

/**
 * Created by CMGT400 on 6/8/2015.
 */
public class Admin implements Mirrorable {
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private int role;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }
   public String getUserName(){
       return userName;
   }
    public void setUserName(String userName){
        this.userName=userName;
    }
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
        if (lastName==null){
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
}
