package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.bumptech.glide.Glide;

public class test1 extends Activity {
    TextView title,pNum,addr,ovview;
    ImageView image;

    Context context;

    static String mx;
    static String my;
    static String iUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);



        title = (TextView)findViewById(R.id.tvTitle);
        pNum = (TextView)findViewById(R.id.tvPnum);
        addr = (TextView)findViewById(R.id.tvAddr);
        ovview = (TextView)findViewById(R.id.tvOvviwe);
        image = (ImageView)findViewById(R.id.imageView);

        Intent intent = getIntent();

        String conId = intent.getExtras().getString("conId");


        new Thread(new Runnable() {
            @Override
            public void run() {

                StringBuffer buffer = new StringBuffer(); // 데이터를 담을 임시공간 선언;
                String queryUrl="http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?"

                        +"serviceKey=" + "Mnd5loDKrv7cw39tujxqlBxVsNrPl2lI5cPZ42QmJTzRIRtOibD66%2BpLD0MRFtCJDoCcIqvzb4V29lnnidqkrA%3D%3D"
                        +"&numOfRows=" + "10"
                        +"&pageNo=" + "1"
                        +"&MobileOS=" + "AND"
                        +"&MobileApp=" + "AppTest"
                        +"&contentId=" + conId
                        +"&contentTypeId=" + "12"
                        +"&defaultYN=" + "Y"
                        +"&firstImageYN=" + "Y"
                        +"&areacodeYN=" + "Y"
                        +"&addrinfoYN=" + "Y"
                        +"&mapinfoYN=" + "Y"
                        +"&overviewYN=" + "Y";
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
                    while (eventType != XmlPullParser.END_DOCUMENT) {  // 문서의 끝일때는 while문 종료
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                buffer.append("파싱 시작...\n\n");
                                break;

                            case XmlPullParser.START_TAG:  // 시작 태그로 데이터를 얻기위함
                                tag = xpp.getName();//태그 이름 얻어오기

                                if (tag.equals("item")) {
                                } else if (tag.equals("addr1")) {
                                    xpp.next();
                                    addr.setText(xpp.getText());
                                }else if (tag.equals("firstimage")) {
                                    xpp.next();
                                    iUrl = xpp.getText();


                                }else if (tag.equals("mapx")) {

                                    xpp.next();
                                    mx = xpp.getText();
                                }else if (tag.equals("mapy")) {

                                    xpp.next();
                                    my = xpp.getText();
                                } else if (tag.equals("overview")) {
                                    xpp.next();
                                    ovview.setText(xpp.getText());

                                } else if (tag.equals("tel")) {
                                    xpp.next();
                                    pNum.setText(xpp.getText());
                                }else if (tag.equals("title")) {
                                    xpp.next();
                                    title.setText(xpp.getText());


                                }
                                break;

                            case XmlPullParser.TEXT:
                                break;

                            case XmlPullParser.END_TAG:
                                tag = xpp.getName(); //테그 이름 얻어오기

                                if (tag.equals("item")) {
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
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Glide.with(view).load(iUrl).fallback(R.drawable.no_image).into(image);
            }


        });
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),lodgingActivity.class);
                i.putExtra("mapX",mx);
                i.putExtra("mapY",my);
                startActivity(i);
            }
        });
        Button button4 = (Button) findViewById(R.id.button4);   //이 버튼 누르면 찜하기 페이지로 넘어가면서
        button4.setOnClickListener(new View.OnClickListener() { //콘텐츠 ID넘깁니다!
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),UserPage.class);
                i.putExtra("conId",conId);
                startActivity(i);
            }
        });
    }
}


