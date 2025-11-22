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

    private AppDatabase db;

    public static UserEntity currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MyGarage);  // optional if you use custom theme
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        signupRedirect = findViewById(R.id.signupRedirect);

        loginBtn.setOnClickListener(v -> {
            String enteredEmail = email.getText().toString().trim();
            String enteredPassword = password.getText().toString();

            if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            UserEntity user = db.userDao().getByEmail(enteredEmail);
            if (user != null && user.password.equals(enteredPassword)) {
                currentUser = user;
                Toast.makeText(this, "Welcome, " + user.firstName, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            } else {
                Toast.makeText(this,
                        "Invalid credentials.\nPlease sign up if you don't have an account.",
                        Toast.LENGTH_LONG).show();
            }
        });

        signupRedirect.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SignupActivity.class)));
    }
}
