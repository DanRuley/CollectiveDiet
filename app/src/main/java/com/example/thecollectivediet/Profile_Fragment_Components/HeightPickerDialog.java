package com.example.thecollectivediet.Profile_Fragment_Components;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.thecollectivediet.R;

public class HeightPickerDialog extends Dialog {

    EditProfileFragment parent;
    EditText feet;
    EditText inches;

    public HeightPickerDialog(@NonNull Context context, EditProfileFragment parent, Float hgt) {
        super(context);
        this.parent = parent;
        initializeComponents(hgt);
    }

    private void initializeComponents(Float hgt) {
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setContentView(R.layout.height_pick_dialog);
        this.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        feet = findViewById(R.id.edit_profile_ft_input);
        inches = findViewById(R.id.edit_profile_inch_input);

        int ft;
        float in;

        if (hgt == null || hgt.compareTo(0.0f) == 0) {
            ft = 0;
            in = 0;
        } else {
            ft = Math.round(hgt) / 12;
            in = hgt - ft * 12;
        }

        feet.setText(String.valueOf(ft));
        inches.setText(String.valueOf(in));
    }

    @Override
    public void onStop() {
        new CountDownTimer(500, 250) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

            }
        }.start();
        super.onStop();
        parent.heightChangeCallback(Integer.parseInt(feet.getText().toString()), Float.parseFloat(inches.getText().toString()));
        dismiss();
    }
}
