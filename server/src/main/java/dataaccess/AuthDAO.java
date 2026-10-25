package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clear();
    void createAuth(AuthData authData);
    AuthData getAuth(String AuthToken);
    void deleteAuth(String AuthToken);
}
