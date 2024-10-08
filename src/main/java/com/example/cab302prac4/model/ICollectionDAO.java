package com.example.cab302prac4.model;

import java.util.List;

/**
 * Interface for the Contact Data Access Object that handles
 * the CRUD operations for the Contact class with the database.
 */
public interface ICollectionDAO {
    /**
     * Adds a new contact to the database.
     * @param collection The contact to add.
     */
    public void addCollection(Collection collection);
    /**
     * Updates an existing contact in the database.
     * @param collection The contact to update.
     */
    public void updateCollection(Collection collection);
    /**
     * Deletes a contact from the database.
     * @param collection The contact to delete.
     */
    public void deleteCollection(Collection collection);
    /**
     * Retrieves a contact from the database.
     * @param id The id of the contact to retrieve.
     * @return The contact with the given id, or null if not found.
     */
    public Collection getCollection(int id);
    /**
     * Retrieves all contacts from the database.
     * @return A list of all contacts in the database.
     */
    public List<Collection> getAllCollections();

    public List<Collection> getAllCollectionsByID(int id);

    public List<Collection> getAllCollectionItemsSearch(String search);
    public List<Collection> getAllCollectionItemsSearchByID(String search,int userid);
}