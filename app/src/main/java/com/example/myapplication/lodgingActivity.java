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
import java.util.ArrayList;

public class lodgingActivity extends Activity {
    static double my;
    static double mx;
    RecyclerView recyclerView;
    ArrayList<lodgingSingleItem> singleItems = new ArrayList<>();
    lodgingAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    TextView g;
    ImageButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lodging);
        Intent intent = getIntent();
        String getmx = intent.getExtras().getString("mapX");    //관광지 기준 5km범위 내의 숙소를 찾아야 하기 때문에
        String getmy = intent.getExtras().getString("mapY");    //x좌표와 y좌표를 가져옴
        String arrange = intent.getExtras().getString("arrange");//선택한 정렬방식 가져옴
        double getMx = Double.parseDouble(getmx);
        double getMy = Double.parseDouble(getmy);
        btn = (ImageButton) findViewById(R.id.back);
        //뒤로가기 버튼
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(lodgingActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        g = findViewById(R.id.guideline);
        recyclerView = findViewById(R.id.searchRecycler);
        refreshLayout= findViewById(R.id.layout_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { //리스트의 갱신을 위해 SwipeRefresh 사용
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
                StringBuffer buffer = new StringBuffer();

                String queryUrl="http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchStay?"

                        + "serviceKey=" + "46rvMskXxMWkqJuYkMxwx%2FZnnKSI0pO%2FOGiu3%2FCVSsUlEdoCAMWXfC%2Bk5DRGlrorCjbvJbb2ZcbPbWh8ZxNY6Q%3D%3D"

                        +"&numOfRows=" + "218"
                        +"&pageNo=" + "1"
                        +"&MobileOS=" + "AND"
                        +"&MobileApp=" + "AppTest"
                        +"&arrange=" + arrange
                        +"&listYN=" + "Y"
                        +"&areaCode=" + "39";

                try {   //xml데이터를 파싱
                    URL url = new URL(queryUrl);
                    InputStream is = url.openStream();

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(new InputStreamReader(is, "UTF-8"));

                    String tag;

                    xpp.next();
                    int eventType = xpp.getEventType();
                    lodgingSingleItem a = null;
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                buffer.append("파싱 시작...\n\n");
                                break;

                            case XmlPullParser.START_TAG:
                                tag = xpp.getName();

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
                                tag = xpp.getName();

                                if (tag.equals("item")) {
                                    if(distance(my,mx,getMy,getMx)<=5){
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
    private static double distance(double lat1, double lon1, double lat2, double lon2) { //거리를 계산후 km값을 반환

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;


        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
