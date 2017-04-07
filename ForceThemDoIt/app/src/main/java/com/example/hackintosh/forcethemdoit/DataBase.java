package com.example.hackintosh.forcethemdoit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hackintosh on 4/2/17.
 */

public class DataBase {
    private SQLiteDatabase db;
    private DataBaseHelper dbHelper;
    private Context context;

    public DataBase(Context context) {
        this.context = context;
        dbHelper = new DataBaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void populateDB(String tableName, List<String[]> victims) {
        deleteTable(tableName);
        if(!checkIfExist(tableName)) {
            dbHelper.addNewReceiversTable(tableName,db);
        }
        db = dbHelper.getWritableDatabase();

        for(String[] victim : victims) {
            ContentValues values = new ContentValues();
            values.put(DataBaseHelper.ReceiverModel.NUMBER, victim[0]);
            values.put(DataBaseHelper.ReceiverModel.MESSAGE, victim[1]);
            db.insert(tableName, null, values);
        }
        showTable(tableName,
                DataBaseHelper.ReceiverModel.NUMBER,
                DataBaseHelper.ReceiverModel.MESSAGE);

    }

    public void populateFlagTable(String flag, String schedule) {
        deleteTable(DataBaseHelper.FlagModel.TABLE_NAME);
        if(!checkIfExist(DataBaseHelper.FlagModel.TABLE_NAME)) {
            dbHelper.addNewFlagTable(db);
        }
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.FlagModel.FLAG, flag);
        values.put(DataBaseHelper.FlagModel.SCHEDULE, schedule);
        db.insert(DataBaseHelper.FlagModel.TABLE_NAME, null, values);
    }

    public boolean checkIfExist(String tableName) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+ tableName +"'", null);

        if(cursor.getCount() <= 0) { return false; }
        else { return true; }
    }

    public void showTable(String tableName, String tableColumn1, String tableColumn2) {
        if(checkIfExist(tableName)) {
            Cursor cursor = db.rawQuery("select * from " + tableName, null);
            Log.d("Show table","table exist");
            List<String[]> items = new ArrayList<String[]>();
            while (cursor.moveToNext()) {
                String[] item = new String[2];
                item[0] = cursor.getString(
                        cursor.getColumnIndexOrThrow(tableColumn1));
                item[1] = cursor.getString(
                        cursor.getColumnIndexOrThrow(tableColumn2));
                items.add(0, item);
            }
            cursor.close();

            for(String[] item: items) {
                Log.d("DataBase item_0", "" + item[0]);
                Log.d("DataBase item_1", "" + item[1]);
            }

        }
        else {
            Log.d("Show Table", "table doesn't exit");
        }
    }

    public String getFlag() {
        if(checkIfExist(DataBaseHelper.FlagModel.TABLE_NAME)) {
            Cursor cursor = db.rawQuery("select * from " + DataBaseHelper.FlagModel.TABLE_NAME, null);

            String flag = null;
            while (cursor.moveToNext()) {
                flag = cursor.getString(
                        cursor.getColumnIndexOrThrow(DataBaseHelper.FlagModel.FLAG));
            }
            cursor.close();
            return flag;
        }

        return null;
    }

    public String getSchedule() {
        if(checkIfExist(DataBaseHelper.FlagModel.TABLE_NAME)) {
            Cursor cursor = db.rawQuery("select * from " + DataBaseHelper.FlagModel.TABLE_NAME, null);

            String schedule = null;
            while (cursor.moveToNext()) {
                schedule = cursor.getString(
                        cursor.getColumnIndexOrThrow(DataBaseHelper.FlagModel.SCHEDULE));
            }
            cursor.close();
            return schedule;
        }

        return null;
    }

    public List<String[]> getVictims(String schedule) {
        String tableName = schedule;
        if(checkIfExist(tableName)) {
            Cursor cursor = db.rawQuery("select * from " + tableName, null);

            List<String[]> items = new ArrayList<String[]>();
            while (cursor.moveToNext()) {
                String[] item = new String[2];
                item[0] = cursor.getString(
                        cursor.getColumnIndexOrThrow(DataBaseHelper.ReceiverModel.NUMBER));
                item[1] = cursor.getString(
                        cursor.getColumnIndexOrThrow(DataBaseHelper.ReceiverModel.MESSAGE));
                items.add(item);
            }
            cursor.close();

            Log.d("DataBase", "" + items);

            return items;
        }

        return null;
    }

    public void deleteTable(String tableName) {
        String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + tableName;
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    public List<String> getDBschedules() {
        List<String> schedules = new ArrayList<String>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (cursor.moveToFirst()) {
            while ( !cursor.isAfterLast() ) {
                Log.d("TableNames",cursor.getString(0));
                if(!cursor.getString(0).equals(DataBaseHelper.FlagModel.TABLE_NAME)) {
                    schedules.add(cursor.getString(0));
                }
                cursor.moveToNext();

            }
        }
        for(String schedule: schedules) {
            Log.d("DataBase Schedules", "" + schedule);
        }
        return schedules;
    }
}
