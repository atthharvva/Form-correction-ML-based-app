package com.example.accurepp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class backkk extends AppCompatActivity {

    ImageButton bentoverr, seatedcable, latpull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_backkk);
        bentoverr = findViewById(R.id.bentoverr);
        bentoverr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(backkk.this, bentoverrow.class); // Change to appropriate activity
                startActivity(i);
                finish();
            }
        });

        // Seated cable row
        seatedcable = findViewById(R.id.seatedcable);
        seatedcable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(backkk.this, com.example.accurepp.seatedcable.class); // Change to appropriate activity
                startActivity(i);
                finish();
            }
        });

        // Lat pulldown
        latpull = findViewById(R.id.latpull);
        latpull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(backkk.this, latpulldown.class); // Change to appropriate activity
                startActivity(i);
                finish();
            }
        });

        // Back button functionality
        ImageButton backk = findViewById(R.id.bac);
        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(backkk.this, workoutlist.class);
                startActivity(i);
                finish();
            }
        });
    }
}