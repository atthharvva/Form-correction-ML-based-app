package com.example.accurepp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class bmibmr extends AppCompatActivity {
    ImageButton bck;

    private Button btnMale, btnFemale, btnBMI, btnBMR, btnMinus, btnPlus;
    private SeekBar heightSlider, weightSlider;
    private TextView tvHeightValue, tvWeightValue, ageDisplay;

    private String selectedGender = "Male";
    private int height = 170;
    private int weight = 70;
    private int age = 22; // Initial age

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmibmr);

        // Find views
        btnMale = findViewById(R.id.btnMale);
        btnFemale = findViewById(R.id.btnFemale);
        btnBMI = findViewById(R.id.btnBMI);
        btnBMR = findViewById(R.id.btnBMR);
        heightSlider = findViewById(R.id.heightSlider);
        weightSlider = findViewById(R.id.weightSlider);
        tvHeightValue = findViewById(R.id.tvHeightValue);
        tvWeightValue = findViewById(R.id.tvWeightValue);
        ageDisplay = findViewById(R.id.tvAgeValue); // Corrected ID
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        bck=findViewById(R.id.back);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Gender selection
        btnMale.setSelected(true);
        btnMale.setOnClickListener(v -> selectGender("Male"));
        btnFemale.setOnClickListener(v -> selectGender("Female"));

        // Height slider change listener
        heightSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                height = progress;
                tvHeightValue.setText(height + " cm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Weight slider change listener
        weightSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                weight = progress;
                tvWeightValue.setText(weight + " kg");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Age adjustment button click listeners
        btnMinus.setOnClickListener(v -> {
            if (age > 0) {
                age--;
                ageDisplay.setText(String.valueOf(age));
            }
        });

        btnPlus.setOnClickListener(v -> {
            age++;
            ageDisplay.setText(String.valueOf(age));
        });

        // BMI/BMR button click listeners
        btnBMI.setOnClickListener(v -> openBottomSheet("BMI", calculateBMI()));
        btnBMR.setOnClickListener(v -> openBottomSheet("BMR", calculateBMR()));
    }

    private void selectGender(String gender) {
        selectedGender = gender;
        if ("Male".equals(gender)) {
            btnMale.setBackgroundTintList(getResources().getColorStateList(R.color.male_selected_color));
            btnFemale.setBackgroundTintList(getResources().getColorStateList(R.color.default_button_color));
        } else {
            btnFemale.setBackgroundTintList(getResources().getColorStateList(R.color.female_selected_color));
            btnMale.setBackgroundTintList(getResources().getColorStateList(R.color.default_button_color));
        }
    }

    private float calculateBMI() {
        return weight / ((height / 100.0f) * (height / 100.0f));
    }

    private float calculateBMR() {
        if ("Male".equals(selectedGender)) {
            return 88.36f + (13.4f * weight) + (4.8f * height) - (5.7f * age);
        } else {
            return 447.6f + (9.2f * weight) + (3.1f * height) - (4.3f * age);
        }
    }

    private void openBottomSheet(String type, float value) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ResultBottomSheetFragment bottomSheet = ResultBottomSheetFragment.newInstance(type, value, height, weight, age, selectedGender);
        bottomSheet.show(fragmentManager, bottomSheet.getTag());
    }
}
