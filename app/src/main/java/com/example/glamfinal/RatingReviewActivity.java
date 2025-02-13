package com.example.glamfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class RatingReviewActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView tvRatingDisplay;
    private EditText etReview;
    private Button btnSubmit;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        ratingBar = findViewById(R.id.ratingBar);
        tvRatingDisplay = findViewById(R.id.tvRatingDisplay);
        etReview = findViewById(R.id.etReview);
        btnSubmit = findViewById(R.id.btnSubmit);

        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) ->
                tvRatingDisplay.setText(String.valueOf(rating)));

        btnSubmit.setOnClickListener(view -> {
            float rating = ratingBar.getRating();
            String review = etReview.getText().toString().trim();
            String phoneno = sharedPreferences.getString("number", null);

            if (phoneno != null && !review.isEmpty()) {
                checkAndSubmitReview(phoneno, rating, review);
            } else {
                Toast.makeText(RatingReviewActivity.this, "Please enter a review", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAndSubmitReview(String phoneno, float rating, String review) {
        databaseReference.child("Reviews").child(phoneno).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) { // No existing review by this user
                    fetchUserNameAndSubmitReview(phoneno, rating, review);
                } else {
                    Toast.makeText(RatingReviewActivity.this, "You have already submitted a review.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RatingReviewActivity.this, "Failed to check existing review: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserNameAndSubmitReview(String phoneno, float rating, String review) {
        databaseReference.child("Users").child(phoneno).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("username")) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    submitReview(phoneno, username, rating, review);
                } else {
                    Toast.makeText(RatingReviewActivity.this, "Username is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RatingReviewActivity.this, "Failed to fetch user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitReview(String phoneno, String username, float rating, String review) {
        Review reviewObj = new Review(phoneno, username, (int) rating, review);
        databaseReference.child("Reviews").child(phoneno).setValue(reviewObj)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RatingReviewActivity.this, "Review submitted!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RatingReviewActivity.this, ReviewListActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RatingReviewActivity.this, "Failed to submit review: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public class Review {
        private String phoneno;
        private String username;
        private int rating;
        private String review;

        public Review(String phoneno, String username, int rating, String review) {
            this.phoneno = phoneno;
            this.username = username;
            this.rating = rating;
            this.review = review;
        }

        public String getPhoneno() {
            return phoneno;
        }

        public void setPhoneno(String phoneno) {
            this.phoneno = phoneno;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }
    }
}
