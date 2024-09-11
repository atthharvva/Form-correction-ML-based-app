package com.example.accurepp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class dashboard extends AppCompatActivity {

    private FirebaseAuth fAuth;

    private Button signOutButton;
    private TextView greetingTextView;
    private ViewPager2 viewPager;
    private SliderAdapter sliderAdapter;
    private final int[] imageIds = {R.drawable.firstwr, R.drawable.secwr, R.drawable.thirdwr}; // Your image resources
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final int slideInterval = 1000;
    ImageButton bbmibbmr, nutri, wrklist, waterlevl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        fAuth = FirebaseAuth.getInstance();
        signOutButton = findViewById(R.id.signOutButton);
        greetingTextView = findViewById(R.id.greetingTextView);
        viewPager = findViewById(R.id.viewPager);
        bbmibbmr= findViewById(R.id.bmibmr);
        nutri= findViewById(R.id.nutrihelp);
        wrklist=findViewById(R.id.workoutlst);
        waterlevl=findViewById(R.id.water);

        bbmibbmr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard.this, bmibmr.class);
                startActivity(i);
                finish();
            }
        });

        nutri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard.this, nutrihelp.class);
                startActivity(i);
                finish();
            }
        });
        wrklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard.this, workoutlist.class);
                startActivity(i);
                finish();

            }
        });


        // Set the greeting based on the current time
        setGreetingMessage();

        // Set up the slider
        sliderAdapter = new SliderAdapter(this, imageIds);
        viewPager.setAdapter(sliderAdapter);

        // Start the automatic sliding
        startImageSlider();

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void setGreetingMessage() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (hour >= 5 && hour < 12) {
            greeting = "Good Morning!";
        } else if (hour >= 12 && hour < 17) {
            greeting = "Good Afternoon!";
        } else {
            greeting = "Good Evening!";
        }

        greetingTextView.setText(greeting);
    }

    private void startImageSlider() {
        final Runnable update = new Runnable() {
            int currentPage = 0;

            @Override
            public void run() {
                if (currentPage == imageIds.length) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                // Schedule the next update
                handler.postDelayed(this, slideInterval);
            }
        };

        handler.postDelayed(update, slideInterval);
    }

    private void signOut() {
        fAuth.signOut();
        Toast.makeText(dashboard.this, "Signed Out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(dashboard.this, SignIn.class));
        finish();
    }
}
