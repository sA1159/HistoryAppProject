package com.example.cab302prac4.model;

import java.util.List;

/**
 * Interface for the Contact Data Access Object that handles
 * the CRUD operations for the Contact class with the database.
 */
public interface ICollectionItemDAO {

    public void addCollectionItem(int collectionid, int documentid);
    /**
     * Deletes a contact from the database.
     * @param collectionItem The contact to delete.
     */
    public void deleteCollectionItem(CollectionItem collectionItem);
    /**
    /**
     * Retrieves all contacts from the database.
     * @return A list of all contacts in the database.
     */
    public List<CollectionItem> getAllCollectionItems(int id);

    public List<Contact> getContactsCollectionID(int collectionid);
    public List<CollectionItem> getAllCollectionItemsSearch(int currentid, String search);
}