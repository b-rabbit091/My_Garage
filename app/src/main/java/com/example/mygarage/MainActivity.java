package com.example.mygarage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    Button loginBtn, signupRedirect;

    // ---- Mock user and vehicle data ----
    private static class UserInfo {
        String password;
        String vehicleDetails;
        UserInfo(String password, String vehicleDetails) {
            this.password = password;
            this.vehicleDetails = vehicleDetails;
        }
    }

    // hardcoded users
    private static final Map<String, UserInfo> mockUsers = new HashMap<>();
    static {
        mockUsers.put("user@test.com", new UserInfo("1234",
                "Owner: Alex Doe\n" +
                        "Vehicle: Honda Civic (2018)\n" +
                        "VIN: 2HGFC2F59JH000111\n" +
                        "Purchase Date: 2019-05-12\n" +
                        "Mileage: 62,340 mi\n" +
                        "Fuel: $96.20 (10/25/2025)\n" +
                        "Wash: $14.00 (10/20/2025)\n" +
                        "Service: $180.00 (09/30/2025)\n" +
                        "Other: $22.50 (Wiper blades)\n" +
                        "Total (last month): $312.70"));

        mockUsers.put("priya@demo.com", new UserInfo("pass1",
                "Owner: Priya Sharma\n" +
                        "Vehicle: Toyota RAV4 (2020)\n" +
                        "VIN: JTMR1RFV2LJ012345\n" +
                        "Purchase Date: 2021-02-08\n" +
                        "Mileage: 41,590 mi\n" +
                        "Fuel: $128.40 (10/28/2025)\n" +
                        "Wash: $18.00 (10/26/2025)\n" +
                        "Service: $420.00 (10/10/2025)\n" +
                        "Total (last month): $566.40"));

        mockUsers.put("john@demo.com", new UserInfo("pass2",
                "Owner: John Kim\n" +
                        "Vehicle: Ford F-150 (2019)\n" +
                        "VIN: 1FTEW1E40KK123456\n" +
                        "Purchase Date: 2019-10-05\n" +
                        "Mileage: 88,210 mi\n" +
                        "Fuel: $215.00 (10/15 and 10/29/2025)\n" +
                        "Wash: $25.00 (10/21/2025)\n" +
                        "Service: $650.00 (Brakes + Rotors)\n" +
                        "Other: $49.99 (Bed cleaner)\n" +
                        "Total (last month): $939.99"));

        mockUsers.put("maria@demo.com", new UserInfo("pass3",
                "Owner: Maria Garcia\n" +
                        "Vehicle: Tesla Model 3 (2021)\n" +
                        "VIN: 5YJ3E1EA1MF012789\n" +
                        "Purchase Date: 2021-07-19\n" +
                        "Mileage: 27,450 mi\n" +
                        "Charging: $54.80 (home)\n" +
                        "Wash: $16.00 (10/18/2025)\n" +
                        "Other: $35.00 (All-weather mats)\n" +
                        "Total (last month): $105.80"));
    }

    // ---- Session data (acts like CurrentSession) ----
    public static String currentUserEmail = null;
    public static String currentUserVehicleData = null;

    // -----------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        signupRedirect = findViewById(R.id.signupRedirect);

        loginBtn.setOnClickListener(v -> {
            String enteredEmail = email.getText().toString().trim();
            String enteredPassword = password.getText().toString();

            UserInfo user = mockUsers.get(enteredEmail);
            if (user != null && user.password.equals(enteredPassword)) {
                // Save user info in session
                currentUserEmail = enteredEmail;
                currentUserVehicleData = user.vehicleDetails;

                Toast.makeText(this, "Welcome, " + enteredEmail, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            } else {
                Toast.makeText(this,
                        "Invalid credentials.\nTry:\nuser@test.com / 1234\npriya@demo.com / pass1\njohn@demo.com / pass2\nmaria@demo.com / pass3",
                        Toast.LENGTH_LONG).show();
            }
        });


        signupRedirect.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SignupActivity.class)));
    }
}
