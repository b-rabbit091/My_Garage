package com.example.mygarage;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText fuel, wash, service, other, otherNote;
    private TextView fuelDateText, washDateText, serviceDateText;
    private Button saveBtn, backBtn;
    private EditText vehicleNumber;


    private Calendar fuelDate, washDate, serviceDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_add_expense);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading layout", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        try {
            // Initialize EditTexts
            fuel = findViewById(R.id.fuel);
            wash = findViewById(R.id.wash);
            service = findViewById(R.id.service);
            other = findViewById(R.id.other);
            otherNote = findViewById(R.id.otherNote);

            // Initialize TextViews for dates
            fuelDateText = findViewById(R.id.fuelDateText);
            washDateText = findViewById(R.id.washDateText);
            serviceDateText = findViewById(R.id.serviceDateText);

            // Initialize buttons
            saveBtn = findViewById(R.id.saveBtn);
            backBtn = findViewById(R.id.backBtn);

            // Initialize Calendar objects
            fuelDate = Calendar.getInstance();
            washDate = Calendar.getInstance();
            serviceDate = Calendar.getInstance();

            // Set date pickers
            fuelDateText.setOnClickListener(v -> showDatePicker(fuelDate, fuelDateText));
            washDateText.setOnClickListener(v -> showDatePicker(washDate, washDateText));
            serviceDateText.setOnClickListener(v -> showDatePicker(serviceDate, serviceDateText));

            // Save button
            saveBtn.setOnClickListener(v -> saveExpenses());

            // Back button
            backBtn.setOnClickListener(v -> onBackPressed());

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initializing views", Toast.LENGTH_LONG).show();
        }
    }

    private void showDatePicker(Calendar calendar, TextView textView) {
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    textView.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void saveExpenses() {
        String fuelAmount = fuel.getText() != null ? fuel.getText().toString() : "";
        String washAmount = wash.getText() != null ? wash.getText().toString() : "";
        String serviceAmount = service.getText() != null ? service.getText().toString() : "";
        String otherAmount = other.getText() != null ? other.getText().toString() : "";
        String otherNoteText = otherNote.getText() != null ? otherNote.getText().toString() : "";
        String vehicle = vehicleNumber.getText() != null ? vehicleNumber.getText().toString() : "";

        String msg = "Vehicle: " + vehicle +
                "\nFuel: $" + fuelAmount + " on " + fuelDateText.getText() +
                "\nWash: $" + washAmount + " on " + washDateText.getText() +
                "\nService: $" + serviceAmount + " on " + serviceDateText.getText() +
                "\nOther: $" + otherAmount +
                "\nNote: " + otherNoteText;

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
