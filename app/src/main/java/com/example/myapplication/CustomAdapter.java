package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter <CustomAdapter.CustomViewHolder>{

    private ArrayList<UserAccount> arratList;
    private Context context; //컨텍트 쓰기위해 선언

    public CustomAdapter(ArrayList<UserAccount> arratList, Context context) {
        this.arratList = arratList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //연결된후에 만들기
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        CustomViewHolder holder =new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        //Glide.with(holder.itemView)
                //.load(arratList.get(position).getProfile()) //유저객체를 받아와 글라이드로 로드
                //.into(holder.iv_profile);
        holder.tv_id.setText(arratList.get(position).getEmailId());//여기로 홀더 뿌려주기
        holder.tv_pw.setText(String.valueOf(arratList.get(position).getPassword()));//여기로 홀더 뿌려주기
        holder.tv_userName.setText(arratList.get(position).getName());//여기로 홀더 뿌려주기
    }

    @Override
    public int getItemCount() {
        //배열리스트가 널이아니면 사이즈 삽입 널이면 0삽입
        return (arratList != null ? arratList.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder { //9줄생성자
        //ImageView iv_profile;
        TextView tv_id;
        TextView tv_pw;
        TextView tv_userName;

        public CustomViewHolder(@NonNull View itemView) {//26번쨰줄생성자
            super(itemView);//상속받았으니깐 아이디 찾아오기
            //this.iv_profile=itemView.findViewById(R.id.iv_profile);
            this.tv_id=itemView.findViewById(R.id.tv_id);
            this.tv_pw=itemView.findViewById(R.id.tv_pw);
            this.tv_userName=itemView.findViewById(R.id.tv_userName);

        }
    }
}
