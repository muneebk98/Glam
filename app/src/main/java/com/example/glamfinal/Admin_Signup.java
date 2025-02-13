package com.example.glamfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Admin_Signup extends AppCompatActivity {
    EditText et_signup_name, et_signup_number, et_signup_email, et_signup_password, et_signup_admin_key;
    TextView tv_tologin;
    Button btn_signup;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        et_signup_name = findViewById(R.id.et_signup_name);
        et_signup_number = findViewById(R.id.et_signup_number);
        et_signup_email = findViewById(R.id.et_signup_email);
        et_signup_password = findViewById(R.id.et_signup_password);
        et_signup_admin_key = findViewById(R.id.et_signup_admin_key);
        tv_tologin = findViewById(R.id.tv_tologin);
        btn_signup = findViewById(R.id.btn_signup);

        reference = FirebaseDatabase.getInstance().getReference("Users");

        tv_tologin.setOnClickListener(v -> startActivity(new Intent(Admin_Signup.this, Admin_Login.class)));

        btn_signup.setOnClickListener(v -> {
            final String name = et_signup_name.getText().toString().trim();
            final String number = et_signup_number.getText().toString().trim();
            final String email = et_signup_email.getText().toString().trim();
            final String password = et_signup_password.getText().toString().trim();
            final String adminKey = et_signup_admin_key.getText().toString().trim();

            if (!name.isEmpty() && !number.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                if ("123".equals(adminKey)) { // Check if the admin key is correct
                    checkNumberExists(number, name, email, password);
                } else {
                    Toast.makeText(Admin_Signup.this, "Invalid Admin Key", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Admin_Signup.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkNumberExists(final String number, final String name, final String email, final String password) {
        reference.child(number).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(Admin_Signup.this, "Number already registered", Toast.LENGTH_SHORT).show();
                } else {
                    registerNewUser(name, number, email, password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin_Signup.this, "Failed to check user existence: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerNewUser(String name, String number, String email, String password) {
        User newUser = new User(name, email, password, number);
        reference.child(number).setValue(newUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Admin_Signup.this, "Successfully registered as Admin", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Admin_Signup.this, Admin_Login.class));
                finish();
            } else {
                Toast.makeText(Admin_Signup.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
