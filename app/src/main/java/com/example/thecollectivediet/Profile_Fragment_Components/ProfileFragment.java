package com.example.thecollectivediet.Profile_Fragment_Components;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.thecollectivediet.FragmentSignIn;
import com.example.thecollectivediet.JSON_Marshall_Objects.User;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.R;
import com.example.thecollectivediet.ViewModelUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Nullable
    Context context;

    private GoogleSignInClient mGoogleSignInClient;

    //UI elements
    AppCompatButton mEdit;
    AppCompatButton mLogout;
    AppCompatButton mLogIn;
    ImageView mProfilePic;
    //TextView mFirstName;
    TextView mNickName;
    TextView mAge;
    TextView mSex;
    TextView mHeight;
    TextView mFeet;
    TextView mInches;
    TextView mWeight;
    TextView mCity;
    TextView mCountry;

    String profilePicPath;

    ViewModelUser viewModelUser;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        context = this.getActivity();
        prefs = context.getSharedPreferences("Lifestyle App Project", Context.MODE_PRIVATE);
        editor = prefs.edit();

        viewModelUser = new ViewModelProvider(getActivity()).get(ViewModelUser.class);
        //viewModelUser.getUserData().observe(requireActivity(), observer);
        //set the observer to get info for user created in User repository
        viewModelUser.getUserData().observe(getViewLifecycleOwner(), observer);

        //todo do something with this
        //User signedInUser = MainActivity.getCurrentUser();

        mEdit = v.findViewById(R.id.ac_button_profile_edit);
        mEdit.setOnClickListener(this);
        mProfilePic = v.findViewById(R.id.profile_image);

        mNickName = v.findViewById(R.id.textview_profile_lastname);
        mAge = v.findViewById(R.id.textview_profile_age);
        mSex = v.findViewById(R.id.textview_profile_sex);
        mWeight = v.findViewById(R.id.textview_profile_weight);
        mHeight = v.findViewById(R.id.textview_profile_height);
        mCity = v.findViewById(R.id.textview_profile_city);
        mCountry = v.findViewById(R.id.textview_profile_country);


        if (isSignedIn()) {
            mLogout = v.findViewById(R.id.ac_button_logout);
            mLogout.setOnClickListener(this);
            mLogout.setVisibility(View.VISIBLE);
            mLogout.setClickable(true);
        } else {
            mLogIn = v.findViewById(R.id.ac_button_login);
            mLogIn.setOnClickListener(this);
            mLogIn.setVisibility(View.VISIBLE);
            mLogIn.setClickable(true);
        }

        profilePicPath = prefs.getString("profile_pic", null);
        Bitmap thumbnailPic = null;
        if (profilePicPath != null)
            thumbnailPic = BitmapFactory.decodeFile(profilePicPath);
        if (thumbnailPic != null) {
            mProfilePic.setImageBitmap(thumbnailPic);
        }

        return v;
    }

    final Observer<User> observer = new Observer<User>() {
        @Override
        public void onChanged(User userData) {
            if(userData != null)
            {
                mNickName.setText(userData.getUser_name());
                mAge.setText("Age: " + userData.getUser_dob());
                mSex.setText("Sex: " + userData.getUser_gender());
                mWeight.setText("Weight: " + userData.getCurrent_wgt());

                int feet = (int) Math.floor(userData.getUser_hgt()/12);
                int inches = (int) Math.floor(userData.getUser_hgt()%12);
                mHeight.setText("Height: " + feet + "' " + inches + "''");

                mCity.setText("City: " + userData.getUser_city());
                mCountry.setText("Country: " + userData.getUser_country());
            }
        }
    };

    @Override
    public void onClick(@NonNull View v) {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch (v.getId()) {

            case R.id.ac_button_profile_edit: {
//                MainActivity.commitFragmentTransaction(getActivity(), R.id.fragmentContainerView, new EditProfileFragment());
                MainActivity.commitFragmentTransaction(getActivity(), R.id.fragmentHolder, new EditProfileFragment());
                break;
            }

            case R.id.ac_button_logout: {
               // signOut();
                break;
            }

            case R.id.ac_button_login: {
                MainActivity.commitFragmentTransaction(getActivity(), R.id.fragmentContainerView, new FragmentSignIn());
                break;
            }
        }


    }

    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(context) != null;
    }

//    private void signOut() {
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
//        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//                TextView login = getActivity().findViewById(R.id.toolbar_login);
//                MainActivity.setCurrentUser(null);
//                login.setText("sign in");
//
//                MainActivity.commitFragmentTransaction(getActivity(), R.id.fragmentHolder, new FragmentSignIn());
//            }
//        });
//    }
}
