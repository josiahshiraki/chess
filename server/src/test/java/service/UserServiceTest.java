package service;

import dataaccess.*;
import exception.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest{

    private UserService userService;
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;

    @BeforeEach
    public void setUp(){
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();

        userService = new UserService(userDAO, authDAO);
    }
    @Test
    public void registerSuccess(){
        UserData user = new UserData("testUser", "password123", "test@example.com");
        RegisterResult result = userService.register(user);

        assertEquals("testUser", result.username());
        assertNotNull(result.authToken());

        try {
            assertNotNull(userDAO.getUser("testUser"));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            assertNotNull(authDAO.getAuth(result.authToken()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void registerDuplicateUsername(){
        UserData user = new UserData("player1", "password123", "player1@test.com");
        userService.register(user);
        assertThrows(AlreadyTakenException.class, () -> userService.register(user));
    }

    @Test
    public void registerMissingUsername(){
        UserData user = new UserData(null, "password123", "player1@test.com");
        assertThrows(BadRequestException.class, () -> userService.register(user));
    }

    @Test
    public void registerMissingPassword(){
        UserData user = new UserData("player1", null, "player1@test.com");
        assertThrows(BadRequestException.class, () -> userService.register(user));
    }

    @Test
    public void registerMissingEmail(){
        UserData user = new UserData("player1", "password123", null);
        assertThrows(BadRequestException.class, () -> userService.register(user));
    }

    @Test
    public void loginWrongPassword(){
        userService.register(new UserData("player1", "password123", "player1@test.com"));
        assertThrows(UnauthorizedException.class, () -> userService.login(new LoginRequest("player1", "wrongPassword")));
    }

    @Test
    public void loginNonexistentUser(){
        assertThrows(UnauthorizedException.class, () -> userService.login(new LoginRequest("nobody", "password123")));
    }


    @Test
    public void logoutSuccess(){
        var result = userService.register(new UserData("player1", "password123", "player1@test.com"));
        String token = result.authToken();
        userService.logout(token);
        try {
            assertNull(authDAO.getAuth(token));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void logoutInvalidToken(){
        assertThrows(UnauthorizedException.class, () -> userService.logout("invalid-token"));
    }

}
