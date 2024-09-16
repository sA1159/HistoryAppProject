package com.example.cab302prac4.controller;

import com.example.cab302prac4.model.User;
import com.example.cab302prac4.model.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ForgotPasswordController {
    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private Label resetPasswordMessageLabel;

    @FXML
    private Button resetPasswordButton;

    @FXML
    private Button cancelButton;

    private UserDAO userDAO;
    public ForgotPasswordController(){
        userDAO = new UserDAO();
    }

    public void forgotPasswordAction(){
        String email = emailTextField.getText().trim();
        String newPassword = newPasswordField.getText();

        // Clear previous messages
        resetPasswordMessageLabel.setVisible(false);
        resetPasswordMessageLabel.setText("");

        // Validate inputs
        if (email.isEmpty() || newPassword.isEmpty()) {
            resetPasswordMessageLabel.setText("Please enter your email and new password.");
            resetPasswordMessageLabel.setVisible(true);
            return;
        }

        // Check if the email exists
        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            resetPasswordMessageLabel.setText("No account associated with this email.");
            resetPasswordMessageLabel.setVisible(true);
            return;
        }

        // Update the user's password
        user.setPassword(newPassword);
        userDAO.updateUser(user);

        resetPasswordMessageLabel.setText("Password reset successful. You can now log in.");
        resetPasswordMessageLabel.setStyle("-fx-text-fill: green;");
        resetPasswordMessageLabel.setVisible(true);

        // Optionally, close the window after successful reset
        Stage stage = (Stage) resetPasswordButton.getScene().getWindow();
        stage.close();
    }

    public void cancelButtonOnAction(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

}
