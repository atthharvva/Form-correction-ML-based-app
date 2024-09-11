package com.example.accurepp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signupActivity1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText edage;
    ProgressBar pb;
    Button btn;
    String name;
    ImageButton bck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        edage = findViewById(R.id.editTextAge);
        pb = findViewById(R.id.progressBar);
        btn = findViewById(R.id.button);

        Spinner spinner = findViewById(R.id.spinnerSex);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        bck=findViewById(R.id.back);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get the name from the previous activity
        name = getIntent().getStringExtra("USER_NAME");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String age = edage.getText().toString().trim();
                if (TextUtils.isEmpty(age)) {
                    edage.setError("Age is required");
                    return;
                }

                pb.setVisibility(View.VISIBLE);
                pb.incrementProgressBy(25);

                Intent intent = new Intent(signupActivity1.this,signupActivity3.class);
                intent.putExtra("USER_NAME", name);
                intent.putExtra("USER_AGE", age);  // Pass the user's age to the next activity
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "You Select: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
