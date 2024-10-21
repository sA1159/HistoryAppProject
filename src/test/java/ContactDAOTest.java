import com.example.cab302prac4.model.Contact;
import com.example.cab302prac4.model.IContactDAO;
import com.example.cab302prac4.model.SqliteContactDAO;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactDAOTest {
    private IContactDAO contactDAO;
    private Contact[] contacts = {
            new Contact("WW2 Collection", "Book", "Joe Bloggs", "Collection of World War 2 documents", "Germany","19/12/1950", "https://WW2TEST.com", 1),
            new Contact("WW1 Collection", "Book", "Joe Bloggs", "Collection of World War 1 documents", "Germany","01/01/1948", "https://WW1TEST.com", 1),
            new Contact("US Army Collection", "Book", "Jeffery Smith", "Collection of American war/army documents", "America","19/12/2000", "https://USATEST.com", 1),
            new Contact("England Great War Collection", "Book", "Jackson Miller", "Collection of World War 1 documents and pictures", "England","19/12/1950", "https://ENGTEST.com", 1),
            new Contact("Australian Digger's Collection", "Book", "Corey Blackburn", "Collection of World War 2 documents", "Germany","19/12/1950", "https://AUSTEST.com", 1)

    };

    @BeforeEach
    public void setUp() {
        contactDAO = new MockContactDAO();
        for (Contact contact : contacts) {
            contactDAO.addContact(contact);
        }
    }

    @AfterEach
    public void tearDown() {
        MockContactDAO.contacts.clear();
    }

    @Test
    void testAddContact() {
        Contact newContact = new Contact("New Collection", "Magazine", "New Author",
                "A new war collection", "France",
                "12/12/2024", "https://NEWTEST.com", 2);
        contactDAO.addContact(newContact);

        List<Contact> allContacts = contactDAO.getAllContacts();
        assertEquals(6, allContacts.size());
        assertEquals(newContact, allContacts.get(5));
    }

    @Test
    void testUpdateContact() {
        Contact contactToUpdate = contacts[0];  // WW2 Collection
        contactToUpdate.setTitle("Updated WW2 Collection");
        contactDAO.updateContact(contactToUpdate);

        Contact updatedContact = contactDAO.getContact(contactToUpdate.getId());
        assertEquals("Updated WW2 Collection", updatedContact.getTitle());
    }

    @Test
    void testDeleteContact() {
        Contact contactToDelete = contacts[1];  // WW1 Collection
        contactDAO.deleteContact(contactToDelete);

        assertNull(contactDAO.getContact(contactToDelete.getId()));
        assertEquals(4, contactDAO.getAllContacts().size());
    }

    @Test
    void testGetContactById() {
        Contact foundContact = contactDAO.getContact(contacts[2].getId());  // US Army Collection
        assertNotNull(foundContact);
        assertEquals("US Army Collection", foundContact.getTitle());

        // Edge Case: Non-existent contact ID
        assertNull(contactDAO.getContact(999));
    }

    @Test
    void testGetAllContacts() {
        List<Contact> allContacts = contactDAO.getAllContacts();
        assertEquals(5, allContacts.size());
        assertTrue(allContacts.contains(contacts[0]));
        assertTrue(allContacts.contains(contacts[4]));
    }

    @Test
    void testGetAllContactsByID() {
        List<Contact> userContacts = contactDAO.getAllContactsByID(1);
        assertEquals(5, userContacts.size());

        // Edge Case: No contacts for user ID 2
        assertTrue(contactDAO.getAllContactsByID(2).isEmpty());
    }

    @Test
    void testGetAllContactsSearch() {
        List<Contact> searchResults = contactDAO.getAllContactsSearch("WW2");
        assertEquals(1, searchResults.size());
        assertEquals("WW2 Collection", searchResults.get(0).getTitle());

        // Edge Case: No match found
        assertTrue(contactDAO.getAllContactsSearch("NonExistent").isEmpty());

        // Case-insensitive search
        searchResults = contactDAO.getAllContactsSearch("us army");
        assertEquals(1, searchResults.size());
        assertEquals("US Army Collection", searchResults.get(0).getTitle());
    }

    @Test
    void testGetAllContactsSearchUserID() {
        List<Contact> searchResults = contactDAO.getAllContactsSearchUserID("Collection", 1);
        assertEquals(5, searchResults.size());

        assertTrue(contactDAO.getAllContactsSearchUserID("Collection", 2).isEmpty());

        assertTrue(contactDAO.getAllContactsSearchUserID("NonExistent", 999).isEmpty());
    }

    @Test
    void testAddContactWithInvalidFields() {
        Contact invalidContact = new Contact("", "", null, "", "Germany", "32/13/2050", "invalid-url", 1);

        List<Contact> allContacts = contactDAO.getAllContacts();
        assertFalse(allContacts.contains(invalidContact), "Invalid contact should not be added.");
    }

    @Test
    void testUpdateNonExistentContact() {
        Contact nonExistentContact = new Contact("Non-Existent", "Book", "John Doe", "Test", "France", "01/01/2025", "https://nonexistent.com", 99);
        List<Contact> allContacts = contactDAO.getAllContacts();
        assertFalse(allContacts.contains(nonExistentContact), "Contact does not exist.");
    }

    @Test
    void testDeleteAlreadyDeletedContact() {
        contactDAO.deleteContact(contacts[0]);
        assertDoesNotThrow(() -> contactDAO.deleteContact(contacts[0]));
    }

    @Test
    void testGetContactWithInvalidID() {
        assertNull(contactDAO.getContact(-1));
        assertNull(contactDAO.getContact(0));
        assertNull(contactDAO.getContact(Integer.MAX_VALUE));
    }

    @Test
    void testGetAllContactsWhenEmpty() {
        MockContactDAO.contacts.clear();
        List<Contact> allContacts = contactDAO.getAllContacts();
        assertTrue(allContacts.isEmpty(), "Contact list should be empty.");
    }

    @Test
    void testGetAllContactsByInvalidUserID() {
        List<Contact> contacts = contactDAO.getAllContactsByID(-1);
        assertTrue(contacts.isEmpty());

        contacts = contactDAO.getAllContactsByID(999);
        assertTrue(contacts.isEmpty());
    }

    @Test
    void testSearchWithEmptyString() {
        List<Contact> searchResults = contactDAO.getAllContactsSearch("");
        assertEquals(contacts.length, searchResults.size(), "Search with empty string should return all contacts.");
    }

    @Test
    void testSearchWithDifferentCasing() {
        List<Contact> results = contactDAO.getAllContactsSearch("ww2");
        assertEquals(1, results.size(), "Search should be case-insensitive.");
        assertEquals("WW2 Collection", results.get(0).getTitle());
    }

    @Test
    void testSearchWithNonExistentEntry() {
        List<Contact> results = contactDAO.getAllContactsSearch("Unknown");
        assertTrue(results.isEmpty(), "Search with non-existent entry should return no results.");
    }

    @Test
    void testSearchWithIdenticalFields() {
        contactDAO.addContact(new Contact("WW2 Collection", "Book", "Joe Bloggs", "Duplicate entry", "Germany", "19/12/1950", "https://duplicate.com", 1));

        List<Contact> results = contactDAO.getAllContactsSearch("WW2 Collection");
        assertEquals(2, results.size(), "Search should return all contacts with matching titles.");
    }
}
