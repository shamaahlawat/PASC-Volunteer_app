package com.example.myapplication;

public class ModelUsers {

    String name, year, dept;

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

    public ModelUsers() {

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
