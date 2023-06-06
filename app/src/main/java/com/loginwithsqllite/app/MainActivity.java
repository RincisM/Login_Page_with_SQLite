package com.loginwithsqllite.app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, usernameText;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        usernameText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button signupButton = findViewById(R.id.signupButton);

        // Connect to SQLite database
        database = openOrCreateDatabase("login_db", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS users (username VARCHAR, email VARCHAR, password VARCHAR)");
        // Set click listener for the login button
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String username = usernameText.getText().toString();

            // Validate email and password
            if (isValidEmail(email) && isValidPassword(password)) {
                // Check if the user exists in the database
                Cursor cursor = database.rawQuery("SELECT * FROM users WHERE email=? AND password=?", new String[]{email, password});
                if (cursor.getCount() > 0) {
                    // Login successful
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // Perform any desired actions after successful login
                    Intent intent = new Intent(MainActivity.this, WelcomePage.class);
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    // Invalid credentials
                    Toast.makeText(MainActivity.this, "User Does not Exists", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            } else {
                // Invalid email or password format
                Toast.makeText(MainActivity.this, "Invalid email or password format", Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for the sign-up button
        signupButton.setOnClickListener(v -> {
            // Open the sign-up activity
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    // Email validation method
    private boolean isValidEmail(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Password validation method
    private boolean isValidPassword(String password) {
        // Add your password validation logic here
        return password.length() >= 6;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection
        database.close();
    }
}
