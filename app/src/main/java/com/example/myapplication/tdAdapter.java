package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class tdAdapter extends RecyclerView.Adapter{
    ArrayList<tdSingleitem> List;
    Context context;

    public tdAdapter(ArrayList<tdSingleitem> List, Context context) {
        this.List = List;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.td_single_item,parent,false);

        tdAdapter.VH vh= new tdAdapter.VH(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        tdAdapter.VH vh= (tdAdapter.VH)holder;


        tdSingleitem s= List.get(position);
        vh.tvTitle.setText(s.getTitle());
        vh.tvAddr.setText(s.getAddr());
        vh.tvPnum.setText(s.getpNum());
        Glide.with(context).load(s.getIurl()).fallback(R.drawable.no_image).into(vh.iv);
        //관광지의 이미지가 없을경우 준비된 다른 이미지를 띄움

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

                    String conId= List.get(getLayoutPosition()).getConId();

                    Toast myToast = Toast.makeText(view.getContext(), conId, Toast.LENGTH_SHORT);
                    myToast.show();

                    Intent intent = new Intent(context,test1.class);
                    intent.putExtra("conId",conId);
                    context.startActivity(intent);
                }
            });




        }
    }
}