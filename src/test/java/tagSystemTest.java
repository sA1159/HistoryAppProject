//
//import com.example.cab302prac4.model.TagSystem;
//import javafx.scene.control.Button;
//import javafx.scene.layout.HBox;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class tagSystemTest
//{
//    private TagSystem tagSystem; //declaring instance of TagSystem for testing
//    private MockTagDAO mockTagDAO; //declaring instance of MockTagDAO for testing
//    private HBox box; //declaring instance of HBox for testing
//
//    @BeforeEach
//    public void setUp() //this method initialises variables for the purpose of testing
//    {
//        mockTagDAO = new MockTagDAO();
//        tagSystem = new TagSystem(mockTagDAO, true);
//        box = new HBox();
//    }
//
//    @Test
//    public void testAddTagsWhenTagsExist() //testing tags will be added successfully, when other tags exist
//    {
//        tagSystem.tags = List.of("Tag1", "Tag2");
//        tagSystem.addTags(1);
//
//        // Verify the updateTags method was called
//        assertTrue(mockTagDAO.isCalledUpdateTags());
//        assertEquals(List.of("Tag1", "Tag2"), mockTagDAO.getMockTags());
//    }
//
//    @Test
//    public void testAddTagsWhenNoTagsExist() //testing tags will be added successfully, when no other tags exist
//    {
//        // Simulate no tags
//        tagSystem.tags = new ArrayList<>();
//        tagSystem.addTags(1);
//
//        assertTrue(mockTagDAO.isCalledAddTags()); //ensuring this method was called
//    }
//
//    @Test
//    public void testRemoveTags() //ensuring tags are removed successfully
//    {
//        // Simulate removing tags
//        tagSystem.removeTags(1);
//
//        // Verify the deleteTags method was called
//        assertTrue(mockTagDAO.isCalledDeleteTags());
//    }
//
//    @Test
//    public void testTagButtonCreatesButton() //ensures button is in input box
//    {
//        String testTag = "TestTag";
//
//        tagSystem.tagButton(box, testTag);
//
//        assertEquals(1, box.getChildren().size());
//    }
//
//    @Test
//    public void testRemoveTagButton() //ensuring button and tags where removed when needed
//    {
//        String testTag = "TagToRemove";
//
//        tagSystem.tags = new ArrayList<>(List.of(testTag));
//        tagSystem.tagButton(box, testTag);
//        assertEquals(1, box.getChildren().size());
//
//        tagSystem.removeTagButton(box, (Button) box.getChildren().get(0), testTag);
//
//        assertEquals(0, box.getChildren().size());
//        assertFalse(tagSystem.tags.contains(testTag));
//    }
//
//    @Test
//    public void testGetTagsPopulatesHBox() //ensures box has text which it needs
//    {
//        mockTagDAO.addTags(List.of("Tag1", "Tag2"), 1);
//
//        tagSystem.getTags(1, box);
//        assertEquals(2, box.getChildren().size());
//    }
//}
