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

public class tricep1 extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private PreviewView previewView;
    private Interpreter tflite;
    private AutoModel4 model;
    private Paint paint;
    private OverlayView_Tri overlayViewTri;

    private int extensionCount = 0;
    private int extensionDirection = 0; // 0 = arm extending, 1 = arm flexing
    private float elbowAngle = 0;
    private long pTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tricep1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button resetButton = findViewById(R.id.reset_button);

        previewView = findViewById(R.id.preview_view);
        overlayViewTri = findViewById(R.id.tricep_overlay_view);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        try {
            model = AutoModel4.newInstance(tricep1.this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startCamera();
        }

        resetButton.setOnClickListener(v -> {
            extensionCount = 0; // Reset counter
            extensionDirection = 0; // Reset direction
            elbowAngle = 0; // Optionally reset elbow angle
            overlayViewTri.updateCount(extensionCount, elbowAngle);
            Log.d("TricepExtensionCounter", "Extension counter reset to 0");
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

        if (keypoints[6][2] > confidenceThreshold && keypoints[8][2] > confidenceThreshold && keypoints[10][2] > confidenceThreshold) {
            // Calculate elbow angle (right arm)
            elbowAngle = calculateAngle(keypoints[6], keypoints[8], keypoints[10]);

            // Count tricep extensions based on elbow angle
            if (elbowAngle > 160) { // Arm fully extended
                if (extensionDirection == 1) {
                    extensionCount++;
                    extensionDirection = 0;
                    Log.d("TricepExtensionCounter", "Extension Count: " + extensionCount);
                }
            } else if (elbowAngle < 45) { // Arm flexed
                if (extensionDirection == 0) {
                    extensionDirection = 1;
                    Log.d("TricepExtensionCounter", "Elbow Angle: " + elbowAngle + ", Start Extending");
                }
            }
        } else {
            Log.d("Keypoint", "Keypoints for elbow angle have low confidence");
            elbowAngle = 0;
        }

        overlayViewTri.updateKeypoints(keypoints);
        overlayViewTri.updateCount(extensionCount, elbowAngle);
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

    private Bitmap convertImageProxyToBitmap(ImageProxy image) {
        ImageProxy.PlaneProxy[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];

        // U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 100, out);
        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1 * 256 * 256 * 3);
        byteBuffer.order(ByteOrder.nativeOrder());

        int[] intValues = new int[256 * 256];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        int pixel = 0;
        for (int i = 0; i < 256; ++i) {
            for (int j = 0; j < 256; ++j) {
                final int val = intValues[pixel++];  // Get color of the pixel
                // Extract RGB values and normalize them to [0, 1]
                byteBuffer.put((byte) ((val >> 16) & 0xFF));  // R
                byteBuffer.put((byte) ((val >> 8) & 0xFF));   // G
                byteBuffer.put((byte) (val & 0xFF));          // B
            }
        }
        return byteBuffer;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (model != null) {
            model.close();
        }
    }
}