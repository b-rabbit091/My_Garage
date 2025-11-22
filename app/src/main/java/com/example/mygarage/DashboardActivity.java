package com.example.mygarage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    Button backBtn;
    TextView txtDashboard;

    LinearLayout addVehicle;
    LinearLayout addExpense;
    LinearLayout reminders;
    LinearLayout vehicleDetails;
    LinearLayout reminderList;
    LinearLayout reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        backBtn = findViewById(R.id.backBtn);
        txtDashboard = findViewById(R.id.txtDashboard);

        UserEntity user = MainActivity.currentUser;
        if (user != null) {
            txtDashboard.setText("Welcome, " + user.firstName + " " + user.lastName);
        } else {
            txtDashboard.setText("Welcome");
        }

        addVehicle = findViewById(R.id.addVehicle);
        addExpense = findViewById(R.id.addExpense);
        reminders = findViewById(R.id.reminders);
        vehicleDetails = findViewById(R.id.vehicleDetails);
        reminderList = findViewById(R.id.reminderList);
        reports = findViewById(R.id.reports);

        backBtn.setOnClickListener(v -> finish());

        addVehicle.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, AddVehicleActivity.class)));

        addExpense.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, AddExpenseActivity.class)));

        reminders.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, RemindersActivity.class)));

        vehicleDetails.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, VehicleDetailsActivity.class)));

        reminderList.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, ReminderListActivity.class)));

        reports.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, ReportsActivity.class)));
    }
}
