package com.example.userpass;

import android.net.Uri;
import android.widget.ListView;

import java.util.List;

public class User {
    public String username;
    public String email;
    public String id;
    public String photoUrl;

    public String list;
    public String selectedFromList;

    public String username1;
    public String password;
    public String other;
    public User() {

    }
    public User(String list){
        this.list=list;
    }

    public User(String username1,String password, String other){
        this.username1=username1;
        this.password=password;
        this.other=other;
    }


    public void setUserId(String id){
         this.id=id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setUserEmail(String email) {
        this.email = email;
    }
    public void setProfilePic(String photoUrl) {
        this.photoUrl=photoUrl;
    }

    public void addList(String list){
        this.list=list;
    }
    public String getList() {
        return list;
    }


    public void setUsername1(String username1) {
        this.username1 = username1;
    }
    public void setOther(String other) {
        this.other = other;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername1() { return username1; }
    public String getOther() {
        return other;
    }
    public String getPassword() {
        return password;
    }

    public void listItem(String selectedFromList) {
        this.selectedFromList=selectedFromList;
    }
    public String getListItem() {
        return selectedFromList;
    }
}
