package Tests;

import com.example.cab302prac4.model.Tag;
import com.example.cab302prac4.model.TagInterface;
import com.example.cab302prac4.model.SqliteTagDAO;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagDAOTest {
    private TagInterface tagDAO;
    private Tag[] tags = {
            new Tag("Blitzkrieg", 1),
            new Tag("USA", 2),
            new Tag("WW2", 1),
            new Tag("Apollo11", 2),
            new Tag("History", 3)
    };

    @BeforeEach
    public void setUp() {
        tagDAO = new MockTagDAO();
    }

    @Test
    public void testAddTag() {
        tagDAO.addTag(tags[0]);
        assertEquals(tagDAO.getTag(0), tags[0]);
    }

    @Test
    public void testDeleteTag() {
        for (Tag tag : tags) {
            tagDAO.addTag(tag);
        }
        Tag deleteTag = tags[1];
        deleteTag.setTagId(1);
        tagDAO.deleteTag(deleteTag);
        assertEquals(tagDAO.getTag(1), null);
    }

    @Test
    public void testGetTag() {
        for (Tag tag : tags) {
            tagDAO.addTag(tag);
        }
        assertEquals(tagDAO.getTag(3), tags[3]);
    }

    @Test
    public void testTagNotExist() {
        for (Tag tag : tags) {
            tagDAO.addTag(tag);
        }
        assertEquals(tagDAO.getTag(10), null);
    }

    @Test
    public void testGetTagNegativeIndex() {
        for (Tag tag : tags) {
            tagDAO.addTag(tag);
        }
        assertEquals(tagDAO.getTag(-1), null);
    }

    @Test
    public void testGetAllTags() {
        ArrayList<Tag> tagList = new ArrayList<>(Arrays.asList(tags));
        for (Tag tag : tags) {
            tagDAO.addTag(tag);
        }
        assertEquals(tagDAO.getAllTags(), tagList);
    }

    @Test
    public void testGetAllTagsIfNoneExist() {
        ArrayList<Tag> emptyCollectionList = new ArrayList<>();
        assertEquals(tagDAO.getAllTags(),emptyCollectionList);
    }
}
