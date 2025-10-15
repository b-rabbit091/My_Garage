package com.example.mygarage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddVehicleActivity extends AppCompatActivity {
    EditText vehicleName, model, make, purchaseDate, mileage;
    Button saveBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        vehicleName = findViewById(R.id.vehicleName);
        model = findViewById(R.id.model);
        make = findViewById(R.id.make);
        purchaseDate = findViewById(R.id.purchaseDate);
        mileage = findViewById(R.id.mileage);
        saveBtn = findViewById(R.id.saveBtn);
        backBtn = findViewById(R.id.backBtn);

        saveBtn.setOnClickListener(v ->
                Toast.makeText(this, "Vehicle saved (mock).", Toast.LENGTH_SHORT).show());

        backBtn.setOnClickListener(v -> onBackPressed());
    }
}
