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

public class Bulking extends AppCompatActivity {
    ImageView chickpeasw, paneetikk, alfredopas, sallmon, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bulking);

        // Initialize ImageViews
        chickpeasw = findViewById(R.id.chickpeasw);
        paneetikk = findViewById(R.id.paneetikk);
        alfredopas = findViewById(R.id.alfredopas);
        sallmon = findViewById(R.id.sallmon);
        btnBack = findViewById(R.id.btnBack);

        // Set onClickListeners for each ImageView
        chickpeasw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bulking.this, chickpe.class);
                startActivity(intent);
            }
        });

        paneetikk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bulking.this, panee.class);
                startActivity(intent);
            }
        });

        alfredopas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bulking.this, pasta.class);
                startActivity(intent);
            }
        });

        sallmon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bulking.this, salbowl.class);
                startActivity(intent);
            }
        });

        // Back button functionality
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bulking.this, nutrihelp.class);
                startActivity(intent);
                finish();
            }
        });
    }
}