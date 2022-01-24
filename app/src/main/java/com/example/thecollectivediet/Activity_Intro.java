package com.example.thecollectivediet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class Activity_Intro extends AppCompatActivity {

    //create slider view in activity view
    private ViewPager viewPager;
    private Intro_ViewPagerAdapter viewPagerAdapter;
    //used to show progress in intro walkthrough
    private DotsIndicator dotsIndicator;


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

        //continue button found on the last slide
        Button continue_button = findViewById(R.id.continue_btn);
        continue_button.setVisibility(View.INVISIBLE);

        continue_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
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
                if(position == 0){
                    relativeLayout.setBackgroundResource(R.drawable.gradient_animation123);
                    AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
                    animationDrawable.setEnterFadeDuration(2500);
                    animationDrawable.setExitFadeDuration(1000);
                    animationDrawable.start();
                }

                if(position == 1){
                    relativeLayout.setBackgroundResource(R.drawable.gradient_animation123);
                    AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
                    animationDrawable.setEnterFadeDuration(2500);
                    animationDrawable.setExitFadeDuration(1000);
                    animationDrawable.start();

                    continue_button.setVisibility(View.VISIBLE);
                }

                if(position == 2){
                    relativeLayout.setBackgroundResource(R.drawable.gradient_animation123);
                    AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
                    animationDrawable.setEnterFadeDuration(2500);
                    animationDrawable.setExitFadeDuration(1000);
                    animationDrawable.start();
                    continue_button.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}