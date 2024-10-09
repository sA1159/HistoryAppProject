package com.example.cab302prac4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CTagDAO implements ITagDAO{
    private Connection connection;

    public CTagDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
    }

    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS ctags ("
                    + "collectionid INTEGER NOT NULL,"
                    + "tags VARCHAR NOT NULL,"
                    + "PRIMARY KEY (collectionid, tags),"
                    + "FOREIGN KEY(collectionid) references collections(id)"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTags(List<String> tagsList, int collectionid) {
        String tags = String.join(",", tagsList);
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO ctags (collectionid, tags) VALUES (?, ?)");
            statement.setInt(1, collectionid);
            statement.setString(2, tags);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTags(List<String> tagsList, int collectionid) {
        String tags = String.join(",", tagsList);
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE ctags SET tags = ? WHERE collectionid = ?");
            statement.setString(1, tags);
            statement.setInt(2, collectionid);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTags(int collectionid) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM ctags WHERE collectionid = ?");
            statement.setInt(1, collectionid);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getTags(int collectionid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * from ctags where collectionid = ?");
            statement.setInt(1, collectionid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String tags = resultSet.getString("tags");
                return new ArrayList<String>(Arrays.asList(tags.split(",")));
            }
            else
            {
                return List.of();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
