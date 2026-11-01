package dataaccess.sql;

import dataaccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTest {

    private SQLUserDAO userDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        DatabaseManager.createDatabase();
        userDAO = new SQLUserDAO();
        userDAO.clear();
    }

    @Test
    public void createUserSuccess() throws DataAccessException{
        UserData user = new UserData("player1", "password123", "player1@test.com");
        userDAO.createUser(user);
        assertNotNull(userDAO.getUser("player1"));
    }

    @Test
    public void getUserSuccess() throws DataAccessException{
        UserData user = new UserData("player1", "password123", "player1@test.com");
        userDAO.createUser(user);
        UserData retrieved = userDAO.getUser("player1");

        assertEquals("player1", retrieved.username());
        assertTrue(BCrypt.checkpw("password123", retrieved.password()));
        assertEquals("player1@test.com", retrieved.email());
    }

    @Test
    public void getUserNotFound() throws DataAccessException{
        assertNull(userDAO.getUser("tu haha"));
    }

    @Test
    public void duplicateUsername() throws DataAccessException{
        UserData user = new UserData("player1", "password123", "player1@test.com");
        userDAO.createUser(user);

        assertThrows(DataAccessException.class, () -> userDAO.createUser(user));
    }

    @Test
    public void storeHashedPassword() throws DataAccessException{

        UserData user = new UserData("player1", "password123", "player1@test.com");
        userDAO.createUser(user);
        UserData toGet = userDAO.getUser("player1");

        assertNotEquals("password123", toGet.password()); //check not-encrypted given pass
        assertTrue(org.mindrot.jbcrypt.BCrypt.checkpw("password123", toGet.password())); //check encrypt
    }

    @Test
    public void createMultipleUsers() throws DataAccessException{
        userDAO.createUser(new UserData("player1", "pass1", "one@test.com"));
        userDAO.createUser(new UserData("player2", "pass2", "two@test.com"));

        assertNotNull(userDAO.getUser("player1"));
        assertNotNull(userDAO.getUser("player2"));
    }

    @Test
    public void clearUsers() throws DataAccessException{
        userDAO.createUser(new UserData("player1", "pass1", "one@test.com"));

        userDAO.createUser(new UserData("player2", "pass2", "two@test.com"));

        userDAO.clear();

        assertNull(userDAO.getUser("player1"));
        assertNull(userDAO.getUser("player2"));
    }

    @Test
    public void createUserAfterClear() throws DataAccessException{
        UserData user = new UserData("player1", "password123", "player1@test.com");

        userDAO.createUser(user);
        userDAO.clear();
        userDAO.createUser(user);

        assertNotNull(userDAO.getUser("player1"));
    }
}
