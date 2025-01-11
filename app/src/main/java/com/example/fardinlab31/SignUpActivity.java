package com.example.fardinlab31;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {

    private EditText etUserName, etEmail, etPhone, etPassword, etCPassword;
    private CheckBox cbRM, cbRUser;


    private SharedPreferences sp;

    private boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    protected void redirectUserOnExistingAccount() {
        sp = this.getSharedPreferences("my_sp", MODE_PRIVATE);
        String email = sp.getString("USER-EMAIL", "NOT-YET-CREATED");

        //Redirect user if an account is already created.
        if (!email.equals("NOT-YET-CREATED")) {
            // System.out.println("from signup");
            startActivity(new Intent(SignUpActivity.this, LogINActivity.class));
            finishAffinity();
            Toast.makeText(this, "From Signup You Have Been Redirected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (!getIntent().getBooleanExtra("FROM-LOGIN", false)) {
            Toast.makeText(this, "You have been redirected form login page",
                    Toast.LENGTH_SHORT).show();
            redirectUserOnExistingAccount();
        }


        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etCPassword);
        //
        cbRM = findViewById(R.id.cbRM);
        cbRUser = findViewById(R.id.cbRUser);
        //
        Button haveAccount = findViewById(R.id.haveAccount);
        Button signUP = findViewById(R.id.signUP);

        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUserName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String cPassword = etCPassword.getText().toString().trim();
                if (!isValidEmailId(email)) {
                    Toast.makeText(getApplicationContext(),
                            "InValid Email Address.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userName.length() < 4) {
                    Toast.makeText(SignUpActivity.this,
                            "Name must be 4 to 8 ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone.length() < 8) {
                    Toast.makeText(SignUpActivity.this,
                            "Phone number must be atleast 8 ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 4) {
                    Toast.makeText(SignUpActivity.this, "password must be 4",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(cPassword)) {
                    Toast.makeText(SignUpActivity.this, "passwords don't match",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                System.out.println(userName);
                System.out.println(email);
                System.out.println(phone);
                System.out.println(password);
                System.out.println(cPassword);
                SharedPreferences.Editor e = sp.edit();
                e.putString("USER-NAME", userName);
                e.putString("USER-EMAIL", email);
                e.putString("USER-PHONE", phone);
                e.putString("PASSWORD", password);
                e.putBoolean("REMEMBER-ME", cbRM.isChecked());
                e.putBoolean("REMEMBER-USER", cbRUser.isChecked());
                e.apply();


                startActivity(new Intent(SignUpActivity.this, LogINActivity.class));
                finishAffinity();
            }
        });
        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LogINActivity.class);
                startActivity(i);
            }
        });
    }
}