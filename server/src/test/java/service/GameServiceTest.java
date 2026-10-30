
package service;

import dataaccess.*;
import exception.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private GameService gameService;
    private UserService userService;
    private MemoryGameDAO gameDAO;

    private String authToken;

    @BeforeEach
    public void setUp(){
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);

        var result = userService.register(new UserData("player1", "password123", "player1@test.com"));
        authToken = result.authToken();
    }

    @Test
    public void createGameSuccess(){
        var result = gameService.createGame(authToken, new NewGame("test game"));
        assertTrue(result.gameID() > 0);

        GameData game = gameDAO.getGame(result.gameID());
        assertNotNull(game);
        assertNull(game.whiteUsername());
        assertNull(game.blackUsername());
    }

    @Test
    public void createGameUnauthorized(){
        assertThrows(UnauthorizedException.class, () -> gameService.createGame("invalid-token", new NewGame("Test Game")));
    }


    @Test
    public void createGameMissingName(){
        assertThrows(BadRequestException.class, () -> gameService.createGame(authToken, new NewGame(null)));
    }

    @Test
    public void createGameBlankName(){
        assertThrows(BadRequestException.class, () -> gameService.createGame(authToken, new NewGame("")));
    }

    @Test
    public void listGamesUnauthorized(){
        assertThrows(UnauthorizedException.class, () -> gameService.listGames("GAY"));
    }

    @Test
    public void joinWhiteSuccess(){
        int gameID = gameService.createGame(authToken, new NewGame("Test Game")).gameID();
        gameService.joinGame(authToken, new JoinGameData("WHITE", gameID));
        GameData game = gameDAO.getGame(gameID);

        assertEquals("player1", game.whiteUsername());
        assertNull(game.blackUsername());
    }

    @Test
    public void joinBlackSuccess(){
        int gameID = gameService.createGame(authToken, new NewGame("Test Game")).gameID();
        gameService.joinGame(authToken, new JoinGameData("BLACK", gameID));

        GameData game = gameDAO.getGame(gameID);

        assertEquals("player1", game.blackUsername());
        assertNull(game.whiteUsername());
    }

    @Test
    public void joinInvalidColor(){
        int gameID = gameService.createGame(authToken, new NewGame("Test Game")).gameID();
        assertThrows(BadRequestException.class, () -> gameService.joinGame(authToken, new JoinGameData("AOI", gameID)));
    }

    @Test
    public void joinInvalidGameID(){
        assertThrows(BadRequestException.class, () -> gameService.joinGame(authToken, new JoinGameData("WHITE", 676767)));
    }



}
