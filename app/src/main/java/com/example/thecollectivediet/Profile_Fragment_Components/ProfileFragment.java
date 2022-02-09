package com.example.thecollectivediet.Profile_Fragment_Components;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thecollectivediet.R;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;

    //UI elements
    TextView mEdit;
    ImageView mProfilePic;
    TextView mFirstName;
    TextView mLastName;
    TextView mAge;
    TextView mSex;
    TextView mHeight;
    TextView mWeight;
    TextView mCity;
    TextView mCountry;

    String profilePicPath;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        context = this.getActivity();
        prefs = context.getSharedPreferences("Lifestyle App Project", Context.MODE_PRIVATE);
        editor = prefs.edit();

        //hook elements in view
        mEdit = v.findViewById(R.id.textview_profile_edit);
        mEdit.setOnClickListener(this);
        mProfilePic = v.findViewById(R.id.profile_image);

        mFirstName = v.findViewById(R.id.textview_profile_firstname);
        mFirstName.setText(prefs.getString("profile_first", "").toString());
        mLastName = v.findViewById(R.id.textview_profile_lastname);
        mLastName.setText(prefs.getString("profile_last", ""));
        mAge = v.findViewById(R.id.textview_profile_age);
        mAge.setText("Age: " + prefs.getString("profile_age", ""));
        mSex = v.findViewById(R.id.textview_profile_sex);
        mSex.setText("Sex: " + prefs.getString("profile_sex", ""));
        mWeight = v.findViewById(R.id.textview_profile_weight);
        mWeight.setText("Weight: " + prefs.getString("profile_weight", ""));
        mHeight = v.findViewById(R.id.textview_profile_height);
        mHeight.setText("Height: " + prefs.getString("profile_height", ""));
        mCity = v.findViewById(R.id.textview_profile_city);
        mCity.setText("City: " + prefs.getString("profile_city", ""));
        mCountry = v.findViewById(R.id.textview_profile_country);
        mCountry.setText("Country: " + prefs.getString("profile_country", ""));

        profilePicPath = prefs.getString("profile_pic", null);
        Bitmap thumbnailPic = BitmapFactory.decodeFile(profilePicPath);
        if(thumbnailPic != null){
            mProfilePic.setImageBitmap(thumbnailPic);
        }

        return v;
    }

    @Override
    public void onClick(View v) {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch (v.getId()){

            case R.id.textview_profile_edit:{
                EditProfileFragment frag = new EditProfileFragment();
                transaction.replace(R.id.fragmentHolder, frag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }


    }
}
