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

public class benchpress extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_benchpress);
        ImageView whiteback=findViewById(R.id.whitebackk);
        whiteback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(benchpress.this,chestt.class);
                startActivity(i);
                finish();
            }
        });


        // Reference the first VideoView
        VideoView videoView = findViewById(R.id.videoView);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.chest2);
        videoView.setVideoURI(videoUri);

        // Add media controls for the first video
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Set looping for the first video
        videoView.setOnCompletionListener(mp -> videoView.start());

        // Start playing the first video automatically
        videoView.start();

        // Reference the second VideoView
        VideoView videoView1 = findViewById(R.id.videoView2);
        Uri videoUri1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.chest1);
        videoView1.setVideoURI(videoUri1);

        // Add media controls for the second video
        MediaController mediaController1 = new MediaController(this);
        mediaController1.setAnchorView(videoView1);
        videoView1.setMediaController(mediaController1);

        // Set looping for the second video
        videoView1.setOnCompletionListener(mp -> videoView1.start());

        // Start playing the second video automatically
        videoView1.start();
    }
}