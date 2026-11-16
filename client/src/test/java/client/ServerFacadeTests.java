
package client;

import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests{

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init(){
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void clearDataBase() throws ResponseException{
        facade.clear();
    }

    @AfterAll
    static void stopServer(){
        server.stop();
    }

    @Test
    public void registerSuccess() throws ResponseException{
        UserData user = new UserData("testuser", "password123", "test@example.com");
        RegisterResult result = facade.register(user);
        assertNotNull(result);
    }

    @Test
    public void registerDuplicate() throws Exception {
        UserData user = new UserData("Sam", "Penguin", "orange_juice@gmail.com");
        facade.register(user);
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.register(user));

        assertEquals(403, exception.getStatusCode());
    }

    @Test
    public void loginTest() throws Exception {
        UserData user = new UserData("Sam", "Penguin", "orange_juice@gmail.com");
        facade.register(user);
        LoginRequest log = new LoginRequest("Sam", "Penguin");
        RegisterResult result = facade.login(log);
        assertEquals("Sam", result.username());
        assertNotNull(result.authToken());
    }

    @Test
    public void logoutTest() throws Exception {
        UserData user = new UserData("Sam", "Penguin", "orange_juice@gmail.com");
        facade.register(user);
        LoginRequest log = new LoginRequest("Sam", "Penguin");
        RegisterResult result = facade.login(log);

        String authToken = result.authToken();

        assertDoesNotThrow(() -> facade.logout(authToken));

    }

    @Test
    public void createGameTest() throws Exception{
        UserData user = new UserData("Sam", "Penguin", "orange_juice@gmail.com");
        facade.register(user);
        LoginRequest log = new LoginRequest("Sam", "Penguin");
        RegisterResult result = facade.login(log);

        CreateGameResult madeGame = facade.createGame(result.authToken(), new NewGame("Sashimi Chess"));
        assertNotNull(madeGame);
    }

    @Test
    public void listGames() throws Exception{
        UserData user = new UserData("Sam", "Penguin", "orange_juice@gmail.com");
        facade.register(user);
        LoginRequest log = new LoginRequest("Sam", "Penguin");
        RegisterResult result = facade.login(log);

        facade.createGame(result.authToken(), new NewGame("Sashimi Chess"));
        facade.createGame(result.authToken(), new NewGame("greg"));

        assertDoesNotThrow(() -> facade.listGames(result.authToken()));
    }

    @Test
    public void joinGameTest() throws Exception{
        UserData user = new UserData("Sam", "Penguin", "orange_juice@gmail.com");
        facade.register(user);
        LoginRequest log = new LoginRequest("Sam", "Penguin");
        RegisterResult result = facade.login(log);

        CreateGameResult res = facade.createGame(result.authToken(), new NewGame("Sashimi Chess"));
        facade.joinGame(result.authToken(), new JoinGameData("WHITE", res.gameID()));
        ListGamesResult lst = facade.listGames(result.authToken());
        //System.out.println(lst.games());
        ListGameData joinedGame = lst.games().iterator().next();
        assertEquals("Sam",joinedGame.whiteUsername());
    }
}
