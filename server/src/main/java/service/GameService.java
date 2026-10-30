package service;

import dataaccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;

    }

    public ListGamesResult listGames(String authToken) {
        try {
            if(authDAO.getAuth(authToken) == null){
                throw new UnauthorizedException("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Collection <GameData> games = null;
        try {
            games = gameDAO.listGames();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        List<ListGameData> gameList = new ArrayList<>();

        for (GameData game : games){
            gameList.add(new ListGameData(game.gameID(),game.whiteUsername(), game.blackUsername(), game.gameName()));
        }
        return new ListGamesResult(gameList);
    }

    public CreateGameResult createGame(String authToken, NewGame newGame) {
        try {
            if(authDAO.getAuth(authToken) == null){
                throw new UnauthorizedException("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        if (newGame == null) {
            throw new BadRequestException("Error: bad request");
        }
        String gameName = newGame.gameName();
        if(gameName == null || gameName.isBlank()){
            throw new BadRequestException("Error: bad request");
        }
        int gameId = 0;
        try {
            gameId = gameDAO.insertGame(gameName);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return new CreateGameResult(gameId);
    }

    public void joinGame(String authToken, JoinGameData joinData) {
        try {
            if(authDAO.getAuth(authToken) == null){
                throw new UnauthorizedException("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        if (joinData == null || joinData.playerColor() == null) { // can gameId be null?
            throw new BadRequestException("Error: bad request");
        }
        String color = joinData.playerColor();
        int gameId = joinData.gameID();
        GameData game = null;
        try {
            game = gameDAO.getGame(gameId);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (!color.equals("WHITE") && !color.equals("BLACK")){
            throw new BadRequestException("Error: bad request");
        }
        if(color.equals("WHITE") && game.whiteUsername() != null){
            throw new AlreadyTakenException("Error: already taken");
        }
        if(color.equals("BLACK") && game.blackUsername() != null){
            throw new AlreadyTakenException("error: already taken");
        }

        String username = null;
        try {
            username = authDAO.getAuth(authToken).username();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            gameDAO.joinGame(gameId, username, color);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
