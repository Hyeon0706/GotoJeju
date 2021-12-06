package com.example.myapplication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

public class Xmlparser extends Thread {

    public final static String URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?";
    public final static String KEY = "6b%2B3xqOgMamq%2BI5CbJ8EXohcSDrrum2lhplZwGlwJqH1qqXCvPQOmiFptqaI2MAxU3DFu%2Bhl%2FbAXp5ZW3nPYmg%3D%3D";
    public static double mapx;
    public static double mapy;
    public static int radius = 5000;



    public Xmlparser() {
        try {
            apiParserSearch();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    public ArrayList<Touristdestination> apiParserSearch() throws Exception {
        URL url = new URL(getURLParam(null));

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        xpp.setInput(bis, "utf-8");

        String tag = null;
        int event_type = xpp.getEventType();

        ArrayList<Touristdestination> list = new ArrayList<Touristdestination>();

        String mapx = null, mapy= null, title = null, addr = null, tel = null, firstimage = null;
        boolean braclility_mapx = false, braclility_mapy = false, braclility_title = false, braclility_addr = false, braclility_tel = false, braclility_firstimage = false;

        while (event_type != XmlPullParser.END_DOCUMENT) {
            if (event_type == XmlPullParser.START_TAG) {
                tag = xpp.getName();
                if (tag.equals("mapx")){
                    braclility_mapx = true;
                }
                if (tag.equals("mapy")){
                    braclility_mapy = true;
                }
                if (tag.equals("title")){
                    braclility_title = true;
                }
                if (tag.equals("addr1")){
                    braclility_addr = true;
                }
                if (tag.equals("tel")){
                    braclility_tel = true;
                }
                if (tag.equals("firstimage")){
                    braclility_firstimage = true;
                }
            } else if (event_type == XmlPullParser.TEXT) {
                if(braclility_mapx == true){
                    mapx = xpp.getText();
                    braclility_mapx = false;
                }else if(braclility_mapy == true){
                    mapy = xpp.getText();
                    braclility_mapy = false;
                }else if(braclility_title == true){
                    title = xpp.getText();
                    braclility_title = false;
                }else if(braclility_addr == true){
                    addr = xpp.getText();
                    braclility_addr = false;
                }else if(braclility_tel == true){
                    tel = xpp.getText();
                    braclility_tel = false;
                }else if(braclility_firstimage == true){
                    firstimage = xpp.getText();
                    braclility_firstimage = false;
                }



            } else if (event_type == XmlPullParser.END_TAG) {
                tag = xpp.getName();
                if (tag.equals("item")) {
                    Touristdestination entity = new Touristdestination();
                    entity.setMapx(Double.valueOf(mapx));
                    entity.setMapy(Double.valueOf(mapy));
                    entity.setTitle(title);
                    entity.setAddr(addr);
                    entity.setTel(tel);
                    entity.setFirstimage(firstimage);
                    list.add(entity);
                }
            }
            event_type = xpp.next();
        }

        return list;
    }


    private String getURLParam(String search){
        String url = URL + "ServiceKey=" + KEY + "&numOfRows=50&pageNo=1&MobileOS=AND&MobileApp=gotojeju&arrange=A&contentTypeId=12"
                + "&mapX=" + mapx + "&mapY=" + mapy + "&radius=" + radius + "&listYN=Y";

        return url;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new Xmlparser();
    }

}