package com.example.thecollectivediet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.thecollectivediet.JSON_Marshall_Objects.User;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<User> user;

//    public LiveData<User> getUser(){
//        user = new MutableLiveData<User>();
//        //updateUser();
//    }
}
