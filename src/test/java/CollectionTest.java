import com.example.cab302prac4.model.Collection;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CollectionTest {
    Collection collection;

    @BeforeEach
    void setUp() {
        collection = new Collection("Example Collection","John Doe",
                "Example Description","10/10/2024",1);
    }

    @Test
    public void testGetId() {
        collection.setId(1);
        assertEquals(1, collection.getId());
    }

    @Test
    public void testGetTitle() {
        assertEquals("Example Collection", collection.getTitle());
    }
    @Test
    public void testSetTitle() {
        collection.setTitle("John Doe Collection");
        assertEquals("John Doe Collection", collection.getTitle());
    }

    @Test
    public void testGetMaker() {
        assertEquals("John Doe", collection.getMaker());
    }
    @Test
    public void testSetMaker() {
        collection.setMaker("John Dough");
        assertEquals("John Dough", collection.getMaker());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Example Description", collection.getDescription());
    }
    @Test
    public void testSetDescription() {
        collection.setDescription("John Doe collection");
        assertEquals("John Doe collection", collection.getDescription());
    }

    @Test
    public void testGetDate() {
        assertEquals("10/10/2024", collection.getDate());
    }
    @Test
    public void testSetDate() {
        collection.setDate("8/8/2011");
        assertEquals("8/8/2011", collection.getDate());
    }

    @Test
    public void testGetUserID() {
        assertEquals(1, collection.getUserid());
    }
    @Test
    public void testSetUserID() {
        collection.setUserid(2);
        assertEquals(2, collection.getUserid());
    }

    @Test
    public void testGetFullName() {
        assertEquals("Example Collection 10/10/2024 (John Doe)", collection.getFullName());
    }

}