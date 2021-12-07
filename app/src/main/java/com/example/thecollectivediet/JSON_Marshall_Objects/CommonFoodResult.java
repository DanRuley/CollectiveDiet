package com.example.thecollectivediet.JSON_Marshall_Objects;

public class CommonFoodResult extends FoodResult {

    private String tag_name;
    private int tag_it;

    public CommonFoodResult(String food_name, String serving_unit, String serving_qty, Photo photo, String locale, String tag_name) {
        super(food_name, serving_unit, serving_qty, photo, locale);
        this.tag_name = tag_name;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public int getTag_it() {
        return tag_it;
    }

    public void setTag_it(int tag_it) {
        this.tag_it = tag_it;
    }

    @Override
    public String getAPI_Identifier() {
        return getFood_name();
    }

    @Override
    public String toString() {
        return "CommonFoodResult{" +
                "tag_name='" + tag_name + '\'' +
                ", tag_it=" + tag_it +
                '}' + super.toString();
    }
}
