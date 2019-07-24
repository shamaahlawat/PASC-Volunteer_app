package com.example.myapplication;

import android.util.Log;

import java.util.ArrayList;

public class ModelUsers {

    String name, year, dept;
    String domain1, domain2, domain3;
    ArrayList<String> dom;

    public ModelUsers(String name, String year, String dept) {
        this.name = name;
        this.year = year;
        this.dept = dept;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", year='" + year + '\'' +
                ", dept='" + dept + '\'' +
                '}';
    }

    public void addnew()
    {
        dom = new ArrayList<>();
        dom.add(domain1);
        dom.add(domain2);
        dom.add(domain3);
    }
    public ModelUsers() {
        dom = new ArrayList<String>();
        Log.d("md", "ModelUsers: called");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

}
