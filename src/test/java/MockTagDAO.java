import com.example.cab302prac4.model.Collection;

import com.example.cab302prac4.model.ITagDAO;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;

import java.util.ArrayList;
import java.util.List;

public class MockTagDAO implements ITagDAO
{
    private List<String> tags = new ArrayList<>();
    private boolean calledAddTags = false;
    private boolean calledUpdateTags = false;
    private boolean calledDeleteTags = false;

    @Override
    public void addTags(List<String> tagsList, int documentid) //method to add test tags
    {
        this.tags.addAll(tagsList);
        calledAddTags = true;
    }

    @Override
    public void updateTags(List<String> tagsList, int documentid) //method to update test tags
    {
        this.tags = tagsList;
        calledUpdateTags = true;
    }

    @Override
    public void deleteTags(int documentid)  //method to delete a tag
    {
        this.tags.clear();
        calledDeleteTags = true;
    }

    @Override
    public List<String> getTags(int documentid) {
        return List.of();
    }


    public boolean isCalledAddTags()
    {
        return calledAddTags;
    }

    public boolean isCalledUpdateTags()
    {
        return calledUpdateTags;
    }

    public boolean isCalledDeleteTags()
    {
        return calledDeleteTags;
    }

    public List<String> getMockTags() //method gives tags stored in tester variable
    {
        return tags;
    }
}
