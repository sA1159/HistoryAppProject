import com.example.cab302prac4.model.Contact;
import com.example.cab302prac4.model.IRatingDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockRatingDAO implements IRatingDAO {
    private Map<Integer, Map<Integer, Boolean>> ratings; // documentid -> (userid -> rated or not)
    private Map<Integer, List<Integer>> documentRatings; // documentid -> list of userids
    private Map<Integer, List<Integer>> userRatings; // userid -> list of documentids

    public MockRatingDAO() {
        ratings = new HashMap<>();
        documentRatings = new HashMap<>();
        userRatings = new HashMap<>();
    }

    @Override
    public void addRating(int userid, int documentid) {
        ratings.computeIfAbsent(documentid, k -> new HashMap<>()).put(userid, true);
        documentRatings.computeIfAbsent(documentid, k -> new ArrayList<>()).add(userid);
        userRatings.computeIfAbsent(userid, k -> new ArrayList<>()).add(documentid);
    }

    @Override
    public void removeRating(int userid, int documentid) {
        if (ratings.containsKey(documentid)) {
            ratings.get(documentid).remove(userid);
            documentRatings.get(documentid).remove(Integer.valueOf(userid));
        }
        if (userRatings.containsKey(userid)) {
            userRatings.get(userid).remove(Integer.valueOf(documentid));
        }
    }

    @Override
    public boolean checkIfRated(int userid, int documentid) {
        return ratings.containsKey(documentid) && ratings.get(documentid).containsKey(userid);
    }

    @Override
    public int getRatingScoreForDocument(int documentid) {
        if (documentRatings.containsKey(documentid)) {
            return documentRatings.get(documentid).size();
        }
        return 0;
    }

    @Override
    public void removeAllDocumentRatings(int documentid) {
        if (documentRatings.containsKey(documentid)) {
            List<Integer> users = documentRatings.get(documentid);
            for (Integer userid : users) {
                userRatings.get(userid).remove(Integer.valueOf(documentid));
            }
            documentRatings.remove(documentid);
            ratings.remove(documentid);
        }
    }

    @Override
    public int getUserTotalRatingScore(int userid) {
        if (userRatings.containsKey(userid)) {
            return userRatings.get(userid).size();
        }
        return 0;
    }

    @Override
    public List<Contact> getAllRatedDocuments(int currentuserid) {
        List<Contact> contacts = new ArrayList<>();
        if (userRatings.containsKey(currentuserid)) {
            for (int documentid : userRatings.get(currentuserid)) {
                Contact contact = new Contact("Title" + documentid, "Type", "Author", "Description", "Location", "Date", "Link", currentuserid);
                contact.setId(documentid);
                contacts.add(contact);
            }
        }
        return contacts;
    }
}
