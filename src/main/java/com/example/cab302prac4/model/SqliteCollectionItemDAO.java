package com.example.cab302prac4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteCollectionItemDAO implements ICollectionItemDAO{
    private Connection connection;

    public SqliteCollectionItemDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
    }

    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS collectionitems ("
                    + "collectionid INTEGER NOT NULL,"
                    + "documentid INTEGER NOT NULL,"
                    + "PRIMARY KEY (collectionid, documentid),"
                    + "FOREIGN KEY(collectionid) references collections(id),"
                    + "FOREIGN KEY(documentid) references contacts(id)"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCollectionItem(int collectionid, int documentid) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO collectionitems (collectionid, documentid) VALUES (?, ?)");
            statement.setInt(1, collectionid);
            statement.setInt(2, documentid);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCollectionItem(CollectionItem collectionItem) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM collectionitems WHERE documentid = ?");
            statement.setInt(1, collectionItem.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CollectionItem> getAllCollectionItems(int currentid) {
        List<CollectionItem> collectionItems = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM collectionitems INNER JOIN contacts on collectionitems.documentid = contacts.id WHERE collectionitems.collectionid = ?");
            statement.setInt(1, currentid);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String type = resultSet.getString("type");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");
                String location = resultSet.getString("location");
                String date = resultSet.getString("date");
                String link = resultSet.getString("link");
                int userid = resultSet.getInt("userid");

                CollectionItem collectionItem = new CollectionItem(title, type, author, description, location, date, link, userid);
                collectionItem.setId(id);
                collectionItems.add(collectionItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return collectionItems;
    }

    @Override
    public List<Contact> getContactsCollectionID(int collectionID) {
        List<Contact> collectionItems = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts INNER JOIN collectionitems on collectionitems.documentid = contacts.id WHERE collectionitems.collectionid = ?");
            statement.setInt(1, collectionID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String type = resultSet.getString("type");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");
                String location = resultSet.getString("location");
                String date = resultSet.getString("date");
                String link = resultSet.getString("link");
                int userid = resultSet.getInt("userid");

                Contact collectionItem = new Contact(title, type, author, description, location, date, link, userid);
                collectionItem.setId(id);
                collectionItems.add(collectionItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return collectionItems;
    }
}
