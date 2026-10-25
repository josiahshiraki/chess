package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    private static final HashMap<String, AuthData> authTokens = new HashMap<>();
    @Override
    public void clear() {
        authTokens.clear();
    }
    public void createAuth(AuthData auth){
        authTokens.put(auth.authToken(), auth);
    }
    public AuthData getAuth(String AuthToken){
        return authTokens.get(AuthToken);
    }
    public void deleteAuth(String AuthToken){
        authTokens.remove(AuthToken);
    }
}
