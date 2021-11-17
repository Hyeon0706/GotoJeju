package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class Categori extends AppCompatActivity {
    ExpandableListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categori);

        ArrayList<Group> DataList = new ArrayList<Group>();
        listView = (ExpandableListView) findViewById(R.id.exListView);
        Group temp = new Group("제주시 동부");
        temp.child.add("제주시내");
        temp.child.add("조천읍");
        temp.child.add("구좌읍");
        temp.child.add("우도면");
        DataList.add(temp);
        temp = new Group("제주시 서부");
        temp.child.add("한경읍");
        temp.child.add("한림읍");
        temp.child.add("애월읍");
        temp.child.add("추자면");
        DataList.add(temp);
        temp = new Group("서귀포시 동부");
        temp.child.add("남원읍");
        temp.child.add("성산읍");
        temp.child.add("표선면");
        DataList.add(temp);
        temp = new Group("서귀포시 서부");
        temp.child.add("서귀포시내");
        temp.child.add("대정읍");
        temp.child.add("안덕면");
        DataList.add(temp);

        ExpandAdapter adapter = new ExpandAdapter(getApplicationContext(),R.layout.group_row,R.layout.child_row,DataList);
        listView.setAdapter(adapter);


        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object vo = (Object)parent.getAdapter().getItem(position);
                String cList;
                Toast.makeText(Categori.this, (String)vo, Toast.LENGTH_SHORT).show();
                if(vo.equals("제주시 서부")){
                    Intent intent = new Intent(getApplicationContext(),WestJeju.class);
                    startActivity(intent);
                }else if(vo.equals("제주시 동부")){
                    Intent intent = new Intent(getApplicationContext(),EastJeju.class);
                    startActivity(intent);
                }else if(vo.equals("서귀포시 서부")){
                    Intent intent = new Intent(getApplicationContext(),WestSeo.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(),EastSeo.class);
                    startActivity(intent);
                }



            }
        });*/
    }


}

