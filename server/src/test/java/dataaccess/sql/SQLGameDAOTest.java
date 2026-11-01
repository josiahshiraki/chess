
package dataaccess.sql;

import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTest{

    private SQLGameDAO gameDAO;

    @BeforeEach
    public void setup() throws DataAccessException{
        DatabaseManager.createDatabase();
        gameDAO = new SQLGameDAO();
        gameDAO.clear();
    }

    @Test
    public void insertGameSuccess() throws DataAccessException{
        int gameId = gameDAO.insertGame("whatDaHerr");

        assertTrue(gameId > 0);
    }

    @Test
    public void getGameSuccess() throws DataAccessException{
        int gameId = gameDAO.insertGame("umbrella");

        GameData game = gameDAO.getGame(gameId);

        assertNotNull(game);
        assertEquals(gameId, game.gameID());
        assertEquals("umbrella", game.gameName());
        assertNotNull(game.game());
    }

    @Test
    public void getGameNotFound() throws DataAccessException{
        assertNull(gameDAO.getGame(67676767));
    }

    @Test
    public void listGamesEmpty() throws DataAccessException{
        Collection<GameData> games = gameDAO.listGames();

        assertNotNull(games);
        assertTrue(games.isEmpty());
    }

    @Test
    public void listMultipleGames() throws DataAccessException{
        gameDAO.insertGame("One");
        gameDAO.insertGame("Two");
        gameDAO.insertGame("Three");

        Collection<GameData> games = gameDAO.listGames();

        assertEquals(3, games.size());
    }

    @Test
    public void joinWhiteSuccess() throws DataAccessException{
        int gameId = gameDAO.insertGame("Happy");

        gameDAO.joinGame(gameId, "creep", "WHITE");

        GameData game = gameDAO.getGame(gameId);

        assertEquals("creep", game.whiteUsername());
        assertNull(game.blackUsername());
    }

    @Test
    public void joinBlackSuccess() throws DataAccessException{
        int gameId = gameDAO.insertGame("Happy");

        gameDAO.joinGame(gameId, "potato", "BLACK");

        GameData game = gameDAO.getGame(gameId);

        assertEquals("potato", game.blackUsername());
        assertNull(game.whiteUsername());
    }

    @Test
    public void joinOccupiedSeat() throws DataAccessException{
        int gameId = gameDAO.insertGame("Happy");
        gameDAO.joinGame(gameId, "Josiah", "WHITE");

        try {
            gameDAO.joinGame(gameId, "RELEASE_ME", "WHITE");
            fail("Expected DataAccessException to be thrown");
        } catch (DataAccessException e) {
            //test will fail, catch
        }

        assertEquals("Josiah", gameDAO.getGame(gameId).whiteUsername());
    }

    @Test
    public void clearGames() throws DataAccessException{
        gameDAO.insertGame("Kasparov");
        gameDAO.insertGame("GothamChess");

        gameDAO.clear();

        assertTrue(gameDAO.listGames().isEmpty());
    }
}
