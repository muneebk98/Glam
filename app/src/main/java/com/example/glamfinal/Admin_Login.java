package com.example.glamfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_Login extends AppCompatActivity {
    TextView tv_signup_option;
    EditText et_login_number, et_login_password, et_login_admin_key;
    Button btnlogin;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        tv_signup_option = findViewById(R.id.tv_signup_option);
        et_login_number = findViewById(R.id.et_login_number);
        et_login_password = findViewById(R.id.et_login_password);
        et_login_admin_key = findViewById(R.id.et_login_admin_key); // Admin key EditText
        btnlogin = findViewById(R.id.btnlogin);

        reference = FirebaseDatabase.getInstance().getReference("Users");

        tv_signup_option.setOnClickListener(v -> startActivity(new Intent(Admin_Login.this, Admin_Signup.class)));

        btnlogin.setOnClickListener(v -> login());
    }

    private void login() {
        String number = et_login_number.getText().toString().trim();
        String password = et_login_password.getText().toString().trim();
        String adminKey = et_login_admin_key.getText().toString().trim(); // Get the admin key

        if (!number.isEmpty() && !password.isEmpty() && !adminKey.isEmpty()) {
            reference.child(number).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null && user.getPassword().equals(password) && adminKey.equals("123")) { // Verify password and admin key
                            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name", user.getUsername());
                            editor.putString("number", number);
                            editor.putBoolean("login", true);
                            editor.putBoolean("admin", true); // Set admin flag
                            editor.apply();

                            Toast.makeText(Admin_Login.this, "Admin Login successful. Welcome, " + user.getUsername() + "!", Toast.LENGTH_SHORT).show();
                            Intent toReviewIntent = new Intent(Admin_Login.this, ViewBookingsActivity.class); // Redirect to an admin-specific activity
                            startActivity(toReviewIntent);
                            finish();
                        } else {
                            Toast.makeText(Admin_Login.this, "Invalid login credentials or admin key", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Admin_Login.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(Admin_Login.this, "Failed to login: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
