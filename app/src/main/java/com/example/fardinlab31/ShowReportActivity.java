package com.example.fardinlab31;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class ShowReportActivity extends AppCompatActivity {

    private ListView lvExpenditureList;
    private Button back, newItem;
    private ArrayList <item> showItem = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_report);
        lvExpenditureList = findViewById(R.id.lvExpenditureList);
        back = findViewById(R.id.back);
        newItem = findViewById(R.id.newItem);

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
        itemDB idb= new itemDB(this     );
        Cursor c = idb.selectEvents("select * from items");

        while (c.moveToNext()){
            String aid = c.getString(0);
            String aitemname = c.getString(1);
            Long adate = c.getLong(2);
            double acost = c.getDouble(3);


            System.out.println("from add");
            System.out.println(aid);
            System.out.println(aitemname);
            System.out.println(adate);
            System.out.println(acost);
        }

    }
}