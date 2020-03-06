package com.example.longcodeapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MessageRecieve extends BroadcastReceiver {

    private static MessageListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar calender = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = simpleDateFormat.format(calender.getTime());
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // get sms objects
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus.length == 0) {
                    return;
                }

                SmsMessage[] messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sb.append(messages[i].getMessageBody());
                }
                String SENDER = messages[0].getOriginatingAddress();
                String MESSAGE = sb.toString();
                String DATETIME = datetime;
                mListener.messageReceived(MESSAGE, SENDER, DATETIME);
            }
    }


    public static void bindListener(MessageListener listener){
        mListener = listener;
    }
}
