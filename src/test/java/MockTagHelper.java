import java.util.Objects;

public class MockTagHelper {
    private int documentId;
    private String tag;

    public MockTagHelper(int documentId, String tag) {
        this.documentId = documentId;
        this.tag = tag;
    }

    public int getTagId() {
        return documentId;
    }

    public void setTagId(int documentId) {
        this.documentId = documentId;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "TagEntry{documentId=" + documentId + ", tag='" + tag + "'}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MockTagHelper other = (MockTagHelper) obj;
        return documentId == other.documentId && tag.equals(other.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentId, tag);
    }


}
