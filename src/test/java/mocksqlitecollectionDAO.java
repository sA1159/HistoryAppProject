import com.example.cab302prac4.model.Collection;
import com.example.cab302prac4.model.ICollectionDAO;

import java.util.ArrayList;
import java.util.List;

public class mocksqlitecollectionDAO implements ICollectionDAO {
    private final List<Collection> collections = new ArrayList<>();
    private int autoIncrementId = 1;

    @Override
    public void addCollection(Collection collection) {
        collection.setId(autoIncrementId++);
        collections.add(collection);
    }

    @Override
    public void updateCollection(Collection collection) {
        for (int i = 0; i < collections.size(); i++) {
            if (collections.get(i).getId() == collection.getId()) {
                collections.set(i, collection);
                return;
            }
        }
    }

    @Override
    public void deleteCollection(Collection collection) {
        collections.removeIf(c -> c.getId() == collection.getId());
    }

    @Override
    public Collection getCollection(int id) {
        return collections.stream()
                .filter(collection -> collection.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Collection> getAllCollections() {
        return new ArrayList<>(collections);
    }

    @Override
    public List<Collection> getAllCollectionsByID(int currentUserid) {
        List<Collection> result = new ArrayList<>();
        for (Collection collection : collections) {
            if (collection.getUserid() == currentUserid) {
                result.add(collection);
            }
        }
        return result;
    }

    @Override
    public List<Collection> getAllCollectionItemsSearch(String search) {
        List<Collection> result = new ArrayList<>();
        for (Collection collection : collections) {
            if (collection.getTitle().contains(search) ||
                    collection.getMaker().contains(search) ||
                    collection.getDescription().contains(search) ||
                    collection.getDate().contains(search)) {
                result.add(collection);
            }
        }
        return result;
    }

    @Override
    public List<Collection> getAllCollectionItemsSearchByID(String search, int currentUserid) {
        List<Collection> result = new ArrayList<>();
        for (Collection collection : collections) {
            if ((collection.getTitle().contains(search) ||
                    collection.getMaker().contains(search) ||
                    collection.getDescription().contains(search) ||
                    collection.getDate().contains(search)) &&
                    collection.getUserid() == currentUserid) {
                result.add(collection);
            }
        }
        return result;
    }
}