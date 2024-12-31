package com.example.accurepp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.accurepp.ml.AutoModel4;
import com.example.accurepp.ml.LogisticModelSquat;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;

public class squats extends AppCompatActivity {

    private TextureView textureView;
    private CameraManager cameraManager;
    private Handler handler;
    private HandlerThread handlerThread;
    private ImageView imageView;
    private AutoModel4 model;
    private ImageProcessor imageProcessor;
    private Paint paint;
    private TextView textView;

    private static final int FRAME_INTERVAL = 14;  // Only process every 14th frame
    private int frameCounter = 0;

    // Labels for the keypoints (you can update these based on your model)
    private static final String[] landmarkLabels = {
            "Nose", "Left Eye", "Right Eye", "Left Ear", "Right Ear",
            "Left Shoulder", "Right Shoulder", "Left Elbow", "Right Elbow",
            "Left Wrist", "Right Wrist", "Left Hip", "Right Hip",
            "Left Knee", "Right Knee", "Left Ankle", "Right Ankle"
    };

    private int squatCounter = 0;  // Counter for squats
    private String stage = "UP";   // To track squat stage (UP/DOWN)

    private static final int LEFT_HIP = 11;
    private static final int LEFT_KNEE = 13;
    private static final int LEFT_ANKLE = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squats);

        textureView = findViewById(R.id.textureView);
        imageView = findViewById(R.id.imageView);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);

        imageProcessor = new ImageProcessor.Builder()
                .add(new ResizeOp(256, 256, ResizeOp.ResizeMethod.BILINEAR))
                .build();

        try {
//            model = AutoModel4.newInstance(this);
            model = AutoModel4.newInstance(squats.this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        handlerThread = new HandlerThread("videoThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {}

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {
                frameCounter++;

                // Process every 14th frame to reduce load
                if (frameCounter % FRAME_INTERVAL != 0) {
                    return;
                }

                // Get the bitmap from the texture view
                Bitmap bitmap = textureView.getBitmap();
                if (bitmap != null) {
                    processFrame(bitmap);
                }
            }
        });

        getPermissions();
    }

    // Method to process the camera frame in a background thread
    private void processFrame(Bitmap bitmap) {
        handler.post(() -> {
            TensorImage tensorImage = new TensorImage(DataType.UINT8);
            tensorImage.load(bitmap);
            tensorImage = imageProcessor.process(tensorImage);

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.UINT8);
            inputFeature0.loadBuffer(tensorImage.getBuffer());

            AutoModel4.Outputs outputs = model.process(inputFeature0);
            float[] outputFeature0 = outputs.getOutputFeature0AsTensorBuffer().getFloatArray();

            int h = bitmap.getHeight();
            int w = bitmap.getWidth();

            float[] rawLandmarks = new float[34];
            PointF[] keypoints = new PointF[17];

//            float x_min = 0.0f;
//            float x_max = 1.0f;
//            float y_min = 0.0f;
//            float y_max = 1.0f;

            // Set these to the min/max raw coordinate values in your dataset (before normalization)
            float raw_x_min = 170.0f; // Example minimum X value from your raw data
            float raw_x_max = 600.0f;  // Example maximum X value from your raw data
            float raw_y_min = 400.0f; // Example minimum Y value from your raw data
            float raw_y_max = 950.0f;  // Example maximum Y value from your raw data

            // Target range (0 to 1) as per your normalized dataset
            float target_min = 0.0f;
            float target_max = 1.0f;


            // Parse the output and extract keypoints
            for (int i = 0; i < outputFeature0.length; i += 3) {
                if (outputFeature0[i + 2] > 0.45) {  // confidence threshold
                    float raw_cx = outputFeature0[i + 1] * w;
                    float raw_cy = outputFeature0[i] * h;

                    Log.d("LandmarkDebug", "Raw X: " + raw_cx + ", Raw Y: " + raw_cy);


//                    float scaledCx = x_min + (cx * (x_max - x_min));
//                    float scaledCy = y_min + (cy * (y_max - y_min));

//                    float scaledCx = (cx - raw_x_min) / (raw_x_max - raw_x_min) * (target_max - target_min) + target_min;
//                    float scaledCy = (cy - raw_y_min) / (raw_y_max - raw_y_min) * (target_max - target_min) + target_min;

                    float normalizedCx = (raw_cx - raw_x_min) / (raw_x_max - raw_x_min);
                    float normalizedCy = (raw_cy - raw_y_min) / (raw_y_max - raw_y_min);

                    normalizedCx = Math.max(0.0f, Math.min(1.0f, normalizedCx));
                    normalizedCy = Math.max(0.0f, Math.min(1.0f, normalizedCy));


//                    rawLandmarks[(i / 3) * 2] = scaledCx;       // X coordinate
//                    rawLandmarks[(i / 3) * 2 + 1] = scaledCy;
//                    keypoints[i / 3] = new PointF(scaledCx, scaledCy);

                    rawLandmarks[(i / 3) * 2] = normalizedCx;
                    rawLandmarks[(i / 3) * 2 + 1] = normalizedCy;

                    keypoints[i / 3] = new PointF(normalizedCx, normalizedCy);
                    Log.d("LandmarkDebug", "Normalized X: " + normalizedCx + ", Normalized Y: " + normalizedCy);



                }
            }

//            if (keypoints[LEFT_HIP] != null && keypoints[LEFT_KNEE] != null && keypoints[LEFT_ANKLE] != null) {
////                double kneeAngle = calculateKneeAngle(keypoints[LEFT_HIP], keypoints[LEFT_KNEE], keypoints[LEFT_ANKLE]);
////                squatCounterLogic(kneeAngle);
//            }

            // Log the keypoints to Logcat
            logLandmarks(keypoints);

            drawLandmarks(bitmap, keypoints);
        });
    }

//    private double calculateKneeAngle(PointF hip, PointF knee, PointF ankle) {
//        double hipToKneeX = hip.x - knee.x;
//        double hipToKneeY = hip.y - knee.y;
//        double kneeToAnkleX = ankle.x - knee.x;
//        double kneeToAnkleY = ankle.y - knee.y;
//
//        double dotProduct = (hipToKneeX * kneeToAnkleX + hipToKneeY * kneeToAnkleY);
//        double magnitudeHipToKnee = Math.sqrt(hipToKneeX * hipToKneeX + hipToKneeY * hipToKneeY);
//        double magnitudeKneeToAnkle = Math.sqrt(kneeToAnkleX * kneeToAnkleX + kneeToAnkleY * kneeToAnkleY);
//
//        return Math.acos(dotProduct / (magnitudeHipToKnee * magnitudeKneeToAnkle)) * (180.0 / Math.PI);  // Convert to degrees
//    }

//    private void squatCounterLogic(double kneeAngle) {
//        if (kneeAngle > 160) {
//            stage = "UP";  // If angle is large, the person is in the standing position
//        }
//        if (kneeAngle < 90 && "UP".equals(stage)) {
//            stage = "DOWN";  // Person is squatting down
//            squatCounter++;
//            Log.d("SquatCounter", "Squat count: " + squatCounter);
//
//            // Update UI to display squat count
//            runOnUiThread(() -> {
//                TextView squatCountTextView = findViewById(R.id.squatCounterTextView);
//                squatCountTextView.setText(String.valueOf(squatCounter));
//            });
//        }
//    }

    // Method to log the keypoints to Logcat
    private void logLandmarks(PointF[] keypoints) {
        StringBuilder landmarkLog = new StringBuilder("Landmarks (X, Y) with Labels: \n");
        float[] landmarksForSquatModel = new float[keypoints.length * 2];
        int index = 0;
        for (int i = 0; i < keypoints.length; i++) {
            if (keypoints[i] != null) {
                landmarkLog.append(landmarkLabels[i])
                        .append(": ")
                        .append(keypoints[i].x)
                        .append(", ")
                        .append(keypoints[i].y)
                        .append("\n");

                landmarksForSquatModel[index++] = keypoints[i].x;
                landmarksForSquatModel[index++] = keypoints[i].y;
            }

        }
        Log.d("Landmarks", landmarkLog.toString());  // Print to Logcat

        float[] normalizedLandmarks = normalizeLandmarks(landmarksForSquatModel);
        runSquatModelInference(normalizedLandmarks);
    }

    // Add this method to normalize the landmarks
    private float[] normalizeLandmarks(float[] landmarks) {
        // These are the values from your scaler in Python
        float[] means = { 31.55998643f, -52.33306569f, 2.40062276f, -55.07787102f, 0.82780038f,
                -55.18254274f, 3.07278189f, -53.72853454f, 0.08447083f, -53.86092845f,
                4.96581689f, -38.75574804f, -2.7918519f, -38.64380354f, 5.64645559f,
                -27.55931119f, -5.4321734f, -27.05961654f, 4.21811288f, -31.040037f,
                -4.59666071f, -31.00816161f, 2.61023366f, 0.06668621f, -2.61023342f,
                -0.06668589f, 5.1564844f, 16.93284451f, -4.9008615f, 17.18959943f,
                4.22445822f, 44.37570399f, -5.01741131f, 44.89616582f };

        float[] stdDevs = { 13.09007612f, 9.53331815f, 12.47934027f, 9.80578436f, 12.4504115f, 9.77888414f,
                10.84287627f, 9.26285486f, 10.88234688f, 9.14232061f, 9.0919901f, 6.38430872f,
                9.4817735f, 6.51904779f, 14.89416176f, 13.62757348f, 15.1260731f, 14.21901822f,
                19.78735728f, 20.95525915f, 19.90564269f, 20.61909623f, 3.30636764f, 1.34937316f,
                3.30636761f, 1.34937364f, 11.48234747f, 14.07930761f, 11.36720904f, 13.63409656f,
                10.21267034f, 15.26823661f, 10.35567388f, 15.35647488f };

        float[] normalizedLandmarks = new float[landmarks.length];
        for (int i = 0; i < landmarks.length; i++) {
            normalizedLandmarks[i] = (landmarks[i] - means[i]) / stdDevs[i];
        }
        return normalizedLandmarks;
    }

    private void runSquatModelInference(float[] normalizedLandmarks) {
        try {
            LogisticModelSquat model = LogisticModelSquat.newInstance(getApplicationContext());

            // Convert the normalized landmarks to TensorBuffer
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 34}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * normalizedLandmarks.length);
            byteBuffer.order(ByteOrder.nativeOrder());

            // Load landmarks into the byte buffer
            for (float value : normalizedLandmarks) {
                byteBuffer.putFloat(value);
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Run inference
            LogisticModelSquat.Outputs outputs = model.process(inputFeature0);
            float[] outputFeature0 = outputs.getOutputFeature0AsTensorBuffer().getFloatArray();

            // Interpret the output: For example, let's assume 0 -> "up", 1 -> "down"
            String resultLabel = (outputFeature0[0] > 0.5) ? "Down" : "Up";

            // Update the UI (TextView) on the main thread
            runOnUiThread(() -> {
                TextView resultTextView = findViewById(R.id.labelTextView);
                resultTextView.setText(resultLabel);
            });

            model.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Method to draw landmarks and connections on the image
    private void drawLandmarks(Bitmap bitmap, PointF[] keypoints) {
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);

        int[][] connections = {
                {0, 1}, {1, 3}, {0, 2}, {2, 4}, {0, 5}, {0, 6}, {5, 7}, {7, 9},
                {6, 8}, {8, 10}, {5, 6}, {5, 11}, {6, 12}, {11, 12}, {11, 13}, {13, 15}, {12, 14}, {14, 16}
        };

        // Draw connections
        for (int[] connection : connections) {
            PointF kp1 = keypoints[connection[0]];
            PointF kp2 = keypoints[connection[1]];

            if (kp1 != null && kp2 != null) {
                canvas.drawLine(kp1.x, kp1.y, kp2.x, kp2.y, paint);
            }
        }

        runOnUiThread(() -> imageView.setImageBitmap(mutableBitmap));
    }

    @SuppressLint("MissingPermission")
    private void openCamera() {
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                Integer cameraFacing = characteristics.get(CameraCharacteristics.LENS_FACING);

                // Open front-facing camera
                if (cameraFacing != null && cameraFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                    cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                        @Override
                        public void onOpened(@NonNull CameraDevice cameraDevice) {
                            try {
                                SurfaceTexture texture = textureView.getSurfaceTexture();
                                if (texture != null) {
                                    Surface surface = new Surface(texture);
                                    CaptureRequest.Builder captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                    captureRequest.addTarget(surface);

                                    cameraDevice.createCaptureSession(
                                            Collections.singletonList(surface),
                                            new CameraCaptureSession.StateCallback() {
                                                @Override
                                                public void onConfigured(@NonNull CameraCaptureSession session) {
                                                    try {
                                                        session.setRepeatingRequest(captureRequest.build(), null, handler);
                                                    } catch (CameraAccessException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                                                    Log.e("Camera","Configuration Failed");
                                                }
                                            }, handler);
                                }
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onDisconnected(@NonNull CameraDevice cameraDevice) {}

                        @Override
                        public void onError(@NonNull CameraDevice cameraDevice, int i) {}
                    }, handler);
                    break;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void getPermissions() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.close();
        handlerThread.quitSafely();
    }
}