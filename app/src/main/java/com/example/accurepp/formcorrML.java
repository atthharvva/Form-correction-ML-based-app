package com.example.accurepp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.opencv.android.OpenCVLoader;

public class formcorrML extends AppCompatActivity {
    Button push, sit,squa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(OpenCVLoader.initDebug()) Log.d("LOADED", "success");
        else Log.d("LOADED", "error");

        setContentView(R.layout.activity_formcorr_ml);
        push=findViewById(R.id.pushup);
        sit=findViewById(R.id.situp);
        squa=findViewById(R.id.squatt);
        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(formcorrML.this, pushup.class);
                startActivity(i);
                finish();
            }
        });
        sit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(formcorrML.this, situps.class);
                startActivity(i);
                finish();
            }
        });
        squa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(formcorrML.this, squats.class);
                startActivity(i);
                finish();
            }
        });

    }
}