package com.example.accurepp;

import static android.app.ProgressDialog.show;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {
//    EditText email,password;
//    Button signinbut;
//    FirebaseAuth fAuth;
//    TextView clicklink;
//    ImageButton bck;

    EditText email, password;
    Button signinbut;
    TextView clicklink;
    ImageButton bck;

    // Firebase Authentication instance
    FirebaseAuth fAuth;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
//        email= findViewById(R.id.email);
//        password = findViewById(R.id.password);
//        signinbut = findViewById(R.id.loginbut);
//        fAuth=FirebaseAuth.getInstance();
//        clicklink=findViewById(R.id.notegis);
//        bck=findViewById(R.id.back);
//        bck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        signinbut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String emaill = email.getText().toString();
//                String passwordd = password.getText().toString().trim();
//
//                if (TextUtils.isEmpty(emaill)) {
//                    email.setError("Email is Required");
//                    return;
//                }
//                if (TextUtils.isEmpty(passwordd)) {
//                    password.setError("Password is Required");
//                    return;
//                }
//                if (passwordd.length() < 8) {
//                    password.setError("Password must be >= 8 Characters");
//                    return;
//                }
//                fAuth.signInWithEmailAndPassword(emaill,passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(SignIn.this,"Logged in Succesfully",Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(), dashboard.class));
//                        }
//                        else{
//                            Toast.makeText(SignIn.this, "Error Occured", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
//        clicklink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(SignIn.this,signupActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });
//    }
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signinbut = findViewById(R.id.loginbut);
        fAuth = FirebaseAuth.getInstance();
        clicklink = findViewById(R.id.notegis);
        bck = findViewById(R.id.back);

        // Password visibility toggle logic
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2; // Index for drawableEnd (right side)

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Toggle password visibility
                        if (password.getTransformationMethod() instanceof PasswordTransformationMethod) {
                            // Show password
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                            // Hide password again after 5 seconds
                            password.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                }
                            }, 5000); // 5 seconds delay
                        } else {
                            // Hide password
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        // Back button logic
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the current activity
            }
        });

        // Sign in button logic
        signinbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emaill = email.getText().toString();
                String passwordd = password.getText().toString().trim();

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

                // Firebase sign-in logic
                fAuth.signInWithEmailAndPassword(emaill, passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignIn.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), dashboard.class)); // Redirect to dashboard
                        } else {
                            Toast.makeText(SignIn.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Sign-up link logic
        clicklink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignIn.this, signupActivity1.class); // Redirect to sign-up activity
                startActivity(i);
                finish();
            }
        });
    }
}