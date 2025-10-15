package com.example.mygarage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RemindersActivity extends AppCompatActivity {
    Switch serviceReminder, maintenanceReminder;
    Button backBtn, saveServiceBtn, saveMaintenanceBtn;
    DatePicker serviceDatePicker, maintenanceDatePicker;
    EditText serviceNote, maintenanceNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        serviceReminder = findViewById(R.id.serviceReminder);
        maintenanceReminder = findViewById(R.id.maintenanceReminder);
        backBtn = findViewById(R.id.backBtn);

        serviceDatePicker = findViewById(R.id.serviceDatePicker);
        maintenanceDatePicker = findViewById(R.id.maintenanceDatePicker);

        serviceNote = findViewById(R.id.serviceNote);
        maintenanceNote = findViewById(R.id.maintenanceNote);

        saveServiceBtn = findViewById(R.id.saveServiceReminder);
        saveMaintenanceBtn = findViewById(R.id.saveMaintenanceReminder);

        // Toggle switches
        serviceReminder.setOnCheckedChangeListener((b, checked) ->
                Toast.makeText(this, "Service reminder " + (checked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show());

        maintenanceReminder.setOnCheckedChangeListener((b, checked) ->
                Toast.makeText(this, "Maintenance reminder " + (checked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show());

        // Save Service Reminder
        saveServiceBtn.setOnClickListener(v -> {
            int day = serviceDatePicker.getDayOfMonth();
            int month = serviceDatePicker.getMonth() + 1; // Month starts from 0
            int year = serviceDatePicker.getYear();
            String note = serviceNote.getText().toString();

            Toast.makeText(this,
                    "Service Reminder set for " + day + "/" + month + "/" + year +
                            "\nNote: " + note, Toast.LENGTH_LONG).show();
        });

        // Save Maintenance Reminder
        saveMaintenanceBtn.setOnClickListener(v -> {
            int day = maintenanceDatePicker.getDayOfMonth();
            int month = maintenanceDatePicker.getMonth() + 1;
            int year = maintenanceDatePicker.getYear();
            String note = maintenanceNote.getText().toString();

            Toast.makeText(this,
                    "Maintenance Reminder set for " + day + "/" + month + "/" + year +
                            "\nNote: " + note, Toast.LENGTH_LONG).show();
        });

        backBtn.setOnClickListener(v -> onBackPressed());
    }
}
