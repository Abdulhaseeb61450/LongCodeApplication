package com.example.longcodeapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    EditText simnumber1,simnumber2,smsliitforsim1,smsliitforsim2,outgoingapiforsim1,outgoingapiforsim2,incomingapiforsim1,incomingapiforsim2;
    public static final String mypreference = "mypref";
    SharedPreferences sharedpreferences;
    int ALL_PERMISSIONS = 101;
    public String FirstTime,SIM1NUMBER,SIM2NUMBER,SMSLIMITFORSIM1,SMSLIMITFORSIM2,OUTGOINGAPIFORSIM1,OUTGOINGAPIFORSIM2,INCOMINGAPIFORSIM1,INCOMINGAPIFORSIM2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        FirstTime = sharedpreferences.getString("FirstTime", "Yes");
        SIM1NUMBER = sharedpreferences.getString("SIMNUMBER1", "");
        SIM2NUMBER = sharedpreferences.getString("SIMNUMBER2", "");
        OUTGOINGAPIFORSIM1 = sharedpreferences.getString("OUTGOINGAPIFORSIM1", "");
        SMSLIMITFORSIM1 = sharedpreferences.getString("SMSLIMITFORSIM1", "");
        SMSLIMITFORSIM2 = sharedpreferences.getString("SMSLIMITFORSIM2", "");
        INCOMINGAPIFORSIM1 = sharedpreferences.getString("INCOMINGAPIFORSIM1", "");
        INCOMINGAPIFORSIM2 = sharedpreferences.getString("INCOMINGAPIFORSIM2", "");

        if (FirstTime.equals("No"))
        {
            Intent intent = new Intent(MainActivity.this, ViewPage.class);
            startActivity(intent);
        }

        simnumber1 = findViewById(R.id.sim1number);
        simnumber2 = findViewById(R.id.sim2number);
        smsliitforsim1 = findViewById(R.id.smslimitforsim1);
        smsliitforsim2 = findViewById(R.id.smslimitforsim2);
        outgoingapiforsim1 = findViewById(R.id.outgoingapiforsmi1);
        incomingapiforsim1 = findViewById(R.id.incomingapiforsim1);
        incomingapiforsim2 = findViewById(R.id.incomingapiforsim2);

        simnumber1.setText(SIM1NUMBER);
        simnumber2.setText(SIM2NUMBER);
        smsliitforsim1.setText(SMSLIMITFORSIM1);
        smsliitforsim2.setText(SMSLIMITFORSIM2);
        outgoingapiforsim1.setText(OUTGOINGAPIFORSIM1);
        incomingapiforsim1.setText(INCOMINGAPIFORSIM1);
        incomingapiforsim2.setText(INCOMINGAPIFORSIM2);

        requestSmsPermission();
    }

    public void SaveData(View view)
    {
        String SIMNUMBER1 = simnumber1.getText().toString().trim();
        String SIMNUMBER2 = simnumber2.getText().toString().trim();
        String SMSLIMITFORSIM1 = smsliitforsim1.getText().toString().trim();
        String SMSLIMITFORSIM2 = smsliitforsim2.getText().toString().trim();
        String OUTGOINGAPIFORSIM1 = outgoingapiforsim1.getText().toString().trim();
        String INCOMINGAPIFORSIM1 = incomingapiforsim1.getText().toString().trim();
        String INCOMINGAPIFORSIM2 = incomingapiforsim2.getText().toString().trim();

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("FirstTime", "No");
        editor.putString("SIMNUMBER1", SIMNUMBER1);
        editor.putString("SIMNUMBER2", SIMNUMBER2);
        editor.putString("SMSLIMITFORSIM1", SMSLIMITFORSIM1);
        editor.putString("SMSLIMITFORSIM2", SMSLIMITFORSIM2);
        editor.putString("OUTGOINGAPIFORSIM1", OUTGOINGAPIFORSIM1);
        editor.putString("INCOMINGAPIFORSIM1", INCOMINGAPIFORSIM1);
        editor.putString("INCOMINGAPIFORSIM2", INCOMINGAPIFORSIM2);
        editor.commit();

        startService(new Intent(getApplicationContext(),MyService.class));
        Intent intent = new Intent(MainActivity.this, ViewPage.class);
        startActivity(intent);

    }

    private void requestSmsPermission() {
        final String[] permissions = new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE};
        ActivityCompat.requestPermissions(this, permissions, ALL_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,"permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this,"permission not granted", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
