package com.example.cab302prac4.model;

import javax.swing.text.html.HTML;
import java.util.List;

public interface TagInterface {

    /**
     * Adds a new tag to the database.
     * @param tag The tag to add.
     */
    public void addTag(Tag tag);

    /**
     * Deletes a contact from the database.
     * @param tag The contact to delete.
     */
    public void deleteTag(Tag tag);

    /**
     * Retrieves a contact from the database.
     * @param id The id of the contact to retrieve.
     * @return The contact with the given id, or null if not found.
     */
    public Tag getTag(int id);

    public List<Tag> getAllTags();

}
