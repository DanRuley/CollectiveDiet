package com.example.thecollectivediet.Intro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.example.thecollectivediet.R;


public class Intro_ViewPagerAdapter extends PagerAdapter {

    Context ctx;

    public Intro_ViewPagerAdapter(Context context) {

        this.ctx = context;

    }

    @Nullable
    LayoutInflater layoutInflater = null;

    @NonNull
    int[] list = new int[]{R.drawable.ic_launcher_foreground, R.drawable.food_pic, R.drawable.googleg_color};

    @NonNull
    String[] textHeader = new String[]{
            "WELCOME",
            "Start Logging!",
            "Sign in to get started!"
    };


    @NonNull
    String[] textBody = new String[]{
            "Our application lets you track your diet by logging your food to gain insights into how your diet affects your life.",
            "The Collective Diet app will allow you to log your meals manually or by camera using object" +
                    "detection. Your meals will be tracked by our database and can be viewed at" +
                    "any time!",
            "Use your google account to sign in"
    };

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_intro, container, false);

        ImageView img = view.findViewById(R.id.intro_image_1);
        TextView txt_head = view.findViewById(R.id.txt_header);
        TextView txt_body = view.findViewById(R.id.txt_body);


        img.setImageResource(list[position]);
        txt_head.setText(textHeader[position]);
        txt_body.setText(textBody[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}