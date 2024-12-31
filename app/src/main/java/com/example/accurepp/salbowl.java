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

public class salbowl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_salbowl);

        // Find the TextView by its ID
        TextView youtubeLink = findViewById(R.id.youtube_link);

        // Set an OnClickListener to the TextView
        youtubeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the YouTube link
                String url = "https://youtu.be/wVzBJE8zKNI"; // Updated link
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent); // Start the Intent to open the link
            }
        });

        // Find the back button by its ID
        ImageView backButton = findViewById(R.id.btnBack);

        // Set an OnClickListener to the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to go back to the previous activity
                Intent intent = new Intent(salbowl.this, Bulking.class); // Replace Bulking.class with your actual class name
                startActivity(intent); // Start the Bulking activity
                finish(); // Optionally finish the current activity
            }
        });
    }
}