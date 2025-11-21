package com.example.mygarage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReminderListActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView recyclerView;
    private TextView titleTv;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);

        db = AppDatabase.getInstance(this);

        recyclerView = findViewById(R.id.reminderRecycler);
        titleTv = findViewById(R.id.titleText);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    private void loadData() {
        UserEntity user = MainActivity.currentUser;
        List<VehicleEntity> vehicles = new ArrayList<>();
        if (user != null) {
            vehicles = db.vehicleDao().getAllForUser(user.id);
        }

        if (vehicles.isEmpty()) {
            Toast.makeText(this, "No vehicles found. Add a vehicle first.", Toast.LENGTH_SHORT).show();
            return;
        }

        titleTv.setText("All Reminders");

        List<ReminderDisplayItem> displayItems = new ArrayList<>();

        for (VehicleEntity v : vehicles) {
            List<ReminderEntity> reminders = db.reminderDao().getByVehicle(v.id);
            for (ReminderEntity r : reminders) {
                ReminderDisplayItem item = new ReminderDisplayItem();
                item.id = r.id;
                item.vehicleId = v.id;
                item.vehicleName = v.vehicleName;
                item.title = r.title;
                item.dueMillis = r.dueMillis;
                item.enabled = r.enabled;
                item.note = r.title != null ? r.title.replaceFirst("^(Service: |Maintenance: )", "") : "";
                displayItems.add(item);
            }
        }

        ReminderAdapter adapter = new ReminderAdapter(displayItems, this);
        recyclerView.setAdapter(adapter);
    }
}
