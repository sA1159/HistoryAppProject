package com.example.cab302prac4.model;

import java.sql.*;

public class SqliteImageDAO {
    private Connection connection;

    public SqliteImageDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
    }

    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS images ("
                    + "userid INTEGER NOT NULL,"
                    + "imagepath VARCHAR NOT NULL,"
                    + "PRIMARY KEY (userid),"
                    + "FOREIGN KEY(userID) references users(userID)"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addImage(String path, int id) {
        String sql = "INSERT INTO images (userid, imagepath) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, path);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getImage(int userID) {
        String sql = "SELECT * FROM images WHERE userID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                String path = resultSet.getString("imagepath");
                return path;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateImage(String path, int id) {
        String sql = "UPDATE images SET imagepath = ? WHERE userID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, path);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
