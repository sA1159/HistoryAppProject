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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class YourCollectionsController {
    @FXML
    private ListView<Collection> collectionsListView;
    private ICollectionDAO collectionDAO;
    private ICollectionItemDAO collectionItemDAO;
    private IUserDAO userDAO;
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
    private TextField tagField;
    @FXML
    private HBox tagsPane;
    @FXML
    private Label scoreLabel;
    private ITagDAO ctagDAO;
    private TagSystem tagSystem;
    private MessageSystem messageSystem;

    public YourCollectionsController() {
        collectionDAO = new SqliteCollectionDAO();
        collectionItemDAO = new SqliteCollectionItemDAO();
        userDAO = new UserDAO();
        ctagDAO = new CTagDAO();
        tagSystem = new TagSystem(ctagDAO,true);
        messageSystem = new MessageSystem();
        cratingDAO = new SqliteCollectionRatingDAO();
    }

    /**
     * Programmatically selects a contact in the list view and
     * updates the text fields with the contact's information.
     * @param collection The contact to select.
     */
    private void selectCollection(Collection collection) {
        scoreLabel.setText(" " + cratingDAO.getRatingScoreForDocument(collection.getId()));
        tagsPane.getChildren().clear();
        tagSystem.getTags(collection.getId(),tagsPane);
        collectionsListView.getSelectionModel().select(collection);
        titleTextField.setText(collection.getTitle());
        makerTextField.setText(collection.getMaker());
        descriptionTextField.setText(collection.getDescription());
        dateTextField.setText(collection.getDate());
        tagField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tagField.getText().isEmpty()) {
                    tagSystem.tagButton(tagsPane, tagField.getText());
                    tagSystem.tags.add(tagField.getText());
                    tagField.clear();
                }
                else{
                    messageSystem.displayMessage("Tag Field Must Not Be Empty",true,exportSuccessLabel);
                }
            }
        });
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
        List<Collection> collections = collectionDAO.getAllCollectionsByID(HelloApplication.userid);
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
    private void onEditConfirm() {
        // Get the selected contact from the list view
        Collection selectedCollection = collectionsListView.getSelectionModel().getSelectedItem();
        if (selectedCollection != null) {
            tagSystem.addTags(selectedCollection.getId());
            selectedCollection.setTitle(titleTextField.getText());
            selectedCollection.setMaker(makerTextField.getText());
            selectedCollection.setDescription(descriptionTextField.getText());
            selectedCollection.setDate(dateTextField.getText());
            collectionDAO.updateCollection(selectedCollection);
            messageSystem.displayMessage("Collection Successfully Updated",false,exportSuccessLabel);
            syncContacts();
        }
    }

    @FXML
    private void onDelete() {
        // Get the selected contact from the list view
        Collection selectedCollection = collectionsListView.getSelectionModel().getSelectedItem();
        if (selectedCollection != null) {
            collectionDAO.deleteCollection(selectedCollection);
            tagSystem.removeTags(selectedCollection.getId());
            syncContacts();
        }
        initialize();
    }

    @FXML
    private void onAdd() {
        // Default values for a new contact
        final String DEFAULT_title = "New Collection";
        final String DEFAULT_description = "Abc";
        final String DEFAULT_date = "2024";
        User currentuser = userDAO.getUser(HelloApplication.userid);
        String currentname = currentuser.getFirstName() + ' ' + currentuser.getLastName();
        Collection newCollection = new Collection(DEFAULT_title, DEFAULT_description, currentname, DEFAULT_date, HelloApplication.userid);
        // Add the new contact to the database
        tagSystem.addTagsTemp(newCollection.getId());
        collectionDAO.addCollection(newCollection);
        syncContacts();
        // Select the new contact in the list view
        // and focus the first name text field
        selectCollection(newCollection);
        titleTextField.requestFocus();
    }

    @FXML
    private void onSearch() {
        // Get the selected contact from the list view
        String search = searchTextField.getText();
        collectionsListView.getItems().clear();
        List<Collection> collections = collectionDAO.getAllCollectionItemsSearchByID(search,HelloApplication.userid);
        boolean hasContact = !collections.isEmpty();
        if (hasContact) {
            collectionsListView.getItems().addAll(collections);
        }
        // Show / hide based on whether there are contacts
        collectionContainer.setVisible(hasContact);
    }

    @FXML
    private void onCancel() {
        // Find the selected contact
        Collection selectedCollection = collectionsListView.getSelectionModel().getSelectedItem();
        if (selectedCollection != null) {
            // Since the contact hasn't been modified,
            // we can just re-select it to refresh the text fields
            selectCollection(selectedCollection);
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
        HelloApplication.collectionedit = true;
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

            messageSystem.displayMessage("Export Success",false,exportSuccessLabel);

        } catch (IOException e) { // if any exception occurs it will catch
            e.printStackTrace();
        }
    }

    @FXML
    private void Switch() throws IOException {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("collections-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }
}