package com.example.thecollectivediet;

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;

import java.util.List;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class CameraFragment extends Fragment {

    //Buttons and views
    PreviewView previewView;
    Button takePic;

    ProcessCameraProvider cameraProvider;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_camera, container, false);

        //initialize components
        initializeComponents(v);

        //set up preview, imageCapture,
        cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider, v);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(getActivity()));




        return v;
    }
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider, View view) {

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageCapture imageCapture =
                new ImageCapture.Builder()
                        .setTargetRotation(view.getDisplay().getRotation())
                        .build();

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, imageCapture, preview);
    }

    private void initializeComponents(View view)
    {
        previewView = view.findViewById(R.id.view_camera);
        takePic = view.findViewById(R.id.take_pic);

    }
}
