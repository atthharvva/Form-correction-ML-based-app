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

public class bentoverrow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bentoverrow);
        ImageView whiteback = findViewById(R.id.whitebackk);
        whiteback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(bentoverrow.this, backkk.class); // Change to the appropriate parent activity
                startActivity(i);
                finish();
            }
        });

        // First VideoView setup
        VideoView videoView = findViewById(R.id.videoView);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bentiverrow); // Change to the correct video resource
        videoView.setVideoURI(videoUri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnCompletionListener(mp -> videoView.start());
        videoView.start();

        // Second VideoView setup
        VideoView videoView2 = findViewById(R.id.videoView2);
        Uri videoUri2 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bentoverow2); // Change to the correct video resource
        videoView2.setVideoURI(videoUri2);

        MediaController mediaController2 = new MediaController(this);
        mediaController2.setAnchorView(videoView2);
        videoView2.setMediaController(mediaController2);

        videoView2.setOnCompletionListener(mp -> videoView2.start());
        videoView2.start();
    }
}