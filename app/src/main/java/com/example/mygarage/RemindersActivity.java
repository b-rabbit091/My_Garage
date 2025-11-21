package com.example.mygarage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RemindersActivity extends AppCompatActivity {

    private static final int REQ_POST_NOTIFICATIONS = 101;

    private Switch serviceReminder;
    private Switch maintenanceReminder;
    private Button backBtn;
    private Button saveServiceBtn;
    private Button saveMaintenanceBtn;
    private DatePicker serviceDatePicker;
    private DatePicker maintenanceDatePicker;
    private TimePicker serviceTimePicker;
    private TimePicker maintenanceTimePicker;
    private EditText serviceNote;
    private EditText maintenanceNote;
    private Spinner vehicleSpinner;

    private AppDatabase db;
    private final List<Long> vehicleIds = new ArrayList<>();
    private long selectedVehicleId = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        db = AppDatabase.getInstance(this);

        serviceReminder = findViewById(R.id.serviceReminder);
        maintenanceReminder = findViewById(R.id.maintenanceReminder);
        backBtn = findViewById(R.id.backBtn);

        serviceDatePicker = findViewById(R.id.serviceDatePicker);
        maintenanceDatePicker = findViewById(R.id.maintenanceDatePicker);
        serviceTimePicker = findViewById(R.id.serviceTimePicker);
        maintenanceTimePicker = findViewById(R.id.maintenanceTimePicker);

        serviceNote = findViewById(R.id.serviceNote);
        maintenanceNote = findViewById(R.id.maintenanceNote);

        saveServiceBtn = findViewById(R.id.saveServiceReminder);
        saveMaintenanceBtn = findViewById(R.id.saveMaintenanceReminder);

        vehicleSpinner = findViewById(R.id.spinnerVehicle);

        if (serviceTimePicker != null) {
            serviceTimePicker.setIs24HourView(false);
        }
        if (maintenanceTimePicker != null) {
            maintenanceTimePicker.setIs24HourView(false);
        }

        ensureNotificationPermission();
        loadVehicles();

        backBtn.setOnClickListener(v -> onBackPressed());

        saveServiceBtn.setOnClickListener(v -> {
            if (selectedVehicleId == -1L) {
                Toast.makeText(this, "Add a vehicle first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (serviceTimePicker == null) {
                Toast.makeText(this, "Time picker not available", Toast.LENGTH_SHORT).show();
                return;
            }

            long due = getMillisFromPickers(serviceDatePicker, serviceTimePicker);
            String note = serviceNote.getText().toString();

            ReminderEntity r = new ReminderEntity();
            r.vehicleId = selectedVehicleId;
            r.title = "Service: " + note;
            r.dueMillis = due;
            r.enabled = serviceReminder.isChecked();

            long id = db.reminderDao().insert(r);

            if (r.enabled) {
                scheduleAlarm(id, r.title, r.dueMillis);
                Toast.makeText(this, "Service reminder scheduled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Service reminder saved (disabled)", Toast.LENGTH_LONG).show();
            }
        });

        saveMaintenanceBtn.setOnClickListener(v -> {
            if (selectedVehicleId == -1L) {
                Toast.makeText(this, "Add a vehicle first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (maintenanceTimePicker == null) {
                Toast.makeText(this, "Time picker not available", Toast.LENGTH_SHORT).show();
                return;
            }

            long due = getMillisFromPickers(maintenanceDatePicker, maintenanceTimePicker);
            String note = maintenanceNote.getText().toString();

            ReminderEntity r = new ReminderEntity();
            r.vehicleId = selectedVehicleId;
            r.title = "Maintenance: " + note;
            r.dueMillis = due;
            r.enabled = maintenanceReminder.isChecked();

            long id = db.reminderDao().insert(r);

            if (r.enabled) {
                scheduleAlarm(id, r.title, r.dueMillis);
                Toast.makeText(this, "Maintenance reminder scheduled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Maintenance reminder saved (disabled)", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void ensureNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        REQ_POST_NOTIFICATIONS
                );
            }
        }
    }

    private void loadVehicles() {
        UserEntity user = MainActivity.currentUser;
        if (user == null) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            return;
        }

        List<VehicleEntity> vehicles = db.vehicleDao().getAllForUser(user.id);
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
        vehicleSpinner.setAdapter(adapter);

        vehicleSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent,
                                       android.view.View view,
                                       int position,
                                       long id) {
                if (position >= 0 && position < vehicleIds.size()) {
                    selectedVehicleId = vehicleIds.get(position);
                } else {
                    selectedVehicleId = -1L;
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedVehicleId = -1L;
            }
        });

        if (!vehicleIds.isEmpty()) {
            selectedVehicleId = vehicleIds.get(0);
        }
    }

    private int getHour(TimePicker tp) {
        if (tp == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return tp.getHour();
        } else {
            return tp.getCurrentHour();
        }
    }

    private int getMinute(TimePicker tp) {
        if (tp == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return tp.getMinute();
        } else {
            return tp.getCurrentMinute();
        }
    }

    private long getMillisFromPickers(DatePicker dp, TimePicker tp) {
        int day = dp.getDayOfMonth();
        int month = dp.getMonth();
        int year = dp.getYear();

        int hour = getHour(tp);
        int minute = getMinute(tp);

        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }
    private void scheduleAlarm(long reminderId, String title, long dueMillis) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (am == null) {
            return;
        }

        long now = System.currentTimeMillis();
        if (dueMillis <= now) {
            dueMillis = now + 5000; // 5 seconds later if in the past
        }

        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("notification_id", (int) reminderId);

        PendingIntent pi = PendingIntent.getBroadcast(
                this,
                (int) reminderId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Use inexact alarm – no SCHEDULE_EXACT_ALARM permission needed
        am.set(AlarmManager.RTC_WAKEUP, dueMillis, pi);
    }


//    private void scheduleAlarm(long reminderId, String title, long dueMillis) {
//        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        if (am == null) {
//            return;
//        }
//
//        long now = System.currentTimeMillis();
//        if (dueMillis <= now) {
//            dueMillis = now + 5000;
//        }
//
//        Intent intent = new Intent(this, ReminderReceiver.class);
//        intent.putExtra("title", title);
//        intent.putExtra("notification_id", (int) reminderId);
//
//        PendingIntent pi = PendingIntent.getBroadcast(
//                this,
//                (int) reminderId,
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
//        );
//
//        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, dueMillis, pi);
//    }
}
