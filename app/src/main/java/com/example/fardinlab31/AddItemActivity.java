package com.example.fardinlab31;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddItemActivity extends AppCompatActivity {

    private EditText etItemName,etCost,etDate;
    private Button addItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);
        etItemName = findViewById(R.id.etItemName);
        etCost = findViewById(R.id.etCost);
        etDate = findViewById(R.id.etDate);
        addItem = findViewById(R.id.addItem);
    }
}