package com.example.cab302prac4.controller;

import com.example.cab302prac4.HelloApplication;
import com.example.cab302prac4.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class UsersPageController {
    @FXML
    private ListView<User> usersListView;
    private IUserDAO userDAO;
    @FXML
    private TextField firstNameTextFIeld;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private VBox userContainer;
    @FXML
    private Button returnButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label totalUsers;
    @FXML
    private Button profileButton;

    public UsersPageController()
    {

        userDAO = new UserDAO();
    }

    /**
     * Programmatically selects a user in the list view and
     * updates the text fields with the user's information.
     * @param user The user to select.
     */
    private void selectUser(User user) {
        usersListView.getSelectionModel().select(user);
        firstNameTextFIeld.setText(user.getFirstName());
        lastNameTextField.setText(user.getLastName());
        emailTextField.setText(user.getEmail());
    }

    /**
     * Renders a cell in the contacts list view by setting the text to the contact's full name.
     * @param usersListView The list view to render the cell for.
     * @return The rendered cell.
     */
    private ListCell<User> renderCell(ListView<User> usersListView) {
        return new ListCell<>() {
            /**
             * Handles the event when a contact is selected in the list view.
             * @param mouseEvent The event to handle.
             */
            private void onUserSelected(MouseEvent mouseEvent) {
                ListCell<User> clickedCell = (ListCell<User>) mouseEvent.getSource();
                // Get the selected contact from the list view
                User selectedUser = clickedCell.getItem();
                if (selectedUser != null) selectUser(selectedUser);
            }

            /**
             * Updates the item in the cell by setting the text to the contact's full name.
             * @param user The contact to update the cell with.
             * @param empty Whether the cell is empty.
             */
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                // If the cell is empty, set the text to null, otherwise set it to the contact's full name
                if (empty || user == null || user.getFullName() == null) {
                    setText(null);
                    super.setOnMouseClicked(this::onUserSelected);
                } else {
                    setText(user.getFullName());
                }
            }
        };
    }

    /**
     * Synchronizes the contacts list view with the contacts in the database.
     */
    private void syncUSers() {
        usersListView.getItems().clear();
        List<User> users = userDAO.getAllUsers();
        boolean hasContact = !users.isEmpty();
        if (hasContact) {
            usersListView.getItems().addAll(users);
        }
        // Show / hide based on whether there are contacts
        userContainer.setVisible(hasContact);
    }

    @FXML
    public void initialize() {
        usersListView.setCellFactory(this::renderCell);
        syncUSers();
        String totalusers = String.valueOf(userDAO.getTotalUsers());
        totalUsers.setText("Total Users: " + totalusers);
        // Select the first contact and display its information
        usersListView.getSelectionModel().selectFirst();
        User firstUser = usersListView.getSelectionModel().getSelectedItem();
        if (firstUser != null) {
            selectUser(firstUser);
        }
    }


    @FXML
    private void onSearch() {
        // Get the selected contact from the list view
        String search = searchTextField.getText();
        usersListView.getItems().clear();
        List<User> users = userDAO.getAllUsersSearch(search);
        boolean hasUser = !users.isEmpty();
        if (hasUser) {
            usersListView.getItems().addAll(users);
        }
        // Show / hide based on whether there are contacts
        userContainer.setVisible(hasUser);
    }

    @FXML
    protected void onReturnButtonClick() throws IOException {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }

    @FXML
    protected void onProfileButtonClick() throws IOException {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }

}