package dataaccess;

import java.util.HashMap;

import model.*;

public class MemoryUserDAO implements UserDAO {

    //<username, UserData>
    private final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void clear(){
        users.clear();
    }

    @Override
    public void createUser(UserData user) {
        //START HERE GO!!!!
        users.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

}
