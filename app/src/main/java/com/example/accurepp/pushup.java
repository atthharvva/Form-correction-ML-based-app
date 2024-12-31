package com.example.accurepp;

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
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.accurepp.ml.AutoModel4;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class pushup extends AppCompatActivity {

    TextureView textureView;
    CameraManager cameraManager;
    Handler handler;
    HandlerThread handlerThread;
    ImageView imageView;
    Bitmap bitmap;
    AutoModel4 model;
    ImageProcessor imageProcessor;
    Paint paint;

    ArrayList<PointF[]> landmarksPerFrame;
    private final String[] landmarkLabels = {
            "Nose", "Left Eye", "Right Eye", "Left Ear", "Right Ear",
            "Left Shoulder", "Right Shoulder", "Left Elbow", "Right Elbow",
            "Left Wrist", "Right Wrist", "Left Hip", "Right Hip",
            "Left Knee", "Right Knee", "Left Ankle", "Right Ankle"
    };

    private static final int FRAME_INTERVAL = 14;
    private int frameCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pushup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textureView = findViewById(R.id.textureView);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        imageView = findViewById(R.id.imageView);

        paint = new Paint();
        paint.setColor(Color.BLUE);

        imageProcessor = new ImageProcessor.Builder().add(new ResizeOp(256, 256, ResizeOp.ResizeMethod.BILINEAR)).build();

        try {
            model = AutoModel4.newInstance(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        handlerThread = new HandlerThread("videoThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        landmarksPerFrame = new ArrayList<>();

        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {
//                frameCounter++;
//                if(frameCounter % FRAME_INTERVAL == 0) {
//                    Bitmap bitmap = textureView.getBitmap();
//
//
//
//                    TensorImage tensorImage = new TensorImage(DataType.UINT8);
//                    tensorImage.load(bitmap);
//                    tensorImage = imageProcessor.process(tensorImage);
//
//                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.UINT8);
//                    inputFeature0.loadBuffer(tensorImage.getBuffer());
//
//                    // Runs model inference and gets result.
//                    AutoModel4.Outputs outputs = model.process(inputFeature0);
//                    float[] outputFeature0 = outputs.getOutputFeature0AsTensorBuffer().getFloatArray();
//
////                Bitmap mutable = bitmap.copy(Bitmap.Config.ARGB_8888,true);
////                Canvas canvas = new Canvas(mutable);
//                    int h = bitmap.getHeight();
//                    int w = bitmap.getWidth();
//                    int x = 0;
//                    PointF[] keypoints = new PointF[17];
//
//                    while(x <= 49) {
//                        if(outputFeature0[x+2] > 0.45) {
//                            float cx = outputFeature0[x + 1] * w;
//                            float cy = outputFeature0[x] * h;
////                        canvas.drawCircle(cx, cy, 20f, paint);
//                            keypoints[x / 3] = new PointF(cx, cy);
//                        }
//                        x += 3;
//                    }
////                    StringBuilder landmarkLog = new StringBuilder("Landmarks (X, Y) with Labels: \n");
////                    for (int i = 0; i < keypoints.length; i++) {
////                        if (keypoints[i] != null) {
////                            landmarkLog.append(landmarkLabels[i]).append(": ")
////                                    .append(keypoints[i].x).append(", ").append(keypoints[i].y).append("\n");
////                        }
////                    }
////                    Log.d("Landmarks", landmarkLog.toString());
//
//                    Bitmap mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//                    Canvas canvas = new Canvas(mutable);
//
////                landmarksPerFrame.add(keypoints);
//
//
//                    paint.setColor(Color.RED);
//                    paint.setStrokeWidth(5);
//
//                    int[][] connections = {
//                            {0, 1}, {1, 3}, {0, 2}, {2, 4}, {0, 5}, {0, 6}, {5, 7}, {7, 9}, {6, 8}, {8, 10},
//                            {5, 6}, {5, 11}, {6, 12}, {11, 12}, {11, 13}, {13, 15}, {12, 14}, {14, 16}
//                    };
//
//                    for (int[] connection : connections) {
//                        PointF kp1 = keypoints[connection[0]];
//                        PointF kp2 = keypoints[connection[1]];
//
//                        if (kp1 != null && kp2 != null) {
//                            canvas.drawLine(kp1.x, kp1.y, kp2.x, kp2.y, paint);
//                        }
//                    }
//
//                    imageView.setImageBitmap(mutable);
//
//                }
                Bitmap bitmap = textureView.getBitmap();



                TensorImage tensorImage = new TensorImage(DataType.UINT8);
                tensorImage.load(bitmap);
                tensorImage = imageProcessor.process(tensorImage);

                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.UINT8);
                inputFeature0.loadBuffer(tensorImage.getBuffer());

                // Runs model inference and gets result.
                AutoModel4.Outputs outputs = model.process(inputFeature0);
                float[] outputFeature0 = outputs.getOutputFeature0AsTensorBuffer().getFloatArray();

//                Bitmap mutable = bitmap.copy(Bitmap.Config.ARGB_8888,true);
//                Canvas canvas = new Canvas(mutable);
                int h = bitmap.getHeight();
                int w = bitmap.getWidth();
                int x = 0;
                PointF[] keypoints = new PointF[17];

                while(x <= 49) {
                    if(outputFeature0[x+2] > 0.45) {
                        float cx = outputFeature0[x + 1] * w;
                        float cy = outputFeature0[x] * h;
//                        canvas.drawCircle(cx, cy, 20f, paint);
                        keypoints[x / 3] = new PointF(cx, cy);
                    }
                    x += 3;
                }
//                StringBuilder landmarkLog = new StringBuilder("Landmarks (X, Y) with Labels: \n");
//                    for (int i = 0; i < keypoints.length; i++) {
//                        if (keypoints[i] != null) {
//                            landmarkLog.append(landmarkLabels[i]).append(": ")
//                                    .append(keypoints[i].x).append(", ").append(keypoints[i].y).append("\n");
//                        }
//                    }
//                Log.d("Landmarks", landmarkLog.toString());

                Bitmap mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(mutable);

//                landmarksPerFrame.add(keypoints);


                paint.setColor(Color.RED);
                paint.setStrokeWidth(5);

                int[][] connections = {
                        {0, 1}, {1, 3}, {0, 2}, {2, 4}, {0, 5}, {0, 6}, {5, 7}, {7, 9}, {6, 8}, {8, 10},
                        {5, 6}, {5, 11}, {6, 12}, {11, 12}, {11, 13}, {13, 15}, {12, 14}, {14, 16}
                };

                for (int[] connection : connections) {
                    PointF kp1 = keypoints[connection[0]];
                    PointF kp2 = keypoints[connection[1]];

                    if (kp1 != null && kp2 != null) {
                        canvas.drawLine(kp1.x, kp1.y, kp2.x, kp2.y, paint);
                    }
                }

                imageView.setImageBitmap(mutable);

            }
            });


        getPermissions();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.close();
    }

    @SuppressLint("MissingPermission")
    private void openCamera() {
        try {

            cameraManager.openCamera(cameraManager.getCameraIdList()[0], new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice cameraDevice) {
                    try {
//                        CaptureRequest.Builder captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//                        Surface surface = new Surface(textureView.getSurfaceTexture());
//                        captureRequest.addTarget(surface);
//                        cameraDevice.createCaptureSession(
//                                java.util.Collections.singletonList(surface),
//                                new CameraCaptureSession.StateCallback() {
//                                    @Override
//                                    public void onConfigured(CameraCaptureSession session) {
//                                        try {
//                                            session.setRepeatingRequest(captureRequest.build(), null, null);
//                                        } catch (CameraAccessException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onConfigureFailed(CameraCaptureSession session) {
//                                    }
//                                },
//                                handler
//                        );
                        for (String cameraId : cameraManager.getCameraIdList()) {
                            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                            Integer cameraFacing = characteristics.get(CameraCharacteristics.LENS_FACING);

                            if (cameraFacing != null && cameraFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                                cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {

                                    @Override
                                    public void onOpened(@NonNull CameraDevice cameraDevice) {
                                        try {
                                            CaptureRequest.Builder captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                            Surface surface = new Surface(textureView.getSurfaceTexture());
                                            captureRequest.addTarget(surface);
                                            cameraDevice.createCaptureSession(
                                                    java.util.Collections.singletonList(surface),
                                                    new CameraCaptureSession.StateCallback() {
                                                        @Override
                                                        public void onConfigured(CameraCaptureSession session) {
                                                            try {
                                                                session.setRepeatingRequest(captureRequest.build(), null, null);
                                                            } catch (CameraAccessException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void onConfigureFailed(CameraCaptureSession session) {
                                                        }
                                                    },
                                                    handler
                                            );
                                        } catch (CameraAccessException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onDisconnected(@NonNull CameraDevice cameraDevice) {

                                    }

                                    @Override
                                    public void onError(@NonNull CameraDevice cameraDevice, int i) {
                                    }
                                }, handler);
                                break;
                            }
                        }
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDisconnected(CameraDevice cameraDevice) {
                }

                @Override
                public void onError(CameraDevice cameraDevice, int error) {
                }
            }, handler);


        }catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    void getPermissions(){
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED){
            getPermissions();
        }
    }
}