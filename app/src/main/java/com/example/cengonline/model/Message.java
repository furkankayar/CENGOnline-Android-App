package com.example.cengonline.model;

import java.io.Serializable;

public class Message implements Serializable {

    private String senderKey;
    private MyTimestamp sentAt;
    private String body;

    public Message(){

    }

    public Message(String senderKey, MyTimestamp sentAt, String body) {
        this.senderKey = senderKey;
        this.sentAt = sentAt;
        this.body = body;
    }

    public String getSenderKey() {
        return senderKey;
    }

    public void setSenderKey(String senderKey) {
        this.senderKey = senderKey;
    }

    public MyTimestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(MyTimestamp sentAt) {
        this.sentAt = sentAt;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
