import com.example.cab302prac4.model.Contact;
import com.example.cab302prac4.model.ITagDAO;

import java.util.ArrayList;
import java.util.List;


public class MockTagDAO implements ITagDAO{
    public final ArrayList<MockTagHelper> tags = new ArrayList<>();


    @Override
    public void addTags(List<String> tagsList, int documentid) {
        for (String tag : tagsList) {
            tags.add(new MockTagHelper(documentid, tag));
        }
    }

    @Override
    public void updateTags(List<String> tagsList, int documentId) {
        boolean found = false;

        // Check if the documentId exists and update tags if found
        for (int i = 0; i < tags.size(); i++) {
            if (tags.get(i).getTagId() == documentId) {
                tags.removeIf(tagEntry -> tagEntry.getTagId() == documentId);
                found = true;
                break;
            }
        }

        // Add new tags regardless of whether the documentId existed or not
        for (String tag : tagsList) {
            tags.add(new MockTagHelper(documentId, tag));
        }
    }

    @Override
    public void deleteTags(int documentid) {
        tags.removeIf(tagEntry -> tagEntry.getTagId() == documentid);

    }

    @Override
    public List<String> getTags(int documentid) {
        List<String> result = new ArrayList<>();
        for (MockTagHelper tag : tags) {
            if (tag.getTagId() == documentid) {
                result.add(tag.getTag());
            }
        }
        return result;
    }
}
