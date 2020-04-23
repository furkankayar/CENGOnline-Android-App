package com.example.cengonline.post;


import com.example.cengonline.model.User;

import java.io.Serializable;

public abstract class AbstractPost implements Postable, Editable, Serializable {

    protected String postedBy;
    private String postedAt;
    private String body;
    private String editedAt;
    protected String editedBy;

    public AbstractPost(){

    }

    public AbstractPost(String postedBy, String postedAt, String body){
        this.postedAt = postedAt;
        this.postedBy = postedBy;
        this.body = body;
    }


    @Override
    public String getPostedBy(){
        return this.postedBy;
    }

    @Override
    public String getPostedAt(){
        return this.postedAt;
    }

    @Override
    public String getBody(){
        return this.body;
    }

    @Override
    public String getEditedAt(){
        return this.editedAt;
    }

    @Override
    public String getEditedBy() {
        return this.editedBy;
    }

    @Override
    public void setPostedAt(String postedAt){
        this.postedAt = postedAt;
    }

    @Override
    public void setBody(String body){
        this.body = body;
    }

    @Override
    public void setEditedAt(String editedAt){
        this.editedAt = editedAt;
    }

    @Override
    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    @Override
    public void setEditedBy(String editedBy) {
        this.editedBy = editedBy;
    }
}
