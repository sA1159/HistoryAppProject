package com.example.cab302prac4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteRatingDAO implements IRatingDAO {
    private Connection connection;

    public SqliteRatingDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
    }


    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS ratings ("
                    + "documentid INTEGER NOT NULL,"
                    + "userid INTEGER NOT NULL,"
                    + "PRIMARY KEY (documentid, userid),"
                    + "FOREIGN KEY(userID) references users(userID),"
                    + "FOREIGN KEY(documentid) references contacts(id)"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addRating(int userid, int documentid) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO ratings (documentid, userid) VALUES (?, ?)");
            statement.setInt(1, documentid);
            statement.setInt(2, userid);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeRating(int userid, int documentid) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM ratings WHERE userid = ? AND documentid = ?");
            statement.setInt(1, userid);
            statement.setInt(2, documentid);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkIfRated(int userid, int documentid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ratings WHERE userid = ? AND documentid = ?");
            statement.setInt(1, userid);
            statement.setInt(2, documentid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            else
            {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getRatingScoreForDocument(int documentid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS score from ratings where documentid = ?");
            statement.setInt(1, documentid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int score = resultSet.getInt("score");
                return score;
            }
            else
            {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void removeAllDocumentRatings(int documentid) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE from ratings where documentid = ?");
            statement.setInt(1, documentid);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getUserTotalRatingScore(int userid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT count(*) AS score from ratings INNER JOIN contacts ON contacts.id = ratings.documentid WHERE contacts.userid = ?");
            statement.setInt(1, userid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int score = resultSet.getInt("score");
                return score;
            }
            else
            {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Contact> getAllRatedDocuments(int currentuserid){
        List<Contact> contacts = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * from ratings INNER JOIN contacts ON contacts.id = ratings.documentid WHERE contacts.userid = ?");
            statement.setInt(1, currentuserid);

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

                // Create a Contact object and populate it with data
                Contact contact = new Contact(title, type, author, description, location, date, link, userid);
                contact.setId(id);
                contacts.add(contact);  // Add the contact to the list
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacts;
    }
}
