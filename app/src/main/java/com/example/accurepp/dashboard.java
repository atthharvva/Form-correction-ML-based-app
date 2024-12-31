package com.example.accurepp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Calendar;

public class dashboard extends AppCompatActivity {

//    private FirebaseAuth fAuth;
//
//    private Button signOutButton;
//    private TextView greetingTextView;
//    private ViewPager2 viewPager;
//    private SliderAdapter sliderAdapter;
//    private final int[] imageIds = {R.drawable.firstwr, R.drawable.secwr, R.drawable.thirdwr}; // Your image resources
//    private final Handler handler = new Handler(Looper.getMainLooper());
//    private final int slideInterval = 1000;
//    ImageButton bbmibbmr, nutri, wrklist, waterlevl;

    private FirebaseAuth fAuth;
    private DatabaseReference databaseReference;

    private Button signOutButton;
    private TextView greetingTextView, usernameTextView; // Added TextView for username
    private ViewPager2 viewPager;
    private SliderAdapter sliderAdapter;
    private final int[] imageIds = {R.drawable.firstwr, R.drawable.secwr, R.drawable.thirdwr}; // Your image resources
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final int slideInterval = 1000;
    ImageButton bbmibbmr, nutri, wrklist, waterlevl;
    ImageView profileImage;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private Uri imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);


//        fAuth = FirebaseAuth.getInstance();
//        signOutButton = findViewById(R.id.signOutButton);
//        greetingTextView = findViewById(R.id.greetingTextView);
//        viewPager = findViewById(R.id.viewPager);
//        bbmibbmr= findViewById(R.id.bmibmr);
//        nutri= findViewById(R.id.nutrihelp);
//        wrklist=findViewById(R.id.workoutlst);
//        waterlevl=findViewById(R.id.water);
//
//        bbmibbmr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(dashboard.this, bmibmr.class);
//                startActivity(i);
//                finish();
//            }
//        });
//
//        nutri.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(dashboard.this, nutrihelp.class);
//                startActivity(i);
//                finish();
//            }
//        });
//        wrklist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(dashboard.this, workoutlist.class);
//                startActivity(i);
//                finish();
//
//            }
//        });
//
//
//        // Set the greeting based on the current time
//        setGreetingMessage();
//
//        // Set up the slider
//        sliderAdapter = new SliderAdapter(this, imageIds);
//        viewPager.setAdapter(sliderAdapter);
//
//        // Start the automatic sliding
//        startImageSlider();
//
//        signOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signOut();
//            }
//        });
//    }
//
//    private void setGreetingMessage() {
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//
//        String greeting;
//        if (hour >= 5 && hour < 12) {
//            greeting = "Good Morning!";
//        } else if (hour >= 12 && hour < 17) {
//            greeting = "Good Afternoon!";
//        } else {
//            greeting = "Good Evening!";
//        }
//
//        greetingTextView.setText(greeting);
//    }
//
//    private void startImageSlider() {
//        final Runnable update = new Runnable() {
//            int currentPage = 0;
//
//            @Override
//            public void run() {
//                if (currentPage == imageIds.length) {
//                    currentPage = 0;
//                }
//                viewPager.setCurrentItem(currentPage++, true);
//                // Schedule the next update
//                handler.postDelayed(this, slideInterval);
//            }
//        };
//
//        handler.postDelayed(update, slideInterval);
//    }
//
//    private void signOut() {
//        fAuth.signOut();
//        Toast.makeText(dashboard.this, "Signed Out", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(dashboard.this, SignIn.class));
//        finish();
//    }

        fAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(fAuth.getCurrentUser().getUid()); // Reference to current user

        signOutButton = findViewById(R.id.signOutButton);
        greetingTextView = findViewById(R.id.greetingTextView);
        usernameTextView = findViewById(R.id.usernamee); // Initialize the username TextView
        viewPager = findViewById(R.id.viewPager);
        bbmibbmr = findViewById(R.id.bmibmr);
        nutri = findViewById(R.id.nutrihelp);
        wrklist = findViewById(R.id.workoutlst);
        waterlevl = findViewById(R.id.waterr);
        profileImage = findViewById(R.id.profileImage);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this, formcorrML.class);
                startActivity(i);
                finish();
            }
        });

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
        waterlevl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard.this,waterrem.class);
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

        // Retrieve username and display it
        retrieveUsername();

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

    private void retrieveUsername() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue(String.class);
                    usernameTextView.setText(username); // Corrected this line
                } else {
                    Toast.makeText(dashboard.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(dashboard.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void showImagePickerOptions() {
//        // Show options to pick image from gallery or capture with camera
//        String[] options = {"Camera", "Gallery"};
//        new androidx.appcompat.app.AlertDialog.Builder(this)
//                .setTitle("Choose Profile Picture")
//                .setItems(options, (dialog, which) -> {
//                    if (which == 0) {
//                        // Open camera
//                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
//                    } else {
//                        // Open gallery
//                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
//                    }
//                })
//                .show();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
//                // Image selected from gallery
//                imageUri = data.getData();
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                    Bitmap resizedBitmap = resizeBitmap(bitmap, profileImage.getWidth(), profileImage.getHeight());
//                    profileImage.setImageBitmap(resizedBitmap); // Update the ImageView with the resized profile picture
//                    saveProfileImageUri(imageUri); // Save the URI for persistence
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
//                }
//            } else if (requestCode == CAMERA_REQUEST && data != null && data.getExtras() != null) {
//                // Image taken with camera
//                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
//                Bitmap resizedBitmap = resizeBitmap(imageBitmap, profileImage.getWidth(), profileImage.getHeight());
//                profileImage.setImageBitmap(resizedBitmap); // Update the ImageView with the resized profile picture
//                // Here, you may want to save this bitmap as a file for persistence
//            }
//        }
//    }
//
//    private Bitmap resizeBitmap(Bitmap originalBitmap, int width, int height) {
//        return Bitmap.createScaledBitmap(originalBitmap, width, height, true);
//    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                Bitmap resizedBitmap = resizeBitmap(bitmap, profileImage.getWidth(), profileImage.getHeight());
                profileImage.setImageBitmap(resizedBitmap);
                saveProfileImageUri(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Bitmap resizeBitmap(Bitmap originalBitmap, int width, int height) {
        return Bitmap.createScaledBitmap(originalBitmap, width, height, true);
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

    private void saveProfileImageUri(Uri uri) {
        // Implement your method to save the image URI for persistence
        // This could be saving it to SharedPreferences or Firebase Storage
    }
}
