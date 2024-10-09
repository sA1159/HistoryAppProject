package test;

import com.example.cab302prac4.model.Contact;
import com.example.cab302prac4.model.IContactDAO;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;

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
    }

    @Test
    public void testAddContact() {
        contactDAO.addContact(contacts[0]);
        assertEquals(contactDAO.getContact(0), contacts[0]);
    }

    @Test
    public void testUpdateContact() {
        for (Contact contact : contacts) {
            contactDAO.addContact(contact);
        }
        Contact updateContact = new Contact("Update TEST", "Update TEST", "Update TEST", "Update TEST", "Update TEST","19/12/2024", "https://TEST.com", 1);
        updateContact.setId(1);
        contactDAO.updateContact(updateContact);
        assertEquals(contactDAO.getContact(1), updateContact);
    }

    @Test
    public void testDeleteContacts() {
        for (Contact contact : contacts) {
            contactDAO.addContact(contact);
        }
        Contact deleteContact = contacts[1];
        deleteContact.setId(1);
        contactDAO.deleteContact(deleteContact);
        assertEquals(contactDAO.getContact(1), null);
    }

    @Test
    public void testGetContact() {
        for (Contact contact : contacts) {
            contactDAO.addContact(contact);
        }
        assertEquals(contactDAO.getContact(3), contacts[3]);
    }

    @Test
    public void testGetContactNotExist() {
        for (Contact contact : contacts) {
            contactDAO.addContact(contact);
        }
        assertEquals(contactDAO.getContact(10), null);
    }

    @Test
    public void testGetContactNegativeIndex() {
        for (Contact contact : contacts) {
            contactDAO.addContact(contact);
        }
        assertEquals(contactDAO.getContact(-1), null);
    }

    @Test
    public void testGetAllContacts() {
        ArrayList<Contact> contactList = new ArrayList<>(Arrays.asList(contacts));
        for (Contact contact : contacts) {
            contactDAO.addContact(contact);
        }
        assertEquals(contactDAO.getAllContacts(), contactList);
    }

    @Test
    public void testGetAllContactsIfNoneExist() {
        ArrayList<Contact> emptyContactList = new ArrayList<>();
        assertEquals(contactDAO.getAllContacts(),emptyContactList);
    }
}
