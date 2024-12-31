package com.example.accurepp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.opencv.android.OpenCVLoader;

public class formcorrML extends AppCompatActivity {
    Button push, sit,squa;
    ImageButton  squat1, bicep1, shoulder1, tricep1;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        if(OpenCVLoader.initDebug()) Log.d("LOADED", "success");
        else Log.d("LOADED", "error");

        setContentView(R.layout.activity_formcorr_ml);
//        push=findViewById(R.id.pushup);
//        sit=findViewById(R.id.situp);
//        squa=findViewById(R.id.squatt);
        squat1=findViewById(R.id.squat1);
        bicep1=findViewById(R.id.bicep1);
        shoulder1=findViewById(R.id.shoulder1);
        tricep1=findViewById(R.id.tricep1);
        back=findViewById(R.id.backblack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(formcorrML.this, dashboard.class);
                startActivity(i);
                finish();
            }
        });

//        push.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(formcorrML.this, pushup.class);
//                startActivity(i);
//                finish();
//            }
//        });


//        sit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(formcorrML.this, situps.class);
//                startActivity(i);
//                finish();
//            }
//        });


//        squa.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(formcorrML.this, squats.class);
//                startActivity(i);
//                finish();
//            }
//        });


        squat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(formcorrML.this, squat1.class);
                startActivity(i);
                finish();
            }
        });

        bicep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(formcorrML.this, bicep1.class);
                startActivity(i);
                finish();
            }
        });

        shoulder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(formcorrML.this, com.example.accurepp.shoulder1.class);
                startActivity(i);
                finish();
            }
        });

        tricep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(formcorrML.this, com.example.accurepp.tricep1.class);
                startActivity(i);
                finish();
            }
        });

    }
}