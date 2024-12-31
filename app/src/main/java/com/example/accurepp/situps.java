//package com.example.accurepp;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.PointF;
//import android.graphics.SurfaceTexture;
//import android.hardware.camera2.CameraAccessException;
//import android.hardware.camera2.CameraCaptureSession;
//import android.hardware.camera2.CameraCharacteristics;
//import android.hardware.camera2.CameraDevice;
//import android.hardware.camera2.CameraManager;
//import android.hardware.camera2.CaptureRequest;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.util.Log;
//import android.view.Surface;
//import android.view.TextureView;
//import android.widget.ImageView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.example.accurepp.ml.AutoModel4;
//
//import org.opencv.android.CameraActivity;
//import org.opencv.android.CameraBridgeViewBase;
//import org.opencv.android.OpenCVLoader;
//import org.opencv.core.Mat;
//import org.tensorflow.lite.DataType;
//import org.tensorflow.lite.support.image.ImageProcessor;
//import org.tensorflow.lite.support.image.TensorImage;
//import org.tensorflow.lite.support.image.ops.ResizeOp;
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class situps extends AppCompatActivity {
//
//    TextureView textureView;
//    CameraManager cameraManager;
//    Handler handler;
//    HandlerThread handlerThread;
//    ImageView imageView;
//    Bitmap bitmap;
//    AutoModel4 model;
//    ImageProcessor imageProcessor;
//    Paint paint;
//
//    ArrayList<PointF[]> landmarksPerFrame;
//    private final String[] landmarkLabels = {
//            "Nose", "Left Eye", "Right Eye", "Left Ear", "Right Ear",
//            "Left Shoulder", "Right Shoulder", "Left Elbow", "Right Elbow",
//            "Left Wrist", "Right Wrist", "Left Hip", "Right Hip",
//            "Left Knee", "Right Knee", "Left Ankle", "Right Ankle"
//    };
//
//    private static final int FRAME_INTERVAL = 14;
//    private int frameCounter = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_situps);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        textureView = findViewById(R.id.textureView);
//
//        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//
//        imageView = findViewById(R.id.imageView);
//
//        paint = new Paint();
//        paint.setColor(Color.BLUE);
//
//        imageProcessor = new ImageProcessor.Builder().add(new ResizeOp(256, 256, ResizeOp.ResizeMethod.BILINEAR)).build();
//
//        try {
//            model = AutoModel4.newInstance(this);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        handlerThread = new HandlerThread("videoThread");
//        handlerThread.start();
//        handler = new Handler(handlerThread.getLooper());
//
//        landmarksPerFrame = new ArrayList<>();
//
//        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
//            @Override
//            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
//                openCamera();
//            }
//
//            @Override
//            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
//
//            }
//
//            @Override
//            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
//                return false;
//            }
//
//            @Override
//            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {
//                Bitmap bitmap = textureView.getBitmap();
//
//
//
//                TensorImage tensorImage = new TensorImage(DataType.UINT8);
//                tensorImage.load(bitmap);
//                tensorImage = imageProcessor.process(tensorImage);
//
//                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.UINT8);
//                inputFeature0.loadBuffer(tensorImage.getBuffer());
//
//                // Runs model inference and gets result.
//                AutoModel4.Outputs outputs = model.process(inputFeature0);
//                float[] outputFeature0 = outputs.getOutputFeature0AsTensorBuffer().getFloatArray();
//
////                Bitmap mutable = bitmap.copy(Bitmap.Config.ARGB_8888,true);
////                Canvas canvas = new Canvas(mutable);
//                int h = bitmap.getHeight();
//                int w = bitmap.getWidth();
//                int x = 0;
//                PointF[] keypoints = new PointF[17];
//
//                while(x <= 49) {
//                    if(outputFeature0[x+2] > 0.45) {
//                        float cx = outputFeature0[x + 1] * w;
//                        float cy = outputFeature0[x] * h;
////                        canvas.drawCircle(cx, cy, 20f, paint);
//                        keypoints[x / 3] = new PointF(cx, cy);
//                    }
//                    x += 3;
//                }
////                StringBuilder landmarkLog = new StringBuilder("Landmarks (X, Y) with Labels: \n");
////                    for (int i = 0; i < keypoints.length; i++) {
////                        if (keypoints[i] != null) {
////                            landmarkLog.append(landmarkLabels[i]).append(": ")
////                                    .append(keypoints[i].x).append(", ").append(keypoints[i].y).append("\n");
////                        }
////                    }
////                Log.d("Landmarks", landmarkLog.toString());
//
//                Bitmap mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//                Canvas canvas = new Canvas(mutable);
//
////                landmarksPerFrame.add(keypoints);
//
//
//                paint.setColor(Color.RED);
//                paint.setStrokeWidth(5);
//
//                int[][] connections = {
//                        {0, 1}, {1, 3}, {0, 2}, {2, 4}, {0, 5}, {0, 6}, {5, 7}, {7, 9}, {6, 8}, {8, 10},
//                        {5, 6}, {5, 11}, {6, 12}, {11, 12}, {11, 13}, {13, 15}, {12, 14}, {14, 16}
//                };
//
//                for (int[] connection : connections) {
//                    PointF kp1 = keypoints[connection[0]];
//                    PointF kp2 = keypoints[connection[1]];
//
//                    if (kp1 != null && kp2 != null) {
//                        canvas.drawLine(kp1.x, kp1.y, kp2.x, kp2.y, paint);
//                    }
//                }
//
//                imageView.setImageBitmap(mutable);
//
//            }
//            });
//
//        getPermissions();
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        model.close();
//    }
//
//    @SuppressLint("MissingPermission")
//    private void openCamera() {
////        try {
////
////            cameraManager.openCamera(cameraManager.getCameraIdList()[0], new CameraDevice.StateCallback() {
////                @Override
////                public void onOpened(CameraDevice cameraDevice) {
////                    try {
////                        CaptureRequest.Builder captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
////                        Surface surface = new Surface(textureView.getSurfaceTexture());
////                        captureRequest.addTarget(surface);
////                        cameraDevice.createCaptureSession(
////                                java.util.Collections.singletonList(surface),
////                                new CameraCaptureSession.StateCallback() {
////                                    @Override
////                                    public void onConfigured(CameraCaptureSession session) {
////                                        try {
////                                            session.setRepeatingRequest(captureRequest.build(), null, null);
////                                        } catch (CameraAccessException e) {
////                                            e.printStackTrace();
////                                        }
////                                    }
////
////                                    @Override
////                                    public void onConfigureFailed(CameraCaptureSession session) {
////                                    }
////                                },
////                                handler
////                        );
////                    } catch (CameraAccessException e) {
////                        e.printStackTrace();
////                    }
////                }
////
////                @Override
////                public void onDisconnected(CameraDevice cameraDevice) {
////                }
////
////                @Override
////                public void onError(CameraDevice cameraDevice, int error) {
////                }
////            }, handler);
////
////
////        }catch (CameraAccessException e) {
////            e.printStackTrace();
////        }
//        try {
//
//            cameraManager.openCamera(cameraManager.getCameraIdList()[0], new CameraDevice.StateCallback() {
//                @Override
//                public void onOpened(CameraDevice cameraDevice) {
//                    try {
////                        CaptureRequest.Builder captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
////                        Surface surface = new Surface(textureView.getSurfaceTexture());
////                        captureRequest.addTarget(surface);
////                        cameraDevice.createCaptureSession(
////                                java.util.Collections.singletonList(surface),
////                                new CameraCaptureSession.StateCallback() {
////                                    @Override
////                                    public void onConfigured(CameraCaptureSession session) {
////                                        try {
////                                            session.setRepeatingRequest(captureRequest.build(), null, null);
////                                        } catch (CameraAccessException e) {
////                                            e.printStackTrace();
////                                        }
////                                    }
////
////                                    @Override
////                                    public void onConfigureFailed(CameraCaptureSession session) {
////                                    }
////                                },
////                                handler
//                        for (String cameraId : cameraManager.getCameraIdList()) {
//                            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
//                            Integer cameraFacing = characteristics.get(CameraCharacteristics.LENS_FACING);
//
//                            if (cameraFacing != null && cameraFacing == CameraCharacteristics.LENS_FACING_FRONT) {
//                                cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
//
//                                    @Override
//                                    public void onOpened(@NonNull CameraDevice cameraDevice) {
//                                        try {
//                                            CaptureRequest.Builder captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//                                            Surface surface = new Surface(textureView.getSurfaceTexture());
//                                            captureRequest.addTarget(surface);
//                                            cameraDevice.createCaptureSession(
//                                                    java.util.Collections.singletonList(surface),
//                                                    new CameraCaptureSession.StateCallback() {
//                                                        @Override
//                                                        public void onConfigured(CameraCaptureSession session) {
//                                                            try {
//                                                                session.setRepeatingRequest(captureRequest.build(), null, null);
//                                                            } catch (CameraAccessException e) {
//                                                                e.printStackTrace();
//                                                            }
//                                                        }
//
//                                                        @Override
//                                                        public void onConfigureFailed(CameraCaptureSession session) {
//                                                        }
//                                                    },
//                                                    handler
//                                            );
//                                        } catch (CameraAccessException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onDisconnected(@NonNull CameraDevice cameraDevice) {
//
//                                    }
//
//                                    @Override
//                                    public void onError(@NonNull CameraDevice cameraDevice, int i) {
//                                    }
//                                }, handler);
//                                break;
//                            }
//                        }
//                    } catch (CameraAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onDisconnected(CameraDevice cameraDevice) {
//                }
//
//                @Override
//                public void onError(CameraDevice cameraDevice, int error) {
//                }
//            }, handler);
//
//
//        }catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    void getPermissions(){
//        if(checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if(grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED){
//            getPermissions();
//        }
//    }
//}
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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.accurepp.ml.AutoModel4;
//import com.example.accurepp.ml.LogisticModelSitups;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;

public class situps extends AppCompatActivity {

    private TextureView textureView;
    private CameraManager cameraManager;
    private Handler handler;
    private HandlerThread handlerThread;
    private ImageView imageView;
    private AutoModel4 model;
    private ImageProcessor imageProcessor;
    private Paint paint;

    private static final int FRAME_INTERVAL = 14;  // Only process every 14th frame
    private int frameCounter = 0;

    // Labels for the keypoints (you can update these based on your model)
    private static final String[] landmarkLabels = {
            "Nose", "Left Eye", "Right Eye", "Left Ear", "Right Ear",
            "Left Shoulder", "Right Shoulder", "Left Elbow", "Right Elbow",
            "Left Wrist", "Right Wrist", "Left Hip", "Right Hip",
            "Left Knee", "Right Knee", "Left Ankle", "Right Ankle"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_situps);

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
            model = AutoModel4.newInstance(this);
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

            // Parse the output and extract keypoints
            for (int i = 0; i < outputFeature0.length; i += 3) {
                if (outputFeature0[i + 2] > 0.45) {  // confidence threshold
                    float cx = outputFeature0[i + 1] * w;
                    float cy = outputFeature0[i] * h;
                    rawLandmarks[(i / 3) * 2] = cx;       // X coordinate
                    rawLandmarks[(i / 3) * 2 + 1] = cy;
                    keypoints[i / 3] = new PointF(cx, cy);
                }
            }

            // Log the keypoints to Logcat
            logLandmarks(keypoints);

            drawLandmarks(bitmap, keypoints);
        });
    }

    // Method to log the keypoints to Logcat
    private void logLandmarks(PointF[] keypoints) {
        StringBuilder landmarkLog = new StringBuilder("Landmarks (X, Y) with Labels: \n");
//        float[] landmarksForSquatModel = new float[keypoints.length * 2];
        float[] distancesForSquatModel = new float[keypoints.length * 2];
        int index = 0;

        if (keypoints[5] != null && keypoints[6] != null && keypoints[11] != null && keypoints[12] != null) {
            // Calculate distances between important landmarks (for squats: hips, knees, ankles)
            float shoulderDist = calculateDistance(keypoints[5], keypoints[6]);  // Left Shoulder to Right Shoulder
            float hipDist = calculateDistance(keypoints[11], keypoints[12]);      // Left Hip to Right Hip

            // Log the distances (just for debug)
            landmarkLog.append("Shoulder Distance: ").append(shoulderDist).append("\n");
            landmarkLog.append("Hip Distance: ").append(hipDist).append("\n");

            // Store in model input array (normalize if needed)
            distancesForSquatModel[index++] = shoulderDist;
            distancesForSquatModel[index++] = hipDist;
        }
//        int index = 0;
//        for (int i = 0; i < keypoints.length; i++) {
//            if (keypoints[i] != null) {
//                landmarkLog.append(landmarkLabels[i])
//                        .append(": ")
//                        .append(keypoints[i].x)
//                        .append(", ")
//                        .append(keypoints[i].y)
//                        .append("\n");
//
//                landmarksForSquatModel[index++] = keypoints[i].x;
//                landmarksForSquatModel[index++] = keypoints[i].y;
//            }
//
//        }
//        Log.d("Landmarks", landmarkLog.toString());  // Print to Logcat

//        float[] normalizedLandmarks = normalizeLandmarks(landmarksForSquatModel);
//        runSitupModelInference(normalizedLandmarks);
    }

    private float calculateDistance(PointF p1, PointF p2) {
        return (float) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    // Add this method to normalize the landmarks
//    private float[] normalizeLandmarks(float[] landmarks) {
//        // These are the values from your scaler in Python
//        float[] means = { 3.39348365f, -37.44242958f, 4.24273925f, -38.31406089f,
//                3.00773902f, -38.46416526f, 4.90818083f, -34.91293358f,
//                2.58312414f, -34.97375537f, 7.05125144f, -22.30025642f,
//                -0.92946178f, -22.96771753f, 6.03249027f, -22.77228029f,
//                -5.34016319f, -24.21189611f, 2.08313611f, -30.90239598f,
//                -4.5433579f, -32.54090896f, 2.32446704f, 0.38184018f,
//                -2.32446612f, -0.3818406f, -0.98247209f, -14.11274399f,
//                -7.42046631f, -14.36306806f, -6.05442741f, 10.80201189f,
//                -10.84274515f, 10.06320205f };
//
//        float[] stdDevs = { 32.39853091f, 19.10309649f, 34.03464613f, 20.96840707f,
//                34.0898428f, 21.0784261f, 33.89834076f, 22.50773924f,
//                34.0059655f, 22.67459572f, 27.33031015f, 19.70314796f,
//                26.86467303f, 19.58943339f, 27.13346117f, 20.81253062f,
//                27.64570312f, 18.47288872f, 33.53439634f, 26.60988187f,
//                32.57591654f, 25.54297364f, 2.88931089f, 5.41987809f,
//                2.88931018f, 5.41987837f, 22.00325288f, 20.19363689f,
//                23.20094907f, 18.91237137f, 40.52445664f, 23.45344811f,
//                43.93972727f, 21.51275162f };
//
//        float[] normalizedLandmarks = new float[landmarks.length];
//        for (int i = 0; i < landmarks.length; i++) {
//            normalizedLandmarks[i] = (landmarks[i] - means[i]) / stdDevs[i];
//        }
//        return normalizedLandmarks;
//    }

//    private void runSitupModelInference(float[] normalizedLandmarks) {
//        try {
//            LogisticModelSitups model = LogisticModelSitups.newInstance(getApplicationContext());
//
//            // Convert the normalized landmarks to TensorBuffer
//            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 34}, DataType.FLOAT32);
//            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * normalizedLandmarks.length);
//            byteBuffer.order(ByteOrder.nativeOrder());
//
//            // Load landmarks into the byte buffer
//            for (float value : normalizedLandmarks) {
//                byteBuffer.putFloat(value);
//            }
//            inputFeature0.loadBuffer(byteBuffer);
//
//            // Run inference
//            LogisticModelSitups.Outputs outputs = model.process(inputFeature0);
//            float[] outputFeature0 = outputs.getOutputFeature0AsTensorBuffer().getFloatArray();
//
//            // Interpret the output: For example, let's assume 0 -> "up", 1 -> "down"
//            String resultLabel = (outputFeature0[0] > 0.5) ? "Down" : "Up";
//
//            // Update the UI (TextView) on the main thread
//            runOnUiThread(() -> {
//                TextView resultTextView = findViewById(R.id.resultTextView);
//                resultTextView.setText(resultLabel);
//            });
//
//            model.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


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
                                                public void onConfigureFailed(@NonNull CameraCaptureSession session) {}
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
