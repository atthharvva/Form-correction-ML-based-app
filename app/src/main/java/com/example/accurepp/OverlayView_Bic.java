package com.example.accurepp;

import static org.tensorflow.lite.TensorFlowLite.init;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class OverlayView_Bic extends View {
    private float[][] keypoints;

    // Variables for curl count and elbow angle
    private int curlCount = 0;
    private float elbowAngle = 0;

    // Paint objects for drawing keypoints and text
    private Paint keypointPaint = new Paint();
    private Paint textPaint = new Paint();

    public OverlayView_Bic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OverlayView_Bic(Context context) {
        super(context);
        init();
    }

    private void init() {
        // Set keypoint paint attributes
        keypointPaint.setColor(Color.BLUE);  // Blue color for keypoints
        keypointPaint.setStyle(Paint.Style.FILL);
        keypointPaint.setStrokeWidth(10f);

        // Set text paint attributes
        textPaint.setColor(Color.WHITE);  // White text color
        textPaint.setTextSize(80);        // Text size
        textPaint.setStyle(Paint.Style.FILL);
    }

    public void updateKeypoints(float[][] newKeypoints) {
        this.keypoints = newKeypoints;   // Set the new keypoints
        invalidate();                    // Request a re-draw of the view
    }

    public void updateCount(int newCurlCount, float newElbowAngle) {
        this.curlCount = newCurlCount;   // Update curl count
        this.elbowAngle = newElbowAngle; // Update elbow angle
        invalidate();  // Request a re-draw of the view to display updated count and elbow angle
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Loop through keypoints and draw them
        if (keypoints != null) {
            for (float[] keypoint : keypoints) {
                float x = keypoint[0];
                float y = keypoint[1];
                float confidence = keypoint[2];

                // Only draw keypoints with confidence above 0.5
                if (confidence > 0.5) {
                    canvas.drawCircle(x, y, 10, keypointPaint); // Draw keypoints as circles
                }
            }
        }

        // Draw the curl count on the screen
        canvas.drawText("Curl Count: " + curlCount, 50, 100, textPaint);

        // Draw the elbow angle on the screen
        canvas.drawText("Elbow Angle: " + elbowAngle, 50, 200, textPaint);
    }
}
