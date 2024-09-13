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
import java.util.List;

public class CollectionsViewController {
    @FXML
    private ListView<CollectionItem> collectionItemListView;
    private ICollectionItemDAO collectionitemDAO;
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
    private Hyperlink linkHyperLink;
    @FXML
    private VBox contactContainer;
    @FXML
    private Button returnButton;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    public int currentid = HelloApplication.currentcollectionid;
    public CollectionsViewController() {
        collectionitemDAO = new SqliteCollectionItemDAO();
    }

    /**
     * Programmatically selects a contact in the list view and
     * updates the text fields with the contact's information.
     * @param collectionItem The contact to select.
     */
    private void selectCollectionItem(CollectionItem collectionItem) {
        collectionItemListView.getSelectionModel().select(collectionItem);
        titleTextField.setText(collectionItem.getTitle());
        typeTextField.setText(collectionItem.getType());
        authorTextField.setText(collectionItem.getAuthor());
        descriptionTextField.setText(collectionItem.getDescription());
        locationTextField.setText(collectionItem.getLocation());
        dateTextField.setText(collectionItem.getDate());
        linkHyperLink.setText(collectionItem.getLink());
    }

    /**
     * Renders a cell in the contacts list view by setting the text to the contact's full name.
     * @param collectionItemListView The list view to render the cell for.
     * @return The rendered cell.
     */
    private ListCell<CollectionItem> renderCell(ListView<CollectionItem> collectionItemListView) {
        return new ListCell<>() {
            /**
             * Handles the event when a contact is selected in the list view.
             * @param mouseEvent The event to handle.
             */
            private void onContactSelected(MouseEvent mouseEvent) {
                ListCell<CollectionItem> clickedCell = (ListCell<CollectionItem>) mouseEvent.getSource();
                // Get the selected contact from the list view
                CollectionItem selectedCollectionItem = clickedCell.getItem();
                if (selectedCollectionItem != null) selectCollectionItem(selectedCollectionItem);
            }

            /**
             * Updates the item in the cell by setting the text to the contact's full name.
             * @param collectionItem The contact to update the cell with.
             * @param empty Whether the cell is empty.
             */
            @Override
            protected void updateItem(CollectionItem collectionItem, boolean empty) {
                super.updateItem(collectionItem, empty);
                // If the cell is empty, set the text to null, otherwise set it to the contact's full name
                if (empty || collectionItem == null || collectionItem.getFullName() == null) {
                    setText(null);
                    super.setOnMouseClicked(this::onContactSelected);
                } else {
                    setText(collectionItem.getFullName());
                }
            }
        };
    }

    /**
     * Synchronizes the contacts list view with the contacts in the database.
     */
    private void syncContacts() {
        collectionItemListView.getItems().clear();
        List<CollectionItem> collectionItems = collectionitemDAO.getAllCollectionItems(currentid);
        boolean hasContact = !collectionItems.isEmpty();
        if (hasContact) {
            collectionItemListView.getItems().addAll(collectionItems);
        }
        // Show / hide based on whether there are contacts
        contactContainer.setVisible(hasContact);
    }

    @FXML
    public void initialize() {
        addButton.setVisible(false);
        deleteButton.setVisible(false);
        if (HelloApplication.collectionedit)
        {
            addButton.setVisible(true);
            deleteButton.setVisible(true);
        }
        collectionItemListView.setCellFactory(this::renderCell);
        syncContacts();
        // Select the first contact and display its information
        collectionItemListView.getSelectionModel().selectFirst();
        CollectionItem firstContact = collectionItemListView.getSelectionModel().getSelectedItem();
        if (firstContact != null) {
            selectCollectionItem(firstContact);
        }
    }

    @FXML
    private void onDelete() {
        // Get the selected contact from the list view
        CollectionItem selectedContact = collectionItemListView.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            collectionitemDAO.deleteCollectionItem(selectedContact);
            syncContacts();
        }
        initialize();
    }

    @FXML
    private void onAdd() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("collectionsadd-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Stage stage = new Stage();
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setTitle("Add to Collection");
        stage.setScene(scene);
        stage.show();
        stage.setOnHidden(e -> {
            syncContacts();
            initialize();
        });
    }

    @FXML
    protected void onReturnButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        if (HelloApplication.collectionedit)
        {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("yourcollections-view.fxml"));
        }
        else
        {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("collections-view.fxml"));
        }
        Stage stage = (Stage) returnButton.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }

    @FXML
    private void onHyperlinkCLick(){
        String link = linkHyperLink.getText();
        try {
            Desktop.getDesktop().browse(new URL(link).toURI());
        } catch (Exception e) {}
    }
}