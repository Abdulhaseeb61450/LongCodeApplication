package com.example.longcodeapplication;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MyService extends Service implements MessageListener {

    public String NetStatus, ID, TEXT, RECIEVERNUMBER, SENDERNUMBER, SMSID, STATUS;
    public String SIMNUMBER1, SIMNUMBER2, SMSRANGEFORSIM1, SMSRANGEFORSIM2, GETOUTGOINGAPIFORSIM1,INCOMINGAPIFORSIM2, INCOMINGAPIFORSIM1;
    public String INCOMINGNUMBER, INCOMINGSMS, DATETIMEE, STATUSS, RECIEVINGNUMBER, IDD;
    public String SIMNUMBER, OUTGOINGAPI, INCOMINGAPI, SMSRANGEE;
    public String HTTP = "http://";
    public static final String SMS_SENT_ACTION = "com.mycompany.myapp.SMS_SENT";

    public int requestchecker = 2;
    public int requestchecker1 = 5;
    public int requestchecker2 = 6;

    DbHandler db = new DbHandler(this);

    public SmsManager smsManager;
    public IntentFilter intentFilter;
    public BroadcastReceiver resultsReceiver;
    public static Handler handler = new Handler();

    public static ArrayList<HashMap<String, String>> OUTGOINGLIST = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> AlreadyExistList = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> CheckList = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> IncomingList = new ArrayList<>();

    public static final String mypreference = "mypref";
    SharedPreferences sharedpreferences;

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("haseeb","Service Started");

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SIMNUMBER1 = sharedpreferences.getString("SIMNUMBER1", "0");
        SIMNUMBER2 = sharedpreferences.getString("SIMNUMBER2", "0");
        SMSRANGEFORSIM1 = sharedpreferences.getString("SMSLIMITFORSIM1", "0");
        SMSRANGEFORSIM2 = sharedpreferences.getString("SMSLIMITFORSIM2", "0");
        GETOUTGOINGAPIFORSIM1 = sharedpreferences.getString("OUTGOINGAPIFORSIM1", "0");
        INCOMINGAPIFORSIM1 = sharedpreferences.getString("INCOMINGAPIFORSIM1", "0");
        INCOMINGAPIFORSIM2 = sharedpreferences.getString("INCOMINGAPIFORSIM2", "0");

        smsManager = SmsManager.getDefault();
        intentFilter = new IntentFilter(SMS_SENT_ACTION);
        registerReceiver(resultsReceiver, intentFilter);


        AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
        asyncTaskRunner.execute();


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "MyService Completed or Stopped.", Toast.LENGTH_SHORT).show();
    }


    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//

    public void CheckDatabase()
    {
        CheckList = db.GetDetailsAccStatus("true");
        CheckList = db.CheckDatabase("true");
        if (CheckList.size() == 0)
        {
            if (SIMNUMBER2.equals("")) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("haseeb","Get Data For SIM1");
                        GetData(SIMNUMBER1, GETOUTGOINGAPIFORSIM1, SMSRANGEFORSIM1);
                    }
                }, 5000);
            }
            else
            {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("haseeb","Get Data For SIM1");
                        GetData(SIMNUMBER1, GETOUTGOINGAPIFORSIM1, SMSRANGEFORSIM1);
                    }
                }, 5000);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("haseeb","Get Data For SIM2");
                        GetData(SIMNUMBER2, GETOUTGOINGAPIFORSIM1, SMSRANGEFORSIM2);
                    }
                }, 5000);
            }
        }
        else
        {
            for (int i = 0; i < CheckList.size(); i++)
            {
                Log.d("haseeb","Updating Previous SMS");
                ID = CheckList.get(i).get("id");
                SMSID = CheckList.get(i).get("smsid");
                TEXT = CheckList.get(i).get("Text");
                RECIEVERNUMBER = CheckList.get(i).get("RecieverNumber");
                SENDERNUMBER = CheckList.get(i).get("SenderNumber");
                STATUS = CheckList.get(i).get("Status");

                PostData1();
            }
            CheckList.clear();
            CheckDatabase();
        }
    }

    public void GetData(String simnumber, String outgoingurl, String smsrangee) {

        if (requestchecker == 0)
        {
            return;
        }

        Log.d("haseeb","Get Request");

        requestchecker = 0;
        SIMNUMBER = simnumber;
        OUTGOINGAPI = outgoingurl;
        SMSRANGEE = smsrangee;

        String URL = HTTP+GETOUTGOINGAPIFORSIM1+"&sender="+SIMNUMBER+"&getsms="+SMSRANGEE+"&apitype=getsms";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            requestchecker = 1;
                            JSONObject RESPONSE = response.getJSONObject("response");
                            String ERROR = RESPONSE.getString("errorno");
                            if (ERROR.equals("0")) {
                                JSONArray DATA = RESPONSE.getJSONArray("data");
                                for (int i = 0; i < DATA.length(); i++) {
                                    JSONObject DATAITEMS = DATA.getJSONObject(i);
                                    String smsid = DATAITEMS.get("id").toString();
                                    String sender = DATAITEMS.get("sender").toString();
                                    String receiver = DATAITEMS.get("receiver").toString();
                                    String msgdata = DATAITEMS.get("msgdata").toString();

                                    Calendar calender = Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                                    String datetime = simpleDateFormat.format(calender.getTime());

                                    Log.d("hello",smsid);

                                    AlreadyExistList.clear();
                                    AlreadyExistList = db.AlreadyExistOrNot(smsid);
                                    int s = AlreadyExistList.size();
                                    if (s>0)
                                    {
                                        //Do Not Add In Database
                                        Log.d("hello",smsid);
                                    }
                                    else
                                    {
                                        db.InsertOutgoingSMSDetails(smsid, msgdata, "notsent", receiver, sender, datetime);
                                        Log.d("haseeb","Data Saved In Database");
                                    }
                                }
                                SENDSURRENTSMS();

                            } else {
                                Log.d("haseeb","Data Saved In ErrorLOG");
                                requestchecker = 1;
                                String DESCRIPTION = RESPONSE.getString("description");
                                Calendar calender = Calendar.getInstance();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                                String datetime = simpleDateFormat.format(calender.getTime());
                                db.InsertErrorLog(ERROR, DESCRIPTION, datetime);
                                CheckDatabase();
                            }


                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestchecker = 1;
                        CheckDatabase();
                    }
                }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//

    public void SENDSURRENTSMS() {

        Log.d("haseeb","SENDING SMS");
        OUTGOINGLIST.clear();
        OUTGOINGLIST = db.GetDetailsAccStatus("notsent");
        int SIZE = OUTGOINGLIST.size();

        if (SIZE == 0) {
            CheckDatabase();
            //GetData(SIMNUMBER,OUTGOINGAPI,SMSRANGEE);
            return;
        }
        else {

            ID = OUTGOINGLIST.get(0).get("id");
            SMSID = OUTGOINGLIST.get(0).get("smsid");
            TEXT = OUTGOINGLIST.get(0).get("Text");
            RECIEVERNUMBER = OUTGOINGLIST.get(0).get("RecieverNumber");
            SENDERNUMBER = OUTGOINGLIST.get(0).get("SenderNumber");
            STATUS = OUTGOINGLIST.get(0).get("Status");

            String SENT = "SMS_SENT";

            Intent sentIntent = new Intent(SENT);
            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent, 0);


            //---when the SMS has been sent---
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Log.d("hello", "SMS SENT");
                            STATUS = "3";
                            db.UpdateOutgoingSmsDetail(ID, STATUS);
                            PostData();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Log.d("haseeb","SMS FAILED");
                            STATUS = "5";
                            db.UpdateOutgoingSmsDetail(ID, STATUS);
                            PostData();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Log.d("haseeb","SMS FAILED");
                            STATUS = "5";
                            db.UpdateOutgoingSmsDetail(ID, STATUS);
                            PostData();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Log.d("haseeb","SMS FAILED");
                            STATUS = "5";
                            db.UpdateOutgoingSmsDetail(ID, STATUS);
                            PostData();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Log.d("haseeb","SMS FAILED");
                            STATUS = "5";
                            db.UpdateOutgoingSmsDetail(ID, STATUS);
                            PostData();
                            break;
                    }
                }
            }, new IntentFilter(SENT));
            OUTGOINGLIST.clear();
            smsManager.sendTextMessage(RECIEVERNUMBER, SENDERNUMBER, TEXT, sentPI, null);
        }

    }

    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//

    public void PostData() {

        if (requestchecker1 == 0)
        {
            return;
        }
        else {
            requestchecker1 = 0;
            Log.d("haseeb", "Post Request");
            String URL = HTTP+GETOUTGOINGAPIFORSIM1+"&sender="+SENDERNUMBER+"&smsid="+SMSID+"&smsstatus="+STATUS+"&apitype=updatesms";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                requestchecker1 = 1;
                                Log.d("hello", "comes in response");
                                JSONObject RESPONSE = response.getJSONObject("response");
                                String StAtUs = RESPONSE.getString("status");
                                String ERROR = RESPONSE.getString("errorno");
                                String DeSCrIpTiON = RESPONSE.getString("description");
                                if (ERROR.equals("0")) {
                                    STATUS = "true";
                                    db.UpdateOutgoingSmsDetail(ID, STATUS);
                                    Log.d("haseeb", "Update sent & Database updated");
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            SENDSURRENTSMS();
                                        }
                                    }, 5000);


                                } else {

                                    Log.d("haseeb", "ERROR NO" + ERROR + "  STATUS" + StAtUs + "  DESCRIPTION" + DeSCrIpTiON);
                                }
                            } catch (JSONException e) {
                                Log.d("haseeb", e.toString() + "ERROR");
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("haseeb", "update not sent error");
                            requestchecker1 = 1;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SENDSURRENTSMS();
                                }
                            }, 5000);
                            Toast.makeText(MyService.this, error.toString(), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }) {

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjReq);
        }
    }

    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//

    public void PostData1() {

        if (requestchecker2 == 0)
        {
            return;
        }
        else {
            requestchecker2 = 0;
            Log.d("haseeb", "Post1 Request");
            String URL = HTTP+GETOUTGOINGAPIFORSIM1+"&sender="+SENDERNUMBER+"&smsid="+SMSID+"&smsstatus="+STATUS+"&apitype=updatesms";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                requestchecker2 = 1;
                                Log.d("hello", "comes in response");
                                JSONObject RESPONSE = response.getJSONObject("response");
                                String StAtUs = RESPONSE.getString("status");
                                String ERROR = RESPONSE.getString("errorno");
                                String DeSCrIpTiON = RESPONSE.getString("description");
                                if (ERROR.equals("0")) {
                                    STATUS = "true";
                                    db.UpdateOutgoingSmsDetail(ID, STATUS);


                                } else {

                                    Log.d("haseeb", "ERROR NO" + ERROR + "  STATUS" + StAtUs + "  DESCRIPTION" + DeSCrIpTiON);
                                }
                            } catch (JSONException e) {
                                Log.d("haseeb", e.toString() + "ERROR");
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("haseeb", "update not sent error");
                            requestchecker2 = 1;
                            Toast.makeText(MyService.this, error.toString(), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }) {

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjReq);
        }
    }

    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//

    @Override
    public void messageReceived(String message, String SENDER, String DATETIME) {
        Log.d("haseeb","SMS RECIEVED");
        INCOMINGSMS = message;
        INCOMINGNUMBER = SENDER;
        DATETIMEE = DATETIME;
        STATUSS = "false";
        RECIEVINGNUMBER = SIMNUMBER1;
        Log.d("haseeb","Saved Incoming SMS in Databse");
        db.InsertIncomingSMSDetails(INCOMINGSMS,DATETIMEE,STATUSS,RECIEVINGNUMBER,INCOMINGNUMBER);

        SENDINCOMINGSMS();
    }

    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//

    public void SENDINCOMINGSMS()
    {
        Log.d("haseeb","SEND INCOMING");
        IncomingList.clear();
        String statuss = "false";
        IncomingList = db.GetIncomingDetailsAccStatus(statuss);
        if (IncomingList.isEmpty())
        {
            return;
        }
        else
        {
            for (int i = 0; i < IncomingList.size(); i++) {
                IDD = IncomingList.get(i).get("id");
                INCOMINGSMS = IncomingList.get(i).get("Text");
                INCOMINGNUMBER = IncomingList.get(i).get("SenderNumber").replace("+", "");
                DATETIMEE = IncomingList.get(i).get("DateTime");
                RECIEVINGNUMBER = IncomingList.get(i).get("RecieverNumber");
                Log.d("haseeb","SEnding Update");

                UPDATEINCOMING();

            }
            IncomingList.clear();
        }
    }

    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//

    public void UPDATEINCOMING()
    {
        String URL = HTTP+INCOMINGAPIFORSIM1;
        JSONObject params = new JSONObject();
        try {
            params.put("key","123456");
            params.put("sender",INCOMINGNUMBER);
            params.put("receiver",RECIEVINGNUMBER);
            params.put("msgdata",INCOMINGSMS);
            params.put("rtime",DATETIMEE);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URL,params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            Toast.makeText(MyService.this, response.toString(), Toast.LENGTH_SHORT).show();
                            JSONObject RESPONSE = response.getJSONObject("response");
                            String ERROR = RESPONSE.getString("errorno");
                            if (ERROR.equals("0")) {
                                Log.d("haseeb","Update Sent");
                                db.UpdateIncomingSmsDetail(IDD,"true");
                                Log.d("haseeb","SavedUpdated in database");
                            }
                            else {
                            }
                        } catch (JSONException e) {
                            Log.d("haseeb",response.toString());
                            Toast.makeText(MyService.this, response.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("haseeb","ERROR:" + "" + error.toString());
                    }
                }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//
    //********************************************************************************************************************************************************************************************//

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            try {
                CheckDatabase();

            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
        }


        @Override
        protected void onPreExecute() {
        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

}




