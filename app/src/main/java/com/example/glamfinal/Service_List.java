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
public class Service_List extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<Item> items;
    private FirebaseDatabase database;
    private DatabaseReference servicesRef;
    private int serviceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service_list);

        // Initialize the Firebase database
        database = FirebaseDatabase.getInstance();

        // Get a reference to the Services node
        servicesRef = database.getReference("services");

        // Get the service type from the intent
        serviceType = getIntent().getIntExtra("service_id", 1);

        recyclerView = findViewById(R.id.rvServices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        items = new ArrayList<>();

        // Add a value event listener to the Services node
        servicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the items list
                items.clear();

                // Iterate through the child nodes of the Services node
                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                    // Parse the service data from the DataSnapshot
                    Item service = serviceSnapshot.getValue(Item.class);
                    service.setType(serviceSnapshot.child("type").getValue(Integer.class));

                    // Check if the service type matches the type received from ServicesActivity
                    if (service.getType() == serviceType) {
                        // Add the service to the items list
                        items.add(service);
                    }
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });

        // Example of retrieving a phone number stored in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userPhoneNo = sharedPreferences.getString("number", "");

        adapter = new RecyclerViewAdapter(items, userPhoneNo);
        recyclerView.setAdapter(adapter);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}