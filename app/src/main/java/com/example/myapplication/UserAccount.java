package com.example.myapplication;

//사용자 계정정보모델 클라스
public class UserAccount {
    private String idToken; //firebase uid (고유 토큰정보)
    private String emailId; // 이메일 아이디
    private String password;// 페스워드
    private String name; //이름
    private String phoneNum; //휴대폰번호


    public UserAccount(){ } //파이어베이스 에서는 빈생성자만들어야함





    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getPhoneNum() { return phoneNum; }

    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
