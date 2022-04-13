package com.example.thecollectivediet.Camera_Fragment_Components;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.thecollectivediet.MainActivity;
import com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing.ManualFoodSearch;
import com.example.thecollectivediet.R;
import com.example.thecollectivediet.ml.LiteModelAiyVisionClassifierFoodV11;
import com.google.common.util.concurrent.ListenableFuture;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class CameraFragment extends Fragment implements View.OnClickListener {

    //Buttons and views
    View v;
    PreviewView previewView;
    Button takePic;

    //camera
    Camera camera;
    Preview preview;
    CameraSelector cameraSelector;
    ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_camera, container, false);

        //hook elements
        previewView = v.findViewById(R.id.view_camera);
        takePic = v.findViewById(R.id.take_pic);
        takePic.setOnClickListener(this);


        if (ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());

            cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider, v);
                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }, ContextCompat.getMainExecutor(getActivity()));
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA);
        }

        return v;
    }

    // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());

                    cameraProviderFuture.addListener(() -> {
                        try {
                            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
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

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider, @NonNull View view) {

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

        camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, preview);
    }

    private void initializeComponents(@NonNull View view) {
        //views
        previewView = view.findViewById(R.id.view_camera);


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

                imageCapture.takePicture(ContextCompat.getMainExecutor(getActivity()), new ImageCapture.OnImageCapturedCallback() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy imageProxy) {
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
                        MainActivity.commitFragmentTransaction(getActivity(), R.id.fragmentHolder, new ManualFoodSearch());
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException e) {
                        super.onError(e);
                    }
                });
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Bitmap resizeBitmap(@NonNull Bitmap bitmap, int newHeight, int newWidth) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float scaleHeight = ((float) newHeight) / height;
        float scaleWidth = ((float) newWidth) / width;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        ColorSpace srbg = ColorSpace.get(ColorSpace.Named.SRGB);
        return newBitmap;
    }

    /**
     * Uses Tensorflow lite model to classify the type of food in the bitmap.
     *
     * @param bitmap bitmap does not need resizing
     * @return String prediction
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prediction;
    }

    /*
    Converts ImageProxy to Bitmap
     */
    private Bitmap convertImageProxyToBitmap(@NonNull ImageProxy image) {
        ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
        byteBuffer.rewind();
        byte[] bytes = new byte[byteBuffer.capacity()];
        byteBuffer.get(bytes);
        byte[] clonedBytes = bytes.clone();
        return BitmapFactory.decodeByteArray(clonedBytes, 0, clonedBytes.length);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.take_pic:{

                imageCapture.takePicture(ContextCompat.getMainExecutor(getActivity()), new ImageCapture.OnImageCapturedCallback() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy imageProxy) {
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
                        MainActivity.commitFragmentTransaction(getActivity(), R.id.fragmentHolder, new ManualFoodSearch());
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException e) {
                        super.onError(e);
                    }
                });
            }
        }
    }
}
