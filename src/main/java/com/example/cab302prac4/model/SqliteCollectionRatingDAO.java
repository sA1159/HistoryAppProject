package com.example.cab302prac4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class SqliteCollectionRatingDAO implements IRatingDAO{
    private Connection connection;

    public SqliteCollectionRatingDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
    }


    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS cratings ("
                    + "collectionid INTEGER NOT NULL,"
                    + "userid INTEGER NOT NULL,"
                    + "PRIMARY KEY (collectionid, userid),"
                    + "FOREIGN KEY(userID) references users(userID),"
                    + "FOREIGN KEY(collectionid) references collections(id)"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addRating(int userid, int collectionid) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO cratings (collectionid, userid) VALUES (?, ?)");
            statement.setInt(1, collectionid);
            statement.setInt(2, userid);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeRating(int userid, int collectionid) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM cratings WHERE userid = ? AND collectionid = ?");
            statement.setInt(1, userid);
            statement.setInt(2, collectionid);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkIfRated(int userid, int collectionid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM cratings WHERE userid = ? AND collectionid = ?");
            statement.setInt(1, userid);
            statement.setInt(2, collectionid);
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
    public int getRatingScoreForDocument(int collectionid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS score from cratings where collectionid = ?");
            statement.setInt(1, collectionid);
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
    public void removeAllDocumentRatings(int collectionid) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE from cratings where collectionid = ?");
            statement.setInt(1, collectionid);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getUserTotalRatingScore(int userid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT count(*) AS SCORE from cratings INNER JOIN collections ON collections.id = cratings.collectionid WHERE collections.userid = ?");
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

    @Override
    public List<Contact> getAllRatedDocuments(int currentuserid) {
        return List.of();
    }
}
