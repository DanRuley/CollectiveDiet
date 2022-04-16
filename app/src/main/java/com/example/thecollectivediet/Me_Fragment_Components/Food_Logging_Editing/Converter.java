package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing;

import androidx.annotation.NonNull;

import com.example.thecollectivediet.JSON_Marshall_Objects.FoodLogItemView;

import java.util.List;
import java.util.Locale;

public class Converter {

    static final double ozToGrams = 28.34952;
    static final double cupsToGrams = 128;

    /**
     * Gets calories of all meals
     * @param items
     * @return
     */
    public static double getTotalMealCalories(@NonNull List<FoodLogItemView> items) {
        double totalCal = 0;

        for (FoodLogItemView food : items)
            totalCal += calculateCalories(food.getEnergy_kcal_100g(), food.getPortion_unit(), food.getPortion_size());

        return totalCal;
    }

    /**
     * Calculates the calories of the food item
     * @param energy100g calories
     * @param servingUnit
     * @param servingSize
     * @return
     */
    public static double calculateCalories(Float energy100g, @NonNull String servingUnit, Float servingSize) {
        double calories = 0;
        double calPerGram = energy100g / 100;

        switch (servingUnit) {
            case "g":
                calories = servingSize * calPerGram;
                break;
            case "cups":
                calories = servingSize * cupsToGrams * calPerGram;
                break;
            case "oz":
                calories = servingSize * ozToGrams * calPerGram;
                break;
        }

        return calories;
    }

    @NonNull
    public static String getCalorieString(Float energy100g, @NonNull String servingUnit, Float servingSize) {

        return String.format(Locale.US, "%.1f Calories", calculateCalories(energy100g, servingUnit, servingSize));
    }
}
