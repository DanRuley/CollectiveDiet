package com.example.thecollectivediet.Me_Fragment_Components;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.thecollectivediet.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

public class TodayFragment extends Fragment implements View.OnClickListener {

    int choice;
    String profilePicPath;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Nullable
    Context context;

    //Rating bars
    RatingBar moodRatingBar;
    RatingBar energyRatingBar;
    RatingBar hungerRatingBar;
    RatingBar focusRatingBar;

    //List of images for rating bars
    @NonNull
    int[] moodList = new int[]{R.drawable.mood_rank1, R.drawable.mood_rank2, R.drawable.mood_rank3, R.drawable.mood_rank4};
    @NonNull
    int[] energyList = new int[]{R.drawable.energy_rank1, R.drawable.energy_rank2, R.drawable.energy_rank3, R.drawable.energy_rank4};
    @NonNull
    int[] hungerList = new int[]{R.drawable.hunger_rank1, R.drawable.hunger_rank2, R.drawable.hunger_rank3, R.drawable.hunger_rank4};
    @NonNull
    int[] focusList = new int[]{R.drawable.focus_rank1, R.drawable.focus_rank2, R.drawable.focus_rank3, R.drawable.focus_rank4};

    //elements for images
    ImageView mProfilePic;
    ImageView moodImage;
    ImageView energyImage;
    ImageView hungerImage;
    ImageView focusImage;
    ImageView mealImage; //edit meal button

    //elements
    TextView mBMI;

    //graph elements
    GraphView mWeightGraph;
    LineGraphSeries<DataPoint> weightSeries;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance) {

        View v = inflater.inflate(R.layout.fragment_today, container, false);

        context = this.getActivity();
        prefs = context.getSharedPreferences("Lifestyle App Project", Context.MODE_PRIVATE);
        editor = prefs.edit();

        mBMI = v.findViewById(R.id.tv_bmi_result);
        setUserBMI();
//        moodImage = v.findViewById(R.id.mood_image);
//        energyImage = v.findViewById(R.id.energy_image);
//        hungerImage = v.findViewById(R.id.hunger_image);
//        focusImage = v.findViewById(R.id.focus_image);
        mProfilePic = v.findViewById(R.id.cv_profile_pic);
        mProfilePic.setOnClickListener(this);


        mWeightGraph = v.findViewById(R.id.today_weight_graph);
//        weightSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{
//
//                new DataPoint(0,1),
//                new DataPoint(1,5),
//                new DataPoint(2,3),
//                new DataPoint(3,2),
//                new DataPoint(4,6)
//        });
        DataPoint[] arr = new DataPoint[1000];
        for(int i = 0; i<1000; i++){
            arr[i] = new DataPoint(i +1, i);
        }
        weightSeries = new LineGraphSeries<DataPoint>(arr);

        mWeightGraph.addSeries(weightSeries);

        weightSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

        weightSeries.setAnimated(true);
        weightSeries.setBackgroundColor(getResources().getColor(R.color.pink));
        weightSeries.setDrawBackground(true);
        mWeightGraph.setTitle("My Weight Graph");
        mWeightGraph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        mWeightGraph.getGridLabelRenderer().setVerticalAxisTitle("Weight");
        mWeightGraph.getGridLabelRenderer().setPadding(50);

        //display pic
        profilePicPath = prefs.getString("profile_pic", null);
        Bitmap thumbnailPic = null;
        if (profilePicPath != null)
            thumbnailPic = BitmapFactory.decodeFile(profilePicPath);
        if (thumbnailPic != null) {
            mProfilePic.setImageBitmap(thumbnailPic);
        }

        //Rating bar for mood
//        moodRatingBar = (RatingBar) v.findViewById(R.id.mood_ratingbar);
//        moodRatingBar.setRating(0);
//
//        moodRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//
//                choice = (int) moodRatingBar.getRating();
//                moodImage.setImageResource(moodList[choice - 1]);
//                /*
//                todo with sergio
//                 */
//            }
//        });

        //Rating bar for energy
//        energyRatingBar = (RatingBar) v.findViewById(R.id.energy_ratingbar);
//        energyRatingBar.setRating(0);
//
//        energyRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//
//                choice = (int) energyRatingBar.getRating();
//                energyImage.setImageResource(energyList[choice - 1]);
//                 /*
//                todo with sergio
//                 */
//            }
//        });

        //Rating bar for hunger
//        hungerRatingBar = (RatingBar) v.findViewById(R.id.hunger_ratingbar);
//        hungerRatingBar.setRating(0);
//
//        hungerRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                choice = (int) hungerRatingBar.getRating();
//                hungerImage.setImageResource(hungerList[choice - 1]);
//                 /*
//                todo with sergio
//                 */
//            }
//        });

        //Rating bar for focus
//        focusRatingBar = (RatingBar) v.findViewById(R.id.focus_ratingbar);
//        focusRatingBar.setRating(0);
//
//        focusRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                choice = (int) focusRatingBar.getRating();
//                focusImage.setImageResource(focusList[choice - 1]);
//                 /*
//                todo with sergio
//                 */
//            }
//        });
//
        return v;
    }

    @Override
    public void onClick(@NonNull View v) {

        switch (v.getId()) {

        }
    }

    private void setUserBMI() {

        int height;
        int weight;
        int feet;
        int inches;
        String bmi;

        //user bmi
        weight = Integer.valueOf(prefs.getString("profile_weight", "0"));
        feet = prefs.getInt("profile_feet", 0);

        inches = prefs.getInt("profile_inches", 0);

        height = (feet * 12) + inches; //convert feet to inches then add


        if (weight > 0 && height > 0) {
            double temp = weight ;
            temp = temp/(height*height);
            temp = temp * 703;
            int temp2 = (int) temp;
            bmi = String.valueOf(temp2);
            mBMI.setText(bmi);


        } else {
            mBMI.setText("BMI: Need personal info");

        }
    }
}
