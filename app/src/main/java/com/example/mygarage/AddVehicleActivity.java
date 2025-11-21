package com.example.mygarage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddVehicleActivity extends AppCompatActivity {

    private AppDatabase db;

    private EditText vehicleNameEt;
    private EditText modelEt;
    private EditText makeEt;
    private EditText purchaseDateEt;
    private EditText mileageEt;
    private Button saveBtn;
    private Button backBtn;
    private Button cancelBtn;
    private Button modifyBtn;

    private long editingVehicleId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        db = AppDatabase.getInstance(this);

        vehicleNameEt = findViewById(R.id.vehicleName);
        modelEt = findViewById(R.id.model);
        makeEt = findViewById(R.id.make);
        purchaseDateEt = findViewById(R.id.purchaseDate);
        mileageEt = findViewById(R.id.mileage);
        saveBtn = findViewById(R.id.saveBtn);
        backBtn = findViewById(R.id.backBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        modifyBtn = findViewById(R.id.modifyBtn);

        backBtn.setOnClickListener(v -> finish());

        cancelBtn.setOnClickListener(v -> {
            vehicleNameEt.setText("");
            modelEt.setText("");
            makeEt.setText("");
            purchaseDateEt.setText("");
            mileageEt.setText("");
            Toast.makeText(AddVehicleActivity.this, "Cleared", Toast.LENGTH_SHORT).show();
        });

        modifyBtn.setOnClickListener(v -> {
            startActivity(new android.content.Intent(AddVehicleActivity.this, VehicleDetailsActivity.class));
        });

        editingVehicleId = getIntent().getLongExtra("vehicle_id", -1);
        if (editingVehicleId != -1) {
            VehicleEntity existing = db.vehicleDao().getById(editingVehicleId);
            if (existing != null) {
                vehicleNameEt.setText(existing.vehicleName);
                modelEt.setText(existing.model);
                makeEt.setText(existing.make);
                purchaseDateEt.setText(existing.purchaseDate);
                mileageEt.setText(String.valueOf(existing.mileage));
                saveBtn.setText("Save Changes");
            }
        }

        saveBtn.setOnClickListener(v -> {
            String name = vehicleNameEt.getText().toString().trim();
            String model = modelEt.getText().toString().trim();
            String make = makeEt.getText().toString().trim();
            String purchaseDate = purchaseDateEt.getText().toString().trim();
            String mileageStr = mileageEt.getText().toString().trim();

            if (name.isEmpty() || mileageStr.isEmpty()) {
                Toast.makeText(AddVehicleActivity.this,
                        "Vehicle name and mileage are required", Toast.LENGTH_SHORT).show();
                return;
            }

            UserEntity user = MainActivity.currentUser;
            if (user == null) {
                Toast.makeText(AddVehicleActivity.this,
                        "Please log in again", Toast.LENGTH_SHORT).show();
                return;
            }

            int mileage;
            try {
                mileage = Integer.parseInt(mileageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(AddVehicleActivity.this,
                        "Mileage must be a number", Toast.LENGTH_SHORT).show();
                return;
            }

            VehicleEntity vEntity = new VehicleEntity();
            vEntity.userId = user.id;
            vEntity.vehicleName = name;
            vEntity.model = model;
            vEntity.make = make;
            vEntity.purchaseDate = purchaseDate;
            vEntity.mileage = mileage;

            if (editingVehicleId == -1) {
                db.vehicleDao().insert(vEntity);
                Toast.makeText(AddVehicleActivity.this,
                        "Vehicle saved", Toast.LENGTH_SHORT).show();
            } else {
                vEntity.id = editingVehicleId;
                db.vehicleDao().update(vEntity);
                Toast.makeText(AddVehicleActivity.this,
                        "Vehicle updated", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }
}
