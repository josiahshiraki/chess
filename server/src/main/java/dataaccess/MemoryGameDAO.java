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
}
