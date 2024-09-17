package com.example.cab302prac4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
    private TextField mobileEntry;

    @FXML
    private TextField genderEntry;

    @FXML
    private TextField passEntry;

    @FXML
    private TextField confirmPassEntry;

    @FXML
    private Button signUpButton;

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
    public Boolean validatePhoneNum(String phone)
    {
        try {
            int pn = Integer.parseInt(phone);
        }
        catch (NumberFormatException e) {
            return(false);
        }
        int len = phone.length();
        if (len == 10)
        {
            return(true);
        }
        else {
            return(false);
        }
    }

    public Boolean validateGender(String gender)
    {
        if (gender.equalsIgnoreCase("male") | gender.equalsIgnoreCase("female"))
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
    private void onHelloButtonClick() {
        // Handle button click event
        String first_name = firstNameField.getText();
        String last_name = lastNameField.getText();
        String email = emailField.getText();
        String mobile = mobileEntry.getText();
        String gender = genderEntry.getText();
        String pass = passEntry.getText();
        String confirm = confirmPassEntry.getText();


        if (first_name.equals("")||last_name.equals("")||email.equals("")||mobile.equals("")||gender.equals("")||pass.equals("")||confirm.equals(""))
        {
            errorLabel.setText("ALL FIELDS ARE MANDATORY");
        }

        else if(checkPassMatches(pass,confirm) == false)
        {
            errorLabel.setText("Passwords do not match");
        }
        else if(validatePhoneNum(mobile) == false)
        {
            errorLabel.setText("invalid phone number");
        }
        else if (validateGender(gender) == false) {

            errorLabel.setText("invalid gender entry");
        }
        else {
            insertData(first_name, last_name, email, pass);
        }


    }
}
