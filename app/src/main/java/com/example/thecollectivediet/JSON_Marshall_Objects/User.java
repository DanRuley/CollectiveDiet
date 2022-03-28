package com.example.thecollectivediet.JSON_Marshall_Objects;

import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {



    String user_id;
    String user_email;
    String user_name;
    String user_dob;
    String user_gender;
    String user_country;
    Float start_wgt;
    Float current_wgt;
    float goal_wgt;
    String signup_dts;
    String user_lifestyle;
    Float user_hgt;
//    int user_feet;//used for height
//    int user_inches;//used for height
    String user_city;
    int goal_cals;

//    public int getUser_feet() {
//        return user_feet;
//    }
//
//    public void setUser_feet(int user_feet) {
//        this.user_feet = user_feet;
//    }
//
//    public int getUser_inches() {
//        return user_inches;
//    }
//
//    public void setUser_inches(int user_inches) {
//        this.user_inches = user_inches;
//    }

    public User(){

    }

    public User(String id, String name, String email, String dts) {
        user_id = id;
        user_email = email;
        user_name = name;
        signup_dts = dts;
    }

    public String getUser_country() {
        return user_country;
    }

    public void setUser_country(String user_country) {
        this.user_country = user_country;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_dob() {
        return user_dob;
    }

    public void setUser_dob(String user_dob) {
        Pattern p = Pattern.compile("(\\d{2})/(\\d{2})/(\\d{4})");
        Matcher m = p.matcher(user_dob);
        if (m.find())
            this.user_dob = String.format("%s-%s-%s", m.group(3), m.group(1), m.group(2));
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public Float getStart_wgt() {
        return start_wgt;
    }

    public void setStart_wgt(Float start_wgt) {
        this.start_wgt = start_wgt;
    }

    public Float getCurrent_wgt() {
        return current_wgt;
    }

    public void setCurrent_wgt(Float current_wgt) {
        this.current_wgt = current_wgt;
    }

    public float getGoal_wgt() {
        return goal_wgt;
    }

    public void setGoal_wgt(float goal_wgt) {
        this.goal_wgt = goal_wgt;
    }

    public String getSignup_dts() {
        return signup_dts;
    }

    public void setSignup_dts(String signup_dts) {
        this.signup_dts = signup_dts;
    }

    public String getUser_lifestyle() {
        return user_lifestyle;
    }

    public void setUser_lifestyle(String user_lifestyle) {
        this.user_lifestyle = user_lifestyle;
    }

    public Float getUser_hgt() {
        return user_hgt;
    }

    public void setUser_hgt(Float user_hgt) {
        this.user_hgt = user_hgt;
    }

    public void setUser_city(String city) {
        this.user_city = city;
    }

    public String getUser_city() {
        return user_city;
    }

    public int getGoal_cals() {
        return goal_cals;
    }

    public void setGoal_cals(int goal_cals) {
        this.goal_cals = goal_cals;
    }

    @Nullable
    public String getPrettyDob() {
        if (user_dob == null)
            return null;

        Pattern p = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
        Matcher m = p.matcher(user_dob);
        if (m.find())
            return String.format("%s/%s/%s", m.group(2), m.group(3), m.group(1));
        else return null;
    }

}
