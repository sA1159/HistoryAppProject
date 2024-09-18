import com.example.cab302prac4.model.Contact;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ContactTest {
    Contact contact;

    @BeforeEach
    void setUp() {
        contact = new Contact("Example Document","Book","John Doe","Example Description","USA",
            "10/10/1945","example.com",1);
    }

    @Test
    public void testGetId() {
        contact.setId(1);
        assertEquals(1, contact.getId());
    }

    @Test
    public void testGetTitle() {
        assertEquals("Example Document", contact.getTitle());
    }
    @Test
    public void testSetTitle() {
        contact.setTitle("John Doe Document");
        assertEquals("John Doe Document", contact.getTitle());
    }

    @Test
    public void testGetType() {
        assertEquals("Book", contact.getType());
    }
    @Test
    public void testSetType() {
        contact.setType("Image");
        assertEquals("Image", contact.getType());
    }

    @Test
    public void testGetAuthor() {
        assertEquals("John Doe", contact.getAuthor());
    }
    @Test
    public void testSetAuthor() {
        contact.setAuthor("Joe Dough");
        assertEquals("Joe Dough", contact.getAuthor());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Example Description", contact.getDescription());
    }
    @Test
    public void testSetDescription() {
        contact.setDescription("John Doe document");
        assertEquals("John Doe document", contact.getDescription());
    }

    @Test
    public void testGetLocation() {
        assertEquals("USA", contact.getLocation());
    }
    @Test
    public void testSetLocation() {
        contact.setLocation("Germany");
        assertEquals("Germany", contact.getLocation());
    }

    @Test
    public void testGetDate() {
        assertEquals("10/10/1945", contact.getDate());
    }
    @Test
    public void testSetDate() {
        contact.setDate("8/8/2011");
        assertEquals("8/8/2011", contact.getDate());
    }

    @Test
    public void testGetLink() {
        assertEquals("example.com", contact.getLink());
    }
    @Test
    public void testSetLink() {
        contact.setLink("google.com");
        assertEquals("google.com", contact.getLink());
    }

    @Test
    public void testGetUserID() {
        assertEquals(1, contact.getUserid());
    }
    @Test
    public void testSetUserID() {
        contact.setUserid(2);
        assertEquals(2, contact.getUserid());
    }

    @Test
    public void testGetFullName() {
        assertEquals("Example Document 10/10/1945 (John Doe)", contact.getFullName());
    }

}