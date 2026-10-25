package dataaccess;

import model.GameData;
import model.ListGamesResult;

import java.util.Collection;

public interface GameDAO {
    void clear();
    Collection<GameData> listGames(String authToken);
}
