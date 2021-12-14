package com.example.myapplication;

public class tdSingleitem {
    private String title;   //관광지 이름
    private String addr;    //관광지 주소
    private String pNum;    //관광지 번호
    private String Iurl;    //관광지 이미지 url
    private String conId;   //관광지의 식별 번호
    public tdSingleitem() {
    }
    public tdSingleitem(String title, String addr, String pNum, String Iurl, String conId){
        this.title=title;
        this.addr=addr;
        this.pNum=pNum;
        this.Iurl=Iurl;
        this.conId=conId;
    }




    public String getTitle(){

        return title;
    }
    public void setTitle(String title){

        this.title=title;
    }
    public String getAddr(){

        return addr;
    }
    public void setAddr(String addr){

        this.addr=addr;
    }
    public String getpNum(){

        return pNum;
    }
    public void setpNum(String pNum){

        this.pNum=pNum;
    }
    public String getIurl(){

        return Iurl;
    }
    public void setIurl(String Iurl){

        this.Iurl=Iurl;
    }
    public String getConId(){

        return conId;
    }
    public void setConId(String conId){

        this.conId=conId;
    }
}
