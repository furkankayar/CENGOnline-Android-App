package com.example.cengonline.model;

import com.example.cengonline.post.Post;

import java.io.Serializable;
import java.util.List;

public class CoursePosts extends CourseInformationStorage<Post> implements Serializable {



    public CoursePosts(){
    }

    public CoursePosts(List<Post> posts, String courseKey, String key) {
        super(key, posts, courseKey);
    }

    public List<Post> getPosts() {
        return this.dataList;
    }

    public void setPosts(List<Post> posts) {
        this.dataList = posts;
    }

    public String getCourseKey() {
        return this.courseKey;
    }

    public void setCourseKey(String courseKey) {
        this.courseKey = courseKey;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
