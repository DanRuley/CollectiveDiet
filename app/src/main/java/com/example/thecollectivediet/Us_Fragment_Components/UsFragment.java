package com.example.thecollectivediet.Us_Fragment_Components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.thecollectivediet.R;

public class UsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_us, container, false);

        //RotateAnimation anim = new RotateAnimation(0f, 350f, , 15f);
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(50000);

// Start animating the image
        final ImageView earth = v.findViewById(R.id.iv_earth);
        earth.startAnimation(anim);

// Later.. stop the animation
        //earth.setAnimation(null);

        return v;
    }
}
