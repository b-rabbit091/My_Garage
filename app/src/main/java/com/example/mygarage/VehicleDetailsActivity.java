package com.example.mygarage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class VehicleDetailsActivity extends AppCompatActivity {

    private AppDatabase db;
    private Button backBtn;
    private LinearLayout vehicleListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        db = AppDatabase.getInstance(this);

        backBtn = findViewById(R.id.backBtn);
        vehicleListContainer = findViewById(R.id.vehicleListContainer);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(VehicleDetailsActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        loadVehicles();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VehicleDetailsActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void loadVehicles() {
        vehicleListContainer.removeAllViews();

        UserEntity user = MainActivity.currentUser;
        List<VehicleEntity> vehicles;
        if (user != null) {
            vehicles = db.vehicleDao().getAllForUser(user.id);
        } else {
            vehicles = java.util.Collections.emptyList();
        }

        if (vehicles.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No vehicles found. Please add one.");
            empty.setTextSize(16f);
            empty.setTextColor(0xFF6B7280);
            vehicleListContainer.addView(empty);
            return;
        }

        for (VehicleEntity v : vehicles) {
            addVehicleCard(v);
        }
    }

    private void addVehicleCard(VehicleEntity vehicle) {
        int pad = dpToPx(16);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.card_bg);
        card.setPadding(pad, pad, pad, pad);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, dpToPx(16), 0, 0);
        card.setLayoutParams(cardParams);

        TextView sectionVehicle = new TextView(this);
        sectionVehicle.setText("Vehicle Overview");
        sectionVehicle.setTextColor(0xFF4F46E5);
        sectionVehicle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        sectionVehicle.setTypeface(null, android.graphics.Typeface.BOLD);
        card.addView(sectionVehicle);

        TextView vehicleInfo = new TextView(this);
        vehicleInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        vehicleInfo.setTextColor(0xFF111827);
        vehicleInfo.setLineSpacing(0f, 1.1f);

        String ownerName = (MainActivity.currentUser != null)
                ? MainActivity.currentUser.firstName
                : "Unknown";

        String vehicleText =
                "Owner: " + ownerName +
                        "\nVehicle: " + vehicle.make + " " + vehicle.model + " (" + vehicle.vehicleName + ")" +
                        "\nPurchase Date: " + vehicle.purchaseDate +
                        "\nMileage: " + vehicle.mileage + " mi";

        vehicleInfo.setText(vehicleText);
        vehicleInfo.setPadding(0, dpToPx(8), 0, dpToPx(8));
        card.addView(vehicleInfo);

        View divider1 = new View(this);
        LinearLayout.LayoutParams divParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1));
        divParams.setMargins(0, dpToPx(8), 0, dpToPx(8));
        divider1.setLayoutParams(divParams);
        divider1.setBackgroundColor(0xFFE5E7EB);
        card.addView(divider1);

        TextView sectionExpenses = new TextView(this);
        sectionExpenses.setText("Expenses");
        sectionExpenses.setTextColor(0xFF4F46E5);
        sectionExpenses.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        sectionExpenses.setTypeface(null, android.graphics.Typeface.BOLD);
        card.addView(sectionExpenses);

        TextView expenseInfo = new TextView(this);
        expenseInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        expenseInfo.setTextColor(0xFF374151);
        expenseInfo.setLineSpacing(0f, 1.1f);
        expenseInfo.setPadding(0, dpToPx(8), 0, dpToPx(8));

        List<ExpenseEntity> expenses = db.expenseDao().getByVehicle(vehicle.id);
        Double total = db.expenseDao().getTotalForVehicle(vehicle.id);
        if (total == null) total = 0.0;

        DateFormat df = android.text.format.DateFormat.getDateFormat(this);

        StringBuilder expSb = new StringBuilder();
        if (expenses.isEmpty()) {
            expSb.append("No expenses recorded.");
        } else {
            for (ExpenseEntity e : expenses) {
                expSb.append("• ")
                        .append(e.category)
                        .append(" - $")
                        .append(String.format("%.2f", e.amount))
                        .append(" (")
                        .append(df.format(new Date(e.dateMillis)))
                        .append(")");
                if (!TextUtils.isEmpty(e.note)) {
                    expSb.append(" - ").append(e.note);
                }
                expSb.append("\n");
            }
            expSb.append("\nTotal Expenses: $")
                    .append(String.format("%.2f", total));
        }
        expenseInfo.setText(expSb.toString());
        card.addView(expenseInfo);

        LinearLayout btnRow = new LinearLayout(this);
        btnRow.setOrientation(LinearLayout.HORIZONTAL);
        btnRow.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        btnRow.setPadding(0, dpToPx(8), 0, 0);

        Button editBtn = new Button(this);
        editBtn.setText("Edit");
        editBtn.setBackgroundResource(R.drawable.outline_button_small);
        editBtn.setTextColor(0xFF111827);
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                0, dpToPx(44), 1);
        editBtn.setLayoutParams(editParams);

        Button deleteBtn = new Button(this);
        deleteBtn.setText("Delete");
        deleteBtn.setBackgroundResource(R.drawable.outline_button_small);
        deleteBtn.setTextColor(0xFFEF4444);
        LinearLayout.LayoutParams delParams = new LinearLayout.LayoutParams(
                0, dpToPx(44), 1);
        delParams.setMargins(dpToPx(8), 0, 0, 0);
        deleteBtn.setLayoutParams(delParams);

        btnRow.addView(editBtn);
        btnRow.addView(deleteBtn);
        card.addView(btnRow);

        editBtn.setOnClickListener(v -> showEditDialog(vehicle));

        deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(VehicleDetailsActivity.this)
                    .setTitle("Delete Vehicle")
                    .setMessage("Are you sure you want to delete this vehicle?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        db.vehicleDao().delete(vehicle);
                        Toast.makeText(VehicleDetailsActivity.this,
                                "Vehicle deleted", Toast.LENGTH_SHORT).show();
                        loadVehicles();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        vehicleListContainer.addView(card);
    }

    private void showEditDialog(VehicleEntity vehicle) {
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_vehicle, null);

        EditText nameEt = view.findViewById(R.id.editVehicleName);
        EditText modelEt = view.findViewById(R.id.editModel);
        EditText makeEt = view.findViewById(R.id.editMake);
        EditText dateEt = view.findViewById(R.id.editPurchaseDate);
        EditText mileEt = view.findViewById(R.id.editMileage);

        nameEt.setText(vehicle.vehicleName);
        modelEt.setText(vehicle.model);
        makeEt.setText(vehicle.make);
        dateEt.setText(vehicle.purchaseDate);
        mileEt.setText(String.valueOf(vehicle.mileage));

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = nameEt.getText().toString().trim();
                    String model = modelEt.getText().toString().trim();
                    String make = makeEt.getText().toString().trim();
                    String date = dateEt.getText().toString().trim();
                    String mileStr = mileEt.getText().toString().trim();

                    if (name.isEmpty() || mileStr.isEmpty()) {
                        Toast.makeText(this,
                                "Vehicle name and mileage are required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int mileage;
                    try {
                        mileage = Integer.parseInt(mileStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this,
                                "Mileage must be a number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    vehicle.vehicleName = name;
                    vehicle.model = model;
                    vehicle.make = make;
                    vehicle.purchaseDate = date;
                    vehicle.mileage = mileage;

                    db.vehicleDao().update(vehicle);
                    Toast.makeText(this, "Vehicle updated", Toast.LENGTH_SHORT).show();
                    loadVehicles();
                })
                .setNegativeButton("Cancel", (DialogInterface dialog, int which) -> {
                })
                .show();
    }

    private int dpToPx(int dp) {
        return Math.round(
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dp,
                        getResources().getDisplayMetrics()
                )
        );
    }
}
