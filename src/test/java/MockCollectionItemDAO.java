import com.example.cab302prac4.model.Collection;
import com.example.cab302prac4.model.CollectionItem;
import com.example.cab302prac4.model.Contact;
import com.example.cab302prac4.model.ICollectionItemDAO;

import java.util.ArrayList;
import java.util.List;

public class MockCollectionItemDAO implements ICollectionItemDAO {
    /**
     * A static list of contacts to be used as a mock database.
     */
    public final ArrayList<CollectionItem> collectiontems = new ArrayList<>();
    public int currentCollectionID;
    @Override
    public void addCollectionItem(int collectionid, int documentid) {
        CollectionItem collectionItem = new CollectionItem("Example Item","Book","John Doe","Example Description","USA","10/10/2024","link",collectionid);
        collectionItem.setId(documentid);
        collectiontems.add(collectionItem);
    }

    @Override
    public void deleteCollectionItem(CollectionItem collectionItem) {
        collectiontems.remove(collectionItem);
    }

    @Override
    public List<CollectionItem> getAllCollectionItems(int id) {
        ArrayList<CollectionItem> collectionItemsIDList = new ArrayList<>();
        for (CollectionItem collectionItem : collectiontems) {
            if (collectionItem.getCollectionid() == id)
            {
                collectionItemsIDList.add(collectionItem);
            }
        }
        return collectionItemsIDList;
    }

    // cant test
    @Override
    public List<Contact> getContactsCollectionID(int collectionid) {
        return null;
    }
}