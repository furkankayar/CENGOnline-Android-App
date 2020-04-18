package com.example.cengonline.post;

import com.example.cengonline.model.User;

public interface Postable {

    public User getPostedBy();
    public String getPostedAt();
    public String getBody();
    public void setPostedBy(User postedBy);
    public void setPostedAt(String postedAt);
    public void setBody(String body);
}
