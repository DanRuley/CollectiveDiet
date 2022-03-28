package com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing;

import android.app.Dialog;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.thecollectivediet.API_Utilities.FoodSearchController;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.R;

public class MealSelectDialog extends Dialog implements View.OnClickListener {

    FragmentActivity ctx;
    FoodSearchController controller;
    FragmentFoodLog parent;
    MealType mealType;

    public enum MealType {
        Breakfast,
        Lunch,
        Dinner,
        Snack
    }

    public MealSelectDialog(FragmentActivity ctx, FragmentFoodLog parent) {
        super(ctx);
        this.ctx = ctx;
        this.parent = parent;
        initializeComponents();
    }

    private void initializeComponents() {

        //this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.setContentView(R.layout.food_log_dialog);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        findViewById(R.id.breakfast_add_btn).setOnClickListener(this);
        findViewById(R.id.lunch_add_btn).setOnClickListener(this);
        findViewById(R.id.dinner_add_btn).setOnClickListener(this);
        findViewById(R.id.snack_add_btn).setOnClickListener(this);

        controller = new FoodSearchController(ctx);
    }

    @Override
    public void onClick(@NonNull View v) {

        int id = v.getId();

        //todo do something with this later
//        if (MainActivity.getCurrentUser() == null) {
//            ((MainActivity)ctx).requireSignInPrompt("Please sign in before logging meals");
//            return;
//        }

        if (id == R.id.breakfast_add_btn)
            mealType = MealType.Breakfast;
        else if (id == R.id.lunch_add_btn)
            mealType = MealType.Lunch;
        else if (id == R.id.dinner_add_btn)
            mealType = MealType.Dinner;
        else
            mealType = MealType.Snack;

        new CountDownTimer(500, 250) {

            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                onStop();
            }
        }.start();
    }

    @Override
    protected void onStop(){

        if(mealType != null)
            parent.inflateFoodSearchFrag(mealType);

        super.onStop();
        dismiss();
    }
}
