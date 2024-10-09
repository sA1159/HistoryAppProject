package com.example.cab302prac4.controller;

import com.example.cab302prac4.HelloApplication;
import com.example.cab302prac4.model.*;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainController {
    @FXML
    private ListView<Contact> contactsListView;
    private IContactDAO contactDAO;
    private IRatingDAO ratingDAO;
    private TagSystem tagSystem;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField typeTextField;
    @FXML
    private TextField authorTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField locationTextField;
    @FXML
    private TextField dateTextField;
    @FXML
    private TextField linkTextField;
    @FXML
    private VBox contactContainer;
    @FXML
    private Button returnButton;
    @FXML
    private TextField tagField;
    @FXML
    private TextField searchTextField;
    @FXML
    private ImageView logoView;
    @FXML
    private HBox tagsPane;
    @FXML
    private Label messageLabel;
    private ITagDAO tagDAO;
    private MessageSystem messageSystem;

    public MainController() {
        contactDAO = new SqliteContactDAO();
        ratingDAO = new SqliteRatingDAO();
        tagDAO = new TagDAO();
        tagSystem = new TagSystem(tagDAO,true);
        messageSystem = new MessageSystem();
    }

    /**
     * Programmatically selects a contact in the list view and
     * updates the text fields with the contact's information.
     * @param contact The contact to select.
     */
    private void selectContact(Contact contact) {
        tagsPane.getChildren().clear();
        tagSystem.getTags(contact.getId(),tagsPane);
        contactsListView.getSelectionModel().select(contact);
        titleTextField.setText(contact.getTitle());
        typeTextField.setText(contact.getType());
        authorTextField.setText(contact.getAuthor());
        descriptionTextField.setText(contact.getDescription());
        locationTextField.setText(contact.getLocation());
        dateTextField.setText(contact.getDate());
        linkTextField.setText(contact.getLink());
        tagField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tagField.getText().isEmpty()) {
                    tagSystem.tagButton(tagsPane, tagField.getText());
                    tagSystem.tags.add(tagField.getText());
                    tagField.clear();
                }
                else{
                    messageSystem.displayMessage("Tag Field Must Not Be Empty",true,messageLabel);
                }
            }
        });
    }

    /**
     * Renders a cell in the contacts list view by setting the text to the contact's full name.
     * @param contactListView The list view to render the cell for.
     * @return The rendered cell.
     */
    private ListCell<Contact> renderCell(ListView<Contact> contactListView) {
        return new ListCell<>() {
            /**
             * Handles the event when a contact is selected in the list view.
             * @param mouseEvent The event to handle.
             */
            private void onContactSelected(MouseEvent mouseEvent) {
                ListCell<Contact> clickedCell = (ListCell<Contact>) mouseEvent.getSource();
                // Get the selected contact from the list view
                Contact selectedContact = clickedCell.getItem();
                if (selectedContact != null) selectContact(selectedContact);
            }

            /**
             * Updates the item in the cell by setting the text to the contact's full name.
             * @param contact The contact to update the cell with.
             * @param empty Whether the cell is empty.
             */
            @Override
            protected void updateItem(Contact contact, boolean empty) {
                super.updateItem(contact, empty);
                // If the cell is empty, set the text to null, otherwise set it to the contact's full name
                if (empty || contact == null || contact.getFullName() == null) {
                    setText(null);
                    super.setOnMouseClicked(this::onContactSelected);
                } else {
                    setText(contact.getFullName());
                }
            }
        };
    }

    /**
     * Synchronizes the contacts list view with the contacts in the database.
     */
    private void syncContacts() {
        contactsListView.getItems().clear();
        List<Contact> contacts = contactDAO.getAllContactsByID(HelloApplication.userid);
        boolean hasContact = !contacts.isEmpty();
        if (hasContact) {
            contactsListView.getItems().addAll(contacts);
        }
        // Show / hide based on whether there are contacts
        contactContainer.setVisible(hasContact);
    }

    @FXML
    public void initialize() {
        messageLabel.setText(null);
        // Load the logo image dynamically, if needed
        javafx.scene.image.Image logo = new Image("file:Images/vaultlogo2.png");  // Adjust path as necessary
        logoView.setImage(logo);
        contactsListView.setCellFactory(this::renderCell);
        syncContacts();
        // Select the first contact and display its information
        contactsListView.getSelectionModel().selectFirst();
        Contact firstContact = contactsListView.getSelectionModel().getSelectedItem();
        if (firstContact != null) {
            selectContact(firstContact);
        }
    }

    @FXML
    private void onEditConfirm() {
        // Get the selected contact from the list view
        Contact selectedContact = contactsListView.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            tagSystem.addTags(selectedContact.getId());
            selectedContact.setTitle(titleTextField.getText());
            selectedContact.setType(typeTextField.getText());
            selectedContact.setAuthor(authorTextField.getText());
            selectedContact.setDescription(descriptionTextField.getText());
            selectedContact.setLocation(locationTextField.getText());
            selectedContact.setDate(dateTextField.getText());
            selectedContact.setLink(linkTextField.getText());
            contactDAO.updateContact(selectedContact);
            messageSystem.displayMessage("Document Successfully Updated",false,messageLabel);
            syncContacts();
        }
    }

    @FXML
    private void onDelete() {
        // Get the selected contact from the list view
        Contact selectedContact = contactsListView.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            contactDAO.deleteContact(selectedContact);
            ratingDAO.removeAllDocumentRatings(selectedContact.getId());
            tagSystem.removeTags(selectedContact.getId());
            syncContacts();
        }
        initialize();
    }

    @FXML
    private void onAdd() {
        // Default values for a new contact
        final String DEFAULT_title = "New Document";
        final String DEFAULT_type = "Document";
        final String DEFAULT_author = "John Doe";
        final String DEFAULT_description = "Abc";
        final String DEFAULT_location = "Abc";
        final String DEFAULT_date = "1998";
        final String DEFAULT_link = "abc";
        Contact newContact = new Contact(DEFAULT_title, DEFAULT_type, DEFAULT_author, DEFAULT_description, DEFAULT_location, DEFAULT_date, DEFAULT_link,HelloApplication.userid);
        tagSystem.addTagsTemp(newContact.getId());
        // Add the new contact to the database
        contactDAO.addContact(newContact);
        syncContacts();
        // Select the new contact in the list view
        // and focus the first name text field
        selectContact(newContact);
        titleTextField.requestFocus();
    }

    @FXML
    private void onCancel() {
        // Find the selected contact
        Contact selectedContact = contactsListView.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            // Since the contact hasn't been modified,
            // we can just re-select it to refresh the text fields
            selectContact(selectedContact);
        }
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
    private void onSearch() {
        // Get the selected contact from the list view
        String search = searchTextField.getText();
        contactsListView.getItems().clear();
        List<Contact> contacts = contactDAO.getAllContactsSearchUserID(search,HelloApplication.userid);
        boolean hasContact = !contacts.isEmpty();
        if (hasContact) {
            contactsListView.getItems().addAll(contacts);
        }
        // Show / hide based on whether there are contacts
        contactContainer.setVisible(hasContact);
    }

    @FXML
    private void Switch() throws IOException {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("search-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }
}