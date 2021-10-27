package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class WestSeo extends AppCompatActivity {
    ListView listView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.westseo);
        ArrayList<String> locale = new ArrayList<>();
        locale.add("서귀포시내");
        locale.add("대정읍");
        locale.add("안덕면");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locale);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

    }
}
