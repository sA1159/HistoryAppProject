import com.example.cab302prac4.model.Collection;
import com.example.cab302prac4.model.CollectionItem;
import com.example.cab302prac4.model.ICollectionDAO;
import com.example.cab302prac4.model.ICollectionItemDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionItemDAOTest {
    private MockCollectionItemDAO collectionItemDAO;

    private CollectionItem[] collectionItems = {
            new CollectionItem("WW2 Tank", "Book", "John Doe", "WW2 Axis Tank", "Germany","10/10/1945","link",1),
            new CollectionItem("WW1 Tank", "Book", "John Doe", "WW1 Axis Tank", "Germany","10/10/1915","link",1),
            new CollectionItem("Adolf Hitler Diary", "Book", "Adolf Hitler", "example", "Germany","10/10/1945","link",1)
    };

    @BeforeEach
    public void setUp() {
        collectionItemDAO = new MockCollectionItemDAO();
    }

    @Test
    public void testAddCollectionItem() {
        collectionItemDAO.addCollectionItem(1,1);
        ArrayList<CollectionItem> collectionItemsList = new ArrayList<>();
        collectionItemsList = (ArrayList<CollectionItem>) collectionItemDAO.getAllCollectionItems(1);

        CollectionItem newItem = collectionItemsList.get(0);
        assertEquals(newItem.getCollectionid(), 1);
        assertEquals(newItem.getId(), 1);
    }

    @Test
    public void testDeleteCollectionItem() {
        collectionItemDAO.addCollectionItemMock(collectionItems[0], 0);

        CollectionItem deleteCollectionItem = collectionItems[0];
        collectionItemDAO.deleteCollectionItem(deleteCollectionItem);
        ArrayList<CollectionItem> result = new ArrayList<>();
        assertEquals(collectionItemDAO.getAllCollectionItems(1), result);
    }

    @Test
    public void testGetAllCollectionItems() {
        int documentid = 0;
        for (CollectionItem collectionItem : collectionItems)
        {
            collectionItemDAO.addCollectionItemMock(collectionItem,documentid);
            documentid++;
        }

        int resultdocumentid = 0;
        ArrayList<CollectionItem> result = new ArrayList<>();
        for (CollectionItem collectionItem : collectionItems)
        {
            collectionItem.setId(resultdocumentid);
            result.add(collectionItem);
            resultdocumentid++;
        }
        assertEquals(collectionItemDAO.getAllCollectionItems(1),result);
    }
}
