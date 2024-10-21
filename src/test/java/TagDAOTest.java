import com.example.cab302prac4.model.Collection;
import com.example.cab302prac4.model.ITagDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TagDAOTest {
    private MockTagDAO tagDAO;

    @BeforeEach
    public void setup() {
        // Initialize the MockTagDAO before each test
        tagDAO = new MockTagDAO();

        // Preload some initial tags for testing
        tagDAO.addTags(Arrays.asList("War"), 1);
        tagDAO.addTags(Arrays.asList("Vehicle"), 2);
        tagDAO.addTags(Arrays.asList("Soldiers"), 3);
    }


    @Test
    public void testAddTags() {
        MockTagDAO mockDAO = new MockTagDAO();
        mockDAO.addTags(Arrays.asList("World", "War"), 1);
        List<String> tags = mockDAO.getTags(1);

        assertEquals(2, tags.size());
        assertTrue(tags.contains("World"));
        assertTrue(tags.contains("War"));
    }

    @Test
    public void testUpdateTags() {
        MockTagDAO mockDAO = new MockTagDAO();
        mockDAO.addTags(Arrays.asList("OldTag"), 1);
        mockDAO.updateTags(Arrays.asList("New", "Tag"), 1);

        List<String> tags = mockDAO.getTags(1);
        assertEquals(2, tags.size());
        assertTrue(tags.contains("New"));
        assertTrue(tags.contains("Tag"));
        assertFalse(tags.contains("OldTag"));
    }

    @Test
    public void testDeleteTags() {
        MockTagDAO mockDAO = new MockTagDAO();
        mockDAO.addTags(Arrays.asList("World", "War"), 1);
        mockDAO.deleteTags(1);

        List<String> tags = mockDAO.getTags(1);
        assertTrue(tags.isEmpty());
    }


    // Test adding tags with empty or null input
    @Test
    public void testAddTagsWithEmptyList() {
        tagDAO.addTags(Collections.emptyList(), 4);
        List<String> tags = tagDAO.getTags(4);
        assertTrue(tags.isEmpty(), "Adding an empty list of tags should result in no tags.");
    }

    @Test
    public void testAddTagsWithNullTag() {
        List<String> tagsWithNull = Arrays.asList("ValidTag", null);
        tagDAO.addTags(tagsWithNull, 5);
        List<String> tags = tagDAO.getTags(5);

        assertEquals(2, tags.size());
        assertTrue(tags.contains("ValidTag"), "Valid tag should be added.");
        assertTrue(tags.contains(null), "Null tag should be stored.");
    }

    // Test updating tags for non-existent documentId
    @Test
    public void testUpdateTagsForNonExistentDocumentId() {
        tagDAO.updateTags(Arrays.asList("New", "Tag"), 99); // DocumentId 99 doesn't exist

        List<String> tags = tagDAO.getTags(99);
        assertEquals(2, tags.size(), "Tags should be added for a new documentId.");
        assertTrue(tags.contains("New"));
        assertTrue(tags.contains("Tag"));
    }

    // Test deleting tags for non-existent documentId
    @Test
    public void testDeleteTagsForNonExistentDocumentId() {
        tagDAO.deleteTags(99); // DocumentId 99 doesn't exist
        assertTrue(tagDAO.getTags(99).isEmpty(), "No error should occur when deleting non-existent tags.");
    }

    // Test retrieving tags for non-existent documentId
    @Test
    public void testGetTagsForNonExistentDocumentId() {
        List<String> tags = tagDAO.getTags(99); // DocumentId 99 doesn't exist
        assertTrue(tags.isEmpty(), "Retrieving tags for a non-existent documentId should return an empty list.");
    }

    // Test adding duplicate tags
    @Test
    public void testAddDuplicateTags() {
        tagDAO.addTags(Arrays.asList("Duplicate", "Duplicate"), 6);
        List<String> tags = tagDAO.getTags(6);

        assertEquals(2, tags.size(), "Both duplicate tags should be added.");
        assertTrue(tags.get(0).equals(tags.get(1)), "Tags should be identical.");
    }

    // Test updating tags with empty or null input
    @Test
    public void testUpdateTagsWithEmptyList() {
        tagDAO.updateTags(Collections.emptyList(), 1); // Existing documentId
        List<String> tags = tagDAO.getTags(1);

        assertTrue(tags.isEmpty(), "Updating with an empty list should remove all existing tags.");
    }

    @Test
    public void testUpdateTagsWithNullInput() {
        List<String> tagsWithNull = Arrays.asList(null, "UpdatedTag");
        tagDAO.updateTags(tagsWithNull, 2);
        List<String> tags = tagDAO.getTags(2);

        assertEquals(2, tags.size());
        assertTrue(tags.contains(null), "Null tag should be allowed.");
        assertTrue(tags.contains("UpdatedTag"), "Updated tag should be added.");
    }

    // Test deleting tags after already deleting them
    @Test
    public void testDeleteTagsTwice() {
        tagDAO.deleteTags(3); // First deletion
        tagDAO.deleteTags(3); // Second deletion

        assertTrue(tagDAO.getTags(3).isEmpty(), "Tags should still be empty after multiple deletions.");
    }

    // Test large input of tags
    @Test
    public void testAddLargeNumberOfTags() {
        String[] largeTagArray = new String[1000];
        Arrays.fill(largeTagArray, "Tag");
        tagDAO.addTags(Arrays.asList(largeTagArray), 7);

        List<String> tags = tagDAO.getTags(7);
        assertEquals(1000, tags.size(), "All 1000 tags should be added.");
    }
}
