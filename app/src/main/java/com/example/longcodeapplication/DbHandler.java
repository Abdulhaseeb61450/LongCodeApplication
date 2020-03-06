package com.example.longcodeapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "LongCodeDatabase";
    private static final String TABLE_Outgoing = "Outgoing_Sms";
    private static final String TABLE_ErrorLog = "Error_Log";
    private static final String TABLE_Incoming = "Incoming_Sms";
    private static final String KEY_ID = "id";
    private static final String KEY_SMSID = "smsid";
    private static final String KEY_Text = "Text";
    private static final String KEY_RecieverNumber = "RecieverNumber";
    private static final String KEY_SenderNumber = "SenderNumber";
    private static final String KEY_Status = "Status";
    private static final String KEY_Description = "Description";
    private static final String KEY_ErrorNo = "ErrorNo";
    private static final String KEY_DateTime = "DateTime";

    public DbHandler(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE1 = "CREATE TABLE " + TABLE_ErrorLog + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DateTime + " TEXT,"
                + KEY_ErrorNo + " TEXT,"
                + KEY_Description + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE1);

        String CREATE_TABLE = "CREATE TABLE " + TABLE_Outgoing + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SMSID + " TEXT,"
                + KEY_Text + " TEXT,"
                + KEY_Status + " TEXT,"
                + KEY_DateTime + " TEXT,"
                + KEY_SenderNumber + " TEXT,"
                + KEY_RecieverNumber + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE);

        String CREATE_TABLE2 = "CREATE TABLE " + TABLE_Incoming + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_Text + " TEXT,"
                + KEY_Status + " TEXT,"
                + KEY_DateTime + " TEXT,"
                + KEY_SenderNumber + " TEXT,"
                + KEY_RecieverNumber + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE2);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Outgoing);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ErrorLog);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Incoming);
        onCreate(db);
    }


    void InsertOutgoingSMSDetails(String smsid ,String Text, String Status, String RecieverNumber, String SenderNumber, String DateTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_SMSID, smsid);
        cValues.put(KEY_Text, Text);
        cValues.put(KEY_DateTime, DateTime);
        cValues.put(KEY_Status, Status);
        cValues.put(KEY_RecieverNumber, RecieverNumber);
        cValues.put(KEY_SenderNumber, SenderNumber);
        long newRowId = db.insert(TABLE_Outgoing,null, cValues);
        db.close();
    }

    public boolean UpdateOutgoingSmsDetail(String ID,String Status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_Status,Status);
        db.update(TABLE_Outgoing, contentValues, "id = ?",new String[] { ID });
        return true;
    }

    public ArrayList<HashMap<String, String>> GetDetailsAccStatus(String Status){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> OutgoingSmsList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_Outgoing, new String[] { KEY_ID,
                        KEY_SMSID,KEY_Text,KEY_Status,KEY_RecieverNumber,KEY_SenderNumber }, KEY_Status + "=?",
                new String[] { Status }, null, null, null, null);
        while (cursor.moveToNext()){
            HashMap<String,String> SmsList = new HashMap<>();
            SmsList.put("id",cursor.getString(cursor.getColumnIndex(KEY_ID)));
            SmsList.put("smsid",cursor.getString(cursor.getColumnIndex(KEY_SMSID)));
            SmsList.put("Text",cursor.getString(cursor.getColumnIndex(KEY_Text)));
            SmsList.put("Status",cursor.getString(cursor.getColumnIndex(KEY_Status)));
            SmsList.put("RecieverNumber",cursor.getString(cursor.getColumnIndex(KEY_RecieverNumber)));
            SmsList.put("SenderNumber",cursor.getString(cursor.getColumnIndex(KEY_SenderNumber)));
            OutgoingSmsList.add(SmsList);
        }
        cursor.close();
        return  OutgoingSmsList;
    }


    public ArrayList<HashMap<String, String>> CheckDatabase(String Status){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> OutgoingSmsList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_Outgoing, new String[] { KEY_ID,
                        KEY_SMSID,KEY_Text,KEY_Status,KEY_RecieverNumber,KEY_SenderNumber }, KEY_Status + "!=?",
                new String[] { Status }, null, null, null, null);
        while (cursor.moveToNext()){
            HashMap<String,String> SmsList = new HashMap<>();
            SmsList.put("id",cursor.getString(cursor.getColumnIndex(KEY_ID)));
            SmsList.put("smsid",cursor.getString(cursor.getColumnIndex(KEY_SMSID)));
            SmsList.put("Text",cursor.getString(cursor.getColumnIndex(KEY_Text)));
            SmsList.put("Status",cursor.getString(cursor.getColumnIndex(KEY_Status)));
            SmsList.put("RecieverNumber",cursor.getString(cursor.getColumnIndex(KEY_RecieverNumber)));
            SmsList.put("SenderNumber",cursor.getString(cursor.getColumnIndex(KEY_SenderNumber)));
            OutgoingSmsList.add(SmsList);
        }
        cursor.close();
        return  OutgoingSmsList;
    }



    public ArrayList<HashMap<String, String>> AlreadyExistOrNot(String Smsid){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> AlreadyExistsList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_Outgoing, new String[] { KEY_ID,
                        KEY_SMSID,KEY_Text,KEY_Status,KEY_RecieverNumber,KEY_SenderNumber }, KEY_SMSID + "=?",
                new String[] { Smsid }, null, null, null, null);
        while (cursor.moveToNext()){
            HashMap<String,String> Exist = new HashMap<>();
            Exist.put("id",cursor.getString(cursor.getColumnIndex(KEY_ID)));
            Exist.put("smsid",cursor.getString(cursor.getColumnIndex(KEY_SMSID)));
            Exist.put("Text",cursor.getString(cursor.getColumnIndex(KEY_Text)));
            Exist.put("Status",cursor.getString(cursor.getColumnIndex(KEY_Status)));
            Exist.put("RecieverNumber",cursor.getString(cursor.getColumnIndex(KEY_RecieverNumber)));
            Exist.put("SenderNumber",cursor.getString(cursor.getColumnIndex(KEY_SenderNumber)));
            AlreadyExistsList.add(Exist);
        }
        cursor.close();
        return  AlreadyExistsList;
    }

    public ArrayList<HashMap<String, String>> GetAllOutgoingDetail(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> AllOutgoingList = new ArrayList<>();
        String query = "SELECT * FROM "+ TABLE_Outgoing;
        Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                HashMap<String, String> SmsList = new HashMap<>();
                SmsList.put("id", cursor.getString(cursor.getColumnIndex(KEY_ID)));
                SmsList.put("smsid",cursor.getString(cursor.getColumnIndex(KEY_SMSID)));
                SmsList.put("Text", cursor.getString(cursor.getColumnIndex(KEY_Text)));
                SmsList.put("Status", cursor.getString(cursor.getColumnIndex(KEY_Status)));
                SmsList.put("RecieverNumber", cursor.getString(cursor.getColumnIndex(KEY_RecieverNumber)));
                SmsList.put("SenderNumber",cursor.getString(cursor.getColumnIndex(KEY_SenderNumber)));
                AllOutgoingList.add(SmsList);
            }
        cursor.close();
        return  AllOutgoingList;
    }

    void InsertErrorLog(String errorno ,String description, String datetime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_ErrorNo, errorno);
        cValues.put(KEY_DateTime, datetime);
        cValues.put(KEY_Description, description);
        long newRowId = db.insert(TABLE_ErrorLog,null, cValues);
        db.close();
    }

    public ArrayList<HashMap<String, String>> GetAllErrorDetail(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> AllErrorList = new ArrayList<>();
        String query = "SELECT * FROM "+ TABLE_ErrorLog;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> ErrorList = new HashMap<>();
            ErrorList.put("id", cursor.getString(cursor.getColumnIndex(KEY_ID)));
            ErrorList.put("DateTime", cursor.getString(cursor.getColumnIndex(KEY_DateTime)));
            ErrorList.put("ErrorNo",cursor.getString(cursor.getColumnIndex(KEY_ErrorNo)));
            ErrorList.put("Description", cursor.getString(cursor.getColumnIndex(KEY_Description)));
            AllErrorList.add(ErrorList);
        }
        cursor.close();
        return  AllErrorList;
    }

    void InsertIncomingSMSDetails(String Text ,String Datetime, String Status, String RecieverNumber, String SenderNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_DateTime, Datetime);
        cValues.put(KEY_Text, Text);
        cValues.put(KEY_Status, Status);
        cValues.put(KEY_RecieverNumber, RecieverNumber);
        cValues.put(KEY_SenderNumber, SenderNumber);
        long newRowId = db.insert(TABLE_Incoming, null, cValues);
        db.close();
    }

    public boolean UpdateIncomingSmsDetail(String ID,String Status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_Status,Status);
        db.update(TABLE_Outgoing, contentValues, "id = ?",new String[] { ID });
        return true;
    }

    public ArrayList<HashMap<String, String>> GetIncomingDetailsAccStatus(String Status){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> IncomingSmsList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_Incoming, new String[] { KEY_ID,
                        KEY_DateTime,KEY_Text,KEY_Status,KEY_RecieverNumber,KEY_SenderNumber }, KEY_Status + "=?",
                new String[] { Status }, null, null, null, null);
        while (cursor.moveToNext()){
            HashMap<String,String> SmsList = new HashMap<>();
            SmsList.put("id",cursor.getString(cursor.getColumnIndex(KEY_ID)));
            SmsList.put("DateTime",cursor.getString(cursor.getColumnIndex(KEY_DateTime)));
            SmsList.put("Text",cursor.getString(cursor.getColumnIndex(KEY_Text)));
            SmsList.put("Status",cursor.getString(cursor.getColumnIndex(KEY_Status)));
            SmsList.put("RecieverNumber",cursor.getString(cursor.getColumnIndex(KEY_RecieverNumber)));
            SmsList.put("SenderNumber",cursor.getString(cursor.getColumnIndex(KEY_SenderNumber)));
            IncomingSmsList.add(SmsList);
        }
        cursor.close();
        return  IncomingSmsList;
    }

    public ArrayList<HashMap<String, String>> GetAllIncomingDetail(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> AllIncomingList = new ArrayList<>();
        String query = "SELECT * FROM "+ TABLE_Incoming;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> SmsList = new HashMap<>();
            SmsList.put("id", cursor.getString(cursor.getColumnIndex(KEY_ID)));
            SmsList.put("DateTime",cursor.getString(cursor.getColumnIndex(KEY_DateTime)));
            SmsList.put("Text", cursor.getString(cursor.getColumnIndex(KEY_Text)));
            SmsList.put("Status", cursor.getString(cursor.getColumnIndex(KEY_Status)));
            SmsList.put("RecieverNumber", cursor.getString(cursor.getColumnIndex(KEY_RecieverNumber)));
            SmsList.put("SenderNumber",cursor.getString(cursor.getColumnIndex(KEY_SenderNumber)));
            AllIncomingList.add(SmsList);
        }
        cursor.close();
        return  AllIncomingList;
    }

    public void DELETE()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql1 = "DELETE FROM Incoming_Sms WHERE DateTime <= date('now','-7 day')";
        String sql = "DELETE FROM Outgoing_Sms WHERE DateTime <= date('now','-7 day')";
        String sql2 = "DELETE FROM Error_Log WHERE DateTime <= date('now','-7 day')";
        db.execSQL(sql);
        db.execSQL(sql1);
        db.execSQL(sql2);

    }

}