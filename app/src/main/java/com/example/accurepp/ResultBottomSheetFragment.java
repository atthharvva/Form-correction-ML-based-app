package com.example.accurepp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ResultBottomSheetFragment extends BottomSheetDialogFragment {

    private static final String ARG_TYPE = "type";
    private static final String ARG_VALUE = "value";
    private static final String ARG_HEIGHT = "height";
    private static final String ARG_WEIGHT = "weight";
    private static final String ARG_AGE = "age";
    private static final String ARG_GENDER = "gender";

    private String type;
    private float value;
    private int height;
    private int weight;
    private int age;
    private String gender;

    public static ResultBottomSheetFragment newInstance(String type, float value, int height, int weight, int age, String gender) {
        ResultBottomSheetFragment fragment = new ResultBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        args.putFloat(ARG_VALUE, value);
        args.putInt(ARG_HEIGHT, height);
        args.putInt(ARG_WEIGHT, weight);
        args.putInt(ARG_AGE, age);
        args.putString(ARG_GENDER, gender);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_bottom_sheet, container, false);

        TextView tvResult = view.findViewById(R.id.tvResult);
        TextView tvMessage = view.findViewById(R.id.tvMessage);

        if (getArguments() != null) {
            type = getArguments().getString(ARG_TYPE);
            value = getArguments().getFloat(ARG_VALUE);
            height = getArguments().getInt(ARG_HEIGHT);
            weight = getArguments().getInt(ARG_WEIGHT);
            age = getArguments().getInt(ARG_AGE);
            gender = getArguments().getString(ARG_GENDER);
        }

        tvResult.setText(String.format("%s: %.1f", type, value));

        if ("BMI".equals(type)) {
            tvMessage.setText(getBMIMessage(value));
        } else {
            tvMessage.setText(getBMRMessage(value));
        }

        return view;
    }

    private String getBMIMessage(float bmi) {
        if (bmi < 18.5f) {
            return "Underweight\nYou have a body weight that is less than what is considered normal. It's important to ensure you're getting enough nutrients.";
        } else if (bmi < 24.9f) {
            return "Normal weight\nYour body weight is within the normal range. Keep up with a balanced diet and regular exercise.";
        } else if (bmi < 29.9f) {
            return "Overweight\nYou have a higher body weight than what is considered normal. Consider adjusting your diet and exercise routine.";
        } else {
            return "Obesity\nYou have a body weight that is significantly higher than what is considered normal. Consult with a healthcare provider for personalized advice.";
        }
    }

    private String getBMRMessage(float bmr) {
        return "Your BMR is " + bmr + " kcal/day.\n\n" +
                "Daily calorie needs based on activity level:\n\n" +
                "Activity Level                                   | Calorie\n" +
                "-------------------------------------------------|---------\n" +
                "Sedentary: little or no exercise               | " + (bmr * 1.2f) + "\n" +
                "Exercise 1-3 times/week                        | " + (bmr * 1.375f) + "\n" +
                "Exercise 4-5 times/week                        | " + (bmr * 1.55f) + "\n" +
                "Daily exercise or intense exercise 3-4 times/week | " + (bmr * 1.725f) + "\n" +
                "Intense exercise 6-7 times/week                | " + (bmr * 1.9f);
    }
}
