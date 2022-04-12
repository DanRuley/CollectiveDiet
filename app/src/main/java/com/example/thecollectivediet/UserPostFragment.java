package com.example.thecollectivediet;

import static android.content.ContentValues.TAG;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.checkerframework.checker.units.UnitsTools.min;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.example.thecollectivediet.API_Utilities.User_API_Controller;
import com.example.thecollectivediet.Share.SharedFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
//import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class UserPostFragment extends Fragment implements View.OnClickListener {

    //Elements
    private GridView gridView; //contains pictures of user on device
    private ProgressBar progressBar;
    private TextView post; //post button to post comment and/or image
    private ImageView postImage; //image to post
    private EditText postComment; //comment to post
    private AppCompatButton addImageToPostButton; //Lets user add image to post

    //temp
    private ImageView postImage2;
    private int imageCode = 1;

    //constants
    private static final int NUM_GRID_COLS = 3;
    private static final String append = "file:/";
    private ViewModelUser viewModelUser;
    //ActivityResultLauncher<String> requestPermissionLauncher;

    //variables
    private String directories;
    FilePaths filePaths;
    String imageToPost;
    ArrayList<String> imgURLs;
    String imageToPostKey;
    int imageFlag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_post, container, false);

        viewModelUser = new ViewModelProvider(requireActivity()).get(ViewModelUser.class);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        imgURLs = new ArrayList<>();


        //hook elements
        gridView = v.findViewById(R.id.gv_post_grid);
        postImage = v.findViewById(R.id.iv_post_image);
        postImage.setImageDrawable(null);
        postComment = v.findViewById(R.id.et_post_comment);
        addImageToPostButton = v.findViewById(R.id.btn_post_add_image);
        addImageToPostButton.setOnClickListener(this);

        /////////////////////temp
        // postImage2 = v.findViewById(R.id.iv_post_image2);

//        progressBar = v.findViewById(R.id.pb_post_progressBar);
//        progressBar.setVisibility(View.GONE);

        post = v.findViewById(R.id.tv_post_btn);
        post.setOnClickListener(this);

        //initialize variables
        imageFlag = 0;
        //directories = new ArrayList<>();

//        checkForPhotos();
        filePaths = new FilePaths();

        // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher, as an instance variable.
//        requestPermissionLauncher =
//                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//                    if (isGranted) {
//                        // Permission is granted. Continue the action or workflow in your
//                        // app.
//                        checkForPhotos();
//                        setupGridView(filePaths.Camera);
//                    } else {
//                        // Explain to the user that the feature is unavailable because the
//                        // features requires a permission that the user has denied. At the
//                        // same time, respect the user's decision. Don't link to system
//                        // settings in an effort to convince the user to change their
//                        // decision.
//                    }
//                });

        if (ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            checkForPhotos();
           // setupGridView(directories);
        }
//        else if (shouldShowRequestPermissionRationale(...)) {
//            // In an educational UI, explain to the user why your app requires this
//            // permission for a specific feature to behave as expected. In this UI,
//            // include a "cancel" or "no thanks" button that allows the user to
//            // continue using your app without granting the permission.
//            showInContextUI(...);
//        }
        else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        return v;
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
    registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your
            // app.
            checkForPhotos();
            //setupGridView(directories);
        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
        }
    });
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_post_btn: {
                //downloadFIle();
                uploadFile();
                MainActivity.commitFragmentTransaction(getActivity(), R.id.fragmentHolder, new SharedFragment());
                break;
            }
            case R.id.btn_post_add_image: {
//                requestPermissionLauncher.launch(
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                String[] projection = new String[] {
                        MediaStore.Images.Media.DATA
                };

                Cursor cursor = getContext().getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        null,
                        null,
                        null
                );

                while (cursor.moveToNext()) {

                    String y = cursor.getString(0);
                    imgURLs.add(y);
                    setupGridView();
                    // Use an ID column from the projection to get
                    // a URI representing the media item itself.
                }

//                if (ContextCompat.checkSelfPermission(
//                        getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
//                        PackageManager.PERMISSION_GRANTED) {
//                    // You can use the API that requires the permission.
//                    checkForPhotos();
//                    setupGridView(filePaths.Camera);
//                }
////        else if (shouldShowRequestPermissionRationale(...)) {
////            // In an educational UI, explain to the user why your app requires this
////            // permission for a specific feature to behave as expected. In this UI,
////            // include a "cancel" or "no thanks" button that allows the user to
////            // continue using your app without granting the permission.
////            showInContextUI(...);
////        }
//                else {
//                    // You can directly ask for the permission.
//                    // The registered ActivityResultCallback gets the result of this request.
//                    requestPermissionLauncher.launch(
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                }

                break;
            }
        }
    }

    private void checkForPhotos() {
      //  FilePaths filePaths = new FilePaths();

        //check for other folders inside "/storage/emulated/0/pictures"
//        if(FileSearch.getDirectoryPaths(filePaths.Pictures) != null){
//            directories = filePaths.Pictures;
//        }
//        else {
//            directories = filePaths.Camera;
//        }
        directories = filePaths.Camera;
    }

    private void setupGridView() {
        Log.d(TAG, "setting up grid view");
        //imgURLs = FileSearch.getFilePath(selectedDirectory);

        //set the grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_GRID_COLS;
        gridView.setColumnWidth(imageWidth);


        //grid image adapter
        GridImageAdapter gridImageAdapter = new GridImageAdapter(getContext(), R.layout.layout_grid_imageview, append, imgURLs);
        gridView.setAdapter(gridImageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected an image: " + imgURLs.get(position));
                setImage(imgURLs.get(position), postImage, append);
                //imageToPost = append + imgURLs.get(position);

                //set up image to upload
                imageToPost = imgURLs.get(position);
                //uploadFile(position);

                //uploadInputStream(imageToPost);
                imageFlag = 1;
            }
        });
    }

    private void setImage(String imgURL, ImageView image, String append) {
        Log.d(TAG, "setImage: setting image");

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                // progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                // progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }


    private void uploadFile() {

        String comment = postComment.getText().toString();
        if (!comment.matches("") || imageFlag != 0) {


            if (imageFlag != 0) {
//        File exampleFile = new File(imgURLs.get(position));
                File exampleFile = new File(imageToPost);

//        String imagePath = imgURLs.get(position).toString();
//        String[] imageName = imagePath.split("/");

                String[] imageName = imageToPost.split("/");

                // nextInt is normally exclusive of the top value,
                // so add 1 to make it inclusive
                int randomNum = ThreadLocalRandom.current().nextInt(min, 2000000000 + 1);
                String ran = String.valueOf(randomNum);

                imageToPostKey = ran + "_" + imageName[imageName.length - 1];

                Amplify.Storage.uploadFile(

                        imageToPostKey,
                        exampleFile,
                        result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),

                        storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
                );

                Amplify.Storage.getUrl(
                        imageToPostKey,
                        result -> {
                            Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());
                            User_API_Controller.pushUserPost(viewModelUser.getUser().getUser_id(), imageToPostKey, comment, getContext(), result.getUrl().toString(), viewModelUser.getUser().getUser_name());
                        },
                        error -> Log.e("MyAmplifyApp", "URL generation failure", error)
                );
            } else {

                User_API_Controller.pushUserPost(viewModelUser.getUser().getUser_id(), imageToPostKey, comment, getContext(), viewModelUser.getUser().getUser_name());
            }
        }

    }

    private void downloadFile() {
        Amplify.Storage.downloadFile(
                imageToPostKey,
                new File(getContext().getFilesDir() + "/download.txt"),
                //result -> Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName()),
                result -> setImage(result.getFile()),
                error -> Log.e("MyAmplifyApp", "Download Failure", error)
        );

    }

    private void setImage(File file) {


        String filePath = file.getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        // postImage2.setImageBitmap(bitmap);

//        ImageLoader imageLoader = ImageLoader.getInstance();
//
//        imageLoader.displayImage(new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//                progressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                progressBar.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                progressBar.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//                progressBar.setVisibility(View.INVISIBLE);
//            }
//        });
        //}
    }
}