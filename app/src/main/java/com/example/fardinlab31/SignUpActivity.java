package com.example.fardinlab31;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {

    private EditText etUserName,etEmail,etPhone,etPassword,etCPassword;
    private CheckBox cbRM,cbRUser;
    private Button haveAccount,signUP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etCPassword);
        cbRM = findViewById(R.id.cbRM);
        cbRUser = findViewById(R.id.cbRUser);
        haveAccount = findViewById(R.id.haveAccount);
        signUP = findViewById(R.id.signUP);
    }
}