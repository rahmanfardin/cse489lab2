package com.example.fardinlab31;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LogINActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox cbRUser, cbRM;
    private Button noAccount, login;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences("my_sp", MODE_PRIVATE);
        setContentView(R.layout.activity_log_inactivity);
        String getEmail = sp.getString("USER-EMAIL", "");
        String getPassword = sp.getString("PASSWORD", "");
        Boolean cbReUser = sp.getBoolean("REMEMBER-USER", false);
        Boolean cbReME = sp.getBoolean("REMEMBER-ME", false);


        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRM = findViewById(R.id.cbRM);
        cbRUser = findViewById(R.id.cbRUser);
        noAccount = findViewById(R.id.noAccount);
        login = findViewById(R.id.login);

        if (cbReUser) {
            etEmail.setText(getEmail);
            cbRUser.setChecked(true);
        }
        if (cbReME) {
            etEmail.setText(getEmail);
            etPassword.setText(getPassword);
            cbRM.setChecked(true);
        }


        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogINActivity.this, SignUpActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (!getEmail.equals(etEmail.getText().toString().trim())) {
                    Toast.makeText(LogINActivity.this, "Email is INVALID", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!getPassword.equals(etPassword.getText().toString().trim())) {
                    Toast.makeText(LogINActivity.this, "Password IS invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                System.out.println(email);
                System.out.println(password);

                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("REMEMBER-ME", cbRM.isChecked());
                editor.putBoolean("REMEMBER-USER", cbRUser.isChecked());
                editor.apply();

                startActivity(new Intent(LogINActivity.this, ShowReportActivity.class));
            }
        });
    }
}