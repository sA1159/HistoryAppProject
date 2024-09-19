package Tests;

import com.example.cab302prac4.model.Tag;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagTest {
    Tag tag1, tag2, tag3;

    @BeforeEach
    void setUp() {
        tag1 = new Tag("ExampleTag1",1);
    }

    @Test
    public void testGetTagId() {
        tag1.setTagId(1);
        assertEquals(1, tag1.getTagId());
    }

    @Test
    public void testGetTagTest() {
        assertEquals("ExampleTag1", tag1.getTagText());
    }

    @Test
    public void testSetTitle() {
        tag1.setTagText("TESTTAG2");
        assertEquals("TESTTAG2", tag1.getTagText());
    }

    @Test
    public void testGetDocumentID() {
        assertEquals(1, tag1.getDocumentId());
    }
    @Test
    public void testSetDocumentID() {
        tag1.setDocumentId(2);
        assertEquals(2, tag1.getDocumentId());
    }
}
