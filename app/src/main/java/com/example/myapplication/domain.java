package com.example.myapplication;

public class domain {
    private int image;
    private String dname;

    public domain(int image, String dname) {
        this.image = image;
        this.dname = dname;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
