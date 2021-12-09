package com.example.thecollectivediet.Camera_Fragment_Components;

import static android.hardware.SensorManager.getOrientation;
import static java.lang.Math.min;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Size;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import com.example.thecollectivediet.Me_Fragment_Components.Food_Logging.ManualFoodSearch;
import com.example.thecollectivediet.R;
import com.example.thecollectivediet.ml.LiteModelAiyVisionClassifierFoodV11;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;

import org.tensorflow.lite.Tensor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

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
    Display display;

    private ActivityResultLauncher<String> requestPermissionLauncher;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, container, false);

        // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher, as an instance variable.
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.

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
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });


        if (ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
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
                    Manifest.permission.CAMERA);
        }


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
                        Bitmap bitmap = convertImageProxyToBitmap(imageProxy);

                        // Get a prediction given the image taken from camera
                        String prediction = classifyImage(bitmap);

                        //Save string in SharedPreferences to use in ManualFoodSearch frag
                        SharedPreferences prefs;
                        SharedPreferences.Editor editor;

                        //Any class in this app can use this
                        prefs = getActivity().getSharedPreferences("TheCollectiveDiet", Context.MODE_PRIVATE);

                        editor = prefs.edit();

                        editor.putString("prediction", prediction);
                        editor.commit();

                        //Make sure to close the image buffer for next picture
                        imageProxy.close();

                        //Switch over to ManualFoodSearch frag
                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragmentHolder, new ManualFoodSearch());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    @Override
                    public void onError(ImageCaptureException e){
                        super.onError(e);
                    }
                });
            }
        });
    }


    public Bitmap resizeBitmap(Bitmap bitmap, int newHeight, int newWidth) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float scaleHeight = ((float) newHeight) / height;
        float scaleWidth = ((float) newWidth) / width;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        return newBitmap;
    }

    /**
     * Uses Tensorflow lite model to classify the type of food in the bitmap.
     * @param bitmap bitmap does not need resizing
     * @return String prediction
     */
    private String classifyImage(Bitmap bitmap) {
        int newWidth = 240;
        int newHeight = 240;
        bitmap = resizeBitmap(bitmap, newWidth, newHeight);
        String prediction = "";
        try {
            // Load model and classify image
            LiteModelAiyVisionClassifierFoodV11 model = LiteModelAiyVisionClassifierFoodV11.newInstance(getActivity());
            // Image input
            TensorImage image = TensorImage.fromBitmap(bitmap);
            // Run classifier
            LiteModelAiyVisionClassifierFoodV11.Outputs outputs = model.process(image);
            List<Category> probability = outputs.getProbabilityAsCategoryList();
            // Find highest label score
            float maxScore = 0;
            for (int i = 0; i < probability.size(); i++) {
                Category currFood = probability.get(i);
                float currScore = currFood.getScore();
                if (currScore > maxScore) {
                    maxScore = currScore;
                    prediction = currFood.getLabel();
                }
            }
            model.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return prediction;
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
