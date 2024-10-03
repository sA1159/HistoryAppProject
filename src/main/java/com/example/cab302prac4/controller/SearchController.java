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

public class SearchController {
    @FXML
    private ListView<Contact> contactsListView;
    private IContactDAO contactDAO;
    private IRatingDAO ratingDAO;
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
    private Button rateButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label scoreLabel;
    @FXML
    private Button searchButton;

    public SearchController()
    {

        contactDAO = new SqliteContactDAO();
        ratingDAO = new SqliteRatingDAO();
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
                setRatingButton(selectedContact.getId());
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
            setRatingButton(firstContact.getId());
        }
    }


    @FXML
    private void onSearch() {
        // Get the selected contact from the list view
        String search = searchTextField.getText();
        contactsListView.getItems().clear();
        List<Contact> contacts = contactDAO.getAllContactsSearch(search);
        boolean hasContact = !contacts.isEmpty();
        if (hasContact) {
            contactsListView.getItems().addAll(contacts);
        }
        // Show / hide based on whether there are contacts
        contactContainer.setVisible(hasContact);
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
    private void onHyperlinkCLick(){
        String link = linkTextField.getText();
        try {
            Desktop.getDesktop().browse(new URL(link).toURI());
        } catch (Exception e) {}
    }

    @FXML
    protected void onRate() throws IOException {
        Contact selectedContact = contactsListView.getSelectionModel().getSelectedItem();
        if (ratingDAO.checkIfRated(HelloApplication.userid,selectedContact.getId()))
        {
            ratingDAO.removeRating(HelloApplication.userid,selectedContact.getId());
        }
        else
        {
            ratingDAO.addRating(HelloApplication.userid,selectedContact.getId());
        }
        setRatingButton(selectedContact.getId());
    }

    private void setRatingButton(int documentid)
    {
        if (ratingDAO.checkIfRated(HelloApplication.userid,documentid))
        {
            rateButton.setText("Remove Rating");
        }
        else
        {
            rateButton.setText("Rate");
        }
        String labeltext = "Total Ratings: ";
        labeltext += String.valueOf(ratingDAO.getRatingScoreForDocument(documentid));
        scoreLabel.setText(labeltext);
    }
}