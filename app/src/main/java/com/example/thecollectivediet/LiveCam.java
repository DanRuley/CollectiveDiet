package com.example.thecollectivediet;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

public class LiveCam extends AppCompatActivity {

    PreviewView mCameraView;
    Button takePic;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_camera);

        Button takePic = findViewById(R.id.take_pic);
    }
}
