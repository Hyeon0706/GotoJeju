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
        String type = intent.getExtras().getString("type");     //원하는 날씨 정보를 출력하기 위해 가져옴
        String local = intent.getExtras().getString("local");   //제주시와 서귀포시로 나누기 위해 가져옴
        if(local.equals("제주시")){    //api에서 경도,위도 좌표를 이용한 날씨 조회를 지원하지 않아서 제주시와 서귀포시로 나눔
            nx = "53";
            ny = "38";
        }else{
            nx = "52";
            ny = "33";
        }
        TextView tv1 = (TextView) findViewById(R.id.textView);
        String wtType = intent.getExtras().getString("ktype");  //원하는 날씨 정보를 출력하기 위해 가져옴
        tv1.setText(wtType);
        btn = (ImageButton) findViewById(R.id.back);
        //뒤로가기 버튼
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(weatherActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        g = findViewById(R.id.guideline);
        recyclerView = findViewById(R.id.searchRecycler);
        refreshLayout = findViewById(R.id.layout_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { //리스트의 갱신을 위해 사용
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
                StringBuffer buffer = new StringBuffer();
                SimpleDateFormat sd1 = new SimpleDateFormat("YYYYMMdd");    //현재 날짜와
                SimpleDateFormat sd2 = new SimpleDateFormat("hh");          //현재 시간을 얻어옴
                Date date = new Date();
                String day = sd1.format(date);
                String time = sd2.format(date);
                int time1 = Integer.parseInt(time);
                int time2 = time1-5;
                String time3 = Integer.toString(time2);
                String queryUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?"

                        + "serviceKey=" + "46rvMskXxMWkqJuYkMxwx%2FZnnKSI0pO%2FOGiu3%2FCVSsUlEdoCAMWXfC%2Bk5DRGlrorCjbvJbb2ZcbPbWh8ZxNY6Q%3D%3D"

                        + "&pageNo=" + "1"
                        + "&numOfRows=" + "288"
                        + "&base_date=" + day
                        + "&base_time=" + time3 + "00"
                        + "&nx=" + nx
                        + "&ny=" + ny;

                try {   //날씨 정보 xml데이터 파싱
                    URL url = new URL(queryUrl);
                    InputStream is = url.openStream();

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(new InputStreamReader(is, "UTF-8"));

                    String tag;
                    xpp.next();
                    int eventType = xpp.getEventType();
                    weatherSingleitem a = null;
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                buffer.append("파싱 시작...\n\n");
                                break;

                            case XmlPullParser.START_TAG:
                                tag = xpp.getName();

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
                                        String value = xpp.getText();   //선택한 기상 정보에 따라 다른 값을 출력
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
                                tag = xpp.getName();

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
