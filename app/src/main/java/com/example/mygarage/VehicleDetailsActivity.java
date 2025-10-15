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

        summary.setText("Vehicle: Honda Civic\nFuel: $100\nService: $50\nOther: $20\nTotal: $170 (Mock data)");

        backBtn.setOnClickListener(v -> onBackPressed());
    }
}
