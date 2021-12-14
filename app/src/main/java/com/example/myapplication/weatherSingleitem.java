package com.example.myapplication;

public class weatherSingleitem {
    private String fcstDate;    //예보 날짜
    private String fcstTime;    //예보 시간
    private String category;    //예보 타입
    private String fcstValue;   //결과값
    public weatherSingleitem() {
    }
    public weatherSingleitem(String fcstDate, String fcstTime, String category, String fcstValue){
        this.fcstDate=fcstDate;
        this.fcstTime=fcstTime;
        this.category=category;
        this.fcstValue=fcstValue;
    }




    public String getFcstDate(){

        return fcstDate;
    }
    public void setFcstDate(String fcstDate){

        this.fcstDate=fcstDate;
    }
    public String getFcstTime(){

        return fcstTime;
    }
    public void setFcstTime(String fcstTime){

        this.fcstTime=fcstTime;
    }
    public String getCategory(){

        return category;
    }
    public void setCategory(String category){

        this.category=category;
    }
    public String getFcstValue(){

        return fcstValue;
    }
    public void setFcstValue(String fcstValue){

        this.fcstValue=fcstValue;
    }
}