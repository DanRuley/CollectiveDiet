package com.example.thecollectivediet.JSON_Marshall_Objects;

import org.json.JSONException;

public abstract class FoodResult {

    private String food_name;
    private String serving_unit;
    private String serving_qty;
    private Photo photo;

    public FoodResult(String food_name, String serving_unit, String serving_qty, Photo photo, String locale) {
        this.food_name = food_name;
        this.serving_unit = serving_unit;
        this.serving_qty = serving_qty;
        this.photo = photo;
        this.locale = locale;
    }

    public FoodResult() {

    }

    public abstract String getAPI_Identifier();

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

    public String getServing_qty() {
        return serving_qty;
    }

    public void setServing_qty(String serving_qty) {
        this.serving_qty = serving_qty;
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
                "\nserving_qty='" + serving_qty + '\'' +
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
}

