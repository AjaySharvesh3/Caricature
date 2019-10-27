package com.example.photoblog;

import java.util.Date;

public class Comments {

    private String message, user_id, userName, thumbnail;
    private Date timestamp;

    public Comments(){

    }

    public Comments(String message, String user_id, Date timestamp, String userName, String thumbnail) {
        this.message = message;
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.userName = userName;
        this.thumbnail = thumbnail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
