import com.example.cab302prac4.model.Contact;
import com.example.cab302prac4.model.IContactDAO;

import java.util.ArrayList;
import java.util.List;

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
        return List.of();
    }

    @Override
    public List<Contact> getAllContactsSearch(String search) {
        return List.of();
    }

    @Override
    public List<Contact> getAllContactsSearchUserID(String search, int userid) {
        return List.of();
    }

    @Override
    public List<Contact> getAllContactsByID(int ID) {
        return List.of();
    }
}