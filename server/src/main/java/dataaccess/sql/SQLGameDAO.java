package dataaccess.sql;

import java.sql.*;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        String statement = """
            CREATE TABLE IF NOT EXISTS games (
            gameID INT AUTO_INCREMENT PRIMARY KEY,
            whiteUsername VARCHAR(256),
            blackUsername VARCHAR(256),
            gameName VARCHAR(256) NOT NULL,
            game JSON NOT NULL
            )
            """;
        try(Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(statement)){
            ps.execute();
        }catch(SQLException e){
            throw new DataAccessException("Unable to configure database", e);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = """
                DELETE FROM games
                """;
        try(Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(statement)){
            ps.executeUpdate();
        }catch(SQLException e){
            throw new DataAccessException("unable to clear database",e);
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        String statement = """
                SELECT gameID, whiteUsername, blackUsername, gameName, game
                FROM games
                """;
        Collection<GameData> listGames = new ArrayList<>();
        try(Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement(statement);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                listGames.add(readGameData(rs));
            }
        }catch(SQLException e){
            throw new DataAccessException("Unable to retrieve list of games",e);
        }
        return listGames;
    }

    @Override
    public int insertGame(String gameName) throws DataAccessException {
        String statement = """
                INSERT INTO games (gameName, game)
                VALUES(?,?)
                """;
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, gameName);

            ChessGame chessGame = new ChessGame();
            String newGame = new Gson().toJson(chessGame);
            ps.setString(2, newGame);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            }else{
                throw new DataAccessException("No generated game ID returned");
            }
        }catch(SQLException e){
            throw new DataAccessException("Unable to create new game", e);
        }
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        String statement = """
                SELECT gameID, whiteUsername, blackUsername, gameName, game
                FROM games
                WHERE gameID = ?
                """;
        try(Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setInt(1,gameId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return readGameData(rs);
            }else{
                return null;
            }
        }catch(SQLException e){
            throw new DataAccessException("Unable to retrieve requested game",e);
        }
    }

    private GameData readGameData(ResultSet rs) throws SQLException{
        String game = rs.getString("game");
        ChessGame chessGame = new Gson().fromJson(game, ChessGame.class);

        return new GameData(rs.getInt("gameID"),
                rs.getString("whiteUsername"),
                rs.getString("blackUsername"),
                rs.getString("gameName"),
                chessGame
        );
    }

    @Override
    public void joinGame(int gameId, String username, String color) throws DataAccessException {
        String statement;
        if(color.equals("BLACK")) {
            statement = """
                    UPDATE games
                    SET blackUsername = ?
                    WHERE gameID = ? AND blackUsername IS NULL
                    """;
        }else{
            statement = """
                    UPDATE games
                    SET whiteUsername = ?
                    WHERE gameID = ? AND whiteUsername IS NULL
                    """;
        }
        try(Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(statement)){
            ps.setString(1,username);
            ps.setInt(2,gameId);
            int updated = ps.executeUpdate();

            if (updated == 0){ //did not update, updated lines is 0
                throw new DataAccessException("unable to join requested game");
            }

        }catch(SQLException e){
            throw new DataAccessException("unable to join requested game",e);
        }
    }
}
