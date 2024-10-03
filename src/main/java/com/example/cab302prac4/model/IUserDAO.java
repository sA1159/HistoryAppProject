package com.example.cab302prac4.model;

import java.util.List;

/**
 * Interface for the User Data Access Object that handles
 * the CRUD operations for the User class with the database.
 */
public interface IUserDAO {

    // Adds a new user to the database
    void addUser(User user);

    // Updates an existing user's information in the database
    void updateUser(User user);

    // Deletes a user from the database
    void deleteUser(User user);

    // Retrieves a user from the database by user ID
    User getUser(int userID);

    // Retrieves a user by their email (for login purposes)
    User getUserByEmail(String email);

    // Retrieves all users from the database
    List<User> getAllUsers();

    // Retrieves all users that match the search string from the database
    List<User> getAllUsersSearch(String search);

    // Retrieves the total number of users
    int getTotalUsers();
}
