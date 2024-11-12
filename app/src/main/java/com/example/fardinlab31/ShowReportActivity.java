package com.example.fardinlab31;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShowReportActivity extends AppCompatActivity {

    private ListView lvExpenditureList;
    private Button back, newItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_report);
        lvExpenditureList = findViewById(R.id.lvExpenditureList);
        back = findViewById(R.id.back);
        newItem = findViewById(R.id.newItem);
    }
}