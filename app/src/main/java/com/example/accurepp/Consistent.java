package com.example.accurepp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Consistent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consistent);

        // Back button setup
        ImageView backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to go back to the previous activity
                Intent intent = new Intent(Consistent.this, nutrihelp.class); // Replace with your actual previous activity class name
                startActivity(intent);
                finish(); // Optionally finish the current activity
            }
        });

        // ImageView for lentil
        ImageView lentil = findViewById(R.id.lentilll);
        lentil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Consistent.this, lentill.class); // Replace with your LentilActivity
                startActivity(intent);
            }
        });

        // ImageView for quinoa
        ImageView quinoa = findViewById(R.id.quinoaa);
        quinoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Consistent.this, quinoa.class); // Replace with your QuinoaActivity
                startActivity(intent);
            }
        });

        // ImageView for turkey
        ImageView turkey = findViewById(R.id.gturkey);
        turkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Consistent.this, turkey.class); // Replace with your TurkeyActivity
                startActivity(intent);
            }
        });

        // ImageView for baked cod
        ImageView bakedCod = findViewById(R.id.bakeddcod);
        bakedCod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Consistent.this, bakedcod.class); // Replace with your BakedCodActivity
                startActivity(intent);
            }
        });
    }
}