package com.gourav.led_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class Area_Add extends AppCompatActivity {

    MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area__add);



        myDatabase=new MyDatabase(this);

        final EditText editText=(EditText)findViewById(R.id.roomname);

        ImageView ok=(ImageView)findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomname=editText.getText().toString();
                myDatabase.insertData(roomname);
                startActivity(new Intent(Area_Add.this,Home_Activity.class));
            }
        });



    }
}
