package com.example.mygarage;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private AppDatabase db;

    private Spinner vehicleSpinner;
    private List<Long> vehicleIdList = new ArrayList<>();

    private EditText fuelAmountEt;
    private EditText fuelDateEt;
    private Button fuelSaveBtn;
    private Button fuelCancelBtn;

    private EditText washAmountEt;
    private EditText washDateEt;
    private Button washSaveBtn;
    private Button washCancelBtn;

    private EditText serviceAmountEt;
    private EditText serviceDateEt;
    private Button serviceSaveBtn;
    private Button serviceCancelBtn;

    private EditText otherAmountEt;
    private EditText otherDateEt;
    private EditText otherNoteEt;
    private Button otherSaveBtn;
    private Button otherCancelBtn;

    private Button backBtn;

    private final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        db = AppDatabase.getInstance(this);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

        vehicleSpinner = findViewById(R.id.spinnerVehicle);

        fuelAmountEt = findViewById(R.id.fuelAmount);
        fuelDateEt = findViewById(R.id.fuelDate);
        fuelSaveBtn = findViewById(R.id.btnFuelSave);
        fuelCancelBtn = findViewById(R.id.btnFuelCancel);

        washAmountEt = findViewById(R.id.washAmount);
        washDateEt = findViewById(R.id.washDate);
        washSaveBtn = findViewById(R.id.btnWashSave);
        washCancelBtn = findViewById(R.id.btnWashCancel);

        serviceAmountEt = findViewById(R.id.serviceAmount);
        serviceDateEt = findViewById(R.id.serviceDate);
        serviceSaveBtn = findViewById(R.id.btnServiceSave);
        serviceCancelBtn = findViewById(R.id.btnServiceCancel);

        otherAmountEt = findViewById(R.id.otherAmount);
        otherDateEt = findViewById(R.id.otherDate);
        otherNoteEt = findViewById(R.id.otherNote);
        otherSaveBtn = findViewById(R.id.btnOtherSave);
        otherCancelBtn = findViewById(R.id.btnOtherCancel);

        loadVehicles();

        fuelSaveBtn.setOnClickListener(v -> saveExpense("Fuel", fuelAmountEt, fuelDateEt, null));
        washSaveBtn.setOnClickListener(v -> saveExpense("Wash", washAmountEt, washDateEt, null));
        serviceSaveBtn.setOnClickListener(v -> saveExpense("Service", serviceAmountEt, serviceDateEt, null));
        otherSaveBtn.setOnClickListener(v -> saveExpense("Other", otherAmountEt, otherDateEt, otherNoteEt));

        fuelCancelBtn.setOnClickListener(v -> clearSection(fuelAmountEt, fuelDateEt, null));
        washCancelBtn.setOnClickListener(v -> clearSection(washAmountEt, washDateEt, null));
        serviceCancelBtn.setOnClickListener(v -> clearSection(serviceAmountEt, serviceDateEt, null));
        otherCancelBtn.setOnClickListener(v -> clearSection(otherAmountEt, otherDateEt, otherNoteEt));
    }

    private void loadVehicles() {
        UserEntity user = MainActivity.currentUser;
        List<VehicleEntity> vehicles = new ArrayList<>();
        if (user != null) {
            vehicles = db.vehicleDao().getAllForUser(user.id);
        }

        List<String> names = new ArrayList<>();
        vehicleIdList.clear();

        for (VehicleEntity v : vehicles) {
            names.add(v.vehicleName);
            vehicleIdList.add(v.id);
        }

        if (names.isEmpty()) {
            names.add("Add a vehicle first");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleSpinner.setAdapter(adapter);

        vehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void saveExpense(String category, EditText amountEt, EditText dateEt, EditText noteEt) {
        if (vehicleIdList.isEmpty()) {
            Toast.makeText(this, "Add a vehicle first", Toast.LENGTH_SHORT).show();
            return;
        }

        int pos = vehicleSpinner.getSelectedItemPosition();
        if (pos < 0 || pos >= vehicleIdList.size()) {
            Toast.makeText(this, "Select a vehicle", Toast.LENGTH_SHORT).show();
            return;
        }

        String amountStr = amountEt.getText().toString().trim();
        String dateStr = dateEt.getText().toString().trim();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Enter amount for " + category.toLowerCase(Locale.US), Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        long dateMillis = parseDate(dateStr);

        ExpenseEntity e = new ExpenseEntity();
        e.vehicleId = vehicleIdList.get(pos);
        e.category = category;
        e.amount = amount;
        e.dateMillis = dateMillis;
        e.note = noteEt != null ? noteEt.getText().toString().trim() : "";

        db.expenseDao().insert(e);

        Toast.makeText(this, category + " expense saved", Toast.LENGTH_SHORT).show();
        clearSection(amountEt, dateEt, noteEt);
    }

    private void clearSection(EditText amountEt, EditText dateEt, EditText noteEt) {
        amountEt.setText("");
        dateEt.setText("");
        if (noteEt != null) {
            noteEt.setText("");
        }
        Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show();
    }

    private long parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return System.currentTimeMillis();
        }
        try {
            Date d = sdf.parse(dateStr);
            if (d != null) {
                return d.getTime();
            }
        } catch (ParseException ignored) {
        }
        return System.currentTimeMillis();
    }
}

