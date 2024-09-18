import com.example.cab302prac4.model.Contact;
import com.example.cab302prac4.model.User;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User user;

    @BeforeEach
    void setUp() {
        user = new User("John","Doe","jd@gmail.com","password");
    }

    @Test
    public void testGetId() {
        user.setUserID(1);
        assertEquals(1, user.getUserID());
    }

    @Test
    public void testGetFirstName() {
        assertEquals("John", user.getFirstName());
    }

    @Test
    public void testSetFirstName() {
        user.setFirstName("Joe");
        assertEquals("Joe", user.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("Doe", user.getLastName());
    }

    @Test
    public void testSetLastName() {
        user.setLastName("Dough");
        assertEquals("Dough", user.getLastName());
    }

    @Test
    public void testGetEmail() {
        assertEquals("jd@gmail.com", user.getEmail());
    }

    @Test
    public void testSetEmail() {
        user.setEmail("Dough@gmail.com");
        assertEquals("Dough@gmail.com", user.getEmail());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", user.getPassword());
    }

    @Test
    public void testSetPassword() {
        user.setPassword("Dough");
        assertEquals("Dough", user.getPassword());
    }
}
