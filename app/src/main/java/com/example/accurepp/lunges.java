package com.example.accurepp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class lunges extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lunges);
        ImageView whiteback = findViewById(R.id.whitebackk);
        whiteback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(lunges.this, legss.class);
                startActivity(i);
                finish();
            }
        });

        // Reference and configure the first VideoView
        VideoView videoView = findViewById(R.id.videoView);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.leftlunge);
        videoView.setVideoURI(videoUri);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setOnCompletionListener(mp -> videoView.start());
        videoView.start();  // Start playing the first video automatically

        // Reference and configure the second VideoView
        VideoView videoView1 = findViewById(R.id.videoView2);
        Uri videoUri1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.rightleglunge);
        videoView1.setVideoURI(videoUri1);
        MediaController mediaController1 = new MediaController(this);
        mediaController1.setAnchorView(videoView1);
        videoView1.setMediaController(mediaController1);
        videoView1.setOnCompletionListener(mp -> videoView1.start());
        videoView1.start();  // Start playing the second video automatically
    }
}