package com.example.myapplication;

public class lodgingSingleItem {
    private String title;   //숙소 이름
    private String addr;    //숙소 주소
    private String pNum;    //숙소 번호
    private String Iurl;    //숙소 이미지
    public lodgingSingleItem() {
    }
    public lodgingSingleItem(String title, String addr, String pNum, String Iurl){
        this.title=title;
        this.addr=addr;
        this.pNum=pNum;
        this.Iurl=Iurl;
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
}
