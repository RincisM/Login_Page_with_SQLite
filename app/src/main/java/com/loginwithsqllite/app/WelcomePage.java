package com.loginwithsqllite.app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        Button changelog = findViewById(R.id.changelog);
        Button logOutButton = findViewById(R.id.logout);
        TextView welcome = findViewById(R.id.welcome);
        Bundle extra = getIntent().getExtras();
        String usernameWel = extra.getString("username");
        String emailWel = extra.getString("email");
        String welcomeMessage = "Welcome "+usernameWel;
        welcome.setText(welcomeMessage);

        changelog.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomePage.this, ChangeLog.class);
            intent.putExtra("usernameChange", usernameWel);
            intent.putExtra("emailChange", emailWel);
            startActivity(intent);
        });
        logOutButton.setOnClickListener(v -> finish());
    }
}