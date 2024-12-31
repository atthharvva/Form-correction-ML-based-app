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

public class seatedcable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seatedcable);
        ImageView whiteback = findViewById(R.id.whitebackk);
        whiteback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(seatedcable.this, backkk.class);
                startActivity(i);
                finish();
            }
        });

        // Reference the VideoView
        VideoView videoView = findViewById(R.id.videoView);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.seatedcablerow);
        videoView.setVideoURI(videoUri);

        // Add media controls to the video
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);  // Set the correct anchor view
        videoView.setMediaController(mediaController);  // Set media controller to the correct view

        // Set looping for the video
        videoView.setOnCompletionListener(mp -> videoView.start());

        // Start playing the video automatically
        videoView.start();


    }
}