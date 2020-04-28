package com.example.cengonline.post;


import com.example.cengonline.model.MyTimestamp;

import java.io.Serializable;

public class Announcement extends AbstractPost implements Serializable {

    public Announcement(){

    }

    public Announcement(String postedBy, MyTimestamp postedAt, String body){
        super(postedBy, postedAt, body);
    }

}
