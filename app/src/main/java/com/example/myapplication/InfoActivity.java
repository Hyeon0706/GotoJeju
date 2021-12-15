package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.bumptech.glide.Glide;

public class InfoActivity extends Activity {
    TextView title,pNum,addr,ovview;
    ImageView image;
    ImageButton map;
    ImageButton categori;
    Context context;

    static String mx;
    static String my;
    static String iUrl;
    static String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //지도로 보기 이동 버튼
        map = (ImageButton) findViewById(R.id.moveMap);
        //뒤로가기 버튼
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        //목록으로 보기 이동 버튼
        categori = (ImageButton) findViewById(R.id.moveCategori);
        //뒤로가기 버튼
        categori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, Categori.class);
                startActivity(intent);
            }
        });

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
                                    address = xpp.getText();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);

                builder.setTitle("정렬 방식을 선택하세요!");

                builder.setItems(R.array.selectArrange, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int pos)
                    {
                        Intent i = new Intent(getApplicationContext(),lodgingActivity.class);
                        String[] items = getResources().getStringArray(R.array.selectArrange);
                        i.putExtra("mapX",mx);
                        i.putExtra("mapY",my);
                        if(items[pos].equals("가나다순")){
                            i.putExtra("arrange","A");
                            startActivity(i);
                        }else{
                            i.putExtra("arrange","B");
                            startActivity(i);
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
        Button button5 = (Button) findViewById(R.id.button5);   //이 버튼 누르면 찜하기 페이지로 넘어가면서
        button5.setOnClickListener(new View.OnClickListener() { //콘텐츠 ID넘깁니다!
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);

                builder.setTitle("알고싶은 정보를 선택하세요!");

                builder.setItems(R.array.selectCategory, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int pos)
                    {
                        Intent i = new Intent(getApplicationContext(),weatherActivity.class);
                        String[] items = getResources().getStringArray(R.array.selectCategory);
                        if(items[pos].equals("하늘 상태")){
                            i.putExtra("type","SKY");
                            i.putExtra("ktype","하늘 상태");
                            i.putExtra("local",checkCity(address));
                            startActivity(i);
                        }else if(items[pos].equals("강수 확률")){
                            i.putExtra("type","POP");
                            i.putExtra("ktype","강수 확률");
                            i.putExtra("local",checkCity(address));
                            startActivity(i);
                        }else{
                            i.putExtra("type","TMP");
                            i.putExtra("ktype","기온");
                            i.putExtra("local",checkCity(address));
                            startActivity(i);
                        }

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }
    private static String checkCity(String address){
        String str = address;
        String[] arr = str.split(" ");
        String city = arr[1];
        return city;
    }
}


