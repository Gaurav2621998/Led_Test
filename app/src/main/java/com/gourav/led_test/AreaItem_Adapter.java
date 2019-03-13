package com.gourav.led_test;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class AreaItem_Adapter extends RecyclerView.Adapter<AreaItem_Adapter.ViewHolder> {

    public Context context;

    TextView roomname;
    Button addroom;
    Spinner spinner;

    List<BluetoothDevice>bluetoothDeviceList;

    MyDatabase myDatabase;
    List<String>address;


    ProgressDialog progressDialog;

    ImageView okbutton;
    List<DeviceInfo>DeviceList;

    String areaname;
    EditText editText;

    List<Integer> count=new ArrayList<>();
    //DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("root").child("users");
    public AreaItem_Adapter(Context context, List<DeviceInfo>DeviceList, ImageView okbutton, String areaname, EditText editText) {
        this.context = context;
        this.DeviceList=DeviceList;
        this.okbutton=okbutton;
        this.editText=editText;
        this.areaname=areaname;
    }



    public int image_position;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.area_item, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        myDatabase=new MyDatabase(context);


        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {

                    count.add(i);

                }
                else
                {
                    count.remove(i);
                }

            }
        });


        okbutton.setOnClickListener(new View.OnClickListener() {
            List<DeviceInfo>addDeviceList=new ArrayList<>();
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+areaname, Toast.LENGTH_SHORT).show();
                if(!(areaname.equals(""))) {
                    String newareaname = editText.getText().toString();
                    Toast.makeText(context, ""+newareaname, Toast.LENGTH_SHORT).show();
                    if (newareaname.equals("")) {
                        Toast.makeText(context, "Area Name is Empty", Toast.LENGTH_SHORT).show();
                    } else {
                        myDatabase.updateArea(areaname,newareaname);

                    }
                }
                else
                {
                    String newareaaname = editText.getText().toString();
                    Toast.makeText(context, ""+newareaaname, Toast.LENGTH_SHORT).show();
                    if (newareaaname.equals("")) {
                        Toast.makeText(context, "Area Name is Empty", Toast.LENGTH_SHORT).show();
                    } else {


                        if(count.size()>0) {
                            for (int i = 0; i < count.size(); i++) {

                                    DeviceInfo deviceInfo=DeviceList.get(count.get(i));
                                    addDeviceList.add(deviceInfo);

                            }
                            myDatabase.insertArea(newareaaname,addDeviceList);

                        }
                        else
                        {
                            Toast.makeText(context, "Select Any Devices", Toast.LENGTH_SHORT).show();
                        }

                    }

                }

//          if(count.size()>0) {
//                    for (int i = 0; i < count.size(); i++) {
//
//                            DeviceInfo deviceInfo=DeviceList.get(count.get(i));
//                            addDeviceList.add(deviceInfo);
//
//                    }
//                    myDatabase.insertArea(,addDeviceList);
//
//                }
//                else
//                {
//                    Toast.makeText(context, "Select Any Devices", Toast.LENGTH_SHORT).show();
//                }


            }


        });







    }


    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        TextView devicename;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=(CheckBox)itemView.findViewById(R.id.check);
            devicename=(TextView)itemView.findViewById(R.id.light_name);

        }
    }
}
