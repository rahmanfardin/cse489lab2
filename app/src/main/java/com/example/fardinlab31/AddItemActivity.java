package com.example.fardinlab31;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddItemActivity extends AppCompatActivity {

    private EditText etItemName,etCost,etDate;
    private Button addItem, back;
    private String prevID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);
        etItemName = findViewById(R.id.etItemName);
        etCost = findViewById(R.id.etCost);
        etDate = findViewById(R.id.etDate);
        addItem = findViewById(R.id.addItem);
        back = findViewById(R.id.back);
        Intent i = getIntent();
        if (i.hasExtra("ITEM-ID")){
            prevID = i.getStringExtra("ITEM-ID");
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddItemActivity.this, ShowReportActivity.class));
            }
        });
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemname = etItemName.getText().toString().trim();
                String cost = etCost.getText().toString().trim();
                String date = etDate.getText().toString().trim();

                int YEAR = 2024;
                int MONTH = 12;
                int DATE = 3;

                double costValue = Double.parseDouble(cost);
                Calendar currentCal = Calendar.getInstance();
                long currentTime = currentCal.getTimeInMillis();

                currentCal.setTimeInMillis(0);
                currentCal.set(YEAR, MONTH, DATE);
                long dateValue= currentCal.getTimeInMillis();


                String ID = itemname+";"+currentTime;
                itemDB idb  = new itemDB(AddItemActivity.this);

                if (prevID != null){
                    ID = prevID;
                    idb.updateEvent(ID, itemname, dateValue,costValue);
                } else {
                    idb.insertEvent(ID, itemname, dateValue,costValue);
                }
                //validate data


                //if all data are valid the store.



                System.out.println(itemname);
                System.out.println(cost);
                System.out.println(date);

                //startActivity(new Intent(AddItemActivity.this, ShowReportActivity.class));
            }
        });
    }
}