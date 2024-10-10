package com.example.cab302prac4.controller;

import com.example.cab302prac4.HelloApplication;
import com.example.cab302prac4.model.*;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Scene;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ProfileController2 {


    // Declare the UserDAO object
    private UserDAO userDAO;
    private SqliteImageDAO imageDAO;
    private IRatingDAO cratingDAO;
    private IRatingDAO ratingDAO;
    @FXML
    private ListView<Collection> collectionsListView;
    private ICollectionDAO collectionDAO;
    private ICollectionItemDAO collectionItemDAO;

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
    private TextField descriptionTextField;
    @FXML
    private TextField makerTextField;
    @FXML
    private TextField dateTextField;
    @FXML
    private VBox collectionContainer;
    @FXML
    private Label exportSuccessLabel;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button rateButton;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label collectionsTitle;
    @FXML
    private Label totalscoreLabel;
    @FXML
    private HBox tagsPane;
    private TagSystem tagSystem;
    private ITagDAO ctagDAO;
    private MessageSystem messageSystem;

    // Constructor to initialize the UserDAO
    public ProfileController2() {
        // Initialize the UserDAO to interact with the SQLite database
        userDAO = new UserDAO();
        imageDAO = new SqliteImageDAO();
        collectionDAO = new SqliteCollectionDAO();
        collectionItemDAO = new SqliteCollectionItemDAO();
        cratingDAO = new SqliteCollectionRatingDAO();
        ratingDAO = new SqliteRatingDAO();
        ctagDAO = new CTagDAO();
        tagSystem = new TagSystem(ctagDAO,false);
        messageSystem = new MessageSystem();
    }

    /**
     * Programmatically selects a contact in the list view and
     * updates the text fields with the contact's information.
     * @param collection The contact to select.
     */
    private void selectCollection(Collection collection) {
        tagsPane.getChildren().clear();
        tagSystem.getTags(collection.getId(),tagsPane);
        collectionsListView.getSelectionModel().select(collection);
        titleTextField.setText(collection.getTitle());
        makerTextField.setText(collection.getMaker());
        descriptionTextField.setText(collection.getDescription());
        dateTextField.setText(collection.getDate());
    }

    public void initialize() {
        // Load the logo image dynamically, if needed
        javafx.scene.image.Image logo = new Image("file:Images/vaultlogo2.png");  // Adjust path as necessary
        logoView.setImage(logo);
        loadImage();
        User user = userDAO.getUser(HelloApplication.profileid);
        profileImageName.setText(user.getFullName());
        profileName.setText("Profile: " + user.getFullName());
        collectionsTitle.setText("Collections by " + user.getFullName() + ":");
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());

        collectionsListView.setCellFactory(this::renderCell);
        syncContacts();
        // Select the first contact and display its information
        collectionsListView.getSelectionModel().selectFirst();
        Collection firstCollection = collectionsListView.getSelectionModel().getSelectedItem();
        if (firstCollection != null) {
            selectCollection(firstCollection);
            setRatingButton(firstCollection.getId());
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
     * Renders a cell in the contacts list view by setting the text to the contact's full name.
     * @param collectionsListView The list view to render the cell for.
     * @return The rendered cell.
     */
    private ListCell<Collection> renderCell(ListView<Collection> collectionsListView) {
        return new ListCell<>() {
            /**
             * Handles the event when a contact is selected in the list view.
             * @param mouseEvent The event to handle.
             */
            private void onCollectionSelected (MouseEvent mouseEvent) {
                ListCell<Collection> clickedCell = (ListCell<Collection>) mouseEvent.getSource();
                // Get the selected contact from the list view
                Collection selectedCollection = clickedCell.getItem();
                if (selectedCollection != null) selectCollection(selectedCollection);
                setRatingButton(selectedCollection.getId());
            }

            /**
             * Updates the item in the cell by setting the text to the contact's full name.
             * @param collection The contact to update the cell with.
             * @param empty Whether the cell is empty.
             */
            @Override
            protected void updateItem(Collection collection, boolean empty) {
                super.updateItem(collection, empty);
                // If the cell is empty, set the text to null, otherwise set it to the contact's full name
                if (empty || collection == null || collection.getFullName() == null) {
                    setText(null);
                    super.setOnMouseClicked(this::onCollectionSelected);
                } else {
                    setText(collection.getFullName());
                }
            }
        };
    }

    /**
     * Synchronizes the contacts list view with the contacts in the database.
     */
    private void syncContacts() {
        CalculateTotalScore(HelloApplication.profileid);
        collectionsListView.getItems().clear();
        List<Collection> collections = collectionDAO.getAllCollectionsByID(HelloApplication.profileid);
        boolean hasCollections = !collections.isEmpty();
        if (hasCollections) {
            collectionsListView.getItems().addAll(collections);
        }
        // Show / hide based on whether there are contacts
        collectionContainer.setVisible(hasCollections);
    }


    @FXML
    protected void onView() throws IOException {
        Collection selectedCollection = collectionsListView.getSelectionModel().getSelectedItem();
        Stage stage = (Stage) returnButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("collectionview-view.fxml"));
        HelloApplication.currentcollectionid = selectedCollection.getId();
        HelloApplication.collectionedit = false;
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }

    @FXML
    protected void onExport() throws IOException {
        try {
            // this is for monitoring runtime Exception within the block
            // content to write into the file
            Collection selectedCollection = collectionsListView.getSelectionModel().getSelectedItem();
            int currentId = selectedCollection.getId();
            String content = "COLLECTION DETAILS:\n"
                    + "Collection Title: " + selectedCollection.getTitle()  +"\n"
                    + "Collection Maker: " + selectedCollection.getMaker()  +"\n"
                    + "Collection Description: " + selectedCollection.getDescription()  +"\n"
                    + "Collection Date: " + selectedCollection.getDate()  +"\n"+"\n"+"\n";
            List<CollectionItem> collectionItems = collectionItemDAO.getAllCollectionItems(currentId);
            int idcount = 1;
            for (CollectionItem collectionItem : collectionItems)
            {
                content += "Collection Entry " + idcount + ":" + "\n" +
                        "Title: " + collectionItem.getTitle() + "\n" +
                        "Type: " + collectionItem.getType() + "\n" +
                        "Author: " + collectionItem.getAuthor() + "\n" +
                        "Description: " + collectionItem.getDescription() + "\n" +
                        "Location: " + collectionItem.getLocation() + "\n" +
                        "Date: " + collectionItem.getDate() + "\n" +
                        "Link: " + collectionItem.getLink() + "\n"
                        + "" + "\n";
                idcount ++;
            }
            String name = selectedCollection.getTitle();
            File file = new  File("export/" + name + ".txt"); // here file not created here

            // if file doesnt exists, then create it
            if (!file.exists()) {   // checks whether the file is Exist or not
                file.createNewFile();   // here if file not exist new file created
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true); // creating fileWriter object with the file
            BufferedWriter bw = new BufferedWriter(fw); // creating bufferWriter which is used to write the content into the file
            bw.write(content); // write method is used to write the given content into the file
            bw.close(); // Closes the stream, flushing it first. Once the stream has been closed, further write() or flush() invocations will cause an IOException to be thrown. Closing a previously closed stream has no effect.

            messageSystem.displayMessage("Export Successful",false,exportSuccessLabel);

        } catch (IOException e) { // if any exception occurs it will catch
            e.printStackTrace();
        }
    }

    @FXML
    private void onSearch() {
        // Get the selected contact from the list view
        String search = searchTextField.getText();
        collectionsListView.getItems().clear();
        List<Collection> collections = collectionDAO.getAllCollectionItemsSearchByID(search,HelloApplication.profileid);
        boolean hasContact = !collections.isEmpty();
        if (hasContact) {
            collectionsListView.getItems().addAll(collections);
        }
        // Show / hide based on whether there are contacts
        collectionContainer.setVisible(hasContact);
    }

    @FXML
    private void Switch() throws IOException {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("profiles-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }

    @FXML
    protected void onRate() throws IOException {
        Collection selectedCollection = collectionsListView.getSelectionModel().getSelectedItem();
        if (cratingDAO.checkIfRated(HelloApplication.userid,selectedCollection.getId()))
        {
            cratingDAO.removeRating(HelloApplication.userid,selectedCollection.getId());
        }
        else
        {
            cratingDAO.addRating(HelloApplication.userid,selectedCollection.getId());
        }
        setRatingButton(selectedCollection.getId());
        CalculateTotalScore(HelloApplication.profileid);
    }

    private void setRatingButton(int collectionid)
    {
        if (cratingDAO.checkIfRated(HelloApplication.userid,collectionid))
        {
            rateButton.setText("Remove Rating");
            rateButton.setId("delete_button");
        }
        else
        {
            rateButton.setText("Rate");
            rateButton.setId("confirm_button");
        }
        String labeltext = " ";
        labeltext += String.valueOf(cratingDAO.getRatingScoreForDocument(collectionid));
        scoreLabel.setText(labeltext);
        CalculateTotalScore(HelloApplication.profileid);
    }

    private void CalculateTotalScore(int id)
    {
        int score = 0;
        score += ratingDAO.getUserTotalRatingScore(id);
        score += cratingDAO.getUserTotalRatingScore(id);
        totalscoreLabel.setText(String.valueOf(score));
    }

}
