package com.example.cab302prac4.controller;

import com.example.cab302prac4.HelloApplication;
import com.example.cab302prac4.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainController {
    @FXML
    private ListView<Contact> contactsListView;
    private IContactDAO contactDAO;

    private TagInterface tagDAO;
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
    public MainController() {
        contactDAO = new SqliteContactDAO();
        tagDAO = new SqliteTagDAO();
    }


    /**
     * Programmatically selects a contact in the list view and
     * updates the text fields with the contact's information.
     * @param contact The contact to select.
     */
    private void selectContact(Contact contact) {
        contactsListView.getSelectionModel().select(contact);
        titleTextField.setText(contact.getTitle());
        typeTextField.setText(contact.getType());
        authorTextField.setText(contact.getAuthor());
        descriptionTextField.setText(contact.getDescription());
        locationTextField.setText(contact.getLocation());
        dateTextField.setText(contact.getDate());
        linkTextField.setText(contact.getLink());
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
        List<Contact> contacts = contactDAO.getAllContacts();
        boolean hasContact = !contacts.isEmpty();
        if (hasContact) {
            contactsListView.getItems().addAll(contacts);
        }
        // Show / hide based on whether there are contacts
        contactContainer.setVisible(hasContact);
    }

    @FXML
    public void initialize() {
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
            selectedContact.setTitle(titleTextField.getText());
            selectedContact.setType(typeTextField.getText());
            selectedContact.setAuthor(authorTextField.getText());
            selectedContact.setDescription(descriptionTextField.getText());
            selectedContact.setLocation(locationTextField.getText());
            selectedContact.setDate(dateTextField.getText());
            selectedContact.setLink(linkTextField.getText());
            contactDAO.updateContact(selectedContact);
            syncContacts();
        }
    }

    @FXML
    private void onDelete() {
        // Get the selected contact from the list view
        Contact selectedContact = contactsListView.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            contactDAO.deleteContact(selectedContact);
            syncContacts();
        }
    }

    @FXML
    private void onAdd() {
        // Retrieve user input from text fields
        String title = titleTextField.getText();
        String type = typeTextField.getText();
        String author = authorTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String date = dateTextField.getText();
        String link = linkTextField.getText();

        // Create a new Contact object with user input
        Contact newContact = new Contact(title, type, author, description, location, date, link, 1);

        // Add the new contact to the database
        contactDAO.addContact(newContact);

        // Get the newly generated contact ID
        int contactId = newContact.getId();

        // Create a combined input string from user input fields
        String combinedInput = title + " " + type + " " + author + " " + description + " " + location + " " + date;

        // Split the combined input into individual tags
        String[] tags = combinedInput.split("\\s+"); // Split by spaces
        for (String tag : tags) {
            Tag newTag = new Tag(tag, contactId); // Use the contactId as the documentId
            tagDAO.addTag(newTag);
        }

        // Update the UI
        syncContacts();
        // Select the new contact in the list view
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
        stage.setScene(scene);
    }
}