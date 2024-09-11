package com.example.accurepp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class signupActivity extends AppCompatActivity {

    EditText edName;
    ProgressBar pb;
    Button btn;
    ImageButton bck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edName = findViewById(R.id.editTextName);
        pb = findViewById(R.id.progressBar);
        btn = findViewById(R.id.nextbutton1);
        bck=findViewById(R.id.back);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString().trim();
                if (name.isEmpty()) {
                    edName.setError("Name is required");
                    return;
                }

                pb.setVisibility(View.VISIBLE);
                pb.incrementProgressBy(25);

                Intent intent = new Intent(signupActivity.this, signupActivity1.class);
                intent.putExtra("USER_NAME", name); // Pass the user's name to the next activity
                startActivity(intent);
                finish();
            }
        });
    }
}
