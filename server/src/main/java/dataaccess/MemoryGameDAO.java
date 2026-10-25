package dataaccess;

import model.ListGamesResult;
import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    //int represents ID, GameData represents list of game elements
    private static final HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public Collection<GameData> listGames(){
        return games.values();
    }
}
