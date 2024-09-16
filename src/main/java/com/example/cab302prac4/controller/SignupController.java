package com.example.cab302prac4.controller;

import com.example.cab302prac4.model.User;
import com.example.cab302prac4.model.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class SignupController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label messageLabel;

    private UserDAO userDAO;

    public SignupController() {
        userDAO = new UserDAO();
    }

    @FXML
    public void signUpAction() {

    }

    public void signupAction(ActionEvent actionEvent) {
    }
}
