package com.example.thecollectivediet.JSON_Marshall_Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class FoodLogItemView {
    Long food_id;
    String log_date;
    Float portion_size;
    String portion_unit;
    String category;
    String product_name;
    String sugars_unit;
    String fat_unit;
    String carbohydrates_unit;
    String proteins_unit;
    String sodium_unit;
    String saturated_fat_unit;
    Float fat_100g;
    Float carbohydrates_100g;
    Float proteins_100g;
    Float energy_kcal_100g;
    Float sugars_100g;
    Float sodium_100g;
    Float saturated_fat_100g;

    public FoodLogItemView(Long food_id, String log_date, Float portion_size, String portion_unit, String category, String product_name, Float fat_100g, Float carbohydrates_100g, Float proteins_100g, Float energy_kcal_100g, Float sugars_100g, Float sodium_100g, Float saturated_fat_100g, String sugars_unit, String fat_unit, String carbohydrates_unit, String proteins_unit, String sodium_unit, String saturated_fat_unit) {
        this.food_id = food_id;
        this.log_date = log_date;
        this.portion_size = portion_size;
        this.portion_unit = portion_unit;
        this.category = category;
        this.product_name = product_name;
        this.fat_100g = fat_100g;
        this.carbohydrates_100g = carbohydrates_100g;
        this.proteins_100g = proteins_100g;
        this.energy_kcal_100g = energy_kcal_100g;
        this.sugars_100g = sugars_100g;
        this.sodium_100g = sodium_100g;
        this.saturated_fat_100g = saturated_fat_100g;
        this.sugars_unit = sugars_unit;
        this.fat_unit = fat_unit;
        this.carbohydrates_unit = carbohydrates_unit;
        this.proteins_unit = proteins_unit;
        this.sodium_unit = sodium_unit;
        this.saturated_fat_unit = saturated_fat_unit;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodLogItemView that = (FoodLogItemView) o;
        return food_id.equals(that.food_id) && log_date.equals(that.log_date) && portion_size.equals(that.portion_size) && portion_unit.equals(that.portion_unit) && category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(food_id, log_date, portion_size, portion_unit, category);
    }


    @NonNull
    @Override
    public String toString() {
        return "FoodLogItemView{" +
                "food_id=" + food_id +
                ", log_date='" + log_date + '\'' +
                ", portion_size=" + portion_size +
                ", portion_unit='" + portion_unit + '\'' +
                ", category='" + category + '\'' +
                ", product_name='" + product_name + '\'' +
                '}';
    }

    public Long getFood_id() {
        return food_id;
    }

    public void setFood_id(Long food_id) {
        this.food_id = food_id;
    }

    public String getLog_date() {
        return log_date;
    }

    public void setLog_date(String log_date) {
        this.log_date = log_date;
    }

    public Float getPortion_size() {
        return portion_size;
    }

    public void setPortion_size(Float portion_size) {
        this.portion_size = portion_size;
    }

    public String getPortion_unit() {
        return portion_unit;
    }

    public void setPortion_unit(String portion_unit) {
        this.portion_unit = portion_unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSugars_unit() {
        return sugars_unit;
    }

    public void setSugars_unit(String sugars_unit) {
        this.sugars_unit = sugars_unit;
    }

    public String getFat_unit() {
        return fat_unit;
    }

    public void setFat_unit(String fat_unit) {
        this.fat_unit = fat_unit;
    }

    public String getCarbohydrates_unit() {
        return carbohydrates_unit;
    }

    public void setCarbohydrates_unit(String carbohydrates_unit) {
        this.carbohydrates_unit = carbohydrates_unit;
    }

    public String getProteins_unit() {
        return proteins_unit;
    }

    public void setProteins_unit(String proteins_unit) {
        this.proteins_unit = proteins_unit;
    }

    public String getSodium_unit() {
        return sodium_unit;
    }

    public void setSodium_unit(String sodium_unit) {
        this.sodium_unit = sodium_unit;
    }

    public String getSaturated_fat_unit() {
        return saturated_fat_unit;
    }

    public void setSaturated_fat_unit(String saturated_fat_unit) {
        this.saturated_fat_unit = saturated_fat_unit;
    }

    public Float getFat_100g() {
        return fat_100g;
    }

    public void setFat_100g(Float fat_100g) {
        this.fat_100g = fat_100g;
    }

    public Float getCarbohydrates_100g() {
        return carbohydrates_100g;
    }

    public void setCarbohydrates_100g(Float carbohydrates_100g) {
        this.carbohydrates_100g = carbohydrates_100g;
    }

    public Float getProteins_100g() {
        return proteins_100g;
    }

    public void setProteins_100g(Float proteins_100g) {
        this.proteins_100g = proteins_100g;
    }

    public Float getEnergy_kcal_100g() {
        return energy_kcal_100g;
    }

    public void setEnergy_kcal_100g(Float energy_kcal_100g) {
        this.energy_kcal_100g = energy_kcal_100g;
    }

    public Float getSugars_100g() {
        return sugars_100g;
    }

    public void setSugars_100g(Float sugars_100g) {
        this.sugars_100g = sugars_100g;
    }

    public Float getSodium_100g() {
        return sodium_100g;
    }

    public void setSodium_100g(Float sodium_100g) {
        this.sodium_100g = sodium_100g;
    }

    public Float getSaturated_fat_100g() {
        return saturated_fat_100g;
    }

    public void setSaturated_fat_100g(Float saturated_fat_100g) {
        this.saturated_fat_100g = saturated_fat_100g;
    }
}
