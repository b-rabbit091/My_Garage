package com.example.mygarage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    Button loginBtn, signupRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        signupRedirect = findViewById(R.id.signupRedirect);

        loginBtn.setOnClickListener(v -> {
            // Mock login (no database)
            if (email.getText().toString().equals("user@test.com") &&
                    password.getText().toString().equals("1234")) {
                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            } else {
                Toast.makeText(this, "Invalid credentials (try user@test.com / 1234)", Toast.LENGTH_SHORT).show();
            }
        });

        signupRedirect.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignupActivity.class));
        });
    }
}
