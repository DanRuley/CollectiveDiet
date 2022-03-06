package com.example.thecollectivediet.Profile_Fragment_Components;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.thecollectivediet.API_Utilities.User_API_Controller;
import com.example.thecollectivediet.JSON_Marshall_Objects.User;
import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<String> cameraRequestPermissionLauncher;
    private ActivityResultLauncher<String> readRequestPermissionLauncher;

    private boolean photoChanged;

    final String[] PERMISSIONS = {
            // Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        //took new photo?
        photoChanged = false;

        context = this.getActivity();
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


        back_button = v.findViewById(R.id.edit_profile_back_btn);
        back_button.setOnClickListener(this);

////////////////////////////////////////////////////////////////////
        ActivityResultContracts.RequestMultiplePermissions requestMultiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        //multiplePermissionActivityResultLauncher.launch(PERMISSIONS);
        ActivityResultLauncher<String[]> multiplePermissionActivityResultLauncher = registerForActivityResult(requestMultiplePermissionsContract, isGranted -> {
            Log.d("PERMISSIONS", "Launcher result: " + isGranted.toString());
            if (isGranted.containsValue(false)) {
                Log.d("PERMISSIONS", "At least one of the permissions was not granted, launching again...");
                //multiplePermissionActivityResultLauncher.launch(PERMISSIONS);
            }
        });


        //askPermissions(PERMISSIONS);

        /////////////////////////////////////////////////////////////////////////////////////////////
        //ActivityLauncher for camera
        cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == RESULT_OK) {
                            //get intent data from result
                            Intent data = result.getData();

                            //get the bitmap
                            photoChanged = true;
                            Bundle extras = data.getExtras();
                            bitmap = (Bitmap) extras.get("data");
                            //saveTempProfileImage(bitmap);
                            photo.setImageBitmap(bitmap);
                        }
                    }

                });


        // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher, as an instance variable.

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {

                        readRequestPermissionLauncher.launch(
                                Manifest.permission.READ_EXTERNAL_STORAGE);


                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });

        cameraRequestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue to take picture

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraActivityResultLauncher.launch(cameraIntent);

                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });

        readRequestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue to take picture

                        if (isExternalStorageWritable()) {
                            saveProfileImage(bitmap);
                        }

                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });

        multiplePermissionActivityResultLauncher.launch(PERMISSIONS);

        setupUI(v);
        return v;
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    MainActivity.hideKeyboard(Objects.requireNonNull(getActivity()));
                    v.performClick();
                    return false;
                }
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
    public void onClick(View v) {

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
    private String saveImage(Bitmap finalBitmap) {

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
    private void saveProfileImage(Bitmap finalBitmap) {

        if (photoChanged) {


            File rt = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            //File root = context.getFilesDir();
            String m = Environment.getExternalStorageState();
            File myDir = new File(root, "/saved_images1");
            //myDir.mkdirs();
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

    private void saveProfileChanges() {

        User currentUser = MainActivity.getCurrentUser();

        User_API_Controller.updateUserProfile(currentUser, context);

        editor.commit();
    }

    private boolean isEditTextEmpty(EditText et) {
        if (et.getText().toString().matches("")) {
            return true;
        }
        return false;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
