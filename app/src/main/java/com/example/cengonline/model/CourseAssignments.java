package com.example.cengonline.model;

import com.example.cengonline.post.Assignment;

import java.io.Serializable;
import java.util.List;

public class CourseAssignments implements Serializable {

    private List<Assignment> assignments;
    private String courseKey;
    private String key;


    public CourseAssignments(){
    }

    public CourseAssignments(List<Assignment> announcements, String courseKey, String key) {
        this.assignments = announcements;
        this.courseKey = courseKey;
        this.key = key;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public String getCourseKey() {
        return courseKey;
    }

    public void setCourseKey(String courseKey) {
        this.courseKey = courseKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
