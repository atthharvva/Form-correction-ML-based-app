package com.example.accurepp;

import static org.tensorflow.lite.TensorFlowLite.init;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class OverlayView extends View{

    // List to store keypoints
    private float[][] keypoints;

    private int count = 0;
    private float kneeAngle = 0;
    private Paint textPaint = new Paint();

    // Paint object to style the keypoints
    private Paint paint = new Paint();



    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OverlayView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint.setColor(Color.RED);      // Set the color of keypoints to red
        paint.setStyle(Paint.Style.FILL); // Set fill style for the circle
        paint.setStrokeWidth(10f);      // Set the stroke width for the circle

        textPaint.setColor(Color.WHITE);  // White text color
        textPaint.setTextSize(80);        // Text size
        textPaint.setStyle(Paint.Style.FILL);
    }

    public void updateKeypoints(float[][] newKeypoints) {
        this.keypoints = newKeypoints;   // Set the new keypoints
        invalidate();                    // Request a re-draw of the view
    }

    public void updateCount(int newCount, float newKneeAngle) {
        this.count = newCount;
        this.kneeAngle = newKneeAngle;
        invalidate();  // Request a re-draw of the view to display updated count and knee angle
    }

    @Override
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
                    canvas.drawCircle(x, y, 10, paint); // Draw keypoints as circles
                }
            }
        }

        // Draw the squat count on the screen
        canvas.drawText("Squat Count: " + count, 50, 100, textPaint);

        // Draw the knee angle on the screen
        canvas.drawText("Knee Angle: " + kneeAngle, 50, 200, textPaint);
    }
}
