package com.example.cab302prac4.controller;

import com.example.cab302prac4.model.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;

public class AccountController {

    // Database URL (use the actual path to your existing database)
    private static final String DB_URL = "jdbc:sqlite:contacts.db";

    // Declare the UserDAO object
    private UserDAO userDAO;

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField occupationField;
    @FXML
    private TextField phoneField;

    // Add a PasswordField for updating the password
    @FXML
    private TextField passwordField;

    // Constructor to initialize the UserDAO
    public AccountController() {
        // Initialize the UserDAO to interact with the SQLite database
        userDAO = new UserDAO();
    }
    public void initialize() {
        // Load the logo image dynamically, if needed
        Image logo = new Image("file:Images/THE VAULT LOGO.jpg");  // Adjust path as necessary


    }
    // Handle Save Profile Button Click
    @FXML
    public void handleSaveProfile() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String occupation = occupationField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();  // Get password input

        // SQL query to update the user details
        String sql;

        if (password.isEmpty()) {
            // If password is not being updated
            sql = "UPDATE users SET firstName = ?, lastName = ?, occupation = ?, phoneNumber = ? WHERE email = ?";
        } else {
            // If password is also being updated
            sql = "UPDATE users SET firstName = ?, lastName = ?, occupation = ?, phoneNumber = ?, password = ? WHERE email = ?";
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the query
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, occupation);
            pstmt.setString(4, phone);

            if (password.isEmpty()) {
                // If password is not updated, set the email in the 5th position
                pstmt.setString(5, email);
            } else {
                // If password is updated, set the password and email
                pstmt.setString(5, password);
                pstmt.setString(6, email);
            }

            // Execute the update query
            int rowsUpdated = pstmt.executeUpdate();

            // Check if the update was successful
            if (rowsUpdated > 0) {
                // Show success alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Profile Updated");
                alert.setHeaderText(null);
                alert.setContentText("Profile information updated successfully.");
                alert.showAndWait();
            } else {
                // If no rows were updated, the user does not exist
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Update Failed");
                alert.setHeaderText(null);
                alert.setContentText("No profile found with the provided email.");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            // Handle SQL errors
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update profile. Error: " + e.getMessage());
            alert.showAndWait();
        }
    }


    // Handle Delete Profile Button Click
    @FXML
    public void handleDeleteProfile() {
        String email = emailField.getText(); // Get the email entered by the user

        // SQL Delete Query
        String sql = "DELETE FROM users WHERE email = ?"; // Delete record where email matches

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the email parameter for the query
            pstmt.setString(1, email);

            // Execute the delete query and get the number of rows affected
            int rowsAffected = pstmt.executeUpdate();

            // Clear all fields if deletion was successful
            if (rowsAffected > 0) {
                // Clear the input fields
                firstNameField.clear();
                lastNameField.clear();
                emailField.clear();
                occupationField.clear();
                phoneField.clear();
                passwordField.clear(); // Also clear the password field

                // Show success alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Profile Deleted");
                alert.setHeaderText(null);
                alert.setContentText("Profile deleted successfully.");
                alert.showAndWait();
            } else {
                // Show warning alert if no matching record was found
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Profile Not Found");
                alert.setHeaderText(null);
                alert.setContentText("No profile found with the given email.");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            // Handle any SQL errors
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to delete profile. Error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // Handle Back Button Click
    public void handleBackButtonClick() {
        try {
            // Load the home page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cab302prac4/home-view.fxml"));
            Scene homeScene = new Scene(loader.load());

            Stage stage = (Stage) firstNameField.getScene().getWindow(); // Assuming firstNameField exists in this screen
            stage.setScene(homeScene);
            stage.setTitle("Home - The Vault");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
