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

    public CollectionsController() {
        collectionDAO = new SqliteCollectionDAO();
        collectionItemDAO = new SqliteCollectionItemDAO();
    }

    /**
     * Programmatically selects a contact in the list view and
     * updates the text fields with the contact's information.
     * @param collection The contact to select.
     */
    private void selectCollection(Collection collection) {
        collectionsListView.getSelectionModel().select(collection);
        titleTextField.setText(collection.getTitle());
        makerTextField.setText(collection.getMaker());
        descriptionTextField.setText(collection.getDescription());
        dateTextField.setText(collection.getDate());
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
        collectionsListView.getItems().clear();
        List<Collection> collections = collectionDAO.getAllCollections();
        boolean hasCollections = !collections.isEmpty();
        if (hasCollections) {
            collectionsListView.getItems().addAll(collections);
        }
        // Show / hide based on whether there are contacts
        collectionContainer.setVisible(hasCollections);
    }

    @FXML
    public void initialize() {
        // Load the logo image dynamically, if needed
        javafx.scene.image.Image logo = new Image("file:Images/vaultlogo2.png");  // Adjust path as necessary
        logoView.setImage(logo);
        collectionsListView.setCellFactory(this::renderCell);
        syncContacts();
        // Select the first contact and display its information
        collectionsListView.getSelectionModel().selectFirst();
        Collection firstCollection = collectionsListView.getSelectionModel().getSelectedItem();
        if (firstCollection != null) {
            selectCollection(firstCollection);
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

            exportSuccessLabel.setTextFill(Color.color(0, 0.75, 0));
            exportSuccessLabel.setText("Export Successful");
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(b -> exportSuccessLabel.setText(null));
            pause.play();

        } catch (IOException e) { // if any exception occurs it will catch
            e.printStackTrace();
        }
    }

    @FXML
    private void Switch() throws IOException {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("yourcollections-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }
}