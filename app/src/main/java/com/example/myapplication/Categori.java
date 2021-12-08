package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;


public class Categori extends AppCompatActivity {
    private ExpandableListView explistView;
    private ExpandAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private ImageButton back;
    private ImageButton map;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categori);

        back = findViewById(R.id.back);
        map = findViewById(R.id.moveMap);
        //뒤로가기 버튼
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Categori.this, MainActivity.class);
                startActivity(intent);
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Categori.this, MapActivity.class);
                startActivity(intent);
            }
        });



        explistView = (ExpandableListView)findViewById(R.id.exListView);

        ChildListData();

        listAdapter = new ExpandAdapter(this,listDataHeader,listDataChild);

        explistView.setAdapter(listAdapter);

        // 차일드 뷰를 눌렀을 경우 이벤트 발생
        explistView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), TDListActivity.class);
                intent.putExtra("local",listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                startActivity(intent);
                return false;
            }
        });

    }
    private void ChildListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // 그룹 생성
        listDataHeader.add("제주시 동부");
        listDataHeader.add("제주시 서부");
        listDataHeader.add("서귀포시 동부");
        listDataHeader.add("서귀포시 서부");

        // 그룹 내 차일드 뷰 생성
        List<String> eastJeju = new ArrayList<String>();
        eastJeju.add("제주시내");
        eastJeju.add("조천읍");
        eastJeju.add("구좌읍");
        eastJeju.add("우도면");

        List<String> westJeju = new ArrayList<String>();
        westJeju.add("한경면");
        westJeju.add("한림읍");
        westJeju.add("애월읍");
        westJeju.add("추자면");

        List<String> eastSeo = new ArrayList<String>();
        eastSeo.add("남원읍");
        eastSeo.add("성산읍");
        eastSeo.add("표선면");

        List<String> westSeo = new ArrayList<String>();
        westSeo.add("서귀포시내");
        westSeo.add("대정읍");
        westSeo.add("안덕면");

        //데이터 적용.
        listDataChild.put(listDataHeader.get(0), eastJeju);
        listDataChild.put(listDataHeader.get(1), westJeju);
        listDataChild.put(listDataHeader.get(2), eastSeo);
        listDataChild.put(listDataHeader.get(3), westSeo);
    }
}

