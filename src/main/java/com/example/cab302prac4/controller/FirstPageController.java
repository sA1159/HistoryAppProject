package com.example.cab302prac4.controller;

import com.example.cab302prac4.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class FirstPageController {

    @FXML
    private ImageView logoView;

    @FXML
    private VBox mainVBox;  // The entire VBox

    @FXML
    public void initialize() {
        // Load the image dynamically in the controller
        Image logo = new Image("file:Images/THE VAULT LOGO.jpg"); // Adjust path as necessary
        logoView.setImage(logo);

        // Set click event on the entire VBox to navigate to the login page
        mainVBox.setOnMouseClicked(event -> openLoginPage());
    }

    private void openLoginPage() {
        try {
            Stage stage = (Stage) mainVBox.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-page.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
