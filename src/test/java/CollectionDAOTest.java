import com.example.cab302prac4.model.Collection;
import com.example.cab302prac4.model.ICollectionDAO;
import com.example.cab302prac4.model.SqliteCollectionDAO;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionDAOTest {
    private ICollectionDAO collectionDAO;
    private Collection[] collections = {
            new Collection("WW2 Collection", "Joe Doe", "WW2 Collection of documents", "10/10/2024", 1),
            new Collection("USA Collection", "John Doe", "USA history collection", "11/11/2024", 1 ),
            new Collection("Vietnam War Collection", "Peter Dough", "Vietnam war collection", "9/9/2024", 2),
            new Collection("WW1 Tank Collection", "Graystone", "WW1 tanks", "12/12/2024", 2),
            new Collection("WW2 Tank Collection", "Graystone", "WW2 tanks", "12/12/2024", 1)
    };

    @BeforeEach
    public void setUp() {
        collectionDAO = new MockCollectionDAO();
    }

    @Test
    public void testAddCollection() {
        collectionDAO.addCollection(collections[0]);
        assertEquals(collectionDAO.getCollection(0), collections[0]);
    }

    @Test
    public void testUpdateCollection() {
        for (Collection collection : collections) {
            collectionDAO.addCollection(collection);
        }
        Collection updateCollection = new Collection("WW2 Tank Collection", "Graystone", "WW2 tanks", "12/12/2024", 1);
        updateCollection.setId(1);
        collectionDAO.updateCollection(updateCollection);
        assertEquals(collectionDAO.getCollection(1), updateCollection);
    }

    @Test
    public void testDeleteCollections() {
        for (Collection collection : collections) {
            collectionDAO.addCollection(collection);
        }
        Collection deleteCollection = collections[1];
        deleteCollection.setId(1);
        collectionDAO.deleteCollection(deleteCollection);
        assertEquals(collectionDAO.getCollection(1), null);
    }

    @Test
    public void testGetCollection() {
        for (Collection collection : collections) {
            collectionDAO.addCollection(collection);
        }
        assertEquals(collectionDAO.getCollection(3), collections[3]);
    }

    @Test
    public void testGetCollectionNotExist() {
        for (Collection collection : collections) {
            collectionDAO.addCollection(collection);
        }
        assertEquals(collectionDAO.getCollection(10), null);
    }

    @Test
    public void testGetCollectionNegativeIndex() {
        for (Collection collection : collections) {
            collectionDAO.addCollection(collection);
        }
        assertEquals(collectionDAO.getCollection(-1), null);
    }

    @Test
    public void testGetAllCollections() {
        ArrayList<Collection> collectionList = new ArrayList<>(Arrays.asList(collections));
        for (Collection collection : collections) {
            collectionDAO.addCollection(collection);
        }
        assertEquals(collectionDAO.getAllCollections(), collectionList);
    }

    @Test
    public void testGetAllCollectionsIfNoneExist() {
        ArrayList<Collection> emptyCollectionList = new ArrayList<>();
        assertEquals(collectionDAO.getAllCollections(),emptyCollectionList);
    }

    @Test
    public void testGetAllCollectionsByID() {
        ArrayList<Collection> collectionList = new ArrayList<>();
        for (Collection collection : collections) {
            if (collection.getUserid() == 1)
            {
                collectionList.add(collection);
            }
        }
        for (Collection collection : collections) {
            collectionDAO.addCollection(collection);
        }
        assertEquals(collectionDAO.getAllCollectionsByID(1), collectionList);
    }

    @Test
    public void testGetAllCollectionsByIDThatDoesntExist() {
        ArrayList<Collection> collectionList = new ArrayList<>();
        for (Collection collection : collections) {
            collectionDAO.addCollection(collection);
        }
        assertEquals(collectionDAO.getAllCollectionsByID(-1), collectionList);
    }

}