package com.example.glamfinal;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewListActivity extends AppCompatActivity {

    private RecyclerView rvReviews;
    private ReviewsAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        rvReviews = findViewById(R.id.rvReviews);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReviewsAdapter(new ArrayList<>());
        rvReviews.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        fetchReviews();
    }

    private void fetchReviews() {
        DatabaseReference reviewsRef = databaseReference.child("Reviews");
        reviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Review> fetchedReviews = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Review review = snapshot.getValue(Review.class);
                    if (review != null && review.getPhoneno() != null) {
                        fetchUsername(review, fetchedReviews);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle possible errors
            }
        });
    }

    private void fetchUsername(Review review, List<Review> fetchedReviews) {
        DatabaseReference userRef = databaseReference.child("Users").child(review.getPhoneno());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue(String.class);
                if (username != null) {
                    review.setUsername(username);
                    fetchedReviews.add(review);
                    adapter.addReview(review);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible database errors
            }
        });
    }
}
