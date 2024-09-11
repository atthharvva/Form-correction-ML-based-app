package com.example.accurepp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signupActivity3 extends AppCompatActivity {

    EditText email, password, phoneNumber;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference;
    Button btn;
    ImageButton bck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phno);
        btn = findViewById(R.id.button);
        fAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), dashboard.class));
            finish();
        }
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
                String emaill = email.getText().toString().trim();
                String passwordd = password.getText().toString().trim();
                String phone = phoneNumber.getText().toString().trim();
                Intent intent = getIntent();
                String name = intent.getStringExtra("USER_NAME");
                String age = intent.getStringExtra("USER_AGE");

                if (TextUtils.isEmpty(emaill)) {
                    email.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(passwordd)) {
                    password.setError("Password is Required");
                    return;
                }
                if (passwordd.length() < 8) {
                    password.setError("Password must be >= 8 Characters");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(emaill, passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Retrieve additional data from intent


                            // Save user data to the Realtime Database
                            String userId = fAuth.getCurrentUser().getUid();
                            User user = new User(emaill, passwordd, phone, name, age);
                            databaseReference.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(signupActivity3.this, "Account created ", Toast.LENGTH_SHORT).show();
                                        Intent intent = getIntent();


                                        startActivity(new Intent(getApplicationContext(), dashboard.class));
                                        finish();
                                    } else {
                                        Toast.makeText(signupActivity3.this, "Failed attempt", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(signupActivity3.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
