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

public class legss extends AppCompatActivity {

    ImageButton legpres, legex, lung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_legss);
        legpres = findViewById(R.id.legpr);
        legex = findViewById(R.id.legextt);
        lung = findViewById(R.id.lunges);

        // Set click listeners to navigate to corresponding activities
        lung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(legss.this, lunges.class);
                startActivity(i);
                finish();
            }
        });

        legex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(legss.this, legextensions.class);
                startActivity(i);
                finish();
            }
        });

        legpres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(legss.this, legpress.class);
                startActivity(i);
                finish();
            }
        });

        // Back button functionality
        ImageButton backk = findViewById(R.id.bac);
        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(legss.this, workoutlist.class);
                startActivity(i);
                finish();
            }
        });
    }
}