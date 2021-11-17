package com.example.myapplication;

public class Touristdestination {

    private double mapx;
    private double mapy;

    public Touristdestination(){
        super();
    }


    public Touristdestination(double mapx, double mapy)
    {

        this.mapx = mapx;
        this.mapy = mapy;
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
}

