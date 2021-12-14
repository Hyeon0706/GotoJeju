package com.example.myapplication;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class lodgingAdapter extends RecyclerView.Adapter{
    ArrayList<lodgingSingleItem> List;
    Context context;

    public lodgingAdapter(ArrayList<lodgingSingleItem> List, Context context) {
        this.List = List;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.lodghin_single_item,parent,false);

        VH vh= new VH(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH)holder;


        lodgingSingleItem s= List.get(position);
        vh.tvTitle.setText(s.getTitle());
        vh.tvAddr.setText(s.getAddr());
        vh.tvPnum.setText(s.getpNum());
        Glide.with(context).load(s.getIurl()).fallback(R.drawable.no_image).into(vh.iv);
        //숙소의 이미지는 있을경우 가져오고 없으면 준비된 다른 이미지를 띄움


    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView tvTitle, tvAddr, tvPnum;
        ImageView iv;

        public VH(@NonNull View itemView) {
            super(itemView);

            tvTitle=itemView.findViewById(R.id.title);
            tvAddr=itemView.findViewById(R.id.address);
            tvPnum=itemView.findViewById(R.id.phoneNum);
            Linkify.addLinks(tvPnum, Linkify.PHONE_NUMBERS);
            iv=itemView.findViewById(R.id.imageView);
        }
    }
}
