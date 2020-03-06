package com.example.longcodeapplication;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowIncomingDatabase extends AppCompatActivity {

    public ArrayList<HashMap<String, String>> GetAllIncoming = new ArrayList<>();
    private ListView lv;
    public ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_incoming_database);

        lv = (ListView) findViewById(R.id.list_view);

        DbHandler dbHandler = new DbHandler(this);
        GetAllIncoming = dbHandler.GetAllIncomingDetail();
        GetAllIncoming = dbHandler.GetAllIncomingDetail();

        adapter = new SimpleAdapter(
                ShowIncomingDatabase.this, GetAllIncoming,
                R.layout.list_item2, new String[]{"id", "Text", "SenderNumber", "RecieverNumber", "Status", "DateTime"}, new int[]{
                R.id.id, R.id.text, R.id.sender, R.id.reciever, R.id.status, R.id.datetime});
        lv.setAdapter(adapter);

    }
}
