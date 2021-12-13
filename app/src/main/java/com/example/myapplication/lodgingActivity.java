package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class lodgingActivity extends Activity {
    static double my;
    static double mx;
    RecyclerView recyclerView;
    ArrayList<lodgingSingleItem> singleItems = new ArrayList<>();
    lodgingAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    TextView g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lodging);
        Intent intent = getIntent();
        String getmx = intent.getExtras().getString("mapX");
        String getmy = intent.getExtras().getString("mapY");
        double getMx = Double.parseDouble(getmx);
        double getMy = Double.parseDouble(getmy);

        g = findViewById(R.id.guideline);
        recyclerView = findViewById(R.id.searchRecycler); // 변수연결
        refreshLayout= findViewById(R.id.layout_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
                g.setVisibility(View.GONE);
            }
        });
        adapter = new lodgingAdapter(singleItems, this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                singleItems.clear();
                StringBuffer buffer = new StringBuffer(); // 데이터를 담을 임시공간 선언

                String queryUrl="http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchStay?"

                        + "serviceKey=" + "46rvMskXxMWkqJuYkMxwx%2FZnnKSI0pO%2FOGiu3%2FCVSsUlEdoCAMWXfC%2Bk5DRGlrorCjbvJbb2ZcbPbWh8ZxNY6Q%3D%3D"

                        +"&numOfRows=" + "218"
                        +"&pageNo=" + "1"
                        +"&MobileOS=" + "AND"
                        +"&MobileApp=" + "AppTest"
                        +"&arrange=" + "A"
                        +"&listYN=" + "Y"
                        +"&areaCode=" + "39";
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
                    lodgingSingleItem a = null;
                    while (eventType != XmlPullParser.END_DOCUMENT) {  // 문서의 끝일때는 while문 종료
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                buffer.append("파싱 시작...\n\n");
                                break;

                            case XmlPullParser.START_TAG:  // 시작 태그로 데이터를 얻기위함
                                tag = xpp.getName();//태그 이름 얻어오기

                                if (tag.equals("item"))
                                    a = new lodgingSingleItem();
                                else if (tag.equals("title")) {
                                    xpp.next();
                                    if (a != null) a.setTitle(xpp.getText());
                                } else if (tag.equals("tel")) {

                                    xpp.next();
                                    if (a != null) a.setpNum(xpp.getText());
                                } else if (tag.equals("addr1")) {

                                    xpp.next();
                                    if (a != null) a.setAddr(xpp.getText());
                                } else if (tag.equals("firstimage")) {

                                    xpp.next();
                                    if (a != null) a.setIurl(xpp.getText());
                                } else if (tag.equals("mapy")) {

                                    xpp.next();
                                    my = Double.parseDouble(xpp.getText());

                                } else if (tag.equals("mapx")) {

                                    xpp.next();
                                    mx = Double.parseDouble(xpp.getText());
                                }
                                break;

                            case XmlPullParser.TEXT:
                                break;

                            case XmlPullParser.END_TAG:
                                tag = xpp.getName(); //테그 이름 얻어오기

                                if (tag.equals("item")) {
                                    if(distance(my,mx,getMy,getMx)<=5){ //여기 숫자 좌표에 관광지 좌표 띄우면 됨
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
    private static double distance(double lat1, double lon1, double lat2, double lon2) { //거리 계산

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;


        return (dist);
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
