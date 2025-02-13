package com.example.glamfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

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

public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemRemoveClickListener {

    private RecyclerView recyclerView;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btnProceedToPayment;
    private List<Item> services = new ArrayList<>();
    private DatabaseReference cartRef;
    private List<String> keys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.rvCartItems);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        btnProceedToPayment = findViewById(R.id.btnProceedToPayment);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userPhoneNo = prefs.getString("number", "");

        cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(userPhoneNo);
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                services.clear();
                keys.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    services.add(item);
                    keys.add(snapshot.getKey());
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible database errors
            }
        });

        CartAdapter adapter = new CartAdapter(services, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        btnProceedToPayment.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, Payment.class);
            intent.putExtra("userPhoneNumber", userPhoneNo);
            intent.putExtra("date", datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear());
            intent.putExtra("time", timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
            startActivity(intent);
        });


    }

    @Override
    public void onItemRemoveClicked(int position) {
        if (position >= 0 && position < keys.size()) {
            cartRef.child(keys.get(position)).removeValue();
            services.remove(position); // Remove from local list
            keys.remove(position); // Remove the key from the list
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
