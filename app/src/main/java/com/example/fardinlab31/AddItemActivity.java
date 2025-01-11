package com.example.fardinlab31;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private String id = "";

    private boolean validateInputs(String itemName, String cost, String date) {
        boolean isValid = true;

        // Validate item name
        if (itemName.isEmpty()) {
            etItemName.setError("Item name is required");
            isValid = false;
        }

        // Validate cost
        if (cost.isEmpty()) {
            etCost.setError("Cost is required");
            isValid = false;
        } else {
            try {
                Double.parseDouble(cost);
            } catch (NumberFormatException e) {
                etCost.setError("Invalid cost value");
                isValid = false;
            }
        }

        // Validate date
        if (date.isEmpty()) {
            etDate.setError("Date is required");
            isValid = false;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            try {
                sdf.parse(date);
            } catch (ParseException e) {
                etDate.setError("Invalid date format. Use yyyy-MM-dd");
                isValid = false;
            }
        }

        return isValid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etItemName = findViewById(R.id.etItemName);
        etCost = findViewById(R.id.etCost);
        etDate = findViewById(R.id.etDate);
        Button addItem = findViewById(R.id.addItem);
        Button back = findViewById(R.id.back);
        TextView tvAddItem = findViewById(R.id.tvAddItem);

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

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("ID")) {
            id = intent.getStringExtra("ID");
            String itemName = intent.getStringExtra("ITEM-NAME");
            long dateInMilliseconds = intent.getLongExtra("DATE", 0);
            double cost = intent.getDoubleExtra("COST", 0);
            String date = dateCon(dateInMilliseconds);
            etItemName.setText(itemName);
            etCost.setText(String.valueOf(cost));
            etDate.setText(date);
            tvAddItem.setText("Edit Item");
        }

        back.setOnClickListener(v -> startActivity(new Intent(AddItemActivity.this, ShowReportActivity.class)));

        addItem.setOnClickListener(v -> {
            String itemName = etItemName.getText().toString().trim();
            String cost = etCost.getText().toString().trim();
            String date = etDate.getText().toString().trim();

            if (!validateInputs(itemName, cost, date)) {
                System.out.println("Invalid data");
                return;
            }

            long dateValue;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date parsedDate = sdf.parse(date);
                dateValue = parsedDate.getTime();
            } catch (ParseException e) {
                Toast.makeText(this, "Error parsing date", Toast.LENGTH_SHORT).show();
                return;
            }

            String ID = itemName + ";" + System.currentTimeMillis();
            itemDB idb = new itemDB(AddItemActivity.this);

            try {
                if (id.isEmpty()) {
                    idb.insertEvent(ID, itemName, dateValue, Double.parseDouble(cost));
                } else {
                    ID = id;
                    idb.updateEvent(ID, itemName, dateValue, Double.parseDouble(cost));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String[] keys = {"action", "sid", "semester", "id", "itemName", "cost", "date"};
            String[] values = {"backup", "2021-2-60-008", "2024-3", ID, itemName, cost, String.valueOf(dateValue)};
            httpRequest(keys, values);

            startActivity(new Intent(AddItemActivity.this, ShowReportActivity.class));
        });
    }

    private void httpRequest(final String[] keys, final String[] values) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                List<NameValuePair> params = new ArrayList<>();
                for (int i = 0; i < keys.length; i++) {
                    params.add(new BasicNameValuePair(keys[i], values[i]));
                }
                String url = "https://www.muthosoft.com/univ/cse489/index.php";
                try {
                    return RemoteAccess.getInstance().makeHttpRequest(url, "POST", params);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String data) {
                if (data != null) {
                    Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private String dateCon(long milliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(milliseconds);
        return sdf.format(date);
    }
}
