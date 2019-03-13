package com.gourav.led_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Area_Add extends AppCompatActivity {

    MyDatabase myDatabase;
    EditText editText;
    ImageView ok,delete;

    AreaItem_Adapter areaItem_adapter;

    List<DeviceInfo> DeviceList=new ArrayList<>();

    RecyclerView area_recycler;

    String areaname="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area__add);

        editText=(EditText)findViewById(R.id.roomname);
        ok=(ImageView)findViewById(R.id.ok);
        delete=(ImageView)findViewById(R.id.delete);
        myDatabase=new MyDatabase(this);
        area_recycler=(RecyclerView)findViewById(R.id.area_recycler);

        area_recycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        area_recycler.setLayoutManager(layoutManager);

        DeviceInfo d=new DeviceInfo("light1","kkkkkk");
        DeviceInfo d1=new DeviceInfo("light2","llllll");
        DeviceInfo d2=new DeviceInfo("light1","kkkkkk");

        DeviceList.add(d);
        DeviceList.add(d1);
        DeviceList.add(d2);

        myDatabase.insertDeviceinfo(DeviceList);

        String operation=getIntent().getStringExtra("operation");

        if(operation.equals("add"))
        {

        }
        else if(operation.equals("update"))
        {
            areaname=getIntent().getStringExtra("areaname");
            Toast.makeText(this, ""+areaname, Toast.LENGTH_SHORT).show();
            editText.setText(areaname);

        }


        areaItem_adapter=new AreaItem_Adapter(this,DeviceList,ok,areaname,editText);
        area_recycler.setAdapter(areaItem_adapter);






        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!(areaname.equals(null))) {
//                    String newareaname = editText.getText().toString();
//
//                    if (newareaname.equals("")) {
//                        Toast.makeText(Area_Add.this, "Area Name is Empty", Toast.LENGTH_SHORT).show();
//                    } else {
//                        myDatabase.updateArea(areaname,newareaname);
//
//                    }
//                }
//                else
//                {
//                    String newareaaname = editText.getText().toString();
//
//                    if (newareaaname.equals("")) {
//                        Toast.makeText(Area_Add.this, "Area Name is Empty", Toast.LENGTH_SHORT).show();
//                    } else {
//                        myDatabase.insertArea(newareaaname);
//
//                    }
//
//                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                myDatabase.deleterow(editText.getText().toString());
                startActivity(new Intent(Area_Add.this, Home_Activity.class));

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
