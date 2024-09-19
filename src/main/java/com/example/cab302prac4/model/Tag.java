package com.example.cab302prac4.model;

public class Tag {
    private int id;
    private String tagText;
    private int documentid;

    public Tag(String tagText, int documentid){
        this.tagText = tagText;
        this.documentid = documentid;
    }

    public int getTagId() {
        return id;
    }

    public void setTagId(int id) {
        this.id = id;
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public int getDocumentId() {
        return 1;
    }

    public void setDocumentId(int id) {
        this.documentid = 1;
    }
}
