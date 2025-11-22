package com.example.mygarage;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
    private Spinner spinnerVehicleList;

    private final List<Long> vehicleIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);

        db = AppDatabase.getInstance(this);

        recyclerView = findViewById(R.id.reminderRecycler);
        titleTv = findViewById(R.id.titleText);
        backBtn = findViewById(R.id.backBtn);
        spinnerVehicleList = findViewById(R.id.spinnerVehicleList);

        backBtn.setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupVehicleSpinner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!vehicleIds.isEmpty()) {
            int pos = spinnerVehicleList.getSelectedItemPosition();
            if (pos >= 0 && pos < vehicleIds.size()) {
                long vehicleId = vehicleIds.get(pos);
                loadDataForVehicle(vehicleId);
            }
        } else {
            setupVehicleSpinner();
        }
    }

    private void setupVehicleSpinner() {
        UserEntity user = MainActivity.currentUser;
        List<VehicleEntity> vehicles = new ArrayList<>();
        if (user != null) {
            vehicles = db.vehicleDao().getAllForUser(user.id);
        }

        List<String> names = new ArrayList<>();
        vehicleIds.clear();

        for (VehicleEntity v : vehicles) {
            names.add(v.vehicleName);
            vehicleIds.add(v.id);
        }

        if (names.isEmpty()) {
            names.add("Add a vehicle first");
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicleList.setAdapter(adapter);

        spinnerVehicleList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < vehicleIds.size()) {
                    long vehicleId = vehicleIds.get(position);
                    loadDataForVehicle(vehicleId);
                } else {
                    recyclerView.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                recyclerView.setAdapter(null);
            }
        });

        if (!vehicleIds.isEmpty()) {
            spinnerVehicleList.setSelection(0);
            loadDataForVehicle(vehicleIds.get(0));
        } else {
            Toast.makeText(this, "No vehicles found. Add a vehicle first.", Toast.LENGTH_SHORT).show();
            recyclerView.setAdapter(null);
        }
    }

    private void loadDataForVehicle(long vehicleId) {
        titleTv.setText("All Reminders");

        List<ReminderEntity> reminders = db.reminderDao().getByVehicle(vehicleId);
        List<ReminderDisplayItem> displayItems = new ArrayList<>();

        VehicleEntity v = db.vehicleDao().getById(vehicleId);
        String vehicleName = (v != null) ? v.vehicleName : "";

        for (ReminderEntity r : reminders) {
            ReminderDisplayItem item = new ReminderDisplayItem();
            item.id = r.id;
            item.vehicleId = vehicleId;
            item.vehicleName = vehicleName;
            item.title = r.title;
            item.dueMillis = r.dueMillis;
            item.enabled = r.enabled;
            item.note = r.title != null
                    ? r.title.replaceFirst("^(Fuel: |Washing: |Service: |Maintenance: )", "")
                    : "";
            displayItems.add(item);
        }

        ReminderAdapter adapter = new ReminderAdapter(displayItems, this);
        recyclerView.setAdapter(adapter);
    }
}
