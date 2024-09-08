package com.example.cab302prac4.model;

public class Collection {
    private int id;
    private String title;;
    private String description;
    private String maker;
    private String date;
    private int userid;

    public Collection(String title, String Maker, String Description, String date, int userid) {
        this.title = title;
        this.maker = Maker;
        this.description = Description;
        this.date = date;
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) { this.maker = maker; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUserid() {
        return 1;
    }

    public void setUserid(int id) {
        this.userid = 1;
    }

    public String getFullName() {
        return title + " " + date + " (" + maker + ")";
    }
}