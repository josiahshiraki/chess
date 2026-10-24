package dataaccess;

import java.util.Map;
import java.util.HashMap;

import model.UserData;
import model.AuthData;
import model.GameData;

public class MemoryUserDAO implements UserDAO {
    private static final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void clear(){
        users.clear();
    }

    @Override
    public void createUser(UserData user) {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }
}
