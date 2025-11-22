package com.example.mygarage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText firstName, lastName, email, age, password, confirmPassword;
    Button createAccountBtn, backBtn;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = AppDatabase.getInstance(this);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        age = findViewById(R.id.age);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        backBtn = findViewById(R.id.backBtn);

        createAccountBtn.setOnClickListener(v -> {
            String f = firstName.getText().toString().trim();
            String l = lastName.getText().toString().trim();
            String em = email.getText().toString().trim();
            String ageStr = age.getText().toString().trim();
            String pw = password.getText().toString();
            String cpw = confirmPassword.getText().toString();

            if (f.isEmpty() || em.isEmpty() || pw.isEmpty()) {
                Toast.makeText(this, "Name, email and password are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pw.equals(cpw)) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            int ageVal = 0;
            try {
                if (!ageStr.isEmpty()) {
                    ageVal = Integer.parseInt(ageStr);
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Age must be a number", Toast.LENGTH_SHORT).show();
                return;
            }

            // check if email already exists
            if (db.userDao().getByEmail(em) != null) {
                Toast.makeText(this, "Email already registered. Please login.", Toast.LENGTH_SHORT).show();
                return;
            }

            UserEntity user = new UserEntity();
            user.firstName = f;
            user.lastName = l;
            user.email = em;
            user.age = ageVal;
            user.password = pw;

            db.userDao().insert(user);

            Toast.makeText(this, "Account Created! Please log in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        });

        backBtn.setOnClickListener(v -> onBackPressed());
    }
}
