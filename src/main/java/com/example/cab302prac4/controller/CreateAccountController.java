package com.example.cab302prac4.controller;

import com.example.cab302prac4.HelloApplication;
import com.example.cab302prac4.model.SqliteConnection;
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
        Connection connection = SqliteConnection.getInstance();
        PreparedStatement stat = null;

        try {

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


        if (first_name.equals("")||last_name.equals("")||email.equals("")||pass.equals("")||confirm.equals(""))
        {
            errorLabel.setText("ALL FIELDS ARE MANDATORY");
            errorLabel.setTextFill(Color.color(0.75, 0, 0));
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(b -> errorLabel.setText(null));
            pause.play();
        }

        else if(checkPassMatches(pass,confirm) == false)
        {
            errorLabel.setText("Passwords do not match");
            errorLabel.setTextFill(Color.color(0.75, 0, 0));
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(b -> errorLabel.setText(null));
            pause.play();
        }
        else {
            insertData(first_name, last_name, email, pass);
            errorLabel.setText("Account creation success");
            errorLabel.setTextFill(Color.color(0, 0.75, 0));
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(b -> errorLabel.setText(null));
            pause.play();
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
