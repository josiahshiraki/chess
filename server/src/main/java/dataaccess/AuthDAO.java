package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clear() throws DataAccessException;
    void createAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth(String AuthToken) throws DataAccessException;
    void deleteAuth(String AuthToken) throws DataAccessException;
}
