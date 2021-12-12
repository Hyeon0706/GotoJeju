package com.example.myapplication;

public class Touristdestination {

    private double mapx; // 경도 longitude
    private double mapy; // 위도 latitude
    private String title; // 제목
    private String addr; // 주소
    private String tel; // 전화번호
    private String firstimage; // 이미지
    private String contentid; //

    public Touristdestination(){
        super();
    }


    public Touristdestination(double mapx, double mapy, String title,  String addr, String tel, String firstimage, String contentid)
    {
        this.mapx = mapx;
        this.mapy = mapy;
        this.title = title;
        this.addr = addr;
        this.tel = tel;
        this.firstimage = firstimage;
        this.contentid = contentid;
    }

    public double getMapx() {
        return mapx;
    }
    public void setMapx(double mapx) {
        this.mapx = mapx;
    }

    public double getMapy() {
        return mapy;
    }
    public void setMapy(double mapy) {
        this.mapy = mapy;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddr() {
        return addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFirstimage() {
        return firstimage;
    }
    public void setFirstimage(String firstimage) {
        this.firstimage = firstimage;
    }

    public String getcontentid() {
        return contentid;
    }
    public void setcontentid(String contentid) {
        this.contentid = contentid;
    }
}

