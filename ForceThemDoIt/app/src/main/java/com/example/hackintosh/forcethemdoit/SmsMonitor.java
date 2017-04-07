package com.example.hackintosh.forcethemdoit;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hackintosh on 4/1/17.
 */

public class SmsMonitor extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    private SmsMessage messages;
    private List<String[]> victims;
    private DataBase dataBase;

    public SmsMonitor() {
    }

    public SmsMonitor(List<String[]> victims) {
        this.victims = victims;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BroadcastReceiver","Receive");
        if(dataBase == null) {
            dataBase = new DataBase(context);
        }
        victims = dataBase.getVictims(dataBase.getSchedule());
        if (intent != null && intent.getAction() != null &&
                ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
            Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
            for (Object aPduArray : pduArray) {
                messages = SmsMessage.createFromPdu((byte[]) aPduArray);
                Log.d("Message",messages.getMessageBody().toString());
                Log.d("Address","" + messages.getDisplayOriginatingAddress());
                for(String[] victim : victims) {
                    if(victim[0].contains("0" + messages.getDisplayOriginatingAddress().substring(4))) {
                        Log.d("Victim","number in List");
                        //Log.d("currentNumbers","" + victims);
                        //currentNumbers.remove(messages.getDisplayOriginatingAddress());
//                        Intent serviceIntent = new Intent(context,SmsSender.class);
//                        //Activity activity = (Activity) context;
//                        context.stopService(serviceIntent);
                        Intent localIntent = new Intent ("STOP_SMS");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
                    }
                }
            }
        }
    }
}
