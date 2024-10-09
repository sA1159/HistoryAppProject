package com.example.cab302prac4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagDAO implements ITagDAO {
    private Connection connection;

    public TagDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
    }

    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS tags ("
                    + "documentid INTEGER NOT NULL,"
                    + "tags VARCHAR NOT NULL,"
                    + "PRIMARY KEY (documentid, tags),"
                    + "FOREIGN KEY(documentid) references contacts(id)"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTags(List<String> tagsList, int documentid) {
        String tags = String.join(",", tagsList);
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO tags (documentid, tags) VALUES (?, ?)");
            statement.setInt(1, documentid);
            statement.setString(2, tags);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTags(List<String> tagsList, int documentid) {
        String tags = String.join(",", tagsList);
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE tags SET tags = ? WHERE documentid = ?");
            statement.setString(1, tags);
            statement.setInt(2, documentid);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTags(int documentid) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM tags WHERE documentid = ?");
            statement.setInt(1, documentid);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getTags(int documentid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * from tags where documentid = ?");
            statement.setInt(1, documentid);
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
