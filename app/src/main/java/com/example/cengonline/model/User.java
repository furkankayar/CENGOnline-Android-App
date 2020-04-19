package com.example.cengonline.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable{

    public enum Role{
        STUDENT,
        TEACHER,
        ADMIN
    };

    private String key;
    private String uid;
    private String email;
    private String displayName;
    private List<Role> roles;

    public User(){

    }

    public User(String key, String uid, String email, String displayName, List<Role> roles) {
        this.key = key;
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.roles = roles;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return this.displayName;
    }


    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                ", roles=" + roles +
                '}';
    }
}
