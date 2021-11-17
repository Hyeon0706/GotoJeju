package com.example.myapplication;
import java.util.ArrayList;
public class Group {
    public ArrayList<String> child;
    public String groupName;
    Group(String name){
        groupName = name;
        child = new ArrayList<String>();
    }
}