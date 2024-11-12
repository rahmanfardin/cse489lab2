package com.example.fardinlab31;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LogINActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox cbRUser, cbRM;
    private Button  noAccount, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_inactivity);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRM = findViewById(R.id.cbRM);
        cbRUser = findViewById(R.id.cbRUser);
        noAccount = findViewById(R.id.noAccount);
        login = findViewById(R.id.login);

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(LogINActivity.this, SignUpActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                System.out.println(email);
                System.out.println(password);

                startActivity(new Intent(LogINActivity.this, ShowReportActivity.class));
            }
        });
    }
}