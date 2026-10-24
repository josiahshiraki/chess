package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    private static final HashMap<String, UserData> auth = new HashMap<>();
    @Override
    public void clear() {
        auth.clear();
    }
}
