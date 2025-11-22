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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RemindersActivity extends AppCompatActivity {

    private static final int REQ_POST_NOTIFICATIONS = 101;

    private Button backBtn;

    private Spinner vehicleSpinner;

    private DatePicker fuelDatePicker;
    private TimePicker fuelTimePicker;
    private EditText fuelNote;
    private Button saveFuelBtn;

    private DatePicker washingDatePicker;
    private TimePicker washingTimePicker;
    private EditText washingNote;
    private Button saveWashingBtn;

    private DatePicker serviceDatePicker;
    private TimePicker serviceTimePicker;
    private EditText serviceNote;
    private Button saveServiceBtn;

    private AppDatabase db;
    private final List<Long> vehicleIds = new ArrayList<>();
    private long selectedVehicleId = -1L;

    // editing support
    private long editingReminderId = -1L;
    private String editingType = null; // "fuel", "washing", "service"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        db = AppDatabase.getInstance(this);

        backBtn = findViewById(R.id.backBtn);
        vehicleSpinner = findViewById(R.id.spinnerVehicle);

        // Fuel
        fuelDatePicker = findViewById(R.id.fuelDatePicker);
        fuelTimePicker = findViewById(R.id.fuelTimePicker);
        fuelNote = findViewById(R.id.fuelNote);
        saveFuelBtn = findViewById(R.id.saveFuelReminder);

        // Washing
        washingDatePicker = findViewById(R.id.washingDatePicker);
        washingTimePicker = findViewById(R.id.washingTimePicker);
        washingNote = findViewById(R.id.washingNote);
        saveWashingBtn = findViewById(R.id.saveWashingReminder);

        // Service
        serviceDatePicker = findViewById(R.id.serviceDatePicker);
        serviceTimePicker = findViewById(R.id.serviceTimePicker);
        serviceNote = findViewById(R.id.serviceNote);
        saveServiceBtn = findViewById(R.id.saveServiceReminder);

        if (fuelTimePicker != null) {
            fuelTimePicker.setIs24HourView(false);
        }
        if (washingTimePicker != null) {
            washingTimePicker.setIs24HourView(false);
        }
        if (serviceTimePicker != null) {
            serviceTimePicker.setIs24HourView(false);
        }

        ensureNotificationPermission();
        loadVehicles();
        handleEditIntent(); // must be after loadVehicles so spinner can be positioned

        backBtn.setOnClickListener(v -> onBackPressed());

        saveFuelBtn.setOnClickListener(v -> {
            if (selectedVehicleId == -1L) {
                Toast.makeText(this, "Add a vehicle first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (fuelTimePicker == null) {
                Toast.makeText(this, "Time picker not available", Toast.LENGTH_SHORT).show();
                return;
            }

            long due = getMillisFromPickers(fuelDatePicker, fuelTimePicker);
            String note = fuelNote.getText().toString();

            // update existing fuel reminder if we came from Edit
            if (editingReminderId != -1L && "fuel".equals(editingType)) {
                ReminderEntity existing = db.reminderDao().getById(editingReminderId);
                if (existing != null) {
                    existing.vehicleId = selectedVehicleId;
                    existing.title = "Fuel: " + note;
                    existing.dueMillis = due;
                    existing.enabled = true;
                    db.reminderDao().update(existing);
                    scheduleAlarm(existing.id, existing.title, existing.dueMillis);
                    Toast.makeText(this, "Fuel reminder updated", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }

            // otherwise create new
            ReminderEntity r = new ReminderEntity();
            r.vehicleId = selectedVehicleId;
            r.title = "Fuel: " + note;
            r.dueMillis = due;
            r.enabled = true;

            long id = db.reminderDao().insert(r);
            scheduleAlarm(id, r.title, r.dueMillis);
            Toast.makeText(this, "Fuel reminder scheduled", Toast.LENGTH_LONG).show();
        });

        saveWashingBtn.setOnClickListener(v -> {
            if (selectedVehicleId == -1L) {
                Toast.makeText(this, "Add a vehicle first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (washingTimePicker == null) {
                Toast.makeText(this, "Time picker not available", Toast.LENGTH_SHORT).show();
                return;
            }

            long due = getMillisFromPickers(washingDatePicker, washingTimePicker);
            String note = washingNote.getText().toString();

            if (editingReminderId != -1L && "washing".equals(editingType)) {
                ReminderEntity existing = db.reminderDao().getById(editingReminderId);
                if (existing != null) {
                    existing.vehicleId = selectedVehicleId;
                    existing.title = "Washing: " + note;
                    existing.dueMillis = due;
                    existing.enabled = true;
                    db.reminderDao().update(existing);
                    scheduleAlarm(existing.id, existing.title, existing.dueMillis);
                    Toast.makeText(this, "Washing reminder updated", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }

            ReminderEntity r = new ReminderEntity();
            r.vehicleId = selectedVehicleId;
            r.title = "Washing: " + note;
            r.dueMillis = due;
            r.enabled = true;

            long id = db.reminderDao().insert(r);
            scheduleAlarm(id, r.title, r.dueMillis);
            Toast.makeText(this, "Washing reminder scheduled", Toast.LENGTH_LONG).show();
        });

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

            if (editingReminderId != -1L && "service".equals(editingType)) {
                ReminderEntity existing = db.reminderDao().getById(editingReminderId);
                if (existing != null) {
                    existing.vehicleId = selectedVehicleId;
                    existing.title = "Service: " + note;
                    existing.dueMillis = due;
                    existing.enabled = true;
                    db.reminderDao().update(existing);
                    scheduleAlarm(existing.id, existing.title, existing.dueMillis);
                    Toast.makeText(this, "Service reminder updated", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }

            ReminderEntity r = new ReminderEntity();
            r.vehicleId = selectedVehicleId;
            r.title = "Service: " + note;
            r.dueMillis = due;
            r.enabled = true;

            long id = db.reminderDao().insert(r);
            scheduleAlarm(id, r.title, r.dueMillis);
            Toast.makeText(this, "Service reminder scheduled", Toast.LENGTH_LONG).show();
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

    private void handleEditIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        editingReminderId = intent.getLongExtra("reminderId", -1L);
        editingType = intent.getStringExtra("reminderType"); // may be null

        if (editingReminderId == -1L) {
            return;
        }

        ReminderEntity existing = db.reminderDao().getById(editingReminderId);
        if (existing == null) {
            return;
        }

        // select correct vehicle
        long vehicleId = existing.vehicleId;
        for (int i = 0; i < vehicleIds.size(); i++) {
            if (vehicleIds.get(i) == vehicleId) {
                vehicleSpinner.setSelection(i);
                selectedVehicleId = vehicleId;
                break;
            }
        }

        // derive type from title if not provided
        String title = existing.title != null ? existing.title : "";
        String lower = title.toLowerCase();
        if (editingType == null) {
            if (lower.startsWith("fuel:")) {
                editingType = "fuel";
            } else if (lower.startsWith("washing:")) {
                editingType = "washing";
            } else if (lower.startsWith("service:")) {
                editingType = "service";
            }
        }

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(existing.dueMillis);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        String noteText = title;
        if (lower.startsWith("fuel:")) {
            noteText = title.substring("Fuel: ".length());
        } else if (lower.startsWith("washing:")) {
            noteText = title.substring("Washing: ".length());
        } else if (lower.startsWith("service:")) {
            noteText = title.substring("Service: ".length());
        }

        if ("fuel".equals(editingType)) {
            fuelDatePicker.updateDate(year, month, day);
            setTime(fuelTimePicker, hour, minute);
            fuelNote.setText(noteText.trim());
            saveFuelBtn.setText("Update Fuel Reminder");
            fuelNote.requestFocus();
        } else if ("washing".equals(editingType)) {
            washingDatePicker.updateDate(year, month, day);
            setTime(washingTimePicker, hour, minute);
            washingNote.setText(noteText.trim());
            saveWashingBtn.setText("Update Washing Reminder");
            washingNote.requestFocus();
        } else if ("service".equals(editingType)) {
            serviceDatePicker.updateDate(year, month, day);
            setTime(serviceTimePicker, hour, minute);
            serviceNote.setText(noteText.trim());
            saveServiceBtn.setText("Update Service Reminder");
            serviceNote.requestFocus();
        }
    }

    private void setTime(TimePicker tp, int hour, int minute) {
        if (tp == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tp.setHour(hour);
            tp.setMinute(minute);
        } else {
            tp.setCurrentHour(hour);
            tp.setCurrentMinute(minute);
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
            dueMillis = now + 5000;
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

        am.set(AlarmManager.RTC_WAKEUP, dueMillis, pi);
    }
}
