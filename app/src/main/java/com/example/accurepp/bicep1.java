package com.example.accurepp;

import android.Manifest;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.accurepp.ml.AutoModel4;
import com.google.common.util.concurrent.ListenableFuture;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutionException;

public class bicep1 extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private PreviewView previewView;
    private Interpreter tflite;
    private AutoModel4 model;
    private Paint paint;
    private OverlayView_Bic overlayView;

    private int curlCount = 0;
    private int curlDirection = 0; // 0 = arm extending, 1 = arm curling
    private float elbowAngle = 0;
    private long pTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bicep1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button resetButton = findViewById(R.id.reset_button);

        previewView = findViewById(R.id.preview_view);
        overlayView = findViewById(R.id.bicep_curl_overlay_view);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        try {
            model = AutoModel4.newInstance(bicep1.this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startCamera();
        }

        resetButton.setOnClickListener(v -> {
            curlCount = 0; // Reset the counter
            curlDirection = 0; // Reset direction if needed
            elbowAngle = 0; // Optionally reset elbow angle
            overlayView.updateCount(curlCount, elbowAngle);
            Log.d("BicepCurlCounter", "Curl counter reset to 0");
        });
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();

                Preview preview = new Preview.Builder().setTargetResolution(new Size(1280, 720)).build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(720, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), image -> {
                    analyzeImage(image);
                    image.close();
                });

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                        .build();

                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void analyzeImage(@NonNull ImageProxy image) {
        Bitmap bitmap = convertImageProxyToBitmap(image);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, true);

        ByteBuffer byteBuffer = convertBitmapToByteBuffer(resizedBitmap);

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

                // Extract RGB channels from the pixel and store them as bytes in the ByteBuffer
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
        float[][] keypoints = new float[keypointCount][3];

        for (int i = 0; i < keypointCount; i++) {
            keypoints[i][0] = output[i * 3];     // x-coordinate
            keypoints[i][1] = output[i * 3 + 1]; // y-coordinate
            keypoints[i][2] = output[i * 3 + 2]; // confidence score
        }

        float confidenceThreshold = 0.03f;

        if (keypoints[5][2] > confidenceThreshold && keypoints[7][2] > confidenceThreshold && keypoints[9][2] > confidenceThreshold) {
            // Calculate elbow angle (right arm)
            elbowAngle = calculateAngle(keypoints[5], keypoints[7], keypoints[9]);

            // Count bicep curls based on elbow angle
            if (elbowAngle > 170) { // Arm extended
                if (curlDirection == 1) {
                    curlCount++;
                    curlDirection = 0;
                    Log.d("BicepCurlCounter", "Curl Count: " + curlCount);
                }
            } else if (elbowAngle < 40) { // Arm flexed
                if (curlDirection == 0) {
                    curlDirection = 1;
                    Log.d("BicepCurlCounter", "Elbow Angle: " + elbowAngle + ", Start Curling");
                }
            }
        } else {
            Log.d("Keypoint", "Keypoints for elbow angle have low confidence");
            elbowAngle = 0;
        }

        overlayView.updateKeypoints(keypoints);
        overlayView.updateCount(curlCount, elbowAngle);
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