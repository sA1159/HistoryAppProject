package com.example.cab302prac4.model;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public interface ITagSystem {
    public void tagButton(HBox box, String tag);
    public void removeTagButton(HBox box, Button button, String buttonTag);
    public void addTags(int documentid);
    public void getTags(int documentid,HBox tagsPane);
    public void removeTags(int documentid);
    public void addTagsTemp(int documentid);
}
