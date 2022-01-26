package com.example.thecollectivediet.Intro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.thecollectivediet.R;


public class Intro_ViewPagerAdapter extends PagerAdapter {

    Context ctx;

    public Intro_ViewPagerAdapter(Context context) {

        this.ctx = context;

    }

    LayoutInflater layoutInflater = null;

    int[] list = new int[]{R.drawable.ic_launcher_foreground, R.drawable.food_pic, R.drawable.googleg_color};

    String[] textHeader = new String[]{
            "WELCOME",
            "Collective Diet is cool because....",
            "Sign in to get started!"
    };

    String[] textBody = new String[]{
            "Food Entry",
            "Take pics and stuff...",
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