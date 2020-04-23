package com.example.cengonline.model;

import java.io.Serializable;
import java.util.List;

public class EnrolledCourses implements Serializable {

    private List<String> enrolledCourses;
    private String user;
    private String key;

    public EnrolledCourses() {
    }

    public EnrolledCourses(String key, List<String> enrolledCourses, String user) {
        this.enrolledCourses = enrolledCourses;
        this.user = user;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
