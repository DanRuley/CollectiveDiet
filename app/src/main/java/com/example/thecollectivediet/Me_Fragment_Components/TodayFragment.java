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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.thecollectivediet.ModelViewUser;
import com.example.thecollectivediet.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    ModelViewUser modelViewUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance) {

        View v = inflater.inflate(R.layout.fragment_today, container, false);

        //Creates or gets existing view model to pass around the user data
        modelViewUser = new ViewModelProvider(this).get(ModelViewUser.class);

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

//
//        mWeightGraph = v.findViewById(R.id.today_weight_graph);
////        weightSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{
////
////                new DataPoint(0,1),
////                new DataPoint(1,5),
////                new DataPoint(2,3),
////                new DataPoint(3,2),
////                new DataPoint(4,6)
////        });
//        DataPoint[] arr = new DataPoint[1000];
//        for(int i = 0; i<1000; i++){
//            arr[i] = new DataPoint(i +1, i);
//        }
//        weightSeries = new LineGraphSeries<DataPoint>(arr);
//
//        mWeightGraph.addSeries(weightSeries);
//
//        weightSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
//            @Override
//            public void onTap(Series series, DataPointInterface dataPoint) {
//                Toast.makeText(getActivity(), "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
//            }
//        });


        GraphView mWeightGraph = v.findViewById(R.id.today_weight_graph);
        Calendar calendar = Calendar.getInstance();
        List<Date> dates = new ArrayList<>();
        calendar.add(Calendar.DATE, -190);

        double[] wgts = new double[]{192.0, 191.2, 192.0, 189.8, 189.3, 191.8, 190.3, 190.8, 187.4, 189.4, 188.1, 190.3, 189.8, 189.2, 191.1, 188.9, 187.5, 189.3, 190.4, 189.7, 189.5, 188.2, 187.1, 188.7, 187.1, 186.7, 189.0, 186.6, 188.4, 188.9, 186.5, 185.7, 187.8, 185.6, 185.9, 187.5, 185.9, 189.1, 188.2, 187.6, 187.2, 185.7, 186.4, 186.0, 185.5, 185.2, 187.3, 185.3, 185.5, 185.1, 185.6, 184.7, 184.4, 188.2, 187.7, 184.9, 184.1, 184.8, 184.7, 186.9, 186.6, 184.0, 185.9, 185.7, 183.4, 185.6, 186.3, 186.3, 184.6, 184.5, 184.8, 184.2, 182.6, 184.6, 183.7, 184.1, 185.0, 185.6, 184.6, 184.2, 182.7, 183.9, 184.7, 185.5, 184.1, 182.5, 184.7, 184.5, 184.4, 184.6, 181.9, 182.4, 184.6, 181.5, 182.8, 182.3, 181.4, 181.5, 181.9, 182.5, 181.0, 184.3, 181.9, 183.8, 181.3, 182.8, 182.3, 182.8, 181.5, 180.5, 182.1, 179.8, 182.9, 183.2, 182.1, 183.1, 181.5, 182.0, 180.8, 182.7, 179.1, 179.2, 179.5, 179.0, 181.8, 179.5, 179.9, 181.3, 179.7, 178.8, 178.9, 181.8, 178.8, 181.6, 179.4, 181.1, 178.5, 178.4, 181.3, 179.9, 178.1, 181.2, 177.9, 179.5, 180.5, 179.9, 178.7, 178.0, 180.1, 179.0, 181.2, 179.3, 180.2, 178.2, 176.9, 179.0, 179.4, 180.3, 177.5, 178.2, 176.6, 178.4, 178.4, 179.1, 178.8, 176.3, 179.6, 178.9, 179.2, 177.7, 176.9, 176.7, 177.0, 176.6, 178.7, 176.3, 176.9, 178.4, 176.2, 175.0, 178.0};
        DataPoint[] dps = new DataPoint[180];
        for (int i = 0; i < 180; i++) {
            Date d = calendar.getTime();
            dates.add(d);
            dps[i] = new DataPoint(d, wgts[i]);
            calendar.add(Calendar.DATE, 1);
        }

        // you can directly pass Date objects to DataPoint-Constructor
        // this will convert the Date to double via Date#getTime()
        LineGraphSeries<DataPoint> weightSeries = new LineGraphSeries<>(dps);

        mWeightGraph.addSeries(weightSeries);

        // set date label formatter
        mWeightGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(requireActivity()));
        mWeightGraph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // set manual x bounds to have nice steps
        mWeightGraph.getViewport().setMinY(165);
        mWeightGraph.getViewport().setMaxY(195);
        mWeightGraph.getViewport().setMinX(dates.get(0).getTime());
        mWeightGraph.getViewport().setMaxX(dates.get(dates.size() - 1).getTime());

        mWeightGraph.getViewport().setXAxisBoundsManual(true);
        mWeightGraph.getViewport().setYAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        mWeightGraph.getGridLabelRenderer().setHumanRounding(false);


        weightSeries.setAnimated(true);
        weightSeries.setBackgroundColor(getResources().getColor(R.color.pink));
        weightSeries.setDrawBackground(true);
        mWeightGraph.setTitle("My Weight Graph");
        mWeightGraph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        mWeightGraph.getGridLabelRenderer().setVerticalAxisTitle("Weight");
        mWeightGraph.getGridLabelRenderer().setPadding(75);

        //display pic
        profilePicPath = prefs.getString("profile_pic", null);
        Bitmap thumbnailPic = null;
        if (profilePicPath != null)
            thumbnailPic = BitmapFactory.decodeFile(profilePicPath);
        if (thumbnailPic != null) {
            mProfilePic.setImageBitmap(thumbnailPic);
        }

//        //set the observer to get info for user created in User repository
//        sharedViewModelUser.getUser().observe(getViewLifecycleOwner(), nameObserver);
//        modelViewUser.getUserData().observe(getViewLifecycleOwner(), nameObserver)

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
            bmi = String.valueOf(24.3);
            mBMI.setText(bmi);


        } else {
            mBMI.setText("BMI: Need personal info");

        }
    }
}
