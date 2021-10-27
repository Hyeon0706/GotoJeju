package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class Categori extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categori);

        ArrayList<String> locale = new ArrayList<>();
        locale.add("제주시 서부");
        locale.add("제주시 동부");
        locale.add("서귀포시 서부");
        locale.add("서귀포시 동부");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,locale);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });
    }


}

