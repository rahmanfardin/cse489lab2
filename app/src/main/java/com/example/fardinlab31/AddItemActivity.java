package com.example.fardinlab31;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddItemActivity extends AppCompatActivity {

    private EditText etItemName, etCost, etDate;
    private Button addItem, back;
    private String prevID = null;

    private boolean validateInputs(String itemname, String cost, String date) {
        // Check if the item name is not empty
        boolean returnBool = true;
        if (itemname.isEmpty()) {
            etItemName.setError("Item name is required");
            returnBool = false;
        }

        // Check if the cost is not empty and is a valid number
        if (cost.isEmpty()) {
            etCost.setError("Cost is required");
            returnBool = false;
        }

        try {
            Double.parseDouble(cost);  // Try parsing cost as a double
        } catch (NumberFormatException e) {
            etCost.setError("Invalid cost value");
            returnBool = false;
        }

        // Check if the date is not empty and is a valid date format
        if (date.isEmpty()) {
            etDate.setError("Date is required");
            returnBool = false;
        }

        // Optional: You can add further validation to check if the date format is correct
        // For example, checking if the date is in the format "yyyy-MM-dd" (you can use a SimpleDateFormat to validate)

        // If all checks pass
        return  returnBool;
    }


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
        if (i.hasExtra("ITEM-ID")) {
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
                if (!validateInputs(itemname, cost, date)) {
                    System.out.println("invalid data");
                } else {
                    // Use the current date instead of a fixed date
                    Calendar currentCal = Calendar.getInstance();
                    long currentTime = currentCal.getTimeInMillis();  // Current time in milliseconds

                    // Convert the current date to milliseconds (currentCal already represents today's date)
                    long dateValue = currentTime;

                    // Create a unique ID using itemname and current time
                    String ID = itemname + ";" + currentTime;
                    itemDB idb = new itemDB(AddItemActivity.this);

                    // Update or insert the event into the database
                    if (prevID != null) {
                        ID = prevID;
                        idb.updateEvent(ID, itemname, dateValue, Double.parseDouble(cost));
                    } else {
                        idb.insertEvent(ID, itemname, dateValue, Double.parseDouble(cost));
                    }

                    // Output data to the console (for debugging purposes)
                    System.out.println(itemname);
                    System.out.println(cost);
                    System.out.println(date);

                    // Start the ShowReportActivity if needed
                    // startActivity(new Intent(AddItemActivity.this, ShowReportActivity.class));
                }
            }

        });
    }
}