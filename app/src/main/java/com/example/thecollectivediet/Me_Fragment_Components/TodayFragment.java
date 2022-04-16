package com.example.thecollectivediet.Me_Fragment_Components;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.thecollectivediet.JSON_Marshall_Objects.User;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.R;
import com.example.thecollectivediet.ViewModelUser;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalDate;
import java.time.Period;

public class TodayFragment extends Fragment implements View.OnClickListener {

    int choice;
    String profilePicPath;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Nullable
    Context context;


    //elements for images
    ImageView mProfilePic;
    ImageView mealImage; //edit meal button

    //elements
    TextView mTodaysCalories;
    TextView mBMI;
    TextView mCalGoal;
    TextView mBMR;
    LinearLayout linearLayout;

    //graph elements
    CustomGraphView gg;
    LinearLayout graphContainer;
    LineGraphSeries<DataPoint> weightSeries;

    ViewModelUser viewModelUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance) {

        View v = inflater.inflate(R.layout.fragment_today, container, false);

        //Creates or gets existing view model to pass around the user data
        viewModelUser = new ViewModelProvider(requireActivity()).get(ViewModelUser.class);

        if (viewModelUser != null) {
            update();
        }

        context = this.getActivity();
        prefs = context.getSharedPreferences("Lifestyle App Project", Context.MODE_PRIVATE);
        editor = prefs.edit();

        mBMI = v.findViewById(R.id.tv_bmi_result);

        mProfilePic = v.findViewById(R.id.cv_profile_pic);
        mProfilePic.setOnClickListener(this);

        graphContainer = v.findViewById(R.id.graph);

        weightSeries = new LineGraphSeries<DataPoint>();

        //display pic
        profilePicPath = prefs.getString("profile_pic", null);
        Bitmap thumbnailPic = null;
        if (profilePicPath != null)
            thumbnailPic = BitmapFactory.decodeFile(profilePicPath);
        if (thumbnailPic != null) {
            mProfilePic.setImageBitmap(thumbnailPic);
        }

        //set the observer to get info for user created in User repository
        viewModelUser.getUserData().observe(getViewLifecycleOwner(), nameObserver);

        //set the observer to get info for weights created in User repository
        viewModelUser.getWeights().observe(getViewLifecycleOwner(), weightsObserver);

        //set the observer to get info for weights created in User repository
        viewModelUser.getCals().observe(getViewLifecycleOwner(), todayObserver);

        //hook elements
        mTodaysCalories = v.findViewById(R.id.tv_today_frag_calories);
        mCalGoal = v.findViewById(R.id.tv_cal_goal);
        mBMR = v.findViewById(R.id.tv_bmr);
        linearLayout = v.findViewById(R.id.ll_todays_calories_holder);

        return v;
    }

    /**
     * This observer tracks the calories of the user even as they change and updates
     * views as necessary.
     */
    final Observer<Float> todayObserver = new Observer<Float>() {
        @Override
        public void onChanged(Float t) {
            //Update the ui if this data variable changes
            if (t != null) {

                mTodaysCalories.setText(String.valueOf(t));

                if (t <= viewModelUser.getUser().getGoal_cals()) {
                    linearLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.pastel_yellow));
                }
                if (t < viewModelUser.getUser().getGoal_cals() * .7) {
                    linearLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_green));
                }
                if(t > viewModelUser.getUser().getGoal_cals()){
                    linearLayout.setBackgroundColor(ContextCompat.getColor( getContext(), R.color.red));
                }
            }
        }
    };

    /**create an observer that watches the LiveData<User> object
     * and updates the necessary views.
     */
    final Observer<User> nameObserver = new Observer<User>() {
        @Override
        public void onChanged(User user) {
            //Update the ui if this data variable changes
            if (user != null) {
                mCalGoal.setText(String.valueOf(user.getGoal_cals()));
                setUserBMI();

                //update bmr
                mBMR.setText(setBMR(user));

            }


        }
    };

    /**create an observer that watches the LiveData<Hashmap<Integer, Date>> weights object
    //this will keep the weight graph updated.
     */
    final Observer<DataPoint[]> weightsObserver = new Observer<DataPoint[]>() {
        @Override
        public void onChanged(DataPoint[] w) {
            if (w != null) {

                double minWgt, maxWgt;
                minWgt = maxWgt = w[0].getY();
                for (DataPoint dp : w) {
                    minWgt = Math.min(minWgt, dp.getY());
                    maxWgt = Math.max(maxWgt, dp.getY());
                }

                weightSeries = new LineGraphSeries<>(w);
                weightSeries.setAnimated(true);
                weightSeries.setBackgroundColor(getResources().getColor(R.color.light_blue));
                weightSeries.setDrawBackground(true);


                gg = new CustomGraphView(requireActivity());

                gg.myInit(w[0].getX(), w[w.length - 1].getX(), minWgt, maxWgt);
                gg.addSeries(weightSeries);
                graphContainer.addView(gg);
            }
            }

    };

    @Override
    public void onClick(@NonNull View v) {

        switch (v.getId()) {

        }
    }

    /**
     * Calculates the user's bmi using the formula from www.calculator.net
     */
    private void setUserBMI() {

        Float height = viewModelUser.getUser().getUser_hgt();
        Float weight = viewModelUser.getUser().getCurrent_wgt();
        String bmi;

        if (height > 0 && weight > 0) {
            double temp = weight;
            temp = temp / (height * height);
            temp = temp * 703;
            int temp2 = (int) temp;
            bmi = String.valueOf(temp2);
            mBMI.setText(bmi);

        } else {
            mBMI.setText("BMI: Need personal info");

        }
    }

    /**
     * Flag used to help determine when to update today frag with the user stats
     */
    private void update() {
        if (viewModelUser.getUpdateFlag() == 1) {
            viewModelUser.setUpdateFlag(0);
            viewModelUser.pullUserData((MainActivity) this.getActivity());
        }
    }

    /**
     * Calculates the user's bmi using the formula from www.calculator.net
     */
    private String setBMR(User user) {

        if (user.getUser_gender() == null || user.getCurrent_wgt() == null || user.getUser_hgt() == null || user.getUser_dob() == null) {
            return "Please complete profile";
        } else {
            //get age in years
            String[] age = user.getUser_dob().split("-");

            LocalDate today = LocalDate.now(); // Today's date is 10th Jan 2022
            LocalDate birthday = LocalDate.of(Integer.valueOf(age[0]), Integer.valueOf(age[1]), Integer.valueOf(age[2])); // Birth date

            Period p = Period.between(birthday, today);

            if (user.getUser_gender().matches("Male")) {
                return String.valueOf((10f * user.getCurrent_wgt()) + (6.25 * user.getUser_hgt()) - (5 * p.getYears()) + 5);
            } else if (user.getUser_gender().matches("Female")) {
                return String.valueOf((10 * user.getCurrent_wgt()) + (6.25 * user.getUser_hgt()) - (5 * p.getYears()) - 161);
            } else {
                return "Unable to calculate for sex of \"Other\"";
            }
        }

    }

    /**
     * Sets up the graph view in the today fragment.
     */
    class CustomGraphView extends GraphView {
        public CustomGraphView(Context context) {
            super(context);
        }

        public CustomGraphView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CustomGraphView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public void myInit(double minX, double maxX, double minY, double maxY) {
            super.init();
            setTitle("My Weight Graph");
            getGridLabelRenderer().setHorizontalAxisTitle("Date");
            getGridLabelRenderer().setVerticalAxisTitle("Weight");
            getGridLabelRenderer().setPadding(50);

            // set date label formatter
            getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(requireActivity()));
            getGridLabelRenderer().setNumHorizontalLabels(3);

            getGridLabelRenderer().setNumVerticalLabels(5);

            // set manual x bounds to have nice steps
            getViewport().setMinX(minX);
            getViewport().setMaxX(maxX);
            getViewport().setXAxisBoundsManual(true);

            getViewport().setMinY(Math.round(minY - 10));
            getViewport().setMaxY(Math.round(maxY + 10));
            getViewport().setYAxisBoundsManual(true);

            getGridLabelRenderer().setHumanRounding(false);
        }

        public void init() {
            super.init();
            // set the custom style
        }
    }
}
