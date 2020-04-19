package com.example.cengonline.post;


import com.example.cengonline.model.User;

public abstract class AbstractPost implements Postable, Editable {

    private String key;
    protected String postedBy;
    private String postedAt;
    private String body;
    private String editedAt;
    protected String editedBy;

    public AbstractPost(){

    }

    public AbstractPost(String key, String postedBy, String postedAt, String body){
        this.key = key;
        this.postedAt = postedAt;
        this.postedBy = postedBy;
        this.body = body;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

}
