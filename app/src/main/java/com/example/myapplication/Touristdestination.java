package com.example.myapplication;

public class Touristdestination {

    private double mapx;
    private double mapy;
    private String title;

    public Touristdestination(){
        super();
    }


    public Touristdestination(double mapx, double mapy, String title)
    {
        this.mapx = mapx;
        this.mapy = mapy;
        this.title = title;
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
}

