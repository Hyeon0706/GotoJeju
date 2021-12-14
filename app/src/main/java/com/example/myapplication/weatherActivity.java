package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class weatherActivity extends Activity {
    RecyclerView recyclerView;
    ArrayList<weatherSingleitem> singleItems = new ArrayList<>();
    wtAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    TextView g;
    ImageButton btn;
    static String category;
    static String nx, ny;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Intent intent = getIntent();
        String type = intent.getExtras().getString("type");
        String local = intent.getExtras().getString("local");
        if(local.equals("제주시")){
            nx = "53";
            ny = "38";
        }else{
            nx = "52";
            ny = "33";
        }
        TextView tv1 = (TextView) findViewById(R.id.textView);
        String wtType = intent.getExtras().getString("ktype");
        tv1.setText(wtType);
        btn = (ImageButton) findViewById(R.id.back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(weatherActivity.this, test1.class);
                startActivity(intent);
            }
        });

        g = findViewById(R.id.guideline);
        recyclerView = findViewById(R.id.searchRecycler); // 변수연결
        refreshLayout = findViewById(R.id.layout_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
                g.setVisibility(View.GONE);
            }
        });
        adapter = new wtAdapter(singleItems, this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                singleItems.clear();
                StringBuffer buffer = new StringBuffer(); // 데이터를 담을 임시공간 선언
                SimpleDateFormat sd1 = new SimpleDateFormat("YYYYMMdd");
                SimpleDateFormat sd2 = new SimpleDateFormat("hh");
                Date date = new Date();
                String day = sd1.format(date);
                String time = sd2.format(date);
                String queryUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?"

                        + "serviceKey=" + "46rvMskXxMWkqJuYkMxwx%2FZnnKSI0pO%2FOGiu3%2FCVSsUlEdoCAMWXfC%2Bk5DRGlrorCjbvJbb2ZcbPbWh8ZxNY6Q%3D%3D"

                        + "&pageNo=" + "1"
                        + "&numOfRows=" + "288"
                        + "&base_date=" + "20211214"
                        + "&base_time=" + "0500"
                        + "&nx=" + nx
                        + "&ny=" + ny;
                //itemName 태그로 검색을 하기 위함
                try {
                    URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
                    InputStream is = url.openStream(); //url위치로 입력스트림 연결
                    //XmlPullParser 객체 생성
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

                    String tag; // 태그를 통해 구별하기 위한 변수선언
                    xpp.next();
                    int eventType = xpp.getEventType();
                    weatherSingleitem a = null;
                    while (eventType != XmlPullParser.END_DOCUMENT) {  // 문서의 끝일때는 while문 종료
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                buffer.append("파싱 시작...\n\n");
                                break;

                            case XmlPullParser.START_TAG:  // 시작 태그로 데이터를 얻기위함
                                tag = xpp.getName();//태그 이름 얻어오기

                                if (tag.equals("item"))
                                    a = new weatherSingleitem();
                                else if (tag.equals("category")) {
                                    xpp.next();
                                    category = xpp.getText();
                                } else if (tag.equals("fcstTime")) {

                                    xpp.next();
                                    if (a != null) a.setFcstTime("예보 시간 : " + xpp.getText() + "시");
                                } else if (tag.equals("fcstDate")) {

                                    xpp.next();
                                    if (a != null) a.setFcstDate("예보 날짜 : " + xpp.getText());
                                } else if (tag.equals("fcstValue")) {

                                    xpp.next();
                                    if (a != null){
                                        String value = xpp.getText();
                                        if(type.equals("SKY")){
                                            switch(value){
                                                case "1":
                                                    a.setFcstValue("맑음");
                                                    break;
                                                case "3":
                                                    a.setFcstValue("구름많음");
                                                    break;
                                                case "4":
                                                    a.setFcstValue("흐림");
                                                    break;
                                                default:
                                                    a.setFcstValue("오류");
                                                    break;
                                            }
                                        }else if(type.equals("POP")){
                                            a.setFcstValue(value + "%");
                                        }else{
                                            a.setFcstValue(value + "℃");
                                        }
                                    }
                                }
                                break;

                            case XmlPullParser.TEXT:
                                break;

                            case XmlPullParser.END_TAG:
                                tag = xpp.getName(); //테그 이름 얻어오기

                                if (tag.equals("item")) {
                                    if(category.equals(type)){
                                        singleItems.add(a);
                                        a = null;
                                    }
                                }
                                break;
                        }

                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch blocke.printStackTrace();
                }
            }
        }).start();
    }
}
