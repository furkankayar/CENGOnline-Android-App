package com.example.cengonline.post;

import com.example.cengonline.model.User;
import com.example.cengonline.post.AbstractPost;

public class Announcement extends AbstractPost {

    public Announcement(){

    }

    public Announcement(String key, String postedBy, String postedAt, String body){
        super(key, postedBy, postedAt, body);
    }

    @Override
    public void setEditedBy(User editedBy) {
        this.editedBy =  editedBy.getRoles().contains(User.Role.TEACHER) ? editedBy.getKey() : null;
    }

    @Override
    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy.getRoles().contains(User.Role.TEACHER) ? postedBy.getKey() : null;
    }
}
