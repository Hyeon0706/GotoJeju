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

public class TDListActivity extends Activity {
    static String address;
    RecyclerView recyclerView;
    ArrayList<tdSingleitem> singleItems = new ArrayList<>();
    tdAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tdlist);

        TextView tv1 = (TextView) findViewById(R.id.textview1);

        Intent intent = getIntent(); /*데이터 수신*/

        String name = intent.getExtras().getString("local"); /*String형*/
        tv1.setText(name);

        recyclerView = findViewById(R.id.searchRecycler); // 변수연결
        refreshLayout= findViewById(R.id.layout_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });
        adapter = new tdAdapter(singleItems, this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        singleItems.clear();
                        StringBuffer buffer = new StringBuffer(); // 데이터를 담을 임시공간 선언

                        String queryUrl="http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?"

                                + "serviceKey=" + "46rvMskXxMWkqJuYkMxwx%2FZnnKSI0pO%2FOGiu3%2FCVSsUlEdoCAMWXfC%2Bk5DRGlrorCjbvJbb2ZcbPbWh8ZxNY6Q%3D%3D"

                                +"&pageNo=" + "1"
                                +"&numOfRows=" + "450"
                                +"&MobileOS=" + "AND"
                                +"&MobileApp=" + "AppTest"
                                +"&arrange=" + "A"
                                +"&contentTypeId=" + "12"
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
                            tdSingleitem a = null;
                            while (eventType != XmlPullParser.END_DOCUMENT) {  // 문서의 끝일때는 while문 종료
                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        buffer.append("파싱 시작...\n\n");
                                        break;

                                    case XmlPullParser.START_TAG:  // 시작 태그로 데이터를 얻기위함
                                        tag = xpp.getName();//태그 이름 얻어오기

                                        if (tag.equals("item"))
                                            a = new tdSingleitem();
                                        else if (tag.equals("title")) {
                                            xpp.next();
                                            if (a != null) a.setTitle(xpp.getText());
                                        } else if (tag.equals("tel")) {

                                            xpp.next();
                                            if (a != null) a.setpNum(xpp.getText());
                                        } else if (tag.equals("addr1")) {

                                            xpp.next();
                                            address = xpp.getText();
                                            if (a != null) a.setAddr(xpp.getText());
                                        } else if (tag.equals("firstimage")) {

                                            xpp.next();
                                            if (a != null) a.setIurl(xpp.getText());
                                        } else if (tag.equals("contentid")) {

                                            xpp.next();
                                            if (a != null) a.setConId(xpp.getText());
                                        }
                                        break;

                                    case XmlPullParser.TEXT:
                                        break;

                                    case XmlPullParser.END_TAG:
                                        tag = xpp.getName(); //테그 이름 얻어오기

                                        if (tag.equals("item")) {
                                            if(name.equals("제주시내") && checkCity(address).equals("제주시") && !checkLocal(address).equals("조천읍") && !checkLocal(address).equals("구좌읍") && !checkLocal(address).equals("우도면") && !checkLocal(address).equals("한경면") && !checkLocal(address).equals("한림읍") && !checkLocal(address).equals("애월읍") && !checkLocal(address).equals("추자면")){
                                                singleItems.add(a);
                                                a = null;
                                            }else if(name.equals("서귀포시내") && checkCity(address).equals("서귀포시") && !checkLocal(address).equals("남원읍") && !checkLocal(address).equals("성산읍") && !checkLocal(address).equals("표선면") && !checkLocal(address).equals("대정읍") && !checkLocal(address).equals("안덕면")){
                                                singleItems.add(a);
                                                a = null;
                                            }else if(checkLocal(address).equals(name)){
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
        });
    }
    private static String checkLocal(String address) { //거리 계산
        String str = address;
        String[] arr = str.split(" ");
        String local = arr[2];
        return local;
    }
    private static String checkCity(String address){
        String str = address;
        String[] arr = str.split(" ");
        String city = arr[1];
        return city;
    }
}
