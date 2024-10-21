package com.example.cab302prac4.controller;

import com.example.cab302prac4.HelloApplication;
import com.example.cab302prac4.model.*;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Scene;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.io.File;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import static java.awt.SystemColor.desktop;

public class AccountController {

    // Declare the UserDAO object
    private SqliteImageDAO imageDAO;
    private UserDAO userDAO;
    private IRatingDAO ratingDAO;
    private IRatingDAO cratingDAO;

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button returnButton;
    @FXML
    private ImageView logoView;
    @FXML
    private ImageView profileView;
    @FXML
    private Label errorLabel;
    @FXML
    private Button openButton;
    @FXML
    private Label totalscoreLabel;

    final FileChooser fileChooser = new FileChooser();

    /**
     * Constructor to initialize the DAOs used by this controller.
     */
    public AccountController() {
        userDAO = new UserDAO();
        imageDAO = new SqliteImageDAO();
        ratingDAO = new SqliteRatingDAO();
        cratingDAO = new SqliteCollectionRatingDAO();
    }

    /**
     * Initializes the account page with user data, loads the logo, profile image,
     * and calculates the user's total score.
     */
    public void initialize() {
        CalculateTotalScore(HelloApplication.userid);
        errorLabel.setText(" ");
        javafx.scene.image.Image logo = new Image("file:Images/vaultlogo2.png");
        logoView.setImage(logo);
        loadImage();
        User user = userDAO.getUser(HelloApplication.userid);
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());
        passwordField.setText(user.getPassword());
    }

    /**
     * Handles the action of saving the user profile after updating information.
     * It checks for field validations such as matching passwords and valid email address.
     */
    @FXML
    public void handleSaveProfile() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmpassword = confirmPasswordField.getText();

        if (!Objects.equals(password, confirmpassword)) {
            displayError("Passwords do not match");
        } else if (!isValidEmailAddress(email)) {
            displayError("Email is not valid");
        } else if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            displayError("All fields are mandatory");
        } else {
            User user = new User(firstName, lastName, email, password);
            user.setUserID(HelloApplication.userid);
            userDAO.updateUser(user);
            displaySuccess("Account Successfully Updated");
        }
    }

    /**
     * Handles the action of deleting the user profile.
     * (Functionality is not yet implemented)
     */
    @FXML
    public void handleDeleteProfile() {
        // TODO: Implement delete functionality
    }

    /**
     * Handles the action of returning to the home page.
     * It loads the home page FXML when the user clicks the return button.
     */
    public void handleBackButtonClick() {
        try {
            Stage stage = (Stage) returnButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an error message with a red color, visible for 2 seconds.
     * @param text The error message to be displayed.
     */
    public void displayError(String text) {
        errorLabel.setText(text);
        errorLabel.setTextFill(Color.color(0.75, 0, 0));
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(b -> errorLabel.setText(null));
        pause.play();
    }

    /**
     * Displays a success message with a green color, visible for 2 seconds.
     * @param text The success message to be displayed.
     */
    public void displaySuccess(String text) {
        errorLabel.setText(text);
        errorLabel.setTextFill(Color.color(0, 0.75, 0));
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(b -> errorLabel.setText(null));
        pause.play();
    }

    /**
     * Validates the given email address using a regex pattern.
     * @param email The email address to be validated.
     * @return true if the email is valid, false otherwise.
     */
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * Opens a file chooser dialog to select an image file and updates the user's profile image.
     */
    public void onOpen() {
        Stage stage = (Stage) openButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            updateImage(file);
            loadImage();
        }
    }

    /**
     * Loads the user's profile image from the database or sets a default profile image.
     */
    public void loadImage() {
        if (imageDAO.getImage(HelloApplication.userid) == null) {
            javafx.scene.image.Image image = new Image("file:Images/profile.png");
            profileView.setImage(image);
        } else {
            String profile_path = "file:" + imageDAO.getImage(HelloApplication.userid);
            javafx.scene.image.Image image = new Image(profile_path);
            profileView.setImage(image);
        }
    }

    /**
     * Updates the user's profile image in the database.
     * @param file The image file selected by the user.
     */
    public void updateImage(File file) {
        String path = file.getPath();
        if (imageDAO.getImage(HelloApplication.userid) == null) {
            imageDAO.addImage(path, HelloApplication.userid);
        } else {
            imageDAO.updateImage(path, HelloApplication.userid);
        }
    }

    /**
     * Calculates and displays the total score of the user by summing up ratings and collection ratings.
     * @param id The user ID for which the total score is calculated.
     */
    private void CalculateTotalScore(int id) {
        int score = 0;
        score += ratingDAO.getUserTotalRatingScore(id);
        score += cratingDAO.getUserTotalRatingScore(id);
        totalscoreLabel.setText("Total Score: " + score);
    }
}
