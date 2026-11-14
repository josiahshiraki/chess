
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
        UserData user = new UserData("Josiah", "turtle", "orange_juice@gmail.com");
        facade.register(user);
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.register(user));

        assertEquals(403, exception.getStatusCode());
    }
}
