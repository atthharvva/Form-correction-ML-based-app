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

public class chestt extends AppCompatActivity {

    ImageButton benchh,cablee,pushup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chestt);
        benchh = findViewById(R.id.bench);

        cablee = findViewById(R.id.cable);
        cablee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(chestt.this, cableflyes.class);
                startActivity(i);
                finish();
            }
        });
        benchh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(chestt.this, benchpress.class);
                startActivity(i);
                finish();
            }
        });
        pushup = findViewById(R.id.pushup);
        pushup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(chestt.this, com.example.accurepp.pushupp.class);
                startActivity(i);
                finish();
            }
        });
        ImageButton backk = findViewById(R.id.bac);
        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(chestt.this, workoutlist.class);
                startActivity(i);
                finish();
            }
        });
    }
}