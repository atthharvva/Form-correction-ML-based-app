package com.example.accurepp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class quinoa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quinoa);

        // Set up the YouTube link
        TextView youtubeLink = findViewById(R.id.youtube_link);
        youtubeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the YouTube link for the quinoa recipe
                String url = "https://youtu.be/uK4LibynqSk";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent); // Open the link in a web browser or YouTube app
            }
        });

        // Set up the back button functionality
        ImageView backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the Cutting activity
                Intent intent = new Intent(quinoa.this, Consistent.class);

                startActivity(intent); // Start the Cutting activity
                finish(); // Optionally finish the current activity
            }
        });
    }
}