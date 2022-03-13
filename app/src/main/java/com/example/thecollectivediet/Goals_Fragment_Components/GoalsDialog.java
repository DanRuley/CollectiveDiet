package com.example.thecollectivediet.Goals_Fragment_Components;

import android.app.Dialog;
import android.content.Context;

import android.text.InputFilter;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thecollectivediet.R;
import com.google.android.material.textfield.TextInputEditText;


public class GoalsDialog extends Dialog implements View.OnClickListener {

  Context ctx;
  int dialogType;
  int userInputLength; //max length of input for user depending on dialog type

  //TextViews
    TextView mHeader;
    TextView mUserInputUnit;

  //Buttons
    TextInputEditText mInput;
    ImageView mIncreaseWeight;
    ImageView mDecreaseWeight;
    ImageView mAccept;
    ImageView mDecline;

    /**
     * Creates dialog for entering user weigh in, weight goal, and calorie goal.
     * The dialog will display the appropriate text that is determined by the parameter dialogType.
     * The types are:
     * 1- weigh in
     * 2- weight goal
     * 3- calorie goal
     * @param ctx
     * @param dialogType passed in from FragmentGoals to determine the dialog type.
     */
    public GoalsDialog(Context ctx, int dialogType) {
        // Required empty public constructor
        super(ctx);

        this.ctx = ctx;
        this.dialogType = dialogType;

        this.setContentView(R.layout.dialog_goals);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        mHeader = this.findViewById(R.id.tv_dialog_goals_header);
        mUserInputUnit = this.findViewById(R.id.tv_dialog_goals_user_input_measurement_unit);

        mInput = this.findViewById(R.id.tiet_dialog_goals_user_input);
        mInput.setOnClickListener(this);

        mIncreaseWeight = this.findViewById(R.id.iv_dialog_goals_increase);
        mIncreaseWeight.setOnClickListener(this);

        mDecreaseWeight = this.findViewById(R.id.iv_dialog_goals_decrease);
        mDecreaseWeight.setOnClickListener(this);

        mAccept = this.findViewById(R.id.iv_dialog_goals_accept);
        mAccept.setOnClickListener(this);

        mDecline = this.findViewById(R.id.iv_dialog_goals_decline);
        mDecline.setOnClickListener(this);



        switch (dialogType){
            //User wants to weigh in for today
            case 1:{
                mHeader.setText("Today's Weight");
                userInputLength = 3;
                break;
            }
            //user wants to set weight goal
            case 2:{
                mHeader.setText("Weight Goal");
                userInputLength = 3;
                break;
            }
            //user wants to set calorie goal
            case 3:{
                mHeader.setText("Calorie Goal");
                mUserInputUnit.setText("Calories");
                mUserInputUnit.setHint("Enter Calories");
                userInputLength = 5;
                break;
            }
        }

          //input filter for TextInputEditText
                InputFilter[] filters = new InputFilter[1];
                filters[0] = new InputFilter.LengthFilter(userInputLength);
                mInput.setFilters(filters);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismiss();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.iv_dialog_goals_increase:{
//                int num = 0;
//                if(mInput.getEditableText().toString().matches(""))
//                {
//                    mInput.setText(String.valueOf(num));
//                    break;
//                }
//
//                num = Integer.valueOf(mInput.getEditableText().toString());
//                num++;
//
//                if(num > 999)
//                {
//                    num = 999;
//                }
//                mInput.setText(String.valueOf(num));
                arrowUp();
                break;
            }

            case R.id.iv_dialog_goals_decrease:{
//                int num = 0;
//                if(mInput.getEditableText().toString().matches(""))
//                {
//                    mInput.setText(String.valueOf(num));
//                    break;
//                }
//
//                num = Integer.valueOf(mInput.getEditableText().toString());
//                num--;
//
//                if(num < 0)
//                {
//                    num = 0;
//                }
//                mInput.setText(String.valueOf(num));
//                break;
                arrowDown();
                break;
            }

            case R.id.iv_dialog_goals_decline:{
                onStop();
                break;
            }

            case R.id.iv_dialog_goals_accept:{
                //todo
            }


        }
    }

    private void arrowUp(){
        int num = 0;
        if(mInput.getEditableText().toString().matches(""))
        {
            mInput.setText(String.valueOf(num));
        }
        else if(userInputLength == 5){
            num = Integer.valueOf(mInput.getEditableText().toString());
            num++;

            if (num > 99999) {
                num = 99999;
            }
            mInput.setText(String.valueOf(num));
        }
        else{
            num = Integer.valueOf(mInput.getEditableText().toString());
            num++;

            if(num > 999){
                num = 999;
            }
            mInput.setText(String.valueOf(num));
        }
    }

    private void arrowDown(){

        int num = 0;
        if(mInput.getEditableText().toString().matches(""))
        {
            mInput.setText(String.valueOf(num));
        }

        num = Integer.valueOf(mInput.getEditableText().toString());
        num--;

        if(num < 0)
        {
            num = 0;
        }
        mInput.setText(String.valueOf(num));
    }
}