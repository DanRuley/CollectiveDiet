package com.example.thecollectivediet;

import static android.content.ContentValues.TAG;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.options.StorageDownloadFileOptions;
import com.example.thecollectivediet.Share.SharedFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
//import java.io.InputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class UserPostFragment extends Fragment implements View.OnClickListener {

    //Elements
    private GridView gridView;
    private ProgressBar progressBar;
    private TextView post;
    private ImageView postImage;

    //temp
    private ImageView postImage2;
    String tempo;

    //constants
    private static final int NUM_GRID_COLS = 3;
    private static final String append = "file:/";

    //variables
    private ArrayList<String> directories;
    FilePaths filePaths;
    String imageToPost;
    ArrayList<String> imgURLs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_post, container, false);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        imgURLs = new ArrayList<>();


        //hook elements
        gridView = v.findViewById(R.id.gv_post_grid);
        postImage = v.findViewById(R.id.iv_post_image);
        postImage.setOnClickListener(this);

        /////////////////////temp
       // postImage2 = v.findViewById(R.id.iv_post_image2);

//        progressBar = v.findViewById(R.id.pb_post_progressBar);
//        progressBar.setVisibility(View.GONE);

        post = v.findViewById(R.id.tv_post_btn);
        post.setOnClickListener(this);

        //initialize variables
        directories = new ArrayList<>();

        checkForPhotos();
        filePaths = new FilePaths();

        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_post_image: {
                setupGridView(filePaths.Pictures);
                break;
            }
            case R.id.tv_post_btn:{
                //downloadFIle();
                MainActivity.commitFragmentTransaction(getActivity(), R.id.fragmentHolder, new SharedFragment());
                break;
            }
        }
    }

    private void checkForPhotos() {
        FilePaths filePaths = new FilePaths();

        directories.add(filePaths.Camera);
    }

    private void setupGridView(String selectedDirectory) {
        Log.d(TAG, "setting up grid view");
        imgURLs = FileSearch.getFilePath(selectedDirectory);

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
                imageToPost = append + imgURLs.get(position);
                uploadFile(position);
                //uploadInputStream(imageToPost);
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

//    private void uploadInputStream(String position) {
//        try {
//
//            InputStream exampleInputStream = getContext().getContentResolver().openInputStream(Uri.parse(position));
//
//            Amplify.Storage.uploadInputStream(
//                    "ExampleKey",
//                    exampleInputStream,
//                    result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
//                    storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
//            );
//        }  catch (FileNotFoundException error) {
//            Log.e("MyAmplifyApp", "Could not find file to open for input stream.", error);
//        }
//    }

    private void uploadFile(int position) {
        File exampleFile = new File(imgURLs.get(position));

        String s = imgURLs.get(position).toString();
        String [] ss = s.split("/");

        tempo = ss[ss.length-1];

        Amplify.Storage.uploadFile(


                ss[ss.length-1],
                exampleFile,
                result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                //result -> downloadFIle()),
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
    }

    private void downloadFIle() {
        Amplify.Storage.downloadFile(
                tempo,
                new File(getContext().getFilesDir() + "/download.txt"),
                //result -> Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName()),
                result -> setImage(result.getFile()),
                error -> Log.e("MyAmplifyApp",  "Download Failure", error)
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