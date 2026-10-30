package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear() throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    int insertGame(String gameName) throws DataAccessException;
    GameData getGame(int gameId) throws DataAccessException;
    void joinGame(int gameId, String username, String color) throws DataAccessException;
}
