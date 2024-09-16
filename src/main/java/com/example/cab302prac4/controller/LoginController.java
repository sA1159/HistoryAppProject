package com.example.cab302prac4.controller;

import com.example.cab302prac4.HelloApplication;
import com.example.cab302prac4.model.User;
import com.example.cab302prac4.model.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.application.Platform;
import javafx.stage.Modality;


public class LoginController {

    @FXML
    private ImageView logoView;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private Hyperlink signupLink;

    @FXML
    private Button cancelButton;

    private UserDAO userDAO;

    public LoginController() {
        // Initialize the UserDAO to interact with the SQLite database
        userDAO = new UserDAO();
    }

    @FXML
    public void initialize() {
        // Load the logo image dynamically, if needed
        Image logo = new Image("file:Images/THE VAULT LOGO.jpg");  // Adjust path as necessary
        logoView.setImage(logo);
    }

    @FXML
    public void loginAction() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Attempt to retrieve the user from the database
        User user = userDAO.getUserByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            // If the credentials match, load the home page
            loadHomePage();
        } else {
            // Display an error message if the login fails
            loginMessageLabel.setText("Invalid email or password.");
        }
    }

    private void loadHomePage() {
        try {
            // Load the home page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cab302prac4/home-view.fxml"));
            Scene homeScene = new Scene(loader.load(),HelloApplication.WIDTH, HelloApplication.HEIGHT);
            homeScene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());

            // Get the current stage and set the new scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(homeScene);
            stage.setTitle("Home - The Vault");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            loginMessageLabel.setText("Error loading the home page.");
        }
    }

    @FXML
    public void forgotPasswordAction() {
        try {
            // Load the forgot password FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cab302prac4/forgot-password.fxml"));
            Scene scene = new Scene(loader.load());

            // Create a new stage (window) for the popup
            Stage stage = new Stage();
            stage.setTitle("Forgot Password");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
            stage.showAndWait(); // Wait until the window is closed

        } catch (IOException e) {
            e.printStackTrace();
            loginMessageLabel.setText("Error loading the forgot password window.");
        }
    }

    @FXML
    public void signupAction() {
        // Logic for the "Sign Up" hyperlink
        loginMessageLabel.setText("Sign up link clicked.");
        // You can navigate to a sign-up page here
    }

    @FXML
    public void cancelAction() {
        // Logic for the "Cancel" button
        Platform.exit();
        // Close the application or reset fields as needed
    }
}
