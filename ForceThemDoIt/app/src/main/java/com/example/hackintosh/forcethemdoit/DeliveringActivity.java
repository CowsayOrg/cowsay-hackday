package com.example.hackintosh.forcethemdoit;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class DeliveringActivity extends Activity {

    private RecyclerView deliverListView;
    private RecyclerView.Adapter deliverListAdapter;
    private RecyclerView.LayoutManager deliverListManager;

    private List<String[]> victims;
    private Intent sendSMS;
    private String scheduleName;
    private BroadcastReceiver receiver;
    private DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivering);

        dataBase = new DataBase(getApplicationContext());
        scheduleName = getIntent().getExtras().getString("schedule");
        victims = dataBase.getVictims(scheduleName);

        TextView header = (TextView) findViewById(R.id.delivering);
        if(isMyServiceRunning(SmsSender.class)) {
            header.setText("Delivering On");
        }
        else {
            header.setText("Delivering Off");
        }

        deliverListView = (RecyclerView) findViewById(R.id.receivers_list);
        deliverListManager = new LinearLayoutManager(this);
        deliverListView.setLayoutManager(deliverListManager);
        deliverListAdapter = new DeliveringListAdapter(victims);
        deliverListView.setAdapter(deliverListAdapter);

        Button button = (Button) findViewById(R.id.stopDelivering);
        if(isMyServiceRunning(SmsSender.class)) {
            button.setText("Stop Delivering");
        }
        else {
            button.setText("Start Delivering");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS = new Intent(getApplicationContext(), SmsSender.class);
                sendSMS.putExtra("schedule",scheduleName);
                if(isMyServiceRunning(SmsSender.class)) {
                    stopService(sendSMS);
                }
                else {
                    startService(sendSMS);
                }
                stopActivity();
            }
        });

        receiver  = new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Works");
//                time = intent.getExtras().getInt("time");
//                lexiconDB.setNotificationTIme(time);
                dataBase.populateFlagTable("0",scheduleName);
                stopService(sendSMS);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("STOP_SMS"));
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void stopActivity() {
        this.finish();
    }
}
