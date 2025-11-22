package com.example.mygarage;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ReportsActivity extends AppCompatActivity {

    private AppDatabase db;

    private Spinner spinnerVehicle;
    private Spinner spinnerSummaryType;
    private Button backBtn;

    private LinearLayout cardWeekly;
    private LinearLayout cardMonthly;
    private LinearLayout cardYearly;

    private TextView weeklyTotal;
    private TextView weeklyFuel;
    private TextView weeklyWash;
    private TextView weeklyService;
    private TextView weeklyOther;
    private TextView weeklyRange;

    private TextView monthlyTotal;
    private TextView monthlyFuel;
    private TextView monthlyWash;
    private TextView monthlyService;
    private TextView monthlyOther;
    private TextView monthlyRange;

    private TextView yearlyTotal;
    private TextView yearlyFuel;
    private TextView yearlyWash;
    private TextView yearlyService;
    private TextView yearlyOther;
    private TextView yearlyRange;

    private final List<Long> vehicleIds = new ArrayList<>();

    private static final long WEEK_MS = 7L * 24L * 60L * 60L * 1000L;
    private static final long MONTH_MS = 30L * 24L * 60L * 60L * 1000L;
    private static final long YEAR_MS = 365L * 24L * 60L * 60L * 1000L;

    private DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        db = AppDatabase.getInstance(this);
        dateFormat = android.text.format.DateFormat.getDateFormat(this);

        spinnerVehicle = findViewById(R.id.spinnerVehicle);
        spinnerSummaryType = findViewById(R.id.spinnerSummaryType);
        backBtn = findViewById(R.id.backBtn);

        cardWeekly = findViewById(R.id.cardWeekly);
        cardMonthly = findViewById(R.id.cardMonthly);
        cardYearly = findViewById(R.id.cardYearly);

        weeklyTotal = findViewById(R.id.weeklyTotal);
        weeklyFuel = findViewById(R.id.weeklyFuel);
        weeklyWash = findViewById(R.id.weeklyWash);
        weeklyService = findViewById(R.id.weeklyService);
        weeklyOther = findViewById(R.id.weeklyOther);
        weeklyRange = findViewById(R.id.weeklyRange);

        monthlyTotal = findViewById(R.id.monthlyTotal);
        monthlyFuel = findViewById(R.id.monthlyFuel);
        monthlyWash = findViewById(R.id.monthlyWash);
        monthlyService = findViewById(R.id.monthlyService);
        monthlyOther = findViewById(R.id.monthlyOther);
        monthlyRange = findViewById(R.id.monthlyRange);

        yearlyTotal = findViewById(R.id.yearlyTotal);
        yearlyFuel = findViewById(R.id.yearlyFuel);
        yearlyWash = findViewById(R.id.yearlyWash);
        yearlyService = findViewById(R.id.yearlyService);
        yearlyOther = findViewById(R.id.yearlyOther);
        yearlyRange = findViewById(R.id.yearlyRange);

        backBtn.setOnClickListener(v -> finish());

        setupSummaryTypeSpinner();
        loadVehicles();
    }

    private void setupSummaryTypeSpinner() {
        List<String> types = Arrays.asList("Weekly", "Monthly", "Yearly");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSummaryType.setAdapter(adapter);

        spinnerSummaryType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position == 0) {
                    showWeekly();
                } else if (position == 1) {
                    showMonthly();
                } else {
                    showYearly();
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                showWeekly();
            }
        });

        showWeekly();
    }

    private void showWeekly() {
        cardWeekly.setVisibility(View.VISIBLE);
        cardMonthly.setVisibility(View.GONE);
        cardYearly.setVisibility(View.GONE);
    }

    private void showMonthly() {
        cardWeekly.setVisibility(View.GONE);
        cardMonthly.setVisibility(View.VISIBLE);
        cardYearly.setVisibility(View.GONE);
    }

    private void showYearly() {
        cardWeekly.setVisibility(View.GONE);
        cardMonthly.setVisibility(View.GONE);
        cardYearly.setVisibility(View.VISIBLE);
    }

    private void loadVehicles() {
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicle.setAdapter(adapter);

        spinnerVehicle.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position >= 0 && position < vehicleIds.size()) {
                    long vehicleId = vehicleIds.get(position);
                    loadReportsForVehicle(vehicleId);
                } else {
                    clearAll();
                    Toast.makeText(ReportsActivity.this, "Add a vehicle first", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                clearAll();
            }
        });

        if (!vehicleIds.isEmpty()) {
            loadReportsForVehicle(vehicleIds.get(0));
        } else {
            clearAll();
        }
    }

    private void loadReportsForVehicle(long vehicleId) {
        List<ExpenseEntity> expenses = db.expenseDao().getByVehicle(vehicleId);
        long now = System.currentTimeMillis();

        long weekStart = now - WEEK_MS;
        long monthStart = now - MONTH_MS;
        long yearStart = now - YEAR_MS;

        Summary weekly = computeSummary(expenses, weekStart);
        Summary monthly = computeSummary(expenses, monthStart);
        Summary yearly = computeSummary(expenses, yearStart);

        setSummaryToViews(weekly, weeklyTotal, weeklyFuel, weeklyWash, weeklyService, weeklyOther);
        setSummaryToViews(monthly, monthlyTotal, monthlyFuel, monthlyWash, monthlyService, monthlyOther);
        setSummaryToViews(yearly, yearlyTotal, yearlyFuel, yearlyWash, yearlyService, yearlyOther);

        setRange(weeklyRange, weekStart, now);
        setRange(monthlyRange, monthStart, now);
        setRange(yearlyRange, yearStart, now);
    }

    private Summary computeSummary(List<ExpenseEntity> all, long fromTime) {
        Summary s = new Summary();
        for (ExpenseEntity e : all) {
            if (e.dateMillis < fromTime) {
                continue;
            }
            s.total += e.amount;
            String c = e.category == null ? "" : e.category.toLowerCase();
            if (c.contains("fuel")) {
                s.fuel += e.amount;
            } else if (c.contains("wash")) {
                s.wash += e.amount;
            } else if (c.contains("service")) {
                s.service += e.amount;
            } else {
                s.other += e.amount;
            }
        }
        return s;
    }

    private void setSummaryToViews(Summary s,
                                   TextView tvTotal,
                                   TextView tvFuel,
                                   TextView tvWash,
                                   TextView tvService,
                                   TextView tvOther) {
        tvTotal.setText(String.format("$%.2f", s.total));
        tvFuel.setText(String.format("$%.2f", s.fuel));
        tvWash.setText(String.format("$%.2f", s.wash));
        tvService.setText(String.format("$%.2f", s.service));
        tvOther.setText(String.format("$%.2f", s.other));
    }

    private void setRange(TextView tv, long from, long to) {
        String text = dateFormat.format(new Date(from)) + " - " + dateFormat.format(new Date(to));
        tv.setText(text);
    }

    private void clearAll() {
        Summary zero = new Summary();
        setSummaryToViews(zero, weeklyTotal, weeklyFuel, weeklyWash, weeklyService, weeklyOther);
        setSummaryToViews(zero, monthlyTotal, monthlyFuel, monthlyWash, monthlyService, monthlyOther);
        setSummaryToViews(zero, yearlyTotal, yearlyFuel, yearlyWash, yearlyService, yearlyOther);
        weeklyRange.setText("-");
        monthlyRange.setText("-");
        yearlyRange.setText("-");
    }

    private static class Summary {
        double total;
        double fuel;
        double wash;
        double service;
        double other;
    }
}
