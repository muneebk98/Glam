package com.example.glamfinal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class update_user_info extends AppCompatActivity {
    EditText etUpdateName, etUpdateEmail, etUpdatePassword;
    Button btnUpdate;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        etUpdateName = findViewById(R.id.etUpdateName);
        etUpdateEmail = findViewById(R.id.etUpdateEmail);
        etUpdatePassword = findViewById(R.id.etUpdatePassword);
        btnUpdate = findViewById(R.id.btnUpdate);
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        String phoneNumber = sharedPreferences.getString("number", null);
        reference = FirebaseDatabase.getInstance().getReference("Users").child(phoneNumber);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo(phoneNumber);
            }
        });

        loadExistingData(phoneNumber);
    }

    private void loadExistingData(String phoneNumber) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    etUpdateName.setText(user.getUsername());
                    etUpdateEmail.setText(user.getEmail());
                    etUpdatePassword.setText(user.getPassword());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(update_user_info.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserInfo(String phoneNumber) {
        String newName = etUpdateName.getText().toString().trim();
        String newEmail = etUpdateEmail.getText().toString().trim();
        String newPassword = etUpdatePassword.getText().toString().trim();

        if (!newName.isEmpty() && !newEmail.isEmpty() && !newPassword.isEmpty()) {
            reference.child("username").setValue(newName);
            reference.child("email").setValue(newEmail);
            reference.child("password").setValue(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(update_user_info.this, "User info updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(update_user_info.this, "Failed to update user info", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
    }
}
