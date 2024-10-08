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
import javafx.stage.Window;
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
    // Add a PasswordField for updating the password
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

    // Constructor to initialize the UserDAO
    public AccountController() {
        // Initialize the UserDAO to interact with the SQLite database
        userDAO = new UserDAO();
        imageDAO = new SqliteImageDAO();
        ratingDAO = new SqliteRatingDAO();
        cratingDAO = new SqliteCollectionRatingDAO();
    }

    public void initialize() {
        CalculateTotalScore(HelloApplication.userid);
        errorLabel.setText(" ");
        // Load the logo image dynamically, if needed
        javafx.scene.image.Image logo = new Image("file:Images/vaultlogo2.png");  // Adjust path as necessary
        logoView.setImage(logo);
        loadImage();
        User user = userDAO.getUser(HelloApplication.userid);
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());
        passwordField.setText(user.getPassword());
    }

    // Handle Save Profile Button Click
    @FXML
    public void handleSaveProfile() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmpassword = confirmPasswordField.getText();

        if (!Objects.equals(password, confirmpassword))
        {
            displayError("Passwords do not match");
        }
        else if (!isValidEmailAddress(email))
        {
            displayError("Email is not valid");
        }
        else if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty())
        {
            displayError("All fields are mandatory");
        }
        else {
            User user = new User(firstName,lastName,email,password);
            user.setUserID(HelloApplication.userid);
            userDAO.updateUser(user);
            displaySuccess("Account Successfully Updated");
        }
    }


    // Handle Delete Profile Button Click
    @FXML
    public void handleDeleteProfile() {

    }

    // Handle Back Button Click
    public void handleBackButtonClick() {
        try {
            // Load the home page FXML
            Stage stage = (Stage) returnButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayError(String text)
    {
        errorLabel.setText(text);
        errorLabel.setTextFill(Color.color(0.75, 0, 0));
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(b -> errorLabel.setText(null));
        pause.play();
    }

    public void displaySuccess(String text)
    {
        errorLabel.setText(text);
        errorLabel.setTextFill(Color.color(0, 0.75, 0));
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(b -> errorLabel.setText(null));
        pause.play();
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public void onOpen()
    {
        Stage stage = (Stage) openButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            updateImage(file);
            loadImage();
        }
    }

    public void loadImage()
    {
        if (imageDAO.getImage(HelloApplication.userid) == null)
        {
            javafx.scene.image.Image image = new Image("file:Images/profile.png");
            profileView.setImage(image);
        }
        else
        {
            String profile_path = "file:";
            profile_path += imageDAO.getImage(HelloApplication.userid);
            javafx.scene.image.Image image = new Image(profile_path);
            profileView.setImage(image);
        }
    }

    public void updateImage(File file)
    {
        String path = file.getPath();
        if (imageDAO.getImage(HelloApplication.userid) == null)
        {
            imageDAO.addImage(path,HelloApplication.userid);
        }
        else
        {
            imageDAO.updateImage(path,HelloApplication.userid);
        }
    }

    private void CalculateTotalScore(int id)
    {
        int score = 0;
        score += ratingDAO.getUserTotalRatingScore(id);
        score += cratingDAO.getUserTotalRatingScore(id);
        totalscoreLabel.setText("Total Score: " + score);
    }

}
