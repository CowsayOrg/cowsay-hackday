package com.example.hackintosh.forcethemdoit;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    SmsSender sender;
    Intent sendSMS;
    private List<String[]> victims;
    List<String> projectsList = new ArrayList<>();

    private RecyclerView scheduleListView;
    private RecyclerView.Adapter scheduleListAdapter;
    private RecyclerView.LayoutManager scheduleListManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView header = (TextView) findViewById(R.id.header);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"Milkshake.ttf");
        header.setTypeface(typeface);
        TextView scheduleText = (TextView) findViewById(R.id.scheduleText);
        scheduleText.setTypeface(typeface);

//        victims = new ArrayList<String[]>();
//        victims.add(new String[] {"060642415","Hi, We are developing and testing SMS Sender Now.\nYou are our victim"});
//        victims.add(new String[] {"069000000","Hi, We are developing and testing SMS Sender Now.\nYou are our victim"});
//        victims.add(new String[] {"069111111","Hi, We are developing and testing SMS Sender Now.\nYou are our victim"});
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        }
        //sender.sendSms("068591082", "Test Message");
        DataBase dataBase = new DataBase(getApplicationContext());
        dataBase.deleteTable("android_metadata");
        //dataBase.populateDB(victims);
        //dataBase.populateFlagTable("1");
        //victims = dataBase.getVictims();
        //sendSMS = new Intent(this, SmsSender.class);
        //sendSMS.putExtra("victims",victims);
        //startService(sendSMS);
        FloatingActionButton newSchedule = (FloatingActionButton) findViewById(R.id.addSchedule);

        newSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddSchedule.class);
                startActivity(intent);
                stopActivity();
            }
        });

//        dummyList();

        scheduleListView = (RecyclerView) findViewById(R.id.scheduleList);

        scheduleListManager = new LinearLayoutManager(this);
        scheduleListView.setLayoutManager(scheduleListManager);

        projectsList = dataBase.getDBschedules();
        if (projectsList.isEmpty()) {
            TextView emptyList = (TextView) findViewById(R.id.empty_list);
            emptyList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), AddSchedule.class);
                    startActivity(intent);
                    stopActivity();
                }
            });
            emptyList.setVisibility(View.VISIBLE);
        }
        scheduleListAdapter = new ScheduleListAdapter(projectsList);
        scheduleListView.setAdapter(scheduleListAdapter);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("SMS_permission","Permitted");
                } else {
                    Log.d("SMS_permission","Failed");
                }
                return;
            }
        }
    }

    public void dummyList() {
        for(int i = 0; i < 10; i++) {
            projectsList.add(i,"Element" + i);
        }
    }

    public void stopActivity() {
        this.finish();
    }
}
