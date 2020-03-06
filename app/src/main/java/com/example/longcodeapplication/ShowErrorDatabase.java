package com.example.longcodeapplication;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowErrorDatabase extends AppCompatActivity {

    public ArrayList<HashMap<String, String>> GetAllErrors = new ArrayList<>();
    private ListView lv;
    public ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_error_database);

        lv = findViewById(R.id.list_view);

        DbHandler dbHandler = new DbHandler(this);
        GetAllErrors = dbHandler.GetAllErrorDetail();
        adapter = new SimpleAdapter(
                ShowErrorDatabase.this, GetAllErrors,
                R.layout.list_item1, new String[]{"id", "ErrorNo", "Description", "DateTime"}, new int[]{
                R.id.id, R.id.errorno, R.id.error, R.id.datetime});
        lv.setAdapter(adapter);
    }
}
