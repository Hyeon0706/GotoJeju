package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class WestJeju extends AppCompatActivity {
    ListView listView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.westjeju);

        ArrayList<String> locale = new ArrayList<>();
        locale.add("한경읍");
        locale.add("한림읍");
        locale.add("애월읍");
        locale.add("추자면");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locale);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

    }
}
