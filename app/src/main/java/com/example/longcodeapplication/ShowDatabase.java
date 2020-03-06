package com.example.longcodeapplication;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowDatabase extends AppCompatActivity {

    public ArrayList<HashMap<String, String>> GetAllOutgoingDetail = new ArrayList<>();
    private ListView lv;
    public ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_database);

        lv = (ListView) findViewById(R.id.list_view);

        DbHandler dbHandler = new DbHandler(this);
        GetAllOutgoingDetail = dbHandler.GetAllOutgoingDetail();

        adapter = new SimpleAdapter(
                ShowDatabase.this, GetAllOutgoingDetail,
                R.layout.list_item, new String[]{"smsid","Text","SenderNumber","RecieverNumber","Status"}, new int[]{
                R.id.smsid,R.id.text,R.id.sender,R.id.reciever,R.id.status});
        lv.setAdapter(adapter);
    }
}
