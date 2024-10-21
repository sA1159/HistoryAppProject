package com.example.cab302prac4.controller;

import com.example.cab302prac4.HelloApplication;
import com.example.cab302prac4.model.*;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CollectionsController {

    @FXML
    private ListView<Collection> collectionsListView;
    private ICollectionDAO collectionDAO;
    private ICollectionItemDAO collectionItemDAO;
    private IRatingDAO cratingDAO;

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
    Label exportSuccessLabel;

    @FXML
    private Button returnButton;
    @FXML
    private ImageView logoView;

    @FXML
    private TextField searchTextField;
    @FXML
    private Button rateButton;
    @FXML
    private Label scoreLabel;
    @FXML
    private HBox tagsPane;

    private TagSystem tagSystem;
    private ITagDAO ctagDAO;
    private MessageSystem messageSystem;

    /**
     * Constructor to initialize DAOs and systems used for managing collections and ratings.
     */
    public CollectionsController() {
        collectionDAO = new SqliteCollectionDAO();
        collectionItemDAO = new SqliteCollectionItemDAO();
        cratingDAO = new SqliteCollectionRatingDAO();
        ctagDAO = new CTagDAO();
        tagSystem = new TagSystem(ctagDAO, false);
        messageSystem = new MessageSystem();
    }

    /**
     * Selects a collection and updates the UI fields with the collection's details.
     * @param collection The collection to select.
     */
    private void selectCollection(Collection collection) {
        tagsPane.getChildren().clear();
        tagSystem.getTags(collection.getId(), tagsPane);
        collectionsListView.getSelectionModel().select(collection);
        titleTextField.setText(collection.getTitle());
        makerTextField.setText(collection.getMaker());
        descriptionTextField.setText(collection.getDescription());
        dateTextField.setText(collection.getDate());
    }

    /**
     * Renders a cell in the collection list view by setting the text to the collection's full name.
     * @param collectionsListView The list view to render the cell for.
     * @return The rendered cell.
     */
    private ListCell<Collection> renderCell(ListView<Collection> collectionsListView) {
        return new ListCell<>() {
            /**
             * Handles the event when a collection is selected in the list view.
             * @param mouseEvent The event to handle.
             */
            private void onCollectionSelected(MouseEvent mouseEvent) {
                ListCell<Collection> clickedCell = (ListCell<Collection>) mouseEvent.getSource();
                Collection selectedCollection = clickedCell.getItem();
                if (selectedCollection != null) selectCollection(selectedCollection);
                setRatingButton(selectedCollection.getId());
            }

            /**
             * Updates the item in the cell by setting the text to the collection's full name.
             * @param collection The collection to update the cell with.
             * @param empty Whether the cell is empty.
             */
            @Override
            protected void updateItem(Collection collection, boolean empty) {
                super.updateItem(collection, empty);
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
     * Synchronizes the collection list view with the collections in the database.
     */
    private void syncContacts() {
        collectionsListView.getItems().clear();
        List<Collection> collections = collectionDAO.getAllCollections();
        boolean hasCollections = !collections.isEmpty();
        if (hasCollections) {
            collectionsListView.getItems().addAll(collections);
        }
        collectionContainer.setVisible(hasCollections);
    }

    /**
     * Initializes the controller, loading the first collection and rendering the collections list.
     */
    @FXML
    public void initialize() {
        Image logo = new Image("file:Images/vaultlogo2.png");  // Adjust path as necessary
        logoView.setImage(logo);
        collectionsListView.setCellFactory(this::renderCell);
        syncContacts();
        collectionsListView.getSelectionModel().selectFirst();
        Collection firstCollection = collectionsListView.getSelectionModel().getSelectedItem();
        if (firstCollection != null) {
            selectCollection(firstCollection);
            setRatingButton(firstCollection.getId());
        }
    }

    /**
     * Handles the return button click and switches back to the home view.
     * @throws IOException If the home view FXML fails to load.
     */
    @FXML
    protected void onReturnButtonClick() throws IOException {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }

    /**
     * Opens the selected collection's detailed view.
     * @throws IOException If the collection view FXML fails to load.
     */
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

    /**
     * Exports the selected collection's details to a text file.
     * @throws IOException If there is an error creating the file.
     */
    @FXML
    protected void onExport() throws IOException {
        try {
            Collection selectedCollection = collectionsListView.getSelectionModel().getSelectedItem();
            int currentId = selectedCollection.getId();
            String content = "COLLECTION DETAILS:\n"
                    + "Collection Title: " + selectedCollection.getTitle()  + "\n"
                    + "Collection Maker: " + selectedCollection.getMaker()  + "\n"
                    + "Collection Description: " + selectedCollection.getDescription()  + "\n"
                    + "Collection Date: " + selectedCollection.getDate()  + "\n\n\n";
            List<CollectionItem> collectionItems = collectionItemDAO.getAllCollectionItems(currentId);
            int idcount = 1;
            for (CollectionItem collectionItem : collectionItems) {
                content += "Collection Entry " + idcount + ":\n" +
                        "Title: " + collectionItem.getTitle() + "\n" +
                        "Type: " + collectionItem.getType() + "\n" +
                        "Author: " + collectionItem.getAuthor() + "\n" +
                        "Description: " + collectionItem.getDescription() + "\n" +
                        "Location: " + collectionItem.getLocation() + "\n" +
                        "Date: " + collectionItem.getDate() + "\n" +
                        "Link: " + collectionItem.getLink() + "\n\n";
                idcount++;
            }
            String name = selectedCollection.getTitle();
            File file = new File("export/" + name + ".txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            messageSystem.displayMessage("Export Success", false, exportSuccessLabel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches for collections based on the search text and filters the displayed collections.
     */
    @FXML
    private void onSearch() {
        String search = searchTextField.getText();
        collectionsListView.getItems().clear();
        List<Collection> collections = collectionDAO.getAllCollectionItemsSearch(search);
        boolean hasContact = !collections.isEmpty();
        if (hasContact) {
            collectionsListView.getItems().addAll(collections);
        }
        collectionContainer.setVisible(hasContact);
    }

    /**
     * Switches the view to the user's collections.
     * @throws IOException If the FXML fails to load.
     */
    @FXML
    private void Switch() throws IOException {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("yourcollections-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }

    /**
     * Handles the rating button click, adding or removing the user's rating for the collection.
     * @throws IOException If there is an error during the rating process.
     */
    @FXML
    protected void onRate() throws IOException {
        Collection selectedCollection = collectionsListView.getSelectionModel().getSelectedItem();
        if (cratingDAO.checkIfRated(HelloApplication.userid, selectedCollection.getId())) {
            cratingDAO.removeRating(HelloApplication.userid, selectedCollection.getId());
        } else {
            cratingDAO.addRating(HelloApplication.userid, selectedCollection.getId());
        }
        setRatingButton(selectedCollection.getId());
    }

    /**
     * Sets the rating button text based on whether the user has rated the collection.
     * @param collectionid The ID of the collection being rated.
     */
    private void setRatingButton(int collectionid) {
        if (cratingDAO.checkIfRated(HelloApplication.userid, collectionid)) {
            rateButton.setText("Remove Rating");
            rateButton.setId("delete_button");
        } else {
            rateButton.setText("Rate");
            rateButton.setId("confirm_button");
        }
        scoreLabel.setText(" " + cratingDAO.getRatingScoreForDocument(collectionid));
    }
}
