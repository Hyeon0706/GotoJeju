package com.example.myapplication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

public class Xmlparser extends Thread {

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

        String mapx = null, mapy= null;
        boolean braclility_mapx = false, braclility_mapy = false;

        while (event_type != XmlPullParser.END_DOCUMENT) {
            if (event_type == XmlPullParser.START_TAG) {
                tag = xpp.getName();
                if (tag.equals("mapx")){
                    braclility_mapx = true;
                }
                if (tag.equals("mapy")){
                    braclility_mapy = true;
                }

            } else if (event_type == XmlPullParser.TEXT) {
                if(braclility_mapx == true){
                    mapx = xpp.getText();
                    braclility_mapx = false;
                }else if(braclility_mapy == true){
                    mapy = xpp.getText();
                    braclility_mapy = false;
                }

            } else if (event_type == XmlPullParser.END_TAG) {
                tag = xpp.getName();
                if (tag.equals("item")) {
                    Touristdestination entity = new Touristdestination();
                    entity.setMapx(Double.valueOf(mapx));
                    entity.setMapy(Double.valueOf(mapy));



                    list.add(entity);
                }
            }
            event_type = xpp.next();
        }

        return list;
    }


    private String getURLParam(String search){
        String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?serviceKey=6b%2B3xqOgMamq%2BI5CbJ8EXohcSDrrum2lhplZwGlwJqH1qqXCvPQOmiFptqaI2MAxU3DFu%2Bhl%2FbAXp5ZW3nPYmg%3D%3D&numOfRows=50&MobileApp=Tourist&MobileOS=AND&arrange=B&contentTypeId=12&areaCode=39&listYN=Y";

        return url;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new Xmlparser();
    }

}