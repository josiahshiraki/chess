package service;

import dataaccess.*;
import exception.UnauthorizedException;
import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        Collection <GameData> games = gameDAO.listGames();
        List<ListGameData> gameList = new ArrayList<>();

        for (GameData game : games){
            gameList.add(new ListGameData(game.gameID(),game.whiteUsername(), game.blackUsername(), game.gameName()));
        }
        return new ListGamesResult(gameList);
    }
}
