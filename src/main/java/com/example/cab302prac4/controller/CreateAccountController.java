package com.example.cab302prac4.controller;

import com.example.cab302prac4.HelloApplication;
import com.example.cab302prac4.model.User;
import com.example.cab302prac4.model.UserDAO;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.sql.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class CreateAccountController {
    private static final String Database_Link = "jdbc:sqlite:contacts.db";


    @FXML
    private AnchorPane goldBar;
    @FXML
    private AnchorPane paneForInputs;
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private ImageView photo;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passEntry;

    @FXML
    private PasswordField confirmPassEntry;

    @FXML
    private Button signUpButton;

    @FXML
    private Button returnButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Label caLabel;
    public boolean checkPassMatches(String pass, String confirm)
    {
        Boolean retVal = false;
        if (pass.equals(confirm))
        {
            return(true);
        }
        else {
            return(false);
        }
    }

    private void insertData(String name, String lastName, String email, String password) {
        Connection connection = null;
        PreparedStatement stat = null;

        try {
            // Connect to the database
            connection = DriverManager.getConnection(Database_Link);

            // SQL Insert Statement
            String sq_command = "INSERT INTO users (firstName, lastName, email, password) VALUES (?, ?, ?, ?)";

            // Create PreparedStatement
            stat = connection.prepareStatement(sq_command);
            stat.setString(1, name);
            stat.setString(2, lastName);
            stat.setString(3, email);
            stat.setString(4, password);




            // Execute the statement
            stat.executeUpdate();

            System.out.println("Data inserted successfully.");
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (stat != null) stat.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                errorLabel.setText("Isssue with SQL please contact developer");
            }
        }
    }
    @FXML
    private void onSignUpButtonClick() {
        // Handle button click event
        String first_name = firstNameField.getText();
        String last_name = lastNameField.getText();
        String email = emailField.getText();
        String pass = passEntry.getText();
        String confirm = confirmPassEntry.getText();

        if (first_name.equals("") || last_name.equals("") || email.equals("") || pass.equals("") || confirm.equals("")) {
            errorLabel.setText("ALL FIELDS ARE MANDATORY");
            errorLabel.setTextFill(Color.color(0.75, 0, 0));
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(b -> errorLabel.setText(null));
            pause.play();
        } else if (!checkPassMatches(pass, confirm)) {
            errorLabel.setText("Passwords do not match");
            errorLabel.setTextFill(Color.color(0.75, 0, 0));
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(b -> errorLabel.setText(null));
            pause.play();
        } else {
            // Insert data into the database
            insertData(first_name, last_name, email, pass);
            errorLabel.setText("Account creation success");
            errorLabel.setTextFill(Color.color(0, 0.75, 0));
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(b -> errorLabel.setText(null));
            pause.play();

            // After creating the account, immediately log the user in
            loginUser(email, pass);  // New method to log the user in
        }
    }
    // Method to log the user in and load the home page
    private void loginUser(String email, String password) {
        try {
            // Use the login method from your LoginController
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserByEmail(email);

            if (user != null && user.getPassword().equals(password)) {
                // If credentials match, load the home page
                HelloApplication.userid = user.getUserID();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cab302prac4/home-view.fxml"));
                Scene homeScene = new Scene(loader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
                homeScene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());

                Stage stage = (Stage) signUpButton.getScene().getWindow(); // Get current stage
                stage.setScene(homeScene);
                stage.setTitle("Home - The Vault");
                stage.show();
            } else {
                errorLabel.setText("Login failed after signup.");
                errorLabel.setTextFill(Color.color(0.75, 0, 0));
            }
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error loading the home page.");
        }
    }

    @FXML
    protected void onReturnButtonClick() throws IOException {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }
}
