package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    private final HashMap<String, AuthData> authTokens = new HashMap<>();
    @Override
    public void clear() throws DataAccessException {
        authTokens.clear();
    }
    public void createAuth(AuthData auth) throws DataAccessException {
        authTokens.put(auth.authToken(), auth);
    }
    public AuthData getAuth(String AuthToken) throws DataAccessException {
        return authTokens.get(AuthToken);
    }
    public void deleteAuth(String AuthToken) throws DataAccessException {
        authTokens.remove(AuthToken);
    }
}
