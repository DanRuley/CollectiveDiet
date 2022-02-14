package com.example.thecollectivediet.Profile_Fragment_Components;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.thecollectivediet.Me_Fragment_Components.MeFragmentAdapter;
import com.example.thecollectivediet.Me_Fragment_Components.MeTabLayoutFragment;
import com.example.thecollectivediet.Me_Fragment_Components.TodayFragment;
import com.example.thecollectivediet.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<String> cameraRequestPermissionLauncher;
    private ActivityResultLauncher<String> readRequestPermissionLauncher;

    private String profilePicPath;

    private boolean photoChanged;

    private ActivityResultContracts.RequestMultiplePermissions requestMultiplePermissionsContract;
    private ActivityResultLauncher<String[]> multiplePermissionActivityResultLauncher;

    final String[] PERMISSIONS = {
           // Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };
    /*
    Shared Preferences
        profile_pic = profile pic
        profile_first = first name
        profile_last = last name
        profile_age = age
        profile_sex = sex
        profile_weight = weight
        profile_height = height
        profile_city = city
        profile_country = country
     */
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;

    //buttons
    AppCompatButton editPhoto;
    AppCompatButton saveChanges;
    AppCompatButton back_button;

    //editViews
    EditText mFirstName;
    EditText mLastName;
    EditText mAge;
    EditText mSex;
    EditText mWeight;
    EditText mHeight;
    EditText mCity;
    EditText mCountry;

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


        //fill out fields if available
        mFirstName = v.findViewById(R.id.ev_firstname);
        mFirstName.setHint("first name: " + prefs.getString("profile_first", ""));
        mLastName = v.findViewById(R.id.ev_lastname);
        mLastName.setHint("last name: " + prefs.getString("profile_last", ""));
        mAge = v.findViewById(R.id.ev_age);
        mAge.setHint("age: " + prefs.getString("profile_age", ""));
        mSex = v.findViewById(R.id.ev_sex);
        mSex.setHint("sex: " + prefs.getString("profile_sex", ""));
        mWeight = v.findViewById(R.id.ev_weight);
        mWeight.setHint("weight: " + prefs.getString("profile_weight", ""));
        mHeight = v.findViewById(R.id.ev_height);
        mHeight.setHint("height: " + prefs.getString("profile_height", ""));
        mCity = v.findViewById(R.id.ev_city);
        mCity.setHint("city: " + prefs.getString("profile_city", ""));
        mCountry = v.findViewById(R.id.ev_country);
        mCountry.setHint("country: " + prefs.getString("profile_country", ""));

        saveChanges = v.findViewById(R.id.ac_button_savechanges);
        saveChanges.setOnClickListener(this);

        photo = v.findViewById(R.id.profile_image);
        profilePicPath = prefs.getString("profile_pic", null);
        Bitmap thumbnailPic = BitmapFactory.decodeFile(profilePicPath);
        if(thumbnailPic != null){
            photo.setImageBitmap(thumbnailPic);
        }


        back_button = v.findViewById(R.id.iv_backbutton);
        back_button.setOnClickListener(this);

////////////////////////////////////////////////////////////////////
        requestMultiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        multiplePermissionActivityResultLauncher = registerForActivityResult(requestMultiplePermissionsContract, isGranted -> {
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
                        // Permission is granted. Continue to save profile picture

//                        if (isExternalStorageWritable()) {
//                            saveProfileImage(bitmap);
//                        }

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
        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ac_button_editphoto: {

                //Open camera

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraActivityResultLauncher.launch(cameraIntent);/////////////////

                break;
            }

            case R.id.ac_button_savechanges: {

                if (isExternalStorageWritable()) {
                    saveProfileImage(bitmap);
                }
                saveStats();

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ProfileFragment frag = new ProfileFragment();
                transaction.replace(R.id.fragmentHolder, frag);
                transaction.addToBackStack(null);
                transaction.commit();

                break;
            }

            case R.id.iv_backbutton:{
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                ProfileFragment frag = new ProfileFragment();
//                transaction.replace(R.id.fragmentHolder, frag);
//                transaction.addToBackStack(null);
//                transaction.commit();

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MeTabLayoutFragment frag = new MeTabLayoutFragment(2);
                transaction.replace(R.id.fragmentHolder, frag);
                transaction.addToBackStack(null);
                transaction.commit();


                break;
            }

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

        if(photoChanged) {
//            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//            //File root = Environment.getExternalStorageDirectory();
//
//            String m = Environment.getExternalStorageState();
//            File myDir = new File(root, "/temp_saved_images");
//
//            String fname = "Thumbnail_profile.jpg";
            //File root = Environment.getExternalStorageDirectory();

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

    private void saveStats(){
        if(!isEditTextEmpty(mFirstName))
            editor.putString("profile_first", mFirstName.getText().toString());

        if(!isEditTextEmpty(mLastName))
            editor.putString("profile_last", mLastName.getText().toString());

        if(!isEditTextEmpty(mAge))
            editor.putString("profile_age", mAge.getText().toString());

        if(!isEditTextEmpty(mSex))
            editor.putString("profile_sex", mSex.getText().toString());

        if(!isEditTextEmpty(mWeight))
            editor.putString("profile_weight", mWeight.getText().toString());

        if(!isEditTextEmpty(mHeight))
            editor.putString("profile_height", mHeight.getText().toString());

        if(!isEditTextEmpty(mCity))
            editor.putString("profile_city", mCity.getText().toString());

        if(!isEditTextEmpty(mCountry))
            editor.putString("profile_country", mCountry.getText().toString());
        editor.commit();
    }

    private boolean isEditTextEmpty(EditText et){
        if(et.getText().toString().matches("")){
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
