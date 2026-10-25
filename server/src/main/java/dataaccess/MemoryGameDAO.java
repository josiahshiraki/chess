package dataaccess;

import chess.ChessGame;
import model.ListGamesResult;
import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    //int represents ID, GameData represents list of game elements
    private static final HashMap<Integer, GameData> games = new HashMap<>();
    private int gameIdNum = 1;

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public Collection<GameData> listGames(){
        return games.values();
    }

    @Override
    public int insertGame(String gameName){
        int currentGameId = gameIdNum;
        games.put(gameIdNum, new GameData(gameIdNum, null, null, gameName, new ChessGame()));
        gameIdNum++;
        return currentGameId;
    }
    @Override
    public GameData getGame(int gameId){
        return games.get(gameId);
    }

    @Override
    public void joinGame(int gameId, String username, String color) {
        GameData original = getGame(gameId);
        GameData updated;
        if(color.equals("WHITE")) {
            updated = new GameData(gameId, username, original.blackUsername(), original.gameName(), original.game());
        }else{
            updated = new GameData(gameId, original.whiteUsername(), username, original.gameName(), original.game());
        }
        games.put(gameId, updated);
    }
}
