package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EastSeo extends AppCompatActivity {
    ListView listView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eastseo);

        ArrayList<String> locale = new ArrayList<>();
        locale.add("남원읍");
        locale.add("성산읍");
        locale.add("표선면");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locale);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

    }
}
