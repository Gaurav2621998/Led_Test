package com.gourav.led_test;

import android.arch.lifecycle.LiveData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {


    static final private String DB_Name="Smart_Lights";
    static final private String DB_TableA="Areas";
    static final private String TableA_C1="areaname";
    static final private String TableA_C2="device_address";

    static final private String DB_TableD="Devices";
    static final private int DB_VER =1;

    Context ct;
    SQLiteDatabase myDB;

    public MyDatabase(Context ct)
    {
            super(ct,DB_Name,null,DB_VER);
            this.ct=ct;
    }





    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+DB_TableA+" (id integer primary key autoincrement,areaname text,device_address text)");
        db.execSQL("create table "+DB_TableD+" (id integer primary key autoincrement,devicename text,deviceadd text)");
        Log.i("Database", "Table Created");


    }

    @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);

        }

    public void insertArea(String name,List<DeviceInfo>DeviceList)
    {
        myDB=getReadableDatabase();

        String query="Select * from "+DB_TableA+" where areaname='"+name+"'";
        Cursor cursor=myDB.rawQuery(query,null);

        if(cursor.getCount()<=0)
        {

            Gson gson = new Gson();

            String devices= gson.toJson(DeviceList);

            ContentValues values=new ContentValues();
            values.put(TableA_C1,name);
            values.put(TableA_C2,devices);

            myDB=this.getWritableDatabase();

            Boolean successful=myDB.insert(DB_TableA,null,values)>0;
            Toast.makeText(ct, "Table Created and Data saved"+successful, Toast.LENGTH_LONG).show();
            cursor.close();
            ct.startActivity(new Intent(ct,Home_Activity.class));

        }
        else
        {
            Toast.makeText(ct, "Area Already exist", Toast.LENGTH_SHORT).show();
        }





    }

    public void updateArea(String oldname,String newname)
    {
        myDB=getReadableDatabase();

        String query="Select * from "+DB_TableA+" where areaname='"+oldname+"'";
        Cursor cursor=myDB.rawQuery(query,null);

        if(cursor.getCount()>=0)
        {
            ContentValues contentValues=new ContentValues();
            contentValues.put("areaname",newname);

            myDB=getWritableDatabase();
            myDB.update(DB_TableA,contentValues,"areaname =?",new String[]{oldname});
            cursor.close();
            ct.startActivity(new Intent(ct,Home_Activity.class));

        }






    }





    public void insertDeviceinfo(List<DeviceInfo>deviceInfoList)
    {

        myDB=getReadableDatabase();

        Cursor cursor = myDB.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + DB_TableD + "'", null);

        if(cursor!=null) {

            for (int i = 0; i < deviceInfoList.size(); i++) {

                DeviceInfo deviceInfo = deviceInfoList.get(i);

                ContentValues values = new ContentValues();
                values.put("devicename", deviceInfo.getDevicename());
                values.put("deviceadd", deviceInfo.getDeviceadd());
                myDB.insert(DB_TableD, null, values);

            }
            // myDB.execSQL("insert into "+DB_TableD+"(devicename,deviceadd) values('"+s1+"',);");
            Toast.makeText(ct, "Table Created and Data saved", Toast.LENGTH_LONG).show();
        }


    }

    public List<DeviceInfo> getDeviceinfo()
    {
        myDB=getReadableDatabase();
        List<DeviceInfo>deviceInfoList=new ArrayList<>();

        Cursor cursor = myDB.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + DB_TableD + "'", null);

        if(cursor!=null) {

            Cursor cursor1 = myDB.rawQuery("Select * from " + DB_TableD, null);
            while (cursor.moveToNext()) {
                String devicename = cursor.getString(1);
                String deviceadd = cursor.getString(2);

                DeviceInfo deviceInfo = new DeviceInfo(devicename, deviceadd);
                deviceInfoList.add(deviceInfo);

            }

        }


        return deviceInfoList;
    }



    public List<String> getAllAreas()
    {
        myDB=getReadableDatabase();
        List<String> Arealist1 = new ArrayList<>();

        if(myDB!=null) {


            List<String> Arealist = new ArrayList<>();

            Cursor cursor = myDB.rawQuery("Select * from " + DB_TableA, null);
            StringBuilder str = new StringBuilder();

            while (cursor.moveToNext()) {
                String s1 = cursor.getString(1);
                Arealist.add(s1);

                str.append(s1 + "\n");

            }

            return Arealist;
        }
        else
        {
            return Arealist1;
        }

    }

    public void deleterow(String name)
    {

        SQLiteDatabase myDB=this.getWritableDatabase();

        String query="Select * from "+DB_TableA+" where areaname='"+name+"'";
        Cursor cursor=myDB.rawQuery(query,null);

        if(cursor.getCount()<=0) {
            String deleteQuery = "DELETE FROM " + DB_TableA + " WHERE  areaname='" + name + "'";
            myDB.execSQL(deleteQuery);

        }
        else
        {
            Toast.makeText(ct, "Area Not exist", Toast.LENGTH_SHORT).show();
        }
    }

}
