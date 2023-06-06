package com.loginwithsqllite.app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeLog extends AppCompatActivity {
    private EditText usernameText, emailEditText, typePasswordText, reTypePasswordText;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_log);
        usernameText = findViewById(R.id.usernameChange);
        emailEditText = findViewById(R.id.emailChange);
        typePasswordText = findViewById(R.id.passwordType);
        reTypePasswordText = findViewById(R.id.passwordRetype);
        Button saveChange = findViewById(R.id.ChangeButton);
        Button deleteAccount = findViewById(R.id.deleteButton);
        Bundle extra = getIntent().getExtras();
        String username = extra.getString("usernameChange");
        String email = extra.getString("emailChange");
        usernameText.setText(username);
        emailEditText.setText(email);

        database = openOrCreateDatabase("login_db", MODE_PRIVATE, null);

        deleteAccount.setOnClickListener(v -> {
            database.beginTransaction();
            int rowsAffected = database.delete("users", "email=?", new String[]{email});
            database.setTransactionSuccessful();
            database.endTransaction();
            if (rowsAffected > 0) {
                // Update successful
                Toast.makeText(ChangeLog.this, "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangeLog.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        saveChange.setOnClickListener(v -> {
            String newEmail = emailEditText.getText().toString().trim();
            String newPassword = typePasswordText.getText().toString().trim();
            String reNewPassword = reTypePasswordText.getText().toString().trim();
            String newUsername = usernameText.getText().toString();
            if(newPassword.equals(reNewPassword)) {
                if (isValidEmail(newEmail) && isValidPassword(newPassword)) {
                    // Update the values in the database
                    ContentValues values = new ContentValues();
                    values.put("email", newEmail);
                    values.put("password", newPassword);
                    values.put("username", newUsername);
                    int rowsAffected = database.update("users", values, "username=?", new String[]{username});

                    if (rowsAffected > 0) {
                        // Update successful
                        Toast.makeText(ChangeLog.this, "Changes Updated Successfully", Toast.LENGTH_SHORT).show();
                        // Create an intent to pass the updated values back to the WelcomePage activity
                        Intent intent = new Intent();
                        intent.putExtra("username", newUsername);
                        intent.putExtra("email", newEmail);
                        setResult(RESULT_OK, intent);
                        // Finish the ChangeLog activity
                        finish();
                    } else {
                        // Update failed
                        Toast.makeText(ChangeLog.this, "Failed to update changes", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
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