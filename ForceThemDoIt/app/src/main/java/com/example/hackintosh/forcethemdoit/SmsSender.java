package com.example.hackintosh.forcethemdoit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hackintosh on 4/1/17.
 */

public class SmsSender extends Service {
    SmsManager smsManager;
    Intent sendSMSIntent;
    List<String[]> victims; //= new HashMap<String,String>();
    private String scheduleName;
    private int time;
    private CountDownTimer timer;
    private DataBase dataBase;
    private int flag; //if 1 messages are send, else nothing

    @Override
    public void onCreate() {
        Log.d("Service","create");
        super.onCreate();
        smsManager = SmsManager.getDefault();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service", "start");
        dataBase = new DataBase(getApplicationContext());
        dataBase.showTable(DataBaseHelper.ReceiverModel.TABLE_NAME,
                DataBaseHelper.ReceiverModel.NUMBER,
                DataBaseHelper.ReceiverModel.MESSAGE);
        sendSMSIntent = intent;
        //victims = (HashMap<String, String>) intent.getSerializableExtra("victims");
        scheduleName = intent.getExtras().getString("schedule");
        victims = dataBase.getVictims(scheduleName);
        flag = Integer.parseInt(dataBase.getFlag());
        time = 60000;
        for(String[] victim : victims) {
            Log.d("Victims",victim[0] + " " + victim[1]);
        }
        sendMessagePeriodically();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // STOP YOUR TASKS
        Log.d("Service", "Stop and Destroy");
        timer.cancel();
        if(flag <= 0) {
            shuflleVictims();
            dataBase.populateDB(scheduleName,victims);
            stopSelf();
            restartService();
        }
        stopSelf();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("Service", "TASK REMOVED");
        stopSelf();
        super.onTaskRemoved(rootIntent);
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void sendMessagePeriodically() {
        final int tick = 1000;

        timer = new CountDownTimer(time,tick) {
            @Override
            public void onTick(long l) {
                time -= tick;
                Log.d("Time_tick","" + time);
            }

            @Override
            public void onFinish() {
                time = 60000;
                sendMessagePeriodically();
                Log.d("Time","" + time);
                Log.d("Flag","" + flag);
                if(flag > 0) {
                    Log.d("Send SMS to " + victims.get(0)[0], "Message:" + victims.get(0)[1]);
                    sendSms(victims.get(0)[0], victims.get(0)[1]);
                }
            }
        }.start();
    }

    public void sendSms(String number, String message) {
        smsManager.sendTextMessage(number, null, message, null, null);
    }

    public void restartService() {
        startService(sendSMSIntent);
    }

    public void shuflleVictims() {
        String[] temp = null;
        for(int i = 0; i < victims.size(); i++) {
            if(i == 0) {
                temp = victims.get(i);
            }
            if(i == victims.size() - 1) {
                victims.set(i,temp);
            }
            else {
                victims.set(i,victims.get(i + 1));
            }
        }
    }
}
