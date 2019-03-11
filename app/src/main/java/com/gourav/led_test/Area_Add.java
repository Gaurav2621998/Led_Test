package com.gourav.led_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Area_Add extends AppCompatActivity {

    MyDatabase myDatabase;
    EditText editText;
    ImageView ok,delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area__add);

        editText=(EditText)findViewById(R.id.roomname);
        ok=(ImageView)findViewById(R.id.ok);
        delete=(ImageView)findViewById(R.id.delete);
        myDatabase=new MyDatabase(this);



        String operation=getIntent().getStringExtra("operation");

        if(operation.equals("add"))
        {

        }
        else if(operation.equals("update"))
        {
            String areaname=getIntent().getStringExtra("areaname");
            Toast.makeText(this, ""+areaname, Toast.LENGTH_SHORT).show();
            editText.setText(areaname);

        }





        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomname=editText.getText().toString();
                if(roomname.equals(""))
                {
                    Toast.makeText(Area_Add.this, "Room Name is Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    myDatabase.insertData(roomname);
                    startActivity(new Intent(Area_Add.this, Home_Activity.class));
                }
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
}
