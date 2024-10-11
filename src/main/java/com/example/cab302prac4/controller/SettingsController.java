package com.example.cab302prac4.controller;

import com.example.cab302prac4.HelloApplication;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private ComboBox<String> themeComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button returnButton;

    @FXML
    private Button logoutButton;

    @FXML
    private ImageView logoView;

    @FXML
    public void initialize() {
        // Populate the combo boxes
        languageComboBox.setItems(FXCollections.observableArrayList("English", "French", "Spanish"));
        themeComboBox.setItems(FXCollections.observableArrayList("Light", "Dark"));

        // Optionally set the logo image
        Image logoImage = new Image("file:Images/vaultlogo2.png"); // Adjust path as necessary
        logoView.setImage(logoImage);
    }

    @FXML
    private void onReturnButtonClick(){
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
    @FXML
    protected void onSaveButtonClick() {
        // Implement save logic here
        System.out.println("Save button clicked!");
    }

    @FXML
    private void onLogoutButtonClick() {
        // Implement logout logic here
        System.out.println("Logout button clicked!");
    }
}
