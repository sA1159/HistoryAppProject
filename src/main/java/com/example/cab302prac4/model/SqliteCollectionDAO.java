package com.example.cab302prac4.model;

import com.example.cab302prac4.HelloApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteCollectionDAO implements ICollectionDAO{

    private Connection connection;

    public SqliteCollectionDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
    }

    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS collections ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "title VARCHAR NOT NULL,"
                    + "maker VARCHAR NOT NULL,"
                    + "description VARCHAR NOT NULL,"
                    + "date VARCHAR NOT NULL,"
                    + "userid INTEGER NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCollection(Collection collection) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO collections (title, maker, description, date, userid) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, collection.getTitle());
            statement.setString(2, collection.getMaker());
            statement.setString(3, collection.getDescription());
            statement.setString(4, collection.getDate());
            statement.setInt(5, collection.getUserid());
            statement.executeUpdate();
            // Set the id of the new contact
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                collection.setId(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCollection(Collection collection) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE collections SET title = ?, maker = ?, description = ?, date = ?, userid = ? WHERE id = ?");
            statement.setString(1, collection.getTitle());
            statement.setString(2, collection.getMaker());
            statement.setString(3, collection.getDescription());
            statement.setString(4, collection.getDate());
            statement.setInt(5, collection.getUserid());
            statement.setInt(6, collection.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCollection(Collection collection) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM collections WHERE id = ?");
            PreparedStatement statement2 = connection.prepareStatement("DELETE FROM collectionitems WHERE collectionid = ?");
            statement.setInt(1, collection.getId());
            statement2.setInt(1, collection.getId());
            statement.executeUpdate();
            statement2.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection getCollection(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM collections WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String maker = resultSet.getString("maker");
                String description = resultSet.getString("description");
                String date = resultSet.getString("date");
                int userid = resultSet.getInt("userid");

                Collection collection = new Collection(title, maker, description, date, userid);
                collection.setId(id);
                return collection;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Collection> getAllCollections() {
        List<Collection> collections = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM collections";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String maker = resultSet.getString("maker");
                String description = resultSet.getString("description");
                String date = resultSet.getString("date");
                int userid = resultSet.getInt("userid");

                Collection collection = new Collection(title, maker, description, date, userid);
                collection.setId(id);
                collections.add(collection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return collections;
    }

    @Override
    public List<Collection> getAllCollectionsByID(int currentuserid) {
        List<Collection> collections = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM collections WHERE userid = ?");
            statement.setInt(1, currentuserid);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String maker = resultSet.getString("maker");
                String description = resultSet.getString("description");
                String date = resultSet.getString("date");
                int userid = resultSet.getInt("userid");

                Collection collection = new Collection(title, maker, description, date, userid);
                collection.setId(id);
                collections.add(collection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return collections;
    }

    @Override
    public List<Collection> getAllCollectionItemsSearch(String search) {
        List<Collection> collections = new ArrayList<>();
        try {
            // Use a PreparedStatement to prevent SQL injection
            String query = "SELECT * FROM collections INNER JOIN ctags ON collections.id = ctags.collectionid WHERE (title LIKE ? OR maker LIKE ? OR description LIKE ? OR date LIKE ? OR tags LIKE ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            // Format the search term with wildcards for partial matching
            String filterSearch = "%" + search + "%";

            // Set the search term for each field
            for (int i = 1; i <= 5; i++) {
                statement.setString(i, filterSearch);
            }

            // Execute the query and iterate through the result set
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String maker = resultSet.getString("maker");
                String description = resultSet.getString("description");
                String date = resultSet.getString("date");;
                int userid = resultSet.getInt("userid");

                // Create a Contact object and populate it with data
                Collection collection = new Collection(title, maker, description, date, userid);
                collection.setId(id);
                collections.add(collection);  // Add the contact to the list
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return collections;  // Return the list of matching contacts
    }

    @Override
    public List<Collection> getAllCollectionItemsSearchByID(String search,int currentuserid) {
        List<Collection> collections = new ArrayList<>();
        try {
            // Use a PreparedStatement to prevent SQL injection
            String query = "SELECT * FROM collections INNER JOIN ctags ON collections.id = ctags.collectionid  WHERE (title LIKE ? OR maker LIKE ? OR description LIKE ? OR date LIKE ? OR tags LIKE ?) AND userid = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            // Format the search term with wildcards for partial matching
            String filterSearch = "%" + search + "%";

            // Set the search term for each field
            for (int i = 1; i <= 5; i++) {
                statement.setString(i, filterSearch);
            }
            statement.setInt(6, currentuserid);

            // Execute the query and iterate through the result set
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String maker = resultSet.getString("maker");
                String description = resultSet.getString("description");
                String date = resultSet.getString("date");;
                int userid = resultSet.getInt("userid");

                // Create a Contact object and populate it with data
                Collection collection = new Collection(title, maker, description, date, userid);
                collection.setId(id);
                collections.add(collection);  // Add the contact to the list
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return collections;  // Return the list of matching contacts
    }
}
