package com.example.thecollectivediet.JSON_Marshall_Objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FoodResult {

    private String food_name;
    private String serving_unit;
    private String tag_name;
    private String serving_qty;
    private String common_type;
    private int tag_id;
    private JSONObject photo;
    private String photoUrl;
    private String locale;

    public FoodResult(String food_name, String serving_unit, String tag_name, String serving_qty, String common_type, int tag_id, JSONObject photo, String locale) throws JSONException {
        this.food_name = food_name;
        this.serving_unit = serving_unit;
        this.tag_name = tag_name;
        this.serving_qty = serving_qty;
        this.common_type = common_type;
        this.tag_id = tag_id;
        //photo -> thumb:<url>
        this.photo = photo;
        photoUrl = photo.get("url").toString();
        this.locale = locale;
    }

    public FoodResult() {

    }

    @Override
    public String toString() {
        return "FoodResult{" +
                "\nfood_name='" + food_name + '\'' +
                "\nserving_unit='" + serving_unit + '\'' +
                "\ntag_name='" + tag_name + '\'' +
                "\nserving_qty='" + serving_qty + '\'' +
                "\ncommon_type='" + common_type + '\'' +
                "\ntag_id=" + tag_id +
                "\nphotoUrl='" + photo + '\'' +
                "\nlocale='" + locale + '\'' +
                '}';
    }
}
