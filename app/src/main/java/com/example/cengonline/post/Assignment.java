package com.example.cengonline.post;

import com.example.cengonline.model.MyTimestamp;
import com.example.cengonline.model.User;
import com.example.cengonline.post.AbstractPost;

import java.sql.Timestamp;
import java.util.List;

public class Assignment extends AbstractPost {

    private String title;
    private MyTimestamp dueDate;

    public Assignment(){
        super();
    }

    public Assignment(String title, MyTimestamp dueDate, String postedBy, MyTimestamp postedAt, String body){
        super(postedBy, postedAt, body);
        this.title = title;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MyTimestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(MyTimestamp dueDate) {
        this.dueDate = dueDate;
    }



}
