package com.example.thecollectivediet.Profile_Fragment_Components;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.thecollectivediet.API_Utilities.User_API_Controller;
import com.example.thecollectivediet.JSON_Marshall_Objects.User;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.R;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("ClickableViewAccessibility")
public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<String> cameraRequestPermissionLauncher;
    private ActivityResultLauncher<String> readRequestPermissionLauncher;

    EditText nickNameInput;
    EditText dobInput;
    EditText genderInput;
    EditText heightInput;
    EditText countryInput;
    EditText cityInput;
    EditText weightInput;

    DatePickerDialog dobPicker;

    private boolean photoChanged;

    final String[] PERMISSIONS = {
            // Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Nullable
    Context context;

    //buttons
    AppCompatButton editPhoto;
    AppCompatButton saveChanges;
    AppCompatButton saveChangesBottom;
    AppCompatButton back_button;

    //profile pic
    CircleImageView photo;

    //bitmap that holds profile pic
    Bitmap bitmap;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        initializeComponents(v);

        return v;
    }


    private void initializeComponents(@NonNull View v) {
        //took new photo?
        photoChanged = false;

        context = this.getActivity();
        assert context != null;
        prefs = context.getSharedPreferences("Lifestyle App Project", Context.MODE_PRIVATE);
        editor = prefs.edit();

        //hook elements
        editPhoto = v.findViewById(R.id.ac_button_editphoto);
        editPhoto.setOnClickListener(this);

        saveChanges = v.findViewById(R.id.edit_profile_button_save_top);
        saveChanges.setOnClickListener(this);

        saveChangesBottom = v.findViewById(R.id.edit_profile_bottom_save_button);
        saveChangesBottom.setOnClickListener(this);

        photo = v.findViewById(R.id.profile_image);
        String profilePicPath = prefs.getString("profile_pic", null);
        Bitmap thumbnailPic = null;
        if (profilePicPath != null)
            thumbnailPic = BitmapFactory.decodeFile(profilePicPath);
        if (thumbnailPic != null) {
            photo.setImageBitmap(thumbnailPic);
        }

        assert (MainActivity.getCurrentUser() != null);


        dobPicker = new DatePickerDialog(Objects.requireNonNull(getActivity()), this);

        User currentUser = MainActivity.getCurrentUser();

        //This listener is added to all edittext fields, it just clears the existing text when the user focuses on it and replaces the text
        //if the user makes no changes.
        EditTextEventListener clearInputListener = new EditTextEventListener();

        nickNameInput = ((TextInputLayout) v.findViewById(R.id.edit_profile_name_input)).getEditText();
        Objects.requireNonNull(nickNameInput).setOnFocusChangeListener(clearInputListener);
        cityInput = ((TextInputLayout) v.findViewById(R.id.edit_profile_city_input)).getEditText();
        Objects.requireNonNull(cityInput).setOnFocusChangeListener(clearInputListener);
        weightInput = ((TextInputLayout) v.findViewById(R.id.edit_profile_weight_input)).getEditText();
        Objects.requireNonNull(weightInput).setOnFocusChangeListener(clearInputListener);
        dobInput = ((TextInputLayout) v.findViewById(R.id.edit_profile_dob_input)).getEditText();
        genderInput = ((TextInputLayout) v.findViewById(R.id.edit_profile_gender_input)).getEditText();
        Objects.requireNonNull(genderInput).setOnFocusChangeListener(clearInputListener);
        heightInput = ((TextInputLayout) v.findViewById(R.id.edit_profile_height_input)).getEditText();
        Objects.requireNonNull(heightInput).setOnFocusChangeListener(clearInputListener);
        countryInput = ((TextInputLayout) v.findViewById(R.id.edit_profile_country_input)).getEditText();
        Objects.requireNonNull(countryInput).setOnFocusChangeListener(clearInputListener);


        if (currentUser.getUser_name() != null)
            nickNameInput.setText(currentUser.getUser_name());


        String dob = currentUser.getPrettyDob();
        if (dob != null)
            dobInput.setText(dob);

        dobInput.setOnTouchListener((v1, event) -> {
            dobPicker.show();
            return false;
        });

        if (currentUser.getUser_gender() != null)
            genderInput.setText(currentUser.getUser_gender());

        if (currentUser.getUser_hgt() != null)
            heightInput.setText(String.valueOf(currentUser.getUser_hgt()));

        if (currentUser.getUser_country() != null)
            countryInput.setText(currentUser.getUser_country());

        if (currentUser.getUser_city() != null)
            cityInput.setText(currentUser.getUser_city());

        if (currentUser.getCurrent_wgt() != null)
            weightInput.setText(String.valueOf(currentUser.getCurrent_wgt()));

        back_button = v.findViewById(R.id.edit_profile_back_btn);
        back_button.setOnClickListener(this);

        setupCameraAndFilePermissions();
        setupUI(v);
    }

    private void setupCameraAndFilePermissions() {
        ActivityResultContracts.RequestMultiplePermissions requestMultiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        //multiplePermissionActivityResultLauncher.launch(PERMISSIONS);
        ActivityResultLauncher<String[]> multiplePermissionActivityResultLauncher = registerForActivityResult(requestMultiplePermissionsContract, isGranted -> {
            Log.d("PERMISSIONS", "Launcher result: " + isGranted.toString());
            if (isGranted.containsValue(false)) {
                Log.d("PERMISSIONS", "At least one of the permissions was not granted, launching again...");
                //multiplePermissionActivityResultLauncher.launch(PERMISSIONS);
            }
        });

        cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        //get intent data from result
                        Intent data = result.getData();

                        //get the bitmap
                        photoChanged = true;
                        if (data != null) {
                            Bundle extras = data.getExtras();
                            bitmap = (Bitmap) extras.get("data");
                            //saveTempProfileImage(bitmap);
                            photo.setImageBitmap(bitmap);
                        }
                    }
                });

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {

                        readRequestPermissionLauncher.launch(
                                Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                });

        cameraRequestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue to take picture
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraActivityResultLauncher.launch(cameraIntent);
                    }
                });

        readRequestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        if (isExternalStorageWritable())
                            saveProfileImage(bitmap);
                    }
                });

        multiplePermissionActivityResultLauncher.launch(PERMISSIONS);
    }

    /**
     * Makes it so that clicking outside of text fields hides the keyboard
     * Sets onTouchListener for each non edit text view that hides the keyboard on touch.
     *
     * @param view - view component
     */
    public void setupUI(View view) {

        // Set up touch clearInputListener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    MainActivity.hideKeyboard(Objects.requireNonNull(getActivity()));
                    Log.i("touch event", v.toString());
                    v.performClick();
                }

                return false;
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @Override
    public void onClick(@NonNull View v) {

        int viewID = v.getId();
        if (viewID == R.id.ac_button_editphoto) {

            //Open camera
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraActivityResultLauncher.launch(cameraIntent);
        }

        if (viewID == R.id.edit_profile_bottom_save_button || viewID == R.id.edit_profile_button_save_top) {

            if (isExternalStorageWritable()) {
                saveProfileImage(bitmap);
            }

            saveProfileChanges();
            MainActivity.commitFragmentTransaction(Objects.requireNonNull(getActivity()), R.id.fragmentContainerView, new ProfileFragment());
        }

        if (viewID == R.id.edit_profile_back_btn) {
            MainActivity.commitFragmentTransaction(Objects.requireNonNull(getActivity()), R.id.fragmentContainerView, new ProfileFragment());
        }
    }

    //Method used to save many images to storage
    //Not used in this app at this time.
    @NonNull
    private String saveImage(@NonNull Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "Thumbnail_" + timeStamp + ".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }


    //method used to save profile picture
    private void saveProfileImage(@NonNull Bitmap finalBitmap) {

        if (photoChanged) {
            File rt = Objects.requireNonNull(getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String fname = "Thumbnail_profile.jpg";

            File file = new File(rt, fname);
            if (file.exists()) {
                file.delete();
            }

            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

                // You can use the API that requires the permission.
                editor.putString("profile_pic", file.getAbsolutePath());
                editor.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getEditTextString(EditText input) {
        if (input != null)
            return input.getText().toString();
        else
            return "";
    }

    private void saveProfileChanges() {

        User currentUser = MainActivity.getCurrentUser();
        assert currentUser != null;

        currentUser.setUser_name(getEditTextString(nickNameInput));
        currentUser.setUser_dob(getEditTextString(dobInput));
        currentUser.setUser_city(getEditTextString(cityInput));
        currentUser.setUser_country(getEditTextString(countryInput));
        currentUser.setUser_gender(getEditTextString(genderInput));
        currentUser.setUser_hgt(Float.parseFloat(getEditTextString(heightInput)));
        currentUser.setCurrent_wgt(Float.parseFloat(getEditTextString(weightInput)));

        User_API_Controller.updateUserProfile(currentUser, context);

        editor.commit();
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    protected void datePickerCallBack(String dateString) {
        dobInput.setText(dateString);
    }

    static class DatePickerDialog extends Dialog {

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

    static class EditTextEventListener implements View.OnFocusChangeListener {

        String oldTextVal;

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            EditText et = ((EditText) v);

            if (hasFocus) {
                oldTextVal = et.getText().toString();
                et.setText("");
            } else {
                if (et.getText().toString().equals(""))
                    et.setText(oldTextVal);
            }
        }

    }
}
