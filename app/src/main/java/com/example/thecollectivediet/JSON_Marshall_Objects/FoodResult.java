package com.example.thecollectivediet.JSON_Marshall_Objects;

import androidx.annotation.NonNull;

public class FoodResult {


    private long id;
    private String product_name;
    private String nova_group;
    private String nutriscore_grade;
    private String image_url;


    public FoodResult(long id, String product_name, String nova_group, String nutriscore_grade, String image_url) {
        this.id = id;
        this.product_name = product_name;
        this.nova_group = nova_group;
        this.nutriscore_grade = nutriscore_grade;
        this.image_url = image_url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getNova_group() {
        return nova_group;
    }

    public void setNova_group(String nova_group) {
        this.nova_group = nova_group;
    }

    public String getNutriscore_grade() {
        return nutriscore_grade;
    }

    public void setNutriscore_grade(String nutriscore_grade) {
        this.nutriscore_grade = nutriscore_grade;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


    @NonNull
    @Override
    public String toString() {
        return "FoodResult{" +
                "id=" + id +
                ", product_name='" + product_name + '\'' +
                ", nova_group='" + nova_group + '\'' +
                ", nutriscore_grade='" + nutriscore_grade + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }


}