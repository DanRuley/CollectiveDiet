package com.example.thecollectivediet.Profile_Fragment_Components;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.thecollectivediet.API_Utilities.User_API_Controller;
import com.example.thecollectivediet.JSON_Marshall_Objects.User;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.ModelViewUser;
import com.example.thecollectivediet.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("ClickableViewAccessibility")
public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
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

    ModelViewUser modelViewUser;


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

        //Creates or gets existing view model to pass around the user data
        modelViewUser = new ViewModelProvider(requireActivity()).get(ModelViewUser.class);

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

        assert (modelViewUser.getUser() != null);


        dobPicker = new DatePickerDialog(requireActivity(), this);

        User currentUser = modelViewUser.getUser();

        //This listener is added to all edittext fields, it just clears the existing text when the user focuses on it and replaces the text
        //if the user makes no changes.
        EditTextEventListener clearInputListener = new EditTextEventListener();

        nickNameInput = v.findViewById(R.id.edit_profile_name_input);
        Objects.requireNonNull(nickNameInput).setOnFocusChangeListener(clearInputListener);
        cityInput = v.findViewById(R.id.edit_profile_city_input);
        Objects.requireNonNull(cityInput).setOnFocusChangeListener(clearInputListener);
        weightInput = v.findViewById(R.id.edit_profile_weight_input);
        Objects.requireNonNull(weightInput).setOnFocusChangeListener(clearInputListener);

        dobInput = v.findViewById(R.id.edit_profile_dob_input);
        dobInput.setFocusable(false);
        dobInput.setOnTouchListener((v1, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                dobPicker.show();
            }
            return false;
        });

        genderInput = v.findViewById(R.id.edit_profile_sex_input);
        genderInput.setFocusable(false);
        genderInput.setOnTouchListener((v1, event) -> {

            if (event.getAction() == MotionEvent.ACTION_UP) {
                GenderPickerDialog dialog = new GenderPickerDialog(requireActivity(), this, currentUser.getUser_gender());
                dialog.show();
            }
            return false;
        });

        heightInput = v.findViewById(R.id.edit_profile_height_input);
        heightInput.setFocusable(false);
        heightInput.setOnTouchListener((v1, event) -> {

            if (event.getAction() == MotionEvent.ACTION_UP) {
                HeightPickerDialog dialog = new HeightPickerDialog(requireActivity(), this, currentUser.getUser_hgt());
                dialog.show();
            }
            return false;
        });

        countryInput = v.findViewById(R.id.edit_profile_country_input);
        Objects.requireNonNull(countryInput).setOnFocusChangeListener(clearInputListener);


        if (currentUser.getUser_name() != null)
            nickNameInput.setText(currentUser.getUser_name());


        String dob = currentUser.getPrettyDob();
        if (dob != null)
            dobInput.setText(dob);


        if (currentUser.getUser_gender() != null)
            genderInput.setText(currentUser.getUser_gender());

        if (currentUser.getUser_hgt() != null) {
            Float hgt = currentUser.getUser_hgt();
            int ft = Math.round(hgt) / 12;
            float in = hgt - ft * 12.0f;
            String hgtStr = "" + ft + "' " + in + "\"";
            heightInput.setText(hgtStr);
        }

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

        ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {

                readRequestPermissionLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });

        // Permission is granted. Continue to take picture
        ActivityResultLauncher<String> cameraRequestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
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
                    MainActivity.hideKeyboard(requireActivity());
                    Log.i("touch event", v.toString());
                    v.clearFocus();
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
            MainActivity.commitFragmentTransaction(requireActivity(), R.id.fragmentHolder, new ProfileFragment());
        }

        if (viewID == R.id.edit_profile_back_btn) {
            MainActivity.commitFragmentTransaction(requireActivity(), R.id.fragmentHolder, new ProfileFragment());
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
            File rt = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

        User currentUser = modelViewUser.getUser();
        assert currentUser != null;

        currentUser.setUser_name(getEditTextString(nickNameInput));
        currentUser.setUser_dob(getEditTextString(dobInput));
        currentUser.setUser_city(getEditTextString(cityInput));
        currentUser.setUser_country(getEditTextString(countryInput));
        currentUser.setUser_gender(getEditTextString(genderInput));
        currentUser.setCurrent_wgt(Float.parseFloat(getEditTextString(weightInput)));

        User_API_Controller.updateUserProfile(currentUser, context);
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    protected void datePickerCallBack(String dateString) {
        dobInput.setText(dateString);
    }

    public void heightChangeCallback(int ft, float in) {
        modelViewUser.getUser().setUser_hgt((float) ft * 12 + in);
        String heightTxt = "" + ft + "' " + in + "\"";
        heightInput.setText(heightTxt);
    }

    public void genderChangeCallback(String gender) {
        genderInput.setText(gender);
        modelViewUser.getUser().setUser_gender(gender);
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
