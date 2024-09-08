package com.example.cab302prac4.model;

public class CollectionItem {
    private int id;
    private String title;
    private String type;
    private String author;
    private String description;
    private String location;
    private String date;
    private String link;
    private int collectionid;

    public CollectionItem(String title, String type, String author, String description, String location, String date, String link, int collectionid) {
        this.title = title;
        this.type = type;
        this.author = author;
        this.description = description;
        this.location = location;
        this.date = date;
        this.link = link;
        this.collectionid = collectionid;
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

    public int getCollectionid() {
        return collectionid;
    }

    public void setCollectionid(int id) {
        this.collectionid = id;
    }

    public String getFullName() {
        return title + " " + date + " (" + author + ")";
    }
}