package com.example.thecollectivediet.Profile_Fragment_Components;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.example.thecollectivediet.R;

public class GenderPickerDialog extends Dialog {
    EditProfileFragment parent;
    Spinner genderSpinner;
    Context ctx;

    public GenderPickerDialog(@NonNull Context context, EditProfileFragment parent, String gender) {
        super(context);
        this.ctx = context;
        this.parent = parent;
        initializeComponents(gender);
    }

    private void initializeComponents(String gender) {
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setContentView(R.layout.gender_pick_dialog);
        this.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        genderSpinner = findViewById(R.id.gender_pick_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx, R.array.gender_option_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);


        if (gender == null || gender.startsWith("M"))
            genderSpinner.setSelection(0);
        else if (gender.startsWith("F"))
            genderSpinner.setSelection(1);
        else
            genderSpinner.setSelection(2);
    }

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
        parent.genderChangeCallback(genderSpinner.getSelectedItem().toString());
        dismiss();
    }
}
