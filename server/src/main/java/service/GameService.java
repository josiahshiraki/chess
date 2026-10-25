package service;

import dataaccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
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

    public CreateGameResult createGame(String authToken, NewGame newGame) {
        if(authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (newGame == null) {
            throw new BadRequestException("Error: bad request");
        }
        String gameName = newGame.gameName();
        if(gameName == null || gameName.isBlank()){
            throw new BadRequestException("Error: bad request");
        }
        int gameId = gameDAO.insertGame(gameName);
        return new CreateGameResult(gameId);
    }

    public void joinGame(String authToken, JoinGameData joinData) {
        if(authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (joinData == null || joinData.playerColor() == null) { // can gameId be null?
            throw new BadRequestException("Error: bad request");
        }
        String color = joinData.playerColor();
        int gameId = joinData.gameID();
        GameData game = gameDAO.getGame(gameId);

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

        String username = authDAO.getAuth(authToken).username();
        gameDAO.joinGame(gameId, username, color);
    }
}
