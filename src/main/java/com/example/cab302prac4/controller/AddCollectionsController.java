package com.example.cab302prac4.controller;

import com.example.cab302prac4.HelloApplication;
import com.example.cab302prac4.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AddCollectionsController {

    @FXML
    private ListView<Contact> contactsListView;
    private IContactDAO contactDAO;
    private ICollectionItemDAO collectionItemDAO;

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
    private Hyperlink linkTextField;
    @FXML
    private VBox contactContainer;
    @FXML
    private Button returnButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;

    /**
     * Constructor to initialize DAO objects for contact and collection item interactions.
     */
    public AddCollectionsController() {
        contactDAO = new SqliteContactDAO();
        collectionItemDAO = new SqliteCollectionItemDAO();
    }

    /**
     * Selects a contact from the list and updates the fields in the form with the contact's details.
     *
     * @param contact The contact to be selected.
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
     * Renders the contact cell in the list view with the full name of the contact.
     *
     * @param contactListView The list view to render.
     * @return A ListCell object with the contact's name.
     */
    private ListCell<Contact> renderCell(ListView<Contact> contactListView) {
        return new ListCell<>() {
            /**
             * Handles the mouse click event when a contact is selected.
             *
             * @param mouseEvent The event triggered by selecting a contact.
             */
            private void onContactSelected(MouseEvent mouseEvent) {
                ListCell<Contact> clickedCell = (ListCell<Contact>) mouseEvent.getSource();
                Contact selectedContact = clickedCell.getItem();
                if (selectedContact != null) selectContact(selectedContact);
            }

            /**
             * Updates the cell with the contact's full name or clears the cell if it's empty.
             *
             * @param contact The contact to update the cell with.
             * @param empty Whether the cell is empty.
             */
            @Override
            protected void updateItem(Contact contact, boolean empty) {
                super.updateItem(contact, empty);
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
     * Synchronizes the contacts list view with the contacts in the database, excluding already
     * selected contacts in the current collection.
     */
    private void syncContacts() {
        contactsListView.getItems().clear();
        List<Contact> contacts = contactDAO.getAllContacts();
        List<Contact> contactscollection = collectionItemDAO.getContactsCollectionID(HelloApplication.currentcollectionid);
        List<Integer> contactscollectionsids = new ArrayList<Integer>();
        List<Contact> filtercontacts = new ArrayList<Contact>();
        for (Contact contact : contactscollection) {
            contactscollectionsids.add(contact.getId());
        }
        for (Contact contact : contacts) {
            int id = contact.getId();
            if (!contactscollectionsids.contains(id)) {
                filtercontacts.add(contact);
            }
        }
        boolean hasContact = !filtercontacts.isEmpty();
        if (hasContact) {
            contactsListView.getItems().addAll(filtercontacts);
        }
        contactContainer.setVisible(hasContact);
    }

    /**
     * Initializes the controller, rendering contacts in the list view and selecting the first contact.
     */
    @FXML
    public void initialize() {
        contactsListView.setCellFactory(this::renderCell);
        syncContacts();
        contactsListView.getSelectionModel().selectFirst();
        Contact firstContact = contactsListView.getSelectionModel().getSelectedItem();
        if (firstContact != null) {
            selectContact(firstContact);
        }
    }

    /**
     * Searches the contacts based on the search text input and filters the displayed contacts.
     */
    @FXML
    private void onSearch() {
        String search = searchTextField.getText();
        contactsListView.getItems().clear();
        List<Contact> contacts = contactDAO.getAllContactsSearch(search);
        boolean hasContact = !contacts.isEmpty();
        if (hasContact) {
            contactsListView.getItems().addAll(contacts);
        }
        contactContainer.setVisible(hasContact);
    }

    /**
     * Adds the selected contact to the current collection and updates the contacts list view.
     */
    @FXML
    private void onAdd() {
        Contact selectedContact = contactsListView.getSelectionModel().getSelectedItem();
        int documentid = selectedContact.getId();
        collectionItemDAO.addCollectionItem(HelloApplication.currentcollectionid, documentid);
        syncContacts();
        titleTextField.requestFocus();
        initialize();
    }

    /**
     * Handles the action of opening a hyperlink in the system's default web browser.
     */
    @FXML
    private void onHyperlinkCLick() {
        String link = linkTextField.getText();
        try {
            Desktop.getDesktop().browse(new URL(link).toURI());
        } catch (Exception e) {
            // Handle exceptions for invalid or unreachable links.
        }
    }
}
