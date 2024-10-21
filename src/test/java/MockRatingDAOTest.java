import com.example.cab302prac4.model.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MockRatingDAOTest {
    private MockRatingDAO ratingDAO;

    @BeforeEach
    public void setUp() {
        ratingDAO = new MockRatingDAO();
    }

    @Test
    public void testAddRating()  //ensures ratings are added successfully
    {
        int userId = 1;
        int documentId = 100;
        ratingDAO.addRating(userId, documentId);

        assertTrue(ratingDAO.checkIfRated(userId, documentId));
        assertEquals(1, ratingDAO.getRatingScoreForDocument(documentId));
    }

    @Test
    public void testRemoveRating() //ensures ratings are removed successfully
    {
        int userId = 1;
        int documentId = 100;
        ratingDAO.addRating(userId, documentId);
        ratingDAO.removeRating(userId, documentId);

        assertFalse(ratingDAO.checkIfRated(userId, documentId));
        assertEquals(0, ratingDAO.getRatingScoreForDocument(documentId));
    }

    @Test
    public void testCheckIfRated()
    {
        int userId = 1;
        int documentId = 100;

        assertFalse(ratingDAO.checkIfRated(userId, documentId)); // Not rated yet

        ratingDAO.addRating(userId, documentId);

        assertTrue(ratingDAO.checkIfRated(userId, documentId)); // Rated after add
    }

    @Test
    public void testGetRatingScoreForDocument()
    {
        int documentId = 100;

        assertEquals(0, ratingDAO.getRatingScoreForDocument(documentId)); // No ratings yet

        ratingDAO.addRating(1, documentId);
        ratingDAO.addRating(2, documentId);
        ratingDAO.addRating(3, documentId);

        assertEquals(3, ratingDAO.getRatingScoreForDocument(documentId)); // After 3 ratings
    }

    @Test
    public void testRemoveAllDocumentRatings()
    {
        int documentId = 100;
        ratingDAO.addRating(1, documentId);
        ratingDAO.addRating(2, documentId);

        assertEquals(2, ratingDAO.getRatingScoreForDocument(documentId));

        ratingDAO.removeAllDocumentRatings(documentId);

        assertEquals(0, ratingDAO.getRatingScoreForDocument(documentId));
        assertFalse(ratingDAO.checkIfRated(1, documentId));
        assertFalse(ratingDAO.checkIfRated(2, documentId));
    }

    @Test
    public void testGetUserTotalRatingScore() //testing that when we try and obtain the total rating score, the correct value is obtained
    {
        int userId = 1;

        assertEquals(0, ratingDAO.getUserTotalRatingScore(userId)); // No ratings yet

        ratingDAO.addRating(userId, 100);
        ratingDAO.addRating(userId, 101);
        ratingDAO.addRating(userId, 102);

        assertEquals(3, ratingDAO.getUserTotalRatingScore(userId)); // After 3 ratings
    }

    @Test
    public void testGetAllRatedDocuments() //all the correct ratings documents are obtained
    {
        int userId = 1;

        ratingDAO.addRating(userId, 100);
        ratingDAO.addRating(userId, 101);

        List<Contact> ratedDocuments = ratingDAO.getAllRatedDocuments(userId);

        assertEquals(2, ratedDocuments.size());
        assertEquals(100, ratedDocuments.get(0).getId());
        assertEquals(101, ratedDocuments.get(1).getId());
    }
}
