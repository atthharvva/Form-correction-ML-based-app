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

public class nutrihelp extends AppCompatActivity {
    ImageButton bck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrihelp);
        EdgeToEdge.enable(this);
        bck = findViewById(R.id.back);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(nutrihelp.this, dashboard.class);
                startActivity(i);
                finish();
            }
        });
    }

    // Method to open the bulking activity
    public void openBulking(View view) {
        Intent bulkingIntent = new Intent(nutrihelp.this, Bulking.class);
        startActivity(bulkingIntent);
    }

    // Method to open the cutting activity
    public void openCutting(View view) {
        Intent cuttingIntent = new Intent(nutrihelp.this, Cutting.class);
        startActivity(cuttingIntent);
    }

    // Method to open the consistent activity
    public void openConsistent(View view) {
        Intent consistentIntent = new Intent(nutrihelp.this, Consistent.class);
        startActivity(consistentIntent);
    }
}