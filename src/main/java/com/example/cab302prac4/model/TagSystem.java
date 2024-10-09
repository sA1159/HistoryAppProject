package com.example.cab302prac4.model;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class TagSystem implements ITagSystem {
    public ITagDAO tagDAO;
    private Image toUse = new Image("file:Images/remove.jpg");
    private boolean edit;
    public List<String> tags;

    public TagSystem(ITagDAO dao, boolean edit){
        tagDAO = dao;
        this.edit = edit;
    }

    //box is the pane where this buttons will be placed
    public void tagButton(HBox box, String tag){
        ImageView closeImg = new ImageView(toUse);
        closeImg.setFitHeight(10);
        closeImg.setFitWidth(10);
        Button result;
        if (edit) {
            result = new Button(tag,closeImg);
        }
        else {
            result = new Button(tag);
        }
        result.setPrefHeight(20);
        result.setContentDisplay(ContentDisplay.RIGHT);

        result.setOnAction(event -> removeTagButton(box,result,tag));
        box.getChildren().add(result);
    }

    public void removeTagButton(HBox box,Button button,String buttonTag){
        if (edit) {
            tags.remove(buttonTag);
            box.getChildren().remove(button);
        }
    }

    public void addTags(int documentid)
    {
        if (tagDAO.getTags(documentid).isEmpty()) {
            tagDAO.addTags(tags, documentid);
        }
        else {
            tagDAO.updateTags(tags,documentid);
        }
    }

    public void getTags(int documentid,HBox tagsPane)
    {
        tags = tagDAO.getTags(documentid);
        if (tags.isEmpty())
        {
            tags = new ArrayList<String>();
        }
        else {
            for (String tag : tags) {
                if (!tag.isEmpty()) {
                    tagButton(tagsPane, tag);
                }
            }
        }
    }

    public void removeTags(int documentid)
    {
        tagDAO.deleteTags(documentid);
    }

    public void addTagsTemp(int documentid)
    {
        List<String> tagsList = new ArrayList<String>();
        tagDAO.addTags(tagsList,documentid);
    }
}
