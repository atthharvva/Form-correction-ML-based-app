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

public class Cutting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cutting);

        // Back button setup
        ImageView backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to go back to the previous activity
                Intent intent = new Intent(Cutting.this, nutrihelp.class); // Replace PreviousActivity.class with your actual previous activity class name
                startActivity(intent);
                finish(); // Optionally finish the current activity
            }
        });

        // ImageView for grilled vegetables
        ImageView grilledVeg = findViewById(R.id.grilledveg);
        grilledVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cutting.this, veggrill.class); // Replace with your GrilledVegActivity
                startActivity(intent);
            }
        });

        // ImageView for tofu
        ImageView tofu = findViewById(R.id.tofuu);
        tofu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cutting.this, tofu.class); // Replace with your TofuActivity
                startActivity(intent);
            }
        });

        // ImageView for grilled chicken
        ImageView grilledChicken = findViewById(R.id.grilchick);
        grilledChicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cutting.this, grillchicken.class); // Replace with your GrilledChickenActivity
                startActivity(intent);
            }
        });

        // ImageView for shrimp
        ImageView shrimp = findViewById(R.id.shrimpp);
        shrimp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cutting.this, shrimp.class); // Replace with your ShrimpActivity
                startActivity(intent);
            }
        });
    }
}