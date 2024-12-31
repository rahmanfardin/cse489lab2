package com.example.fardinlab31;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    private EditText etItemName, etCost, etDate;
    private Button addItem, back;

    private String id = "";

    // TODO: date picker. date convertor. workable search feild
    private boolean validateInputs(String itemname, String cost, String date) {
        boolean returnBool = true;

        // Check if the item name is not empty
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
        } else {
            // Validate the date format (yyyy-MM-dd)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);  // Set lenient to false to strictly enforce the date format
            try {
                sdf.parse(date);  // Try parsing the date
            } catch (ParseException e) {
                etDate.setError("Invalid date format. Use yyyy-MM-dd");
                returnBool = false;
            }
        }

        return returnBool;
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

        etDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddItemActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = String.format("%04d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
                        etDate.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });


        Intent J = getIntent();

        if (J != null && J.hasExtra("ID")) {
            id = J.getStringExtra("ID");
            String itemName = J.getStringExtra("ITEM-NAME");
            long dateInMilliSeconds = J.getLongExtra("DATE", 0);
            double cost = J.getDoubleExtra("COST", 0);
            String date = dateCon(dateInMilliSeconds); // write code to convert milliseconds to date
            etItemName.setText(itemName);
            etCost.setText(String.valueOf(cost));
            etDate.setText(date);
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
                    System.out.println(id.isEmpty());
                    try {
                        if (id.isEmpty()) {
                            idb.insertEvent(ID, itemname, dateValue, Double.parseDouble(cost));
                        } else {
                            ID = id;
                            idb.updateEvent(ID, itemname, dateValue, Double.parseDouble(cost));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    String keys[] = {"action", "sid", "semester", "id", "itemName", "cost", "date"};
                    String values[] = {"backup", "2021-2-60-008", "2024-3", ID, itemname, cost, String.valueOf(dateValue)};
                    httpRequest(keys, values);

                    // Output data to the console (for debugging purposes)
//                    System.out.println("ItemName:"+itemname);
//                    System.out.println("ItemCOST:"+cost);
//                    System.out.println("ItemDate:"+date);
                    startActivity(new Intent(AddItemActivity.this,
                            ShowReportActivity.class));
                    // Start the ShowReportActivity if needed
                    // startActivity(new Intent(AddItemActivity.this, ShowReportActivity.class));

                }
            }


        });
    }

    private void httpRequest(final String keys[], final String values[]) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (int i = 0; i < keys.length; i++) {
                    params.add(new BasicNameValuePair(keys[i], values[i]));
                }
                String url = "https://www.muthosoft.com/univ/cse489/index.php";
                try {
                    String data = RemoteAccess.getInstance().makeHttpRequest(url, "POST", params);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String data) {
                if (data != null) {
                    Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private String dateCon(long mS) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(mS);

        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}