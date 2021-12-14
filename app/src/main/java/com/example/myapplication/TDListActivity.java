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

public class TDListActivity extends Activity {
    static String address;
    RecyclerView recyclerView;
    ArrayList<tdSingleitem> singleItems = new ArrayList<>();
    tdAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    TextView g;
    ImageButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tdlist);

        TextView tv1 = (TextView) findViewById(R.id.textView);
        Intent intent = getIntent();
        String name = intent.getExtras().getString("local");    //선택한 지역의 관광지만 출력해야 하기 때문에 local 가져옴
        String arrange = intent.getExtras().getString("arrange");//원하는 정렬방식을 가져옴
        tv1.setText(name);
        btn = (ImageButton) findViewById(R.id.back);
        //뒤로가기 버튼
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TDListActivity.this, Categori.class);
                startActivity(intent);
            }
        });

        g = findViewById(R.id.guideline);
        recyclerView = findViewById(R.id.searchRecycler);
        refreshLayout= findViewById(R.id.layout_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { //리스트의 갱신을 위해 사용
            @Override
            public void onRefresh() {

                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
                g.setVisibility(View.GONE);
            }
        });
        adapter = new tdAdapter(singleItems, this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                singleItems.clear();
                StringBuffer buffer = new StringBuffer();

                String queryUrl="http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?"

                        + "serviceKey=" + "46rvMskXxMWkqJuYkMxwx%2FZnnKSI0pO%2FOGiu3%2FCVSsUlEdoCAMWXfC%2Bk5DRGlrorCjbvJbb2ZcbPbWh8ZxNY6Q%3D%3D"

                        +"&pageNo=" + "1"
                        +"&numOfRows=" + "450"
                        +"&MobileOS=" + "AND"
                        +"&MobileApp=" + "AppTest"
                        +"&arrange=" + arrange
                        +"&contentTypeId=" + "12"
                        +"&listYN=" + "Y"
                        +"&areaCode=" + "39";

                try {   //관광지 정보 xml 데이터를 파싱
                    URL url = new URL(queryUrl);
                    InputStream is = url.openStream();

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(new InputStreamReader(is, "UTF-8"));

                    String tag;

                    xpp.next();
                    int eventType = xpp.getEventType();
                    tdSingleitem a = null;
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                buffer.append("파싱 시작...\n\n");
                                break;

                            case XmlPullParser.START_TAG: 
                                tag = xpp.getName();

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
                                    address = xpp.getText();    //목록에서 선택한 지역의 관광지만 표시해야 하기 때문에 주소를 저장
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
                                tag = xpp.getName();

                                if (tag.equals("item")) {   //목록에서 선택한 주소에 해당하는 관광지만 리스트에 추가 함으로써 관광지 출력
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
    private static String checkLocal(String address) {  //주소에서 위치를 알아내기 위해 문자열을 자름
        String str = address;
        String[] arr = str.split(" ");
        String local = arr[2];
        return local;
    }
    private static String checkCity(String address){    //주소에서 위치를 알아내기 위해 문자열을 자름
        String str = address;
        String[] arr = str.split(" ");
        String city = arr[1];
        return city;
    }
}
