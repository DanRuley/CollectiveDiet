package com.example.thecollectivediet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

/**
 * Controls the layout that creates a loading screen to allow the app
 * to finish with making calls to AWS RDS and AWS S3.
 */
public class FragmentSplashScreen extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance) {
        View v = inflater.inflate(R.layout.waiting_for_login_splash, container, false);

        // create a ProgressDrawable object which we will show as placeholder
        CircularProgressDrawable drawable = new CircularProgressDrawable(requireActivity());
        drawable.setColorSchemeColors(R.color.design_default_color_primary, R.color.design_default_color_primary_dark, R.color.teal_700);
        drawable.setCenterRadius(50f);
        drawable.setStrokeWidth(5f);
        // set all other properties as you would see fit and start it
        drawable.start();

        ImageView loadingSpinner = v.findViewById(R.id.loadingSpinner);

        loadingSpinner.setImageDrawable(drawable);

        return v;
    }
}
