package com.example.mygarage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class VehicleDetailsActivity extends AppCompatActivity {
    TextView summary;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        summary = findViewById(R.id.summary);
        backBtn = findViewById(R.id.backBtn);

        if (MainActivity.currentUserVehicleData != null) {
            summary.setText(MainActivity.currentUserVehicleData);
        } else {
            summary.setText("No vehicle data available. Please log in again.");
        }

        backBtn.setOnClickListener(v -> onBackPressed());
    }
}
