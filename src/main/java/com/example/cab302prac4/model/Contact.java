package com.example.cab302prac4.model;

public class Contact {
    private int id;
    private String title;
    private String type;
    private String author;
    private String description;
    private String location;
    private String date;
    private String link;
    private int userid;

    public Contact(String title, String type, String author, String description, String location, String date, String link, int userid) {
        this.title = title;
        this.type = type;
        this.author = author;
        this.description = description;
        this.location = location;
        this.date = date;
        this.link = link;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getUserid() {
        return 1;
    }

    public void setUserid(int id) {
        this.userid = 1;
    }

    public String getFullName() {
        return title + " " + date + " (" + author + ")";
    }
}