package service;

import dataaccess.*;

public class GameService {
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public GameService(UserDAO userDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;

    }

    public void listGames(String authToken) {
    }
}
