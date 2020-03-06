package com.example.longcodeapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.longcodeapplication.MyService.mypreference;

public class ViewPage extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
    }


    public void EDIT(View view)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("FirstTime", "Yes");
        editor.commit();
        Intent intent = new Intent(ViewPage.this, MainActivity.class);
        startActivity(intent);
    }

    public void VIEW(View view)
    {
        Intent intent = new Intent(ViewPage.this, ShowDatabase.class);
        startActivity(intent);
    }

    public void ERROR(View view)
    {
        Intent intent = new Intent(ViewPage.this, ShowErrorDatabase.class);
        startActivity(intent);
    }

    public void INCOMING(View view)
    {
        Intent intent = new Intent(ViewPage.this, ShowIncomingDatabase.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
