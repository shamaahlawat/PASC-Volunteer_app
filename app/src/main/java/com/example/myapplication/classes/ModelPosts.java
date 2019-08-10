package com.example.myapplication.classes;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

@IgnoreExtraProperties
public class ModelPosts {
    String title, description, date, time, type, ownerOfPost;
    String id;
    Timestamp TimeStamp;


    public ModelPosts(String title, String description, String date, String time, String type, String ownerOfPost, String id) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.type = type;
        this.ownerOfPost = ownerOfPost;
        this.id = id;
        
    }

    public ModelPosts(String title, String description, String date, String time) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getOwnerOfPost() {
        return ownerOfPost;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public  String getTime() {
        return  time;
    }

    public Timestamp getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        TimeStamp = timeStamp;
    }
}
