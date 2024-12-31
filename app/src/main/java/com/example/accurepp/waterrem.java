package com.example.accurepp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class waterrem extends AppCompatActivity {

    private Spinner spinnerCategory;
    private EditText etWaterConsumed, etWeight;
    private Button btnCalculate;
    private ImageView ivAddOneLiter, btnBack; // Added btnBack here
    private TextView tvRemainingWater, tvMotivationQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_waterrem);

        etWeight = findViewById(R.id.etWeight);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        etWaterConsumed = findViewById(R.id.etWaterConsumed);
        btnCalculate = findViewById(R.id.btnCalculate);
        ivAddOneLiter = findViewById(R.id.ivAddOneLiter);
        tvRemainingWater = findViewById(R.id.tvRemainingWater);
        tvMotivationQuote = findViewById(R.id.tvMotivationQuote);
        btnBack = findViewById(R.id.btnBack); // Initialize the back button

        // Set up the spinner adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Set onClickListener for the calculate button
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateRemainingWater();
            }
        });

        // Set onClickListener for the Add One Liter button
        ivAddOneLiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOneLiter();
            }
        });

        // Set onClickListener for the back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to navigate back to the Dashboard activity
                Intent intent = new Intent(waterrem.this, dashboard.class);
                startActivity(intent);
                finish(); // Close the current activity
            }
        });
    }

    private void calculateRemainingWater() {
        String selectedCategory = spinnerCategory.getSelectedItem().toString();
        float targetWater;
        if (selectedCategory.equals("Weight Loss")) {
            targetWater = 4.0f;
        } else if (selectedCategory.equals("Weight Gain")) {
            targetWater = 5.0f;
        } else {
            targetWater = 0.0f;
        }

        String consumedStr = etWaterConsumed.getText().toString();
        if (consumedStr.isEmpty()) {
            consumedStr = "0";
        }
        float consumedWater = Float.parseFloat(consumedStr);
        float remainingWater = targetWater - consumedWater;
        if (remainingWater < 0) remainingWater = 0;

        tvRemainingWater.setText("Remaining Water: " + remainingWater + " L");
        displayMotivationQuote(remainingWater, targetWater);
    }

    private void addOneLiter() {
        String consumedStr = etWaterConsumed.getText().toString();
        if (consumedStr.isEmpty()) {
            consumedStr = "0";
        }
        float consumedWater = Float.parseFloat(consumedStr);
        consumedWater += 1.0f;
        etWaterConsumed.setText(String.valueOf(consumedWater));
        calculateRemainingWater();
    }

    private void displayMotivationQuote(float remainingWater, float targetWater) {
        float progress = (targetWater - remainingWater) / targetWater * 100;
        if (progress <= 25) {
            tvMotivationQuote.setText("You're just starting, stay hydrated!");
        } else if (progress <= 50) {
            tvMotivationQuote.setText("Great job! You're halfway there!");
        } else if (progress <= 75) {
            tvMotivationQuote.setText("Keep going! You're doing awesome!");
        } else if (progress < 100) {
            tvMotivationQuote.setText("Almost there! Just a little more!");
        } else {
            tvMotivationQuote.setText("Congratulations! You've reached your goal!");
        }

    }
}