package com.gourav.led_test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


public class HomeDataAdapter extends RecyclerView.Adapter<HomeDataAdapter.ViewHolder> {

    public Context context;
    List<String>AreaInfoList;
    public HomeDataAdapter(Context context, List<String>Area_infos) {
        this.context = context;
        this.AreaInfoList=Area_infos;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.area_card, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        RelativeLayout card;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card=(RelativeLayout)itemView.findViewById(R.id.areacard);
            textView=(TextView)itemView.findViewById(R.id.roomname);


        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {


        viewHolder.textView.setText(AreaInfoList.get(i));

        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,Area_Add.class);

                intent.putExtra("operation","update");
                intent.putExtra("areaname",AreaInfoList.get(i));

                context.startActivity(intent);


            }
        });







    }

    @Override
    public int getItemCount() {
        return AreaInfoList.size();
    }



}