package com.example.accurepp;

import static org.opencv.android.Utils.matToBitmap;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.impl.ImageAnalysisConfig;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.TextView;

import android.util.Size;
import androidx.camera.core.ExperimentalGetImage;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.support.common.ops.NormalizeOp;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Objects;

import androidx.camera.lifecycle.ProcessCameraProvider;
import com.google.common.util.concurrent.ListenableFuture;

import com.example.accurepp.ml.AutoModel4;
//import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.pose.PoseDetector;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class squat1 extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private PreviewView previewView;
    private Interpreter tflite;
    private AutoModel4 model;
    private Paint paint;
    private OverlayView overlayView;

    private int count = 0;
    private int direction = 0; // 0 = going down, 1 = coming up
    private float kneeAngle = 0;
    private long pTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_squat1);

        Button resetButton = findViewById(R.id.reset_button);

        previewView = findViewById(R.id.preview_view);
        overlayView = findViewById(R.id.overlay_view);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        if (previewView == null) {
            Log.e("CameraPreview", "PreviewView is null!");
            return;
        }


        try {
            model = AutoModel4.newInstance(squat1.this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startCamera();
        }

        resetButton.setOnClickListener(v -> {
            count = 0; // Reset the counter
            direction = 0; // Reset direction if needed
            kneeAngle = 0; // Optionally reset knee angle if needed
            overlayView.updateCount(count, kneeAngle); // Update the overlay view
            Log.d("SquatCounter", "Counter reset to 0");
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start the camera
                startCamera();
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
                Toast.makeText(this, "Camera permission is required to use the camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Unbind all use cases
                cameraProvider.unbindAll();

                //  preview use case
                Preview preview = new Preview.Builder()
                        .setTargetResolution(new Size(1280, 720)) // Set a standard preview resolution
                        .build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // image analysis use case
                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(720, 720)) // Ensure a compatible resolution
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), image -> {
                    analyzeImage(image);
                    image.close();
                });

                // front-facing camera
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                        .build();

                // Bind both Preview and ImageAnalysis
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

            } catch (ExecutionException | InterruptedException e) {

                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }


    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_FRONT_CAMERA, preview);
    }

    private void bindImageAnalysis(@NonNull ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(720, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), image -> {
            analyzeImage(image);
            image.close();
        });

        cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_FRONT_CAMERA, imageAnalysis);
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void analyzeImage(@NonNull ImageProxy image) {

        // Convert ImageProxy to Bitmap
        Image img = image.getImage();
        if (img == null) return;

        Bitmap bitmap = convertImageProxyToBitmap(image);  // image proxy to Bitmap

        // Resize the Bitmap to 256x256
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, true);

        //  Bitmap to ByteBuffer (RGB format)
        ByteBuffer byteBuffer = convertBitmapToByteBuffer(resizedBitmap);

        // input tensor for the model
        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.UINT8);
        inputFeature0.loadBuffer(byteBuffer);


        AutoModel4.Outputs outputs = model.process(inputFeature0);
        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();


        processOutput(outputFeature0);


        image.close();
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private Bitmap convertImageProxyToBitmap(ImageProxy image) {
        Image img = image.getImage();
        if (img == null) return null;

        Image.Plane[] planes = img.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        // Convert to Bitmap (assuming YUV_420_888 format)
        YuvImage yuvImage = new YuvImage(bytes, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 100, out);
        byte[] byteArray = out.toByteArray();
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(256 * 256 * 3);
        byteBuffer.order(ByteOrder.nativeOrder());

        int[] intValues = new int[256 * 256];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        int pixel = 0;
        for (int i = 0; i < 256; ++i) {
            for (int j = 0; j < 256; ++j) {
                final int val = intValues[pixel++];


                byteBuffer.put((byte) ((val >> 16) & 0xFF)); // Red
                byteBuffer.put((byte) ((val >> 8) & 0xFF));  // Green
                byteBuffer.put((byte) (val & 0xFF));         // Blue
            }
        }
        return byteBuffer;
    }

    private void processOutput(TensorBuffer outputFeature0) {
        float[] output = outputFeature0.getFloatArray();
        int keypointCount = output.length / 3;

        // Create 2D array for keypoints
        float[][] keypoints = new float[keypointCount][3];

        for (int i = 0; i < keypointCount; i++) {
            keypoints[i][0] = output[i * 3];     // x-coordinate
            keypoints[i][1] = output[i * 3 + 1]; // y-coordinate
            keypoints[i][2] = output[i * 3 + 2]; // confidence score

            Log.d("Keypoint", "X: " + keypoints[i][0] + " Y: " + keypoints[i][1] + " Confidence: " + keypoints[i][2]);
        }

        // Confidence threshold to filter keypoints
        float confidenceThreshold = 0.03f;

        //  confidence of the required keypoints (left hip, left knee, left ankle) is above the threshold
        if (keypoints[11][2] > confidenceThreshold && keypoints[13][2] > confidenceThreshold && keypoints[15][2] > confidenceThreshold) {
            // Calculate knee angle (left side)
            kneeAngle = calculateAngle(keypoints[11], keypoints[13], keypoints[15]);

            // Count squats based on knee angle
            if (kneeAngle > 160) { // Standing position
                if (direction == 1) {
                    count++;
                    direction = 0;
                    Log.d("SquatCounter", "Squat Count: " + count);
                }
            } else if (kneeAngle < 90) { // Squatting position
                if (direction == 0) {
                    direction = 1;
                    Log.d("SquatCounter", "Knee Angle: " + kneeAngle + ", Start Squatting");
                }
            }
        } else {
            // Handle low confidence scenario, skip angle calculation or log a message
            Log.d("Keypoint", "Keypoints for knee angle have low confidence");
            kneeAngle = 0; // Default value when confidence is low
        }

        // Update the overlay view with the keypoints and current squat count and knee angle
        overlayView.updateKeypoints(keypoints);
        overlayView.updateCount(count, kneeAngle);
    }

    private float calculateAngle(float[] p1, float[] p2, float[] p3) {



        float vectorA_x = p1[0] - p2[0];
        float vectorA_y = p1[1] - p2[1];


        float vectorB_x = p3[0] - p2[0];
        float vectorB_y = p3[1] - p2[1];


        float angle = (float) Math.toDegrees(Math.atan2(vectorB_y, vectorB_x) - Math.atan2(vectorA_y, vectorA_x));


        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (model != null) {
            model.close();
        }
    }
}

