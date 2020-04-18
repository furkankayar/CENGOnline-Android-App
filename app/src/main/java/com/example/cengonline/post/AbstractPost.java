package com.example.cengonline.post;


import com.example.cengonline.model.User;

public abstract class AbstractPost implements Postable, Editable {

    protected User postedBy;
    private String postedAt;
    private String body;
    private String editedAt;
    protected User editedBy;

    public AbstractPost(){

    }

    public AbstractPost(User postedBy, String postedAt, String body){
        this.postedAt = postedAt;
        this.postedBy = postedBy;
        this.body = body;
    }

    @Override
    public User getPostedBy(){
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
    public User getEditedBy() {
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

}
