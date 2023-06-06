package com.loginwithsqllite.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, usernameText;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        usernameText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button signupButton = findViewById(R.id.signupButton);

        // Connect to SQLite database
        database = openOrCreateDatabase("login_db", MODE_PRIVATE, null);

        // Set click listener for the sign-up button
        signupButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String username = usernameText.getText().toString();

            // Validate email and password
            if (isValidEmail(email) && isValidPassword(password)) {
                // Check if the user already exists in the database
                if (isUserExists(email)) {
                    // User already exists
                    Toast.makeText(SignupActivity.this, "User with this mail already exists", Toast.LENGTH_SHORT).show();
                } else if(isUserNameExists(username)) {
                    Toast.makeText(SignupActivity.this, "User Name Already Exists", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Insert the new user into the database
                    ContentValues values = new ContentValues();
                    values.put("username", username);
                    values.put("email", email);
                    values.put("password", password);
                    database.insert("users", null, values);

                    // Registration successful
                    Toast.makeText(SignupActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                    // Finish the sign-up activity and return to login activity
                    finish();
                }
            } else {
                // Invalid email or password format
                Toast.makeText(SignupActivity.this, "Invalid email or password format", Toast.LENGTH_SHORT).show();
            }
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

    // Check if the user already exists in the database
    private boolean isUserExists(String email) {
        String[] columns = {"email"};
        String selection = "email=?";
        String[] selectionArgs = {email};
        Cursor cursor = database.query("users", columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    private boolean isUserNameExists(String username) {
        String[] columns = {"username"};
        String selection = "username=?";
        String[] selectionArgs = {username};
        Cursor cursor = database.query("users", columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection
        database.close();
    }
}
