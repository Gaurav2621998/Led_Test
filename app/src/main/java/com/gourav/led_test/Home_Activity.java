package com.gourav.led_test;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class Home_Activity extends AppCompatActivity {

    FloatingActionButton button;

    Button button1;

    MyDatabase myDatabase;

    List<String>AreaList=new ArrayList<>();

    HomeDataAdapter homeDataAdapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        button1=(Button)findViewById(R.id.fragment);
        recyclerView=(RecyclerView)findViewById(R.id.home_recycler);
        myDatabase=new MyDatabase(this);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        AreaList=myDatabase.getAll();



        if(AreaList.size() > 1)
        {
            homeDataAdapter=new HomeDataAdapter(this,AreaList);
            recyclerView.setAdapter(homeDataAdapter);

        }
        button1.setVisibility(View.GONE);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDatabase.getAll();
            }
        });
        button=(FloatingActionButton) findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(Home_Activity.this,Area_Add.class));
            }
        });





    }
}
