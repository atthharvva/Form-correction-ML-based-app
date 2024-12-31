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

public class workoutlist extends AppCompatActivity {
    ImageButton bck,chst,backk,legss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workoutlist);

        chst=findViewById(R.id.chest);
        backk=findViewById(R.id.back);
        legss=findViewById(R.id.legs);
        bck=findViewById(R.id.bac);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(workoutlist.this, dashboard.class);
                startActivity(i);
                finish();
            }
        });
        chst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(workoutlist.this,chestt.class);
                startActivity(i);
                finish();
            }
        });
        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(workoutlist.this, backkk.class);
                startActivity(i);
                finish();
            }
        });
        legss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(workoutlist.this, legss.class);
                startActivity(i);
                finish();

            }
        });

    }
}