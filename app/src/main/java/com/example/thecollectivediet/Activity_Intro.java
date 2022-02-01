package com.example.thecollectivediet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;


public class Activity_Intro extends AppCompatActivity {

    static final int WELCOME = 0;
    static final int INTRO = 1;
    static final int SIGN_IN = 2;

    //create slider view in activity view
    private ViewPager viewPager;
    private Intro_ViewPagerAdapter viewPagerAdapter;
    //used to show progress in intro walkthrough
    private DotsIndicator dotsIndicator;
    private GoogleSignInClient mGoogleSignInClient;
    private Activity ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //view and animation
        View relativeLayout = findViewById(R.id.layout1);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();
        ctx = this;

        //continue button found on the last slide
        Button continue_button = findViewById(R.id.continue_btn);
        continue_button.setVisibility(View.INVISIBLE);


        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setVisibility(View.INVISIBLE);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //update ui

        ActivityResultLauncher<Intent> signInResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Task<GoogleSignInAccount> task1 = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                if (result.getResultCode() == Activity.RESULT_OK) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInResult(task);
                }
            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInResult.launch(mGoogleSignInClient.getSignInIntent());
            }
        });

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.setResult(RESULT_CANCELED);
                finish();
            }
        });

        //progress bar shown as dots
        dotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);

        viewPager = findViewById(R.id.intro_viewpager);
        viewPagerAdapter = new Intro_ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        dotsIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //Determines which slide/view from into is shown
            @Override
            public void onPageSelected(int position) {
                if (position == WELCOME) {
                    refreshAnimation();
                    continue_button.setVisibility(View.INVISIBLE);
                    signInButton.setVisibility(View.INVISIBLE);
                }

                if (position == INTRO) {
                    refreshAnimation();
                    signInButton.setVisibility(View.INVISIBLE);
                    continue_button.setVisibility(View.VISIBLE);
                }

                if (position == SIGN_IN) {
                    refreshAnimation();
                    signInButton.setVisibility(View.VISIBLE);
                    continue_button.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            void refreshAnimation() {
                relativeLayout.setBackgroundResource(R.drawable.gradient_animation123);
                AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
                animationDrawable.setEnterFadeDuration(2500);
                animationDrawable.setExitFadeDuration(1000);
                animationDrawable.start();
            }
        });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        String msg = "Hello, " + task.getResult().getDisplayName();
        GoogleSignInAccount account = task.getResult();
        String name = account.getDisplayName();
        String email = account.getEmail();
        String id = account.getId();

        SharedPreferences prefs;
        SharedPreferences.Editor editor;

        //Any class in this app can use this
        prefs = this.getSharedPreferences("TheCollectiveDiet", Context.MODE_PRIVATE);

        editor = prefs.edit();

        editor.putString("user", name);
        editor.putString("id", id);
        editor.putString("user_email", email);

        editor.commit();

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        this.setResult(RESULT_OK);
        finish();
    }
}