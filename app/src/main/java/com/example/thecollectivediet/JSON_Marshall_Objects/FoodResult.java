package com.example.thecollectivediet.JSON_Marshall_Objects;

import java.util.ArrayList;

public class FoodResult {
    String fdcId;
    String description;
    String lowercaseDescription;
    String dataType;
    long gtinUpc;
    String publishedDate;
    String brandOwner;
    String ingredients;
    String marketCountry;
    String foodCategory;
    String modifiedDate;
    String dataSource;
    String servingSizeUnit;
    double servingSize;
    String householdServingFullText;
    String allHighlightFields;
    double score;
    ArrayList<FoodNutrient> foodNutrients;

    public FoodResult() {

    }

    @Override
    public String toString() {
        return "FoodResult{" +
                "lowercaseDescription='" + lowercaseDescription + '\'' +
                '}';
    }

}
