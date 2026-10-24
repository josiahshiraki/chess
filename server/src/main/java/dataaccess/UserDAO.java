package dataaccess;

import model.RegisterResult;
import model.UserData;

public interface UserDAO {
    void clear();
    void createUser(UserData user);
    UserData getUser(String username);
}
