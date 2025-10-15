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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        age = findViewById(R.id.age);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        backBtn = findViewById(R.id.backBtn);

        createAccountBtn.setOnClickListener(v -> {
            if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Account Created! Please log in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        });

        backBtn.setOnClickListener(v -> onBackPressed());
    }
}
