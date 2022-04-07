package com.example.thecollectivediet;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class UserPostFragment extends Fragment implements View.OnClickListener {

    //Elements
    private GridView gridView;
    private ProgressBar progressBar;
    private TextView post;
    private ImageView postImage;

    //constants
    private static final int NUM_GRID_COLS = 3;
    private static final String append = "file:/";
    //variables
    private ArrayList<String> directories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_post, container, false);

        //hook elements
        gridView = v.findViewById(R.id.gv_post_grid);
        postImage = v.findViewById(R.id.iv_post_image);

        progressBar = v.findViewById(R.id.pb_post_progressBar);
        progressBar.setVisibility(View.GONE);

        post = v.findViewById(R.id.tv_post_btn);
        post.setOnClickListener(this);

        //initialize variables
        directories = new ArrayList<>();

        checkForPhotos();


        return v;
    }

    @Override
    public void onClick(View v) {

    }

    private void checkForPhotos(){
        FilePaths filePaths = new FilePaths();
//        if(FileSearch.getDirectoryPaths(filePaths.Pictures) != null){
//            directories = FileSearch.getDirectoryPaths(filePaths.Pictures);
//        }

        directories.add(filePaths.Camera);
    }

    private void setupGridView(String selectedDirectory){
        Log.d(TAG, "setting up grid view");
        final ArrayList<String> imgURLs = FileSearch.getFilePath(selectedDirectory);

        //set the grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLS;
        gridView.setColumnWidth(imageWidth);

        //grid image adapter
        GridImageAdapter gridImageAdapter = new GridImageAdapter(getContext(), R.layout.layout_grid_imageview, append, imgURLs);
        gridView.setAdapter(gridImageAdapter);
    }

    private void temp(){
        ArrayList<String> imgs = new ArrayList<>();
        imgs.add("https://www.google.com/imgres?imgurl=https%3A%2F%2Fstatic.wikia.nocookie.net%2Fmetroid%2Fimages%2Fe%2Fe6%2FZamus_art.png%2Frevision%2Flatest%3Fcb%3D20210829043054&imgrefurl=https%3A%2F%2Fmetroid.fandom.com%2Fwiki%2FZero_Suit_Samus&tbnid=I56pqnswK9_b2M&vet=12ahUKEwi9q8zA-ID3AhXhGDQIHeOfAg0QMygBegUIARDdAQ..i&docid=6UJ8rHkaAZjsbM&w=3337&h=4960&q=samus&ved=2ahUKEwi9q8zA-ID3AhXhGDQIHeOfAg0QMygBegUIARDdAQ");
        //setupGridView(imgs);
    }
}