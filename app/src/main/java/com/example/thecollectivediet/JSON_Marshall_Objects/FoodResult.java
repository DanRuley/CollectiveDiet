package com.example.thecollectivediet.JSON_Marshall_Objects;

import org.json.JSONException;
import org.json.JSONObject;

public class FoodResult {

    private String food_name;
    private String serving_unit;
    private String tag_name;
    private String serving_qty;
    private String common_type;
    private int tag_id;
    private Photo photo;

    //For serialization
    private static final String JSON_NAME = "name";
    private static final String JSON_SERVING_SIZE = "servingSize";

    public FoodResult(JSONObject jo) throws JSONException{
        this.food_name = jo.getString(JSON_NAME);
        this.serving_qty = jo.getString(JSON_SERVING_SIZE);
    }

    public FoodResult(String food_name, String serving_unit, String tag_name, String serving_qty, String common_type, int tag_id, Photo photo, String locale) {
        this.food_name = food_name;
        this.serving_unit = serving_unit;
        this.tag_name = tag_name;
        this.serving_qty = serving_qty;
        this.common_type = common_type;
        this.tag_id = tag_id;
        this.photo = photo;
        this.locale = locale;
    }

    public FoodResult() {

    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getServing_unit() {
        return serving_unit;
    }

    public void setServing_unit(String serving_unit) {
        this.serving_unit = serving_unit;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getServing_qty() {
        return serving_qty;
    }

    public void setServing_qty(String serving_qty) {
        this.serving_qty = serving_qty;
    }

    public String getCommon_type() {
        return common_type;
    }

    public void setCommon_type(String common_type) {
        this.common_type = common_type;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getPhotoURL() {
        return photo.getThumb();
    }

    private String locale;


    @Override
    public String toString() {
        return "FoodResult{" +
                "\nfood_name='" + food_name + '\'' +
                "\nserving_unit='" + serving_unit + '\'' +
                "\ntag_name='" + tag_name + '\'' +
                "\nserving_qty='" + serving_qty + '\'' +
                "\ncommon_type='" + common_type + '\'' +
                "\ntag_id=" + tag_id +
                "\nphotoUrl='" + photo.getThumb() + '\'' +
                "\nlocale='" + locale + '\'' +
                '}';
    }

    public class Photo {
        String thumb;

        public Photo(String thumb) {
            this.thumb = thumb;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }
    }

    //Method to serialize FoodResult object
    public JSONObject convertToJSON() throws JSONException{
        JSONObject jo = new JSONObject();

        jo.put(JSON_NAME, food_name);
        jo.put(JSON_SERVING_SIZE, serving_qty);

        return jo;
    }
}

