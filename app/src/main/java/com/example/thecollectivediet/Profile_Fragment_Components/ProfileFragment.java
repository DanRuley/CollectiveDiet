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
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.thecollectivediet.FragmentSignIn;
import com.example.thecollectivediet.JSON_Marshall_Objects.User;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
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
    TextView mGender;
    TextView mHeight;
    TextView mFeet;
    TextView mInches;
    TextView mWeight;
    TextView mCity;
    TextView mCountry;

    String profilePicPath;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        context = this.getActivity();
        prefs = context.getSharedPreferences("Lifestyle App Project", Context.MODE_PRIVATE);
        editor = prefs.edit();

        User signedInUser = MainActivity.getCurrentUser();

        mEdit = v.findViewById(R.id.ac_button_profile_edit);
        mEdit.setOnClickListener(this);
        mProfilePic = v.findViewById(R.id.profile_image);

        mNickName = v.findViewById(R.id.textview_profile_lastname);
        mNickName.setText(prefs.getString("profile_last", ""));
        mAge = v.findViewById(R.id.textview_profile_age);
        mNickName = v.findViewById(R.id.textview_profile_lastname);
        mGender = v.findViewById(R.id.textview_profile_sex);
        mAge = v.findViewById(R.id.textview_profile_age);
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

    @Override
    public void onClick(View v) {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch (v.getId()) {

            case R.id.ac_button_profile_edit: {
//                MainActivity.commitFragmentTransaction(getActivity(), R.id.fragmentContainerView, new EditProfileFragment());
                MainActivity.commitFragmentTransaction(getActivity(), R.id.fragmentHolder, new EditProfileFragment());
                break;
            }

            case R.id.ac_button_logout: {
                signOut();
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

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                TextView login = getActivity().findViewById(R.id.toolbar_login);
                MainActivity.setCurrentUser(null);
                login.setText("sign in");

                MainActivity.commitFragmentTransaction(getActivity(), R.id.fragmentHolder, new FragmentSignIn());
            }
        });
    }
}
