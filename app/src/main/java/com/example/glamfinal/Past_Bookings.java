package com.example.glamfinal;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Past_Bookings extends AppCompatActivity {


    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private DatabaseReference bookingsRef;
    private String userPhoneNumber;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);

        recyclerView = findViewById(R.id.rvBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve user phone number and admin status from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userPhoneNumber = prefs.getString("number", "");
        isAdmin = prefs.getBoolean("admin", false);

        if (userPhoneNumber != null) {
            if (isAdmin) {
                bookingsRef = FirebaseDatabase.getInstance().getReference("PastBookings");
            } else {
                bookingsRef = FirebaseDatabase.getInstance().getReference("PastBookings").child(userPhoneNumber);
            }
            fetchBookings();
        }
    }

    private void fetchBookings() {
        List<Booking> bookings = new ArrayList<>();
        bookingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookings.clear();
                if (isAdmin) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String phoneNumber = userSnapshot.getKey(); // Assuming the key is the phone number
                        for (DataSnapshot bookingSnapshot : userSnapshot.getChildren()) {
                            Booking booking = bookingSnapshot.getValue(Booking.class);
                            if (booking != null) {
                                booking.setPhoneNumber(phoneNumber);
                                booking.setBookingId(bookingSnapshot.getKey());
                                bookings.add(booking);
                            }
                        }
                    }
                } else {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Booking booking = snapshot.getValue(Booking.class);
                        if (booking != null) {
                            booking.setBookingId(snapshot.getKey()); // Store Firebase key in booking for easy reference
                            bookings.add(booking);
                        }
                    }
                }
                if (adapter == null) {
                    adapter = new BookingAdapter(bookings, Past_Bookings.this,true);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible database errors
            }
        });
    }
}