package com.example.thecollectivediet;

import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;

import org.json.JSONException;
import org.json.JSONObject;

public class EditFoodObject {

    private String name;
    private String info;


    //For serialization
    private static final String JSON_NAME = "name";
    private static final String JSON_SERVING_SIZE = "servingSize";

    public EditFoodObject(String name, String info){
        this.name = name;
        this.info = info;
    }

    public EditFoodObject(JSONObject jo) throws JSONException{
        this.name = jo.getString(JSON_NAME);
        this.info = jo.getString(JSON_SERVING_SIZE);
    }

    public String getNameText(){
        return name;
    }

    public String getInfoText(){
        return info;
    }

    //Method to serialize FoodResult object
    public JSONObject convertToJSON() throws JSONException {
        JSONObject jo = new JSONObject();

        jo.put(JSON_NAME, name);
        jo.put(JSON_SERVING_SIZE, info);

        return jo;
    }
}
