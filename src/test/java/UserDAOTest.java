import com.example.cab302prac4.model.Collection;
import com.example.cab302prac4.model.IUserDAO;
import com.example.cab302prac4.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDAOTest {
    private IUserDAO userDAO;
    private User[] users = {
            new User("John", "Doe", "jd@gmail.com", "password"),
            new User("John", "Dough", "jdough@gmail.com", "password"),
            new User("Jane", "Doe", "janed@gmail.com", "password")
    };

    @BeforeEach
    public void setUp() {
        userDAO = new MockUserDAO();
    }

    @Test
    public void testAddUser() {
        userDAO.addUser(users[0]);
        assertEquals(userDAO.getUser(0), users[0]);
    }

    @Test
    public void testUpdateUser() {
        for (User user : users) {
            userDAO.addUser(user);
        }
        User updateUser = new User("Jeffery", "Dough", "jdough@gmail.com", "password");
        updateUser.setUserID(0);
        userDAO.updateUser(updateUser);
        assertEquals(userDAO.getUser(0), updateUser);
    }

    @Test
    public void testDeleteUser() {
        for (User user : users) {
            userDAO.addUser(user);
        }
        User deleteUser = users[1];
        deleteUser.setUserID(1);
        userDAO.deleteUser(deleteUser);
        assertEquals(userDAO.getUser(1), null);
    }

    @Test
    public void testGetUser() {
        for (User user : users) {
            userDAO.addUser(user);
        }
        assertEquals(userDAO.getUser(1), users[1]);
    }

    @Test
    public void testGetUserByEmail() {
        for (User user : users) {
            userDAO.addUser(user);
        }
        assertEquals(userDAO.getUserByEmail("jd@gmail.com"), users[0]);
    }

    @Test
    public void testGetAllUsers() {
        ArrayList<User> userList = new ArrayList<>(Arrays.asList(users));
        for (User user : users) {
            userDAO.addUser(user);
        }
        assertEquals(userDAO.getAllUsers(), userList);
    }
}
