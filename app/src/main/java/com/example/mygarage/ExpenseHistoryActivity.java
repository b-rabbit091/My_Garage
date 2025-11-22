package com.example.mygarage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseHistoryActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView recyclerView;
    private TextView titleTv;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_history);

        db = AppDatabase.getInstance(this);

        recyclerView = findViewById(R.id.expenseRecycler);
        titleTv = findViewById(R.id.titleText);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    private void loadData() {
        UserEntity user = MainActivity.currentUser;
        if (user == null) {
            Toast.makeText(this, "Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        VehicleEntity latest = db.vehicleDao().getLatestVehicleForUser(user.id);
        if (latest == null) {
            Toast.makeText(this, "No vehicle found. Add a vehicle first.", Toast.LENGTH_SHORT).show();
            return;
        }

        titleTv.setText("Expenses for " + latest.vehicleName);

        List<ExpenseEntity> expenses = db.expenseDao().getByVehicle(latest.id);
        ExpenseAdapter adapter = new ExpenseAdapter(expenses, this);
        recyclerView.setAdapter(adapter);
    }
}
