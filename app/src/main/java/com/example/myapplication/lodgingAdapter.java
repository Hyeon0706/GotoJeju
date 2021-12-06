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
    ArrayList<singleItem> List;
    Context context;

    public lodgingAdapter(ArrayList<singleItem> List, Context context) {
        this.List = List;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.single_item,parent,false);

        VH vh= new VH(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH)holder;

        //현재번째(position) 아이템 얻어오기
        singleItem s= List.get(position);
        vh.tvTitle.setText(s.getTitle());
        vh.tvAddr.setText(s.getAddr());
        vh.tvPnum.setText(s.getpNum());

        //이미지는 없을 수도 있음.
        if(s.getIurl()==null){
            vh.iv.setVisibility(View.GONE);
        }else{
            vh.iv.setVisibility(View.VISIBLE);
            //네트워크에 있는 이미지를 보여주려면
            //별도의 Thread가 필요한데 이를 편하게
            //해주는 Library사용(Glide library)

            Glide.with(context).load(s.getIurl()).into(vh.iv);

        }

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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String itemName= List.get(getLayoutPosition()).getTitle();
                    String entpName= List.get(getLayoutPosition()).getAddr();
                    String itemSeq= List.get(getLayoutPosition()).getpNum();
                    String imgUrl= List.get(getLayoutPosition()).getIurl();
                }
            });




        }
    }
}
