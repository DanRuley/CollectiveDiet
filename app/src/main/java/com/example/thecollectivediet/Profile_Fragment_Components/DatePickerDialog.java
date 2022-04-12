package com.example.thecollectivediet.Profile_Fragment_Components;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;

import com.example.thecollectivediet.R;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerDialog extends Dialog {

    DatePicker datePicker;
    Button dobConfirmBtn;
    EditProfileFragment parent;

    public DatePickerDialog(@NonNull Context context, EditProfileFragment parent) {
        super(context);
        this.parent = parent;
        initializeComponents();
    }

    private void initializeComponents() {
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setContentView(R.layout.date_picker_spinner_popup);
        this.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        datePicker = findViewById(R.id.date_picker_spinner);
        dobConfirmBtn = findViewById(R.id.dob_confirm_btn);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -13);

        datePicker.setMaxDate(c.getTimeInMillis());

        dobConfirmBtn.setOnClickListener(v -> {
            parent.datePickerCallBack(String.format(Locale.US, "%02d/%02d/%d", datePicker.getMonth() + 1, datePicker.getDayOfMonth(), datePicker.getYear()));
            onStop();
        });
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
        dismiss();
    }
}

