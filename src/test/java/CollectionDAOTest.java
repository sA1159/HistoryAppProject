import static org.junit.jupiter.api.Assertions.*;

import com.example.cab302prac4.model.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class CollectionDAOTest {

    private mocksqlitecollectionDAO dao;
    private Collection collection1;
    private Collection collection2;

    @BeforeEach
    public void setUp() //initialising variables for testing
    {
        dao = new mocksqlitecollectionDAO();
        collection1 = new Collection("Title1", "Maker1", "Description1", "2024-10-17", 1);
        collection2 = new Collection("Title2", "Maker2", "Description2", "2024-10-17", 2);
    }

    @Test
    public void testAddCollection() //ensuring we can append correctly
    {
        dao.addCollection(collection1);
        assertEquals(1, collection1.getId()); // Auto-increment ID should be set
        assertEquals(1, dao.getAllCollections().size());
    }

    @Test
    public void testUpdateCollection() //ensuring we can update correctly
    {
        dao.addCollection(collection1);
        collection1.setTitle("UpdatedTitle");
        dao.updateCollection(collection1);

        Collection updatedCollection = dao.getCollection(collection1.getId());
        assertEquals("UpdatedTitle", updatedCollection.getTitle());
    }

    @Test
    public void testDeleteCollection() //test that the collection deletes correctly
    {
        dao.addCollection(collection1);
        dao.deleteCollection(collection1);

        assertNull(dao.getCollection(collection1.getId()));
        assertTrue(dao.getAllCollections().isEmpty());
    }

    @Test
    public void testGetCollection() //test that we can obtain collections correctly
    {
        dao.addCollection(collection1);
        Collection retrieved = dao.getCollection(collection1.getId());

        assertNotNull(retrieved);
        assertEquals("Title1", retrieved.getTitle());
    }

    @Test
    public void testGetAllCollections()  //test that we can obtain all collections correctly
    {
        dao.addCollection(collection1);
        dao.addCollection(collection2);

        List<Collection> collections = dao.getAllCollections();
        assertEquals(2, collections.size());
    }

    @Test
    public void testGetAllCollectionsByID() //ensuring we can obtain all collection by their ID
    {
        dao.addCollection(collection1); // UserID = 1
        dao.addCollection(collection2); // UserID = 2

        List<Collection> user1Collections = dao.getAllCollectionsByID(1);
        List<Collection> user2Collections = dao.getAllCollectionsByID(2);

        assertEquals(1, user1Collections.size());
        assertEquals(1, user2Collections.size());
        assertEquals("Title1", user1Collections.get(0).getTitle());
        assertEquals("Title2", user2Collections.get(0).getTitle());
    }

    @Test
    public void testGetAllCollectionItemsSearch()
    {
        dao.addCollection(collection1); // Title = Title1
        dao.addCollection(collection2); // Title = Title2

        List<Collection> searchResults = dao.getAllCollectionItemsSearch("Title1");
        assertEquals(1, searchResults.size());
        assertEquals("Title1", searchResults.get(0).getTitle());
    }

    @Test
    public void testGetAllCollectionItemsSearchByID()
    {
        dao.addCollection(collection1); // UserID = 1, Title = Title1
        dao.addCollection(collection2); // UserID = 2, Title = Title2

        List<Collection> searchResults = dao.getAllCollectionItemsSearchByID("Title1", 1);
        assertEquals(1, searchResults.size());
        assertEquals("Title1", searchResults.get(0).getTitle());
    }
}
