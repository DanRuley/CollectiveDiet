package com.example.thecollectivediet.Camera_Fragment_Components;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.Executors;

import com.example.thecollectivediet.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class CameraFragment extends Fragment {

    //Buttons and views
    PreviewView previewView;
    Button takePic;
    ImageView imageView2;

    //camera
    Preview preview;
    CameraSelector cameraSelector;
    ImageCapture imageCapture;
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

         preview = new Preview.Builder()
                .build();

         cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

         imageCapture =
                new ImageCapture.Builder()
                        .setTargetRotation(view.getDisplay().getRotation())
                        .build();

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, imageCapture, preview);
    }

    private void initializeComponents(View view)
    {
        //views
        previewView = view.findViewById(R.id.view_camera);
        imageView2 = view.findViewById(R.id.imageView2);

        //buttons
        takePic = view.findViewById(R.id.take_pic);

        /*
        Listener for "take pic" button in camera view. This listener will take a pic,
        place pic in an image view, and then be used for inference using Tensorflow lite.
        A string will be returned to be used in the food search function.
        */
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageCapture.takePicture(ContextCompat.getMainExecutor(getActivity()), new ImageCapture.OnImageCapturedCallback(){
                    @Override
                    public void onCaptureSuccess(ImageProxy imageProxy){


                        //Change imageProxy to bitmap to be used with imageView
                        Bitmap bit = convertImageProxyToBitmap(imageProxy);
                        imageView2.setImageBitmap(bit);

                        //Make sure to close the image buffer for next pic
                        imageProxy.close();

                        //todo send the pic out for inference

                    }
                    @Override
                    public void onError(ImageCaptureException e){

                        super.onError(e);
                    }
                });
            }
        });
    }

    /*
    Converts ImageProxy to Bitmap
     */
    private Bitmap convertImageProxyToBitmap(ImageProxy image) {
        ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
        byteBuffer.rewind();
        byte[] bytes = new byte[byteBuffer.capacity()];
        byteBuffer.get(bytes);
        byte[] clonedBytes = bytes.clone();
        return BitmapFactory.decodeByteArray(clonedBytes, 0, clonedBytes.length);
    }


}
