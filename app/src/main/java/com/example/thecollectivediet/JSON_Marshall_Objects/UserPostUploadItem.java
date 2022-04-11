package com.example.thecollectivediet.JSON_Marshall_Objects;

public class UserPostUploadItem {

    String user_id;
    String image_Key;
    String comment;
    String date;

    public UserPostUploadItem(String user_id, String image_Key, String comment, String date){
        this.user_id = user_id;
        this.comment = comment;
        this.image_Key = image_Key;
        this.date = date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_Key() {
        return image_Key;
    }

    public void setImage_Key(String image_Key) {
        this.image_Key = image_Key;
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
}
