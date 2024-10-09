package com.example.cab302prac4.controller;

import com.example.cab302prac4.HelloApplication;
import com.example.cab302prac4.model.*;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Scene;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.io.File;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import static java.awt.SystemColor.desktop;

public class ProfileController {


    // Declare the UserDAO object
    private UserDAO userDAO;
    private SqliteImageDAO imageDAO;
    private IContactDAO contactDAO;
    private IRatingDAO ratingDAO;
    private IRatingDAO cratingDAO;

    @FXML
    private ListView<Contact> contactsListView;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private Button returnButton;
    @FXML
    private ImageView logoView;
    @FXML
    private ImageView profileView;
    @FXML
    private Label profileName;
    @FXML
    private Label profileImageName;
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
    private Button rateButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label documentsTitle;
    @FXML
    private Label totalscoreLabel;
    @FXML
    private HBox tagsPane;
    private TagSystem tagSystem;
    private ITagDAO tagDAO;

    // Constructor to initialize the UserDAO
    public ProfileController() {
        // Initialize the UserDAO to interact with the SQLite database
        userDAO = new UserDAO();
        imageDAO = new SqliteImageDAO();
        contactDAO = new SqliteContactDAO();
        ratingDAO = new SqliteRatingDAO();
        cratingDAO = new SqliteCollectionRatingDAO();
        tagDAO = new TagDAO();
        tagSystem = new TagSystem(tagDAO,false);
    }

    public void initialize() {
        // Load the logo image dynamically, if needed
        javafx.scene.image.Image logo = new Image("file:Images/vaultlogo2.png");  // Adjust path as necessary
        logoView.setImage(logo);
        loadImage();
        User user = userDAO.getUser(HelloApplication.profileid);
        profileImageName.setText(user.getFullName());
        profileName.setText("Profile: " + user.getFullName());
        documentsTitle.setText("Documents by " + user.getFullName() + ":");
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());

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

    // Handle Back Button Click
    public void handleBackButtonClick() {
        try {
            HelloApplication.profileid = -1;
            // Load the home page FXML
            Stage stage = (Stage) returnButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("users-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
            stage.setScene(scene);
                    } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadImage()
    {
        if (imageDAO.getImage(HelloApplication.profileid) == null)
        {
            javafx.scene.image.Image image = new Image("file:Images/profile.png");
            profileView.setImage(image);
        }
        else
        {
            String profile_path = "file:";
            profile_path += imageDAO.getImage(HelloApplication.profileid);
            javafx.scene.image.Image image = new Image(profile_path);
            profileView.setImage(image);
        }
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
        CalculateTotalScore(HelloApplication.profileid);
        contactsListView.getItems().clear();
        List<Contact> contacts = contactDAO.getAllContactsByID(HelloApplication.profileid);
        boolean hasContact = !contacts.isEmpty();
        if (hasContact) {
            contactsListView.getItems().addAll(contacts);
        }
        // Show / hide based on whether there are contacts
        contactContainer.setVisible(hasContact);
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
        CalculateTotalScore(HelloApplication.profileid);
    }

    @FXML
    private void Switch() throws IOException {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("profiles-view2.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }

    private void CalculateTotalScore(int id)
    {
        int score = 0;
        score += ratingDAO.getUserTotalRatingScore(id);
        score += cratingDAO.getUserTotalRatingScore(id);
        totalscoreLabel.setText(String.valueOf(score));
    }

}
