package test;

import com.example.cab302prac4.model.Tag;
import com.example.cab302prac4.model.TagInterface;

import java.util.ArrayList;
import java.util.List;

public class MockTagDAO implements TagInterface {
    /**
     * A static list of tags to be used as a mock database.
     */
    public static final ArrayList<Tag> tags = new ArrayList<>();
    private static int autoIncrementedId = 0;

    public MockTagDAO() {
        // Add some initial tags to the mock database
        addTag(new Tag("a", 1));
    }

    @Override
    public void addTag(Tag tag) {
        tag.setTagId(autoIncrementedId);
        autoIncrementedId++;
        tags.add(tag);
    }


    @Override
    public void deleteTag(Tag tag) {
        tags.remove(tag);
    }

    @Override
    public Tag getTag(int id) {
        for (Tag tag : tags) {
            if (tag.getTagId() == id) {
                return tag;
            }
        }
        return null;
    }

    @Override
    public List<Tag> getAllTags() {
        return new ArrayList<>(tags);
    }
}