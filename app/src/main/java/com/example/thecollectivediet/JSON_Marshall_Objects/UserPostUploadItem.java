package com.example.thecollectivediet.JSON_Marshall_Objects;

/**
 * Simple class for serializing/deserializing UserPost JSON data to provide to views.
 * Note: comments are minimal because most methods are self-explanatory getters/setters.
 */
public class UserPostUploadItem {

    String user_id;
    String image_key;
    String comment;
    String date;
    String image_url;
    String user_name;

    public UserPostUploadItem(String user_id, String image_key, String comment, String date, String name){
        this.user_id = user_id;
        this.comment = comment;
        this.image_key = image_key;
        this.date = date;
        this.user_name = name;
    }

    public UserPostUploadItem(String user_id, String imageKey, String comment, String date, String imageUrl, String name) {
        this.user_id = user_id;
        this.image_key = imageKey;
        this.image_url = imageUrl;
        this.comment = comment;
        this.date = date;
        this.user_name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_key() {
        return image_key;
    }

    public void setImage_key(String image_key) {
        this.image_key = image_key;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return image_url;
    }

    public void setUrl(String url) {
        this.image_url = url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
