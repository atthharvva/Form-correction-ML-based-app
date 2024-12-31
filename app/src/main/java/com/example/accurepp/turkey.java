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

public class turkey extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_turkey); // Ensure that this layout file exists

        // Find the TextView by its ID
        TextView youtubeLink = findViewById(R.id.youtube_link);

        // Set an OnClickListener to the TextView
        youtubeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the YouTube link for the Turkey Recipe
                String url = "https://youtu.be/lPaiK0vTPeI";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent); // Start the Intent to open the YouTube link
            }
        });

        // Find the back button by its ID
        ImageView backButton = findViewById(R.id.btnBack);

        // Set an OnClickListener to the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to go back to the Cutting activity
                Intent intent = new Intent(turkey.this, Consistent.class); // Replace Cutting.class with your actual previous activity class name if different
                startActivity(intent); // Start the Cutting activity
                finish(); // Optionally finish the current activity
            }
        });
    }
}