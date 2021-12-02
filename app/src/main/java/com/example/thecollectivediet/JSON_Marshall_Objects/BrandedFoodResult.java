package com.example.thecollectivediet.JSON_Marshall_Objects;

public class BrandedFoodResult extends FoodResult {

    private String nix_brand_id;
    private String brand_name_item_name;
    private int nf_calories;
    private String brand_name;
    private int region;
    private int brand_type;
    private String nix_item_id;

    public BrandedFoodResult(String food_name, String serving_unit, String serving_qty, Photo photo, String locale, String nix_brand_id, String brand_name_item_name, int nf_calories, String brand_name, int region, int brand_type, String nix_item_id) {
        super(food_name, serving_unit, serving_qty, photo, locale);
        this.nix_brand_id = nix_brand_id;
        this.brand_name_item_name = brand_name_item_name;
        this.nf_calories = nf_calories;
        this.brand_name = brand_name;
        this.region = region;
        this.brand_type = brand_type;
        this.nix_item_id = nix_item_id;
    }

    public String getNix_brand_id() {
        return nix_brand_id;
    }

    public void setNix_brand_id(String nix_brand_id) {
        this.nix_brand_id = nix_brand_id;
    }

    public String getBrand_name_item_name() {
        return brand_name_item_name;
    }

    public void setBrand_name_item_name(String brand_name_item_name) {
        this.brand_name_item_name = brand_name_item_name;
    }

    public int getNf_calories() {
        return nf_calories;
    }

    public void setNf_calories(int nf_calories) {
        this.nf_calories = nf_calories;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getBrand_type() {
        return brand_type;
    }

    public void setBrand_type(int brand_type) {
        this.brand_type = brand_type;
    }

    public String getNix_item_id() {
        return nix_item_id;
    }

    public void setNix_item_id(String nix_item_id) {
        this.nix_item_id = nix_item_id;
    }

    @Override
    public String toString() {
        return "BrandedFoodResult{" +
                "nix_brand_id='" + nix_brand_id + '\'' +
                ", brand_name_item_name='" + brand_name_item_name + '\'' +
                ", nf_calories=" + nf_calories +
                ", brand_name='" + brand_name + '\'' +
                ", region=" + region +
                ", brand_type=" + brand_type +
                ", nix_item_id='" + nix_item_id + '\'' +
                '}' + super.toString();
    }
}
