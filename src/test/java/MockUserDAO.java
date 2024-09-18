import com.example.cab302prac4.model.Collection;
import com.example.cab302prac4.model.IUserDAO;
import com.example.cab302prac4.model.User;

import java.util.ArrayList;
import java.util.List;

public class MockUserDAO implements IUserDAO {
    public final ArrayList<User> users = new ArrayList<>();
    private int autoIncrementedId = 0;

    @Override
    public void addUser(User user) {
        user.setUserID(autoIncrementedId);
        autoIncrementedId++;
        users.add(user);
    }

    @Override
    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserID() == user.getUserID()) {
                users.set(i, user);
                break;
            }
        }
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user);
    }

    @Override
    public User getUser(int userID) {
        for (User user : users) {
            if (user.getUserID() == userID) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        for (User user : users)
        {
            if (user.getEmail() == email);
            {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}
