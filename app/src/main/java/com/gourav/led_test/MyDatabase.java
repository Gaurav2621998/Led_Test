package com.gourav.led_test;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {


    static final private String DB_Name="Smart_Lights";
    static final private String DB_Table="Areas";
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

        db.execSQL("create table "+DB_Table+" (id integer primary key autoincrement,areaname text)");
        Log.i("Database", "Table Created");


    }

    @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);

        }

    public void insertData(String s1)
    {
        myDB=getWritableDatabase();

        myDB.execSQL("insert into "+DB_Table+"(areaname) values('"+s1+"');");
        Toast.makeText(ct, "Table Created and Data saved", Toast.LENGTH_LONG).show();


    }

    public List<String> getAll()
    {
        myDB=getReadableDatabase();
        List<String> Arealist1 = new ArrayList<>();

        if(myDB!=null) {


            List<String> Arealist = new ArrayList<>();

            Cursor cursor = myDB.rawQuery("Select * from " + DB_Table, null);
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
}
