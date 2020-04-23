package com.example.cengonline.post;

import com.example.cengonline.model.User;
import com.example.cengonline.post.AbstractPost;

import java.io.Serializable;

public class Announcement extends AbstractPost implements Serializable {

    public Announcement(){

    }

    public Announcement(String postedBy, String postedAt, String body){
        super(postedBy, postedAt, body);
    }

}
