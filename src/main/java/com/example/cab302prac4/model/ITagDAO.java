package com.example.cab302prac4.model;

import java.util.List;

public interface ITagDAO {
    public void addTags(List<String> tagsList, int documentid);
    public void updateTags(List<String> tagsList, int documentid);
    public void deleteTags(int documentid);
    public List<String> getTags(int documentid);
}
