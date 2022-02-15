package com.example.thecollectivediet.Me_Fragment_Components;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.thecollectivediet.Me_Fragment_Components.Food_Logging.ManualFoodSearch;
import com.example.thecollectivediet.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class TodayFragment extends Fragment implements View.OnClickListener {

    int choice;
    String profilePicPath;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;

    //Rating bars
    RatingBar moodRatingBar;
    RatingBar energyRatingBar;
    RatingBar hungerRatingBar;
    RatingBar focusRatingBar;

    //List of images for rating bars
    int[] moodList = new int[]{R.drawable.mood_rank1, R.drawable.mood_rank2, R.drawable.mood_rank3, R.drawable.mood_rank4};
    int[] energyList = new int[]{R.drawable.energy_rank1, R.drawable.energy_rank2, R.drawable.energy_rank3,R.drawable.energy_rank4};
    int[] hungerList = new int[]{R.drawable.hunger_rank1, R.drawable.hunger_rank2, R.drawable.hunger_rank3,R.drawable.hunger_rank4};
    int[] focusList = new int[]{R.drawable.focus_rank1,R.drawable.focus_rank2, R.drawable.focus_rank3,R.drawable.focus_rank4};

    //hooks for images
    ImageView mProfilePic;
    ImageView moodImage;
    ImageView energyImage;
    ImageView hungerImage;
    ImageView focusImage;
    ImageView mealImage; //edit meal button


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance) {

        View v = inflater.inflate(R.layout.fragment_today, container, false);

        context = this.getActivity();
        prefs = context.getSharedPreferences("Lifestyle App Project", Context.MODE_PRIVATE);
        editor = prefs.edit();

        moodImage = v.findViewById(R.id.mood_image);
        energyImage = v.findViewById(R.id.energy_image);
        hungerImage = v.findViewById(R.id.hunger_image);
        focusImage = v.findViewById(R.id.focus_image);
        mProfilePic = v.findViewById(R.id.cv_profile_pic);

        //display pic
        profilePicPath = prefs.getString("profile_pic", null);
        Bitmap thumbnailPic = BitmapFactory.decodeFile(profilePicPath);
        if(thumbnailPic != null){
            mProfilePic.setImageBitmap(thumbnailPic);
        }

        //Rating bar for mood
        moodRatingBar = (RatingBar)v.findViewById(R.id.mood_ratingbar);
        moodRatingBar.setRating(0);

        moodRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                choice = (int)moodRatingBar.getRating();
                moodImage.setImageResource(moodList[choice - 1]);
                /*
                todo with sergio
                 */
            }
        });

        //Rating bar for energy
        energyRatingBar = (RatingBar)v.findViewById(R.id.energy_ratingbar);
        energyRatingBar.setRating(0);

        energyRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                choice = (int)energyRatingBar.getRating();
                energyImage.setImageResource(energyList[choice - 1]);
                 /*
                todo with sergio
                 */
            }
        });

        //Rating bar for hunger
        hungerRatingBar = (RatingBar) v.findViewById(R.id.hunger_ratingbar);
        hungerRatingBar.setRating(0);

        hungerRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                choice = (int)hungerRatingBar.getRating();
                hungerImage.setImageResource(hungerList[choice - 1]);
                 /*
                todo with sergio
                 */
            }
        });

        //Rating bar for focus
        focusRatingBar = (RatingBar) v.findViewById(R.id.focus_ratingbar);
        focusRatingBar.setRating(0);

        focusRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                choice = (int)focusRatingBar.getRating();
                focusImage.setImageResource(focusList[choice - 1]);
                 /*
                todo with sergio
                 */
            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
//            case R.id.meal:
//            {
//                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragmentHolder, new ManualFoodSearch());
//                transaction.addToBackStack(null);
//                transaction.commit();
//                break;
//            }
        }
    }
}
