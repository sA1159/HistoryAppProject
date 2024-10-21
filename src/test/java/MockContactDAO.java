import com.example.cab302prac4.model.Contact;
import com.example.cab302prac4.model.IContactDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MockContactDAO implements IContactDAO {
    /**
     * A static list of contacts to be used as a mock database.
     */
    public static final ArrayList<Contact> contacts = new ArrayList<>();
    private static int autoIncrementedId = 0;

    @Override
    public void addContact(Contact contact) {
        contact.setId(autoIncrementedId);
        autoIncrementedId++;
        contacts.add(contact);
    }

    @Override
    public void updateContact(Contact contact) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId() == contact.getId()) {
                contacts.set(i, contact);
                break;
            }
        }
    }

    @Override
    public void deleteContact(Contact contact) {
        contacts.remove(contact);
    }

    @Override
    public Contact getContact(int id) {
        for (Contact contact : contacts) {
            if (contact.getId() == id) {
                return contact;
            }
        }
        return null;
    }

    @Override
    public List<Contact> getAllContacts() {
        return new ArrayList<>(contacts);
    }

    @Override
    public List<Contact> getAllContactsSearch(String search) {
        String searchTerm = search.toLowerCase();
        return contacts.stream().filter(c -> containsIgnoreCase(c, searchTerm)).collect(Collectors.toList());

    }

    @Override
    public List<Contact> getAllContactsSearchUserID(String search, int userid) {
        String searchTerm = search.toLowerCase();
        return contacts.stream().filter(c -> c.getUserid() == userid && containsIgnoreCase(c, searchTerm)).collect(Collectors.toList());
    }

    @Override
    public List<Contact> getAllContactsByID(int ID) {
        return contacts.stream()
                .filter(c -> c.getUserid() == ID)
                .collect(Collectors.toList());
    }

    private boolean containsIgnoreCase(Contact contact, String search) {
        return contact.getTitle().toLowerCase().contains(search) ||
                contact.getType().toLowerCase().contains(search) ||
                contact.getAuthor().toLowerCase().contains(search) ||
                contact.getDescription().toLowerCase().contains(search) ||
                contact.getLocation().toLowerCase().contains(search) ||
                contact.getDate().toLowerCase().contains(search) ||
                contact.getLink().toLowerCase().contains(search);
        }
    }