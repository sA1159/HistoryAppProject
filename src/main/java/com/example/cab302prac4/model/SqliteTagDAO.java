package com.example.cab302prac4.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class SqliteTagDAO implements TagInterface {

    private Connection connection;

    public SqliteTagDAO() {
        connection = SqliteConnection.getInstance();
        createTagTable();
    }

    private void createTagTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS tags ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "tagText VARCHAR NOT NULL,"
                    + "documentid INTEGER NOT NULL,"
                    + "FOREIGN KEY (documentid) REFERENCES contacts(id) ON DELETE CASCADE"

                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTag(Tag tag) {
        try {
            // Prepare the SQL query with two placeholders
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO tags (tagText, documentid) VALUES (?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            // Set the values for the placeholders
            statement.setString(1, tag.getTagText());
            statement.setInt(2, tag.getDocumentId());

            // Execute the update query
            statement.executeUpdate();

            // Retrieve the generated key (tag ID) if applicable
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                tag.setTagId(generatedKeys.getInt(1));  // Set the auto-generated tag ID
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTag(Tag tag) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM tags WHERE id = ?");
            statement.setInt(1, tag.getTagId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Tag getTag(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tags WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String tagText = resultSet.getString("tagText");
                int documentid = resultSet.getInt("documentid");

                Tag tag = new Tag(tagText, documentid);
                tag.setTagId(id);
                return tag;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM tags";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String tagText = resultSet.getString("tagText");
                int documentid = resultSet.getInt("documentid");

                Tag tag = new Tag(tagText, documentid);
                tag.setTagId(id);
                tags.add(tag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tags;    }

}
