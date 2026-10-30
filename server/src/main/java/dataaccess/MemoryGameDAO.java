package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    //int represents ID, GameData represents list of game elements
    private final HashMap<Integer, GameData> games = new HashMap<>();
    private int gameIdNum = 1;

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public int insertGame(String gameName) throws DataAccessException {
        int currentGameId = gameIdNum;
        games.put(gameIdNum, new GameData(gameIdNum, null, null, gameName, new ChessGame()));
        gameIdNum++;
        return currentGameId;
    }
    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        return games.get(gameId);
    }

    @Override
    public void joinGame(int gameId, String username, String color) throws DataAccessException {
        GameData original = null;
        try {
            original = getGame(gameId);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        GameData updated;
        if(color.equals("WHITE")) {
            updated = new GameData(gameId, username, original.blackUsername(), original.gameName(), original.game());
        }else{
            updated = new GameData(gameId, original.whiteUsername(), username, original.gameName(), original.game());
        }
        games.put(gameId, updated);
    }
}
