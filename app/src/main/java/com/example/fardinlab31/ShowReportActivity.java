package com.example.fardinlab31;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ShowReportActivity extends AppCompatActivity {

    private final ArrayList<item> items = new ArrayList<>();
    private ListView lvExpenditureList;
    private Button back, newItem, btnSearch;
    private EditText etSearch;
    private CustomEventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_report);
        lvExpenditureList = findViewById(R.id.lvExpenditureList);
        back = findViewById(R.id.back);
        newItem = findViewById(R.id.newItem);
        btnSearch = findViewById(R.id.btnSearch);
        etSearch = findViewById(R.id.etSearch);


        adapter = new CustomEventAdapter(this, items);
        lvExpenditureList.setAdapter(adapter);

        lvExpenditureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item selectedItem = items.get(position);
                Intent intent = new Intent(ShowReportActivity.this, AddItemActivity.class);
                intent.putExtra("ID", selectedItem.ID);
                intent.putExtra("ITEM-NAME", selectedItem.itemName);
                intent.putExtra("DATE", selectedItem.date);
                intent.putExtra("COST", selectedItem.cost);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowReportActivity.this, LogINActivity.class));
            }
        });
        newItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowReportActivity.this, AddItemActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        loadLocalData();
        loadRemoteData();

    }

    private void loadRemoteData() {
        String[] keys = {"action", "sid", "semester"};
        String[] values = {"restore", "2021-2-60-008", "2024-3"};
        httpRequest(keys, values);
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
                    updateLocalDBByServerData(data);
                }
            }
        }.execute();
    }

    private void updateLocalDBByServerData(String data) {
        System.out.println(data);
        try {
            JSONObject jo = new JSONObject(data);
            System.out.println(jo.has("classes"));
            //! this returns false. "if" block doesnt get exe
            if (jo.has("classes")) {
                items.clear();
                double totalCost = 0;
                JSONArray ja = jo.getJSONArray("classes");
                itemDB idb = new itemDB(ShowReportActivity.this);

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject item = ja.getJSONObject(i);
                    String id = item.getString("id");
                    String itemName = item.getString("itemName");
                    double cost = item.getDouble("cost");
                    long date = item.getLong("date");
                    item item1 = new item(id, itemName, cost, date);
                    items.add(item1);
                    totalCost += cost;
                    System.out.println("new entry: " + id + itemName + cost + date + "\n");
                    idb.updateEvent(id, itemName, date, cost);
                }

                idb.close();
                adapter.notifyDataSetChanged();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLocalData() {
        items.clear();
        itemDB idb = new itemDB(this);
        Cursor c = idb.selectEvents("select * from items");
        int count = 0;
        while (c.moveToNext()) {
            String aid = c.getString(0);
            String aitemname = c.getString(1);
            Long adate = c.getLong(2);
            String cDate = dateCon(adate);
            double acost = c.getDouble(3);

            System.out.println("--------------------------------------------");
            System.out.println("from add: "+ count);
            count++;
            System.out.println("ItemID:" + aid);
            System.out.println("ItemName:" + aitemname);
            System.out.println("ItemDate:" + cDate);
            System.out.println("ItemCost:" + acost);
            item i = new item(aid, aitemname, acost, adate);
            items.add(i);
        }
        adapter.notifyDataSetChanged();
    }

    private String dateCon(long mS) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(mS);

        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}