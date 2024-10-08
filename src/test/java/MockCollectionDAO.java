import com.example.cab302prac4.model.Collection;
import com.example.cab302prac4.model.ICollectionDAO;
import com.example.cab302prac4.model.ICollectionItemDAO;

import java.util.ArrayList;
import java.util.List;

public class MockCollectionDAO implements ICollectionDAO {
    /**
     * A static list of contacts to be used as a mock database.
     */
    public final ArrayList<Collection> collections = new ArrayList<>();
    private int autoIncrementedId = 0;

    @Override
    public void addCollection(Collection collection) {
        collection.setId(autoIncrementedId);
        autoIncrementedId++;
        collections.add(collection);
    }

    @Override
    public void updateCollection(Collection collection) {
        for (int i = 0; i < collections.size(); i++) {
            if (collections.get(i).getId() == collection.getId()) {
                collections.set(i, collection);
                break;
            }
        }
    }

    @Override
    public void deleteCollection(Collection collection) {
        collections.remove(collection);
    }

    @Override
    public Collection getCollection(int id) {
        for (Collection collection : collections) {
            if (collection.getId() == id) {
                return collection;
            }
        }
        return null;
    }

    @Override
    public List<Collection> getAllCollections() {
        return new ArrayList<>(collections);
    }

    @Override
    public List<Collection> getAllCollectionsByID(int id) {
        ArrayList<Collection> collectionList = new ArrayList<>();
        for (Collection collection : collections) {
            if (collection.getUserid() == id)
            {
                collectionList.add(collection);
            }
        }
        return collectionList;
    }

    @Override
    public List<Collection> getAllCollectionItemsSearch(String search) {
        return List.of();
    }

    @Override
    public List<Collection> getAllCollectionItemsSearchByID(String search, int userid) {
        return List.of();
    }
}