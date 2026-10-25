package service;

import dataaccess.*;
import exception.UnauthorizedException;
import model.*;

public class GameService {
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;

    }

    public ListGamesResult listGames(String authToken) {
        if(authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        gameDAO.listGames(authToken);
        return null;
    }
}
