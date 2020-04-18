package com.example.cengonline.post;

import com.example.cengonline.model.User;
import com.example.cengonline.post.AbstractPost;

public class Announcement extends AbstractPost {

    public Announcement(){

    }

    public Announcement(User postedBy, String postedAt, String body){
        super(postedBy, postedAt, body);
    }

    @Override
    public void setEditedBy(User editedBy) {
        this.editedBy =  editedBy.getRoles().contains(User.Role.TEACHER) ? editedBy : null;
    }

    @Override
    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy.getRoles().contains(User.Role.TEACHER) ? postedBy : null;
    }
}
