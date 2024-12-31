package com.example.accurepp;

import static org.tensorflow.lite.TensorFlowLite.init;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class OverlayView_Sho extends View{

    private Paint keypointPaint;
    private Paint textPaint;
    private Paint linePaint;
    private float[][] keypoints;
    private int pressCount;
    private float shoulderAngle;

    public OverlayView_Sho(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OverlayView_Sho(Context context) {
        super(context);
        init();
    }

    private void init() {
        keypointPaint = new Paint();
        keypointPaint.setColor(Color.RED);
        keypointPaint.setStyle(Paint.Style.FILL);
        keypointPaint.setStrokeWidth(10f);

        linePaint = new Paint();
        linePaint.setColor(Color.GREEN);
        linePaint.setStrokeWidth(5f);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60f);
        textPaint.setTextAlign(Paint.Align.LEFT);
    }

    public void updateKeypoints(float[][] keypoints) {
        this.keypoints = keypoints;
        invalidate();
    }

    // Method to update press count and shoulder angle
    public void updateCount(int pressCount, float shoulderAngle) {
        this.pressCount = pressCount;
        this.shoulderAngle = shoulderAngle;
        invalidate();
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw keypoints and lines
        if (keypoints != null && keypoints.length > 0) {
            drawKeypointsAndLines(canvas);
        }

        // Display the press count
        canvas.drawText("Press Count: " + pressCount, 50, 100, textPaint);
        canvas.drawText("Shoulder Angle: " + shoulderAngle, 50, 200, textPaint);
    }

    private void drawKeypointsAndLines(Canvas canvas) {
        // Draw keypoints and lines for the right arm (shoulder, elbow, wrist)
        float[] rightShoulder = keypoints[6];  // Right Shoulder
        float[] rightElbow = keypoints[8];     // Right Elbow
        float[] rightWrist = keypoints[10];    // Right Wrist

        if (rightShoulder[2] > 0.03f && rightElbow[2] > 0.03f && rightWrist[2] > 0.03f) {
            // Draw the keypoints
            canvas.drawCircle(rightShoulder[0], rightShoulder[1], 20, keypointPaint);
            canvas.drawCircle(rightElbow[0], rightElbow[1], 20, keypointPaint);
            canvas.drawCircle(rightWrist[0], rightWrist[1], 20, keypointPaint);

            // Draw lines connecting shoulder, elbow, and wrist
            canvas.drawLine(rightShoulder[0], rightShoulder[1], rightElbow[0], rightElbow[1], linePaint);
            canvas.drawLine(rightElbow[0], rightElbow[1], rightWrist[0], rightWrist[1], linePaint);
        }

        // Similarly, you can draw the left arm keypoints and lines (optional)
        float[] leftShoulder = keypoints[5];  // Left Shoulder
        float[] leftElbow = keypoints[7];     // Left Elbow
        float[] leftWrist = keypoints[9];     // Left Wrist

        if (leftShoulder[2] > 0.03f && leftElbow[2] > 0.03f && leftWrist[2] > 0.03f) {
            // Draw the keypoints
            canvas.drawCircle(leftShoulder[0], leftShoulder[1], 20, keypointPaint);
            canvas.drawCircle(leftElbow[0], leftElbow[1], 20, keypointPaint);
            canvas.drawCircle(leftWrist[0], leftWrist[1], 20, keypointPaint);

            // Draw lines connecting shoulder, elbow, and wrist
            canvas.drawLine(leftShoulder[0], leftShoulder[1], leftElbow[0], leftElbow[1], linePaint);
            canvas.drawLine(leftElbow[0], leftElbow[1], leftWrist[0], leftWrist[1], linePaint);
        }
    }

}
