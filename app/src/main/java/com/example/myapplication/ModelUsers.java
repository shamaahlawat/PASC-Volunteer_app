package com.example.myapplication;

import android.util.Log;

import java.util.ArrayList;

public class ModelUsers {

    String name, year, dept;
    String domain1, domain2, domain3;
    String github, linkedin;
    ArrayList<String> dom;

    public ModelUsers(String name, String year, String dept) {
        this.name = name;
        this.year = year;
        this.dept = dept;
    }

    public ModelUsers(String name, String year, String dept, String domain1, String domain2, String domain3, String github, String linkedin) {
        this.name = name;
        this.year = year;
        this.dept = dept;
        this.domain1 = domain1;
        this.domain2 = domain2;
        this.domain3 = domain3;
        this.github = github;
        this.linkedin = linkedin;
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

    public String getDomain1() {
        return domain1;
    }

    public void setDomain1(String domain1) {
        this.domain1 = domain1;
    }

    public String getDomain2() {
        return domain2;
    }

    public void setDomain2(String domain2) {
        this.domain2 = domain2;
    }

    public String getDomain3() {
        return domain3;
    }

    public void setDomain3(String domain3) {
        this.domain3 = domain3;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }
}
