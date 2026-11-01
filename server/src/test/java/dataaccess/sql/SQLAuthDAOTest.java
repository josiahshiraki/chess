
package dataaccess.sql;

import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthDAOTest{

    private SQLAuthDAO authDAO;

    @BeforeEach
    public void setup() throws DataAccessException{
        authDAO = new SQLAuthDAO();
        authDAO.clear();
    }

    @Test
    public void createAuthSuccess() throws DataAccessException{
        AuthData auth = new AuthData("token123", "Josiah");

        authDAO.createAuth(auth);

        AuthData retrieved = authDAO.getAuth("token123");

        assertNotNull(retrieved);
        assertEquals("token123", retrieved.authToken());
        assertEquals("Josiah", retrieved.username());
    }

    @Test
    public void getAuthSuccess() throws DataAccessException{
        AuthData auth = new AuthData("token314", "player2");
        authDAO.createAuth(auth);

        AuthData retrieved = authDAO.getAuth("token314");

        assertNotNull(retrieved);
        assertEquals(auth.authToken(), retrieved.authToken());
        assertEquals(auth.username(), retrieved.username());
    }

    @Test
    public void getAuthNotFound() throws DataAccessException{
        AuthData retrieved = authDAO.getAuth("nashi");

        assertNull(retrieved);
    }

    @Test
    public void multipleTokensForSameUser() throws DataAccessException{
        AuthData first = new AuthData("token1", "Michael");
        AuthData second = new AuthData("token2", "Rachel");

        authDAO.createAuth(first);
        authDAO.createAuth(second);

        assertNotNull(authDAO.getAuth("token1"));
        assertNotNull(authDAO.getAuth("token2"));

        assertEquals("Michael", authDAO.getAuth("token1").username());
        assertEquals("Rachel", authDAO.getAuth("token2").username());
    }

    @Test
    public void deleteAuthSuccess() throws DataAccessException{
        AuthData auth = new AuthData("token123", "Orange_chicken");
        authDAO.createAuth(auth);

        authDAO.deleteAuth("token123");

        assertNull(authDAO.getAuth("token123"));
    }

    @Test
    public void clearAuthTokens() throws DataAccessException{
        authDAO.createAuth(new AuthData("token1", "Henry"));
        authDAO.createAuth(new AuthData("token2", "Phoebe"));

        authDAO.clear();

        assertNull(authDAO.getAuth("token1"));
        assertNull(authDAO.getAuth("token2"));
    }

    @Test
    public void createAuthAfterClear() throws DataAccessException{
        authDAO.createAuth(new AuthData("red", "Josiah"));

        authDAO.clear();

        authDAO.createAuth(new AuthData("blue", "Greg"));

        assertNull(authDAO.getAuth("red"));
        assertNotNull(authDAO.getAuth("blue"));
    }
}
