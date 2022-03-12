package com.example.thecollectivediet.Goals_Fragment_Components;

import android.app.Dialog;
import android.content.Context;

import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatButton;

import com.example.thecollectivediet.R;
import com.google.android.material.textfield.TextInputEditText;


public class WeighInDialog extends Dialog implements View.OnClickListener {

  Context ctx;

  //Buttons
    TextInputEditText mWeight;
    ImageView mIncreaseWeight;
    ImageView mDecreaseWeight;
    ImageView mAccept;
    ImageView mDecline;

    public WeighInDialog(Context ctx) {
        // Required empty public constructor
        super(ctx);

        this.ctx = ctx;

        this.setContentView(R.layout.dialog_weigh_in);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        mWeight = this.findViewById(R.id.tiet_weigh_in_weight);
        mWeight.setOnClickListener(this);

        mIncreaseWeight = this.findViewById(R.id.iv_weigh_in_increase_weight);
        mIncreaseWeight.setOnClickListener(this);

        mDecreaseWeight = this.findViewById(R.id.iv_weigh_in_decrease_weight);
        mDecreaseWeight.setOnClickListener(this);

        mAccept = this.findViewById(R.id.iv_weigh_in_accept);
        mAccept.setOnClickListener(this);

        mDecline = this.findViewById(R.id.iv_weigh_in_decline);
        mDecline.setOnClickListener(this);

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
            case R.id.iv_weigh_in_increase_weight:{
                int num = 0;
                if(mWeight.getEditableText().toString().matches(""))
                {
                    mWeight.setText(String.valueOf(num));
                    break;
                }

                num = Integer.valueOf(mWeight.getEditableText().toString());
                num++;

                if(num > 999)
                {
                    num = 999;
                }
                mWeight.setText(String.valueOf(num));
                break;
            }

            case R.id.iv_weigh_in_decrease_weight:{
                int num = 0;
                if(mWeight.getEditableText().toString().matches(""))
                {
                    mWeight.setText(String.valueOf(num));
                    break;
                }

                num = Integer.valueOf(mWeight.getEditableText().toString());
                num--;

                if(num < 0)
                {
                    num = 0;
                }
                mWeight.setText(String.valueOf(num));
                break;
            }

            case R.id.iv_weigh_in_decline:{
                onStop();
                break;
            }


        }
    }
}