package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;

public class test1 extends Activity {
    TextView title,pNum,addr;
    ImageView image;
    EditText in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        in = (EditText)findViewById(R.id.editText);
        title = (TextView)findViewById(R.id.tvTitle);
        pNum = (TextView)findViewById(R.id.tvPnum);
        addr = (TextView)findViewById(R.id.tvAddr);
        image = (ImageView)findViewById(R.id.imageView);


        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        StringBuffer buffer = new StringBuffer(); // 데이터를 담을 임시공간 선언;
                        String input = in.getText().toString();
                        String key = URLEncoder.encode(input);

                        String queryUrl="http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchKeyword?"

                                + "serviceKey=" + "Mnd5loDKrv7cw39tujxqlBxVsNrPl2lI5cPZ42QmJTzRIRtOibD66%2BpLD0MRFtCJDoCcIqvzb4V29lnnidqkrA%3D%3D"

                                +"&MobileApp=" + "AppTest"
                                +"&MobileOS=" + "AND"
                                +"&pageNo=" + "1"
                                +"&numOfRows=" + "1"
                                +"&arrange=" + "A"
                                +"&listYN=" + "Y"
                                +"&contentTypeId=" + "12"
                                +"&keyword=" + key;
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
                            singleItem a = null;
                            while (eventType != XmlPullParser.END_DOCUMENT) {  // 문서의 끝일때는 while문 종료
                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        buffer.append("파싱 시작...\n\n");
                                        break;

                                    case XmlPullParser.START_TAG:  // 시작 태그로 데이터를 얻기위함
                                        tag = xpp.getName();//태그 이름 얻어오기

                                        if (tag.equals("item"))
                                            a = new singleItem();
                                        else if (tag.equals("title")) {
                                            xpp.next();
                                            if (a != null) title.setText(xpp.getText());
                                        } else if (tag.equals("tel")) {

                                            xpp.next();
                                            if (a != null) pNum.setText(xpp.getText());
                                        } else if (tag.equals("addr1")) {

                                            xpp.next();
                                            if (a != null) addr.setText(xpp.getText());
                                        }else if (tag.equals("firstimage")) {

                                            xpp.next();
                                            if (a != null) Glide.with(getApplicationContext()).load(xpp.getText()).into(image);
                                        }
                                        break;

                                    case XmlPullParser.TEXT:
                                        break;

                                    case XmlPullParser.END_TAG:
                                        tag = xpp.getName(); //테그 이름 얻어오기

                                        if (tag.equals("item")) {
                                            a = null;
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
}


