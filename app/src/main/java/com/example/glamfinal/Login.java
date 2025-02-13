package com.example.glamfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    TextView tv_signup_option;
    EditText et_login_number, et_login_password;
    Button btnlogin;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv_signup_option = findViewById(R.id.tv_signup_option);
        et_login_number = findViewById(R.id.et_login_number); // Assuming this ID is correct for the number EditText
        et_login_password = findViewById(R.id.et_login_password);
        btnlogin = findViewById(R.id.btnlogin);

        reference = FirebaseDatabase.getInstance().getReference("Users");

        tv_signup_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String number = et_login_number.getText().toString().trim();
        String password = et_login_password.getText().toString().trim();

        if (!number.isEmpty() && !password.isEmpty()) {
            reference.child(number).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null && user.getPassword().equals(password)) {
                            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name", user.getUsername());
                            editor.putString("number", number);
                            editor.putString("password", password);
                            editor.putBoolean("login", true);
                            editor.putBoolean("admin",false);
                            editor.apply();


                            Toast.makeText(Login.this, "Login successful. Welcome, " + user.getUsername() + "!", Toast.LENGTH_SHORT).show();

//                            Intent toReviewIntent = new Intent(Login.this, RatingReviewActivity.class);
//                            startActivity(toReviewIntent);
//                            finish();


                            Intent toReviewIntent = new Intent(Login.this, ServicesActivity.class);
                            startActivity(toReviewIntent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Invalid password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(Login.this, "Failed to login: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please enter number and password", Toast.LENGTH_SHORT).show();
        }
    }
}
