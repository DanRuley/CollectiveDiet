package com.example.thecollectivediet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.thecollectivediet.JSON_Marshall_Objects.FoodLogItemView;
import com.example.thecollectivediet.JSON_Marshall_Objects.User;

import java.util.HashMap;
import java.util.List;

public class ViewModelMeals extends AndroidViewModel {

    Application application;

    MutableLiveData<HashMap<String,List<FoodLogItemView>>> list = new MutableLiveData<>();

    public ViewModelMeals(@NonNull Application application) {
        super(application);

        this.application = application;
    }

    /**
     * The MutableLiveData of the user will be passed to other classes/fragments
     * @return userData
     */
    public MutableLiveData<HashMap<String,List<FoodLogItemView>>> getList(){
        return list;
    }

    public void setList(HashMap<String,List<FoodLogItemView>> list){
        this.list.setValue(list);
    }
}
