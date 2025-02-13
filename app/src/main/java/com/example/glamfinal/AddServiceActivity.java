package com.example.glamfinal;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.FirebaseDatabase;

public class AddServiceActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etInfo;
    private EditText etPrice;
    private Spinner spinnerType;
    private Button btnAddService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        etName = findViewById(R.id.et_name);
        etInfo = findViewById(R.id.et_info);
        etPrice = findViewById(R.id.et_price);
        spinnerType = findViewById(R.id.spinner_type);
        btnAddService = findViewById(R.id.btn_add_service);

        // Create an array of service types
        String[] types = {"Makeup", "Hair", "Skin Treatment"};

        // Create an adapter for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);

        // Set the adapter on the spinner
        spinnerType.setAdapter(adapter);

        // Set a click listener for the Add Service button
        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the form
                String name = etName.getText().toString();
                String info = etInfo.getText().toString();
                String price = etPrice.getText().toString();
                String type = spinnerType.getSelectedItem().toString();

                // Assign a numeric value to the type
                int typeValue = 1;
                if (type.equals("Hair")) {
                    typeValue = 2;
                } else if (type.equals("Skin Treatment")) {
                    typeValue = 3;
                }

                // Create a new service object
                Item service = new Item(name, info, price, typeValue);

                // Add the service to the Firebase database
                FirebaseDatabase.getInstance().getReference("services").push().setValue(service);

                // Show a toast message to confirm that the service was added
                Toast.makeText(AddServiceActivity.this, "Service added", Toast.LENGTH_SHORT).show();


            }
        });
    }
}