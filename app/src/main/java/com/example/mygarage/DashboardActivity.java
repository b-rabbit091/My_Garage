package com.example.mygarage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    Button addVehicle, addExpense, reminders, vehicleDetails, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        addVehicle = findViewById(R.id.addVehicle);
        addExpense = findViewById(R.id.addExpense);
        reminders = findViewById(R.id.reminders);
        vehicleDetails = findViewById(R.id.vehicleDetails);
        backBtn = findViewById(R.id.backBtn);

        addVehicle.setOnClickListener(v -> startActivity(new Intent(this, AddVehicleActivity.class)));
        addExpense.setOnClickListener(v -> startActivity(new Intent(this, AddExpenseActivity.class)));
        reminders.setOnClickListener(v -> startActivity(new Intent(this, RemindersActivity.class)));
        vehicleDetails.setOnClickListener(v -> startActivity(new Intent(this, VehicleDetailsActivity.class)));

        backBtn.setOnClickListener(v -> onBackPressed());
    }
}
