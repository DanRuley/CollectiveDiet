package com.example.thecollectivediet.Goals_Fragment_Components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.thecollectivediet.R;
import com.example.thecollectivediet.ViewModelUser;

/**
 * Controls the layout that displays goals which the user can set
 */
public class FragmentGoals extends Fragment implements View.OnClickListener {

    AppCompatButton mEnterWeight;
    AppCompatButton mEnterWeightGoal;
    AppCompatButton mEnterCalorieGoal;

    ViewModelUser viewModelUser;

    /**
     * Required empty public constructor
     */
    public FragmentGoals() {
    }

    /**
     * Instantiate view components for Goals Fragment.
     *
     * @return Goals Fragment view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goals, container, false);

        //Creates or gets existing view model to pass around the user data
        viewModelUser = new ViewModelProvider(getActivity()).get(ViewModelUser.class);

        //hook elements
        mEnterWeight = v.findViewById(R.id.acb_goals_enter_current_weight);
        mEnterWeight.setOnClickListener(this);

        mEnterWeightGoal = v.findViewById(R.id.acb_goals_enter_weight_goal);
        mEnterWeightGoal.setOnClickListener(this);

        mEnterCalorieGoal = v.findViewById(R.id.acb_goals_enter_calorie_goal);
        mEnterCalorieGoal.setOnClickListener(this);

        return v;
    }

    /**
     * Opens up a dialog which allows the user to enter in their goals. The dialog type parameters are:
     * 1- Enter current weight
     * 2- Enter weight goal
     * 3- Enter calorie goal
     */
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.acb_goals_enter_current_weight:{

                GoalsDialog dialog = new GoalsDialog(getContext(), 1, requireActivity());
                dialog.show();
                break;
            }

            case R.id.acb_goals_enter_weight_goal:{

                GoalsDialog dialog = new GoalsDialog(getContext(),2, requireActivity());
                dialog.show();
                break;
            }

            case R.id.acb_goals_enter_calorie_goal:{
                GoalsDialog dialog = new GoalsDialog(getContext(), 3, requireActivity());
                dialog.show();
                break;
            }
        }
    }
}