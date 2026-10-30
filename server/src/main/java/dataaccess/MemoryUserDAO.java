package dataaccess;

import java.util.HashMap;

import model.*;

public class MemoryUserDAO implements UserDAO {

    //<username, UserData>
    private final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        users.clear();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        //START HERE GO!!!!
        users.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }

}
