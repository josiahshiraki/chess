
package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest{

    private ClearService clearService;
    private UserService userService;
    private GameService gameService;

    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;
    private MemoryGameDAO gameDAO;

    @BeforeEach
    public void setUp(){
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    @Test
    public void clearPopulatedDatabase(){
        var userResult = userService.register(new UserData("GYOZA PATI", "ORANGE POTATO", "FACE_BOOK@test.com"));
        String token = userResult.authToken();
        int gameID = gameService.createGame(token, new NewGame("VIET MAN SIM")).gameID();


        clearService.clear();

        assertNull(userDAO.getUser("player1"));
        assertNull(authDAO.getAuth(token));
        assertNull(gameDAO.getGame(gameID));
        assertTrue(gameDAO.listGames().isEmpty());
    }

    @Test
    public void clearEmptyDatabase(){
        assertDoesNotThrow(() -> clearService.clear());

        assertNull(userDAO.getUser("WHAT DA HERR??!"));
        assertNull(authDAO.getAuth("PHO DAC BIET"));
        assertTrue(gameDAO.listGames().isEmpty());
    }

}


