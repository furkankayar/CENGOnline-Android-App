package com.example.cengonline.model;

import com.example.cengonline.post.Announcement;

import java.io.Serializable;
import java.util.List;

public class CourseAnnouncements implements Serializable {

    private List<Announcement> announcements;
    private String courseKey;
    private String key;


    public CourseAnnouncements(){
    }

    public CourseAnnouncements(String key, List<Announcement> announcements, String courseKey){
        this.key = key;
        this.announcements = announcements;
        this.courseKey = courseKey;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
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
