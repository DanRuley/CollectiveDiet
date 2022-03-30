package com.example.thecollectivediet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.thecollectivediet.JSON_Marshall_Objects.FoodLogItemView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ViewModelMeals extends AndroidViewModel {

    Application application;
    MutableLiveData<HashMap<String,List<FoodLogItemView>>> list = new MutableLiveData<>();
    String chosenDate;

    public ViewModelMeals(@NonNull Application application) {
        super(application);

        this.application = application;
        this.chosenDate = getTodayString();
    }

    /**
     * The MutableLiveData of the user will be passed to other classes/fragments
     * @return userData
     */
    public MutableLiveData<HashMap<String,List<FoodLogItemView>>> getList(){
        return list;
    }

    /**
     * set the MutableLiveData to be passed around
     * @param list
     */
    public void setList(HashMap<String,List<FoodLogItemView>> list){
        this.list.setValue(list);
    }

    /**
     * Get today's date in yyyy-MM-dd HH:mm:ss format
     * @return sdf.format(dts)
     */
    @NonNull
    public String getTodayString() {
        java.util.Date dts = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return sdf.format(dts);
    }

    public void setDate(String date){
        this.chosenDate = date;
    }

    public String getDate(){
        return chosenDate;
    }
}
