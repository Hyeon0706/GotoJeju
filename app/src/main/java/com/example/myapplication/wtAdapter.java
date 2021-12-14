package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class wtAdapter extends RecyclerView.Adapter {
    ArrayList<weatherSingleitem> List;
    Context context;

    public wtAdapter(ArrayList<weatherSingleitem> List, Context context) {
        this.List = List;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.weather_single_item,parent,false);

        wtAdapter.VH vh= new wtAdapter.VH(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        wtAdapter.VH vh= (wtAdapter.VH)holder;

        //현재번째(position) 아이템 얻어오기
        weatherSingleitem s= List.get(position);
        vh.value.setText(s.getFcstValue());
        vh.wtime.setText(s.getFcstTime());
        vh.wdate.setText(s.getFcstDate());
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView wtime, value, wdate;

        public VH(@NonNull View itemView) {
            super(itemView);

            wtime=itemView.findViewById(R.id.wtime);
            wdate=itemView.findViewById(R.id.wdate);
            value=itemView.findViewById(R.id.value);
        }
    }
}
