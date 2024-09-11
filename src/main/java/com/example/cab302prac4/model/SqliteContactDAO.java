package com.example.cab302prac4.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteContactDAO implements IContactDAO {
    private Connection connection;

    public SqliteContactDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
    }

    private void insertSampleData() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM contacts";
            clearStatement.execute(clearQuery);
            Statement insertStatement = connection.createStatement();
            String insertQuery = "INSERT INTO contacts (title, type, author, description, location, date, link, userid) VALUES "
                    + "('John Doe Speech', 'Speech', 'John Doe', 'John Doe blah blah', 'USA', '10/10/1998','Johndoe@abc.com',1)";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS contacts ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "title VARCHAR NOT NULL,"
                    + "type VARCHAR NOT NULL,"
                    + "author VARCHAR NOT NULL,"
                    + "description VARCHAR NOT NULL,"
                    + "location VARCHAR NOT NULL,"
                    + "date VARCHAR NOT NULL,"
                    + "link VARCHAR NOT NULL,"
                    + "userid INTEGER NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addContact(Contact contact) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO contacts (title, type, author, description, location, date, link, userid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, contact.getTitle());
            statement.setString(2, contact.getType());
            statement.setString(3, contact.getAuthor());
            statement.setString(4, contact.getDescription());
            statement.setString(5, contact.getLocation());
            statement.setString(6, contact.getDate());
            statement.setString(7, contact.getLink());
            statement.setInt(8, contact.getUserid());
            statement.executeUpdate();
            // Set the id of the new contact
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                contact.setId(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateContact(Contact contact) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE contacts SET title = ?, type = ?, author = ?, description = ?, location = ?, date = ?, link = ?, userid = ? WHERE id = ?");
            statement.setString(1, contact.getTitle());
            statement.setString(2, contact.getType());
            statement.setString(3, contact.getAuthor());
            statement.setString(4, contact.getDescription());
            statement.setString(5, contact.getLocation());
            statement.setString(6, contact.getDate());
            statement.setString(7, contact.getLink());
            statement.setInt(8, contact.getUserid());
            statement.setInt(9, contact.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteContact(Contact contact) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM contacts WHERE id = ?");
            PreparedStatement statement2 = connection.prepareStatement("DELETE FROM collectionitems WHERE documentid = ?");
            statement.setInt(1, contact.getId());
            statement2.setInt(1, contact.getId());
            statement.executeUpdate();
            statement2.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Contact getContact(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String type = resultSet.getString("type");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");
                String location = resultSet.getString("location");
                String date = resultSet.getString("date");
                String link = resultSet.getString("link");
                int userid = resultSet.getInt("userid");

                Contact contact = new Contact(title, type, author, description, location, date, link, userid);
                contact.setId(id);
                return contact;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM contacts";
            ResultSet resultSet = statement.executeQuery(query);
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

                Contact contact = new Contact(title, type, author, description, location, date, link, userid);
                contact.setId(id);
                contacts.add(contact);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacts;
    }

    @Override
    public List<Contact> getAllContactsSearch(String search) {
        List<Contact> contacts = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts WHERE title LIKE ?");
            statement.setString(1, '%' + search + '%');
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
                Contact contact = new Contact(title, type, author, description, location, date, link, userid);
                contact.setId(id);
                contacts.add(contact);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacts;
    }


}