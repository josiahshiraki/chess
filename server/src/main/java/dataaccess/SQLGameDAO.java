package dataaccess;

import java.sql.*;
import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public int insertGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        return null;
    }

    @Override
    public void joinGame(int gameId, String username, String color) throws DataAccessException {

    }
}
