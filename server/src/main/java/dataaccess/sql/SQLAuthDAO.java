package dataaccess.sql;

import java.sql.*;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO () throws DataAccessException {
        configureDatabase();
    }


    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        String statement = """
                CREATE TABLE IF NOT EXISTS authtokens (
                authtoken varchar(256) NOT NULL,
                username varchar(256) NOT NULL
                )
                """;
        try(Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(statement)){
            ps.executeUpdate();
        }catch(SQLException e){
            throw new DataAccessException("Unable to configure database", e);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "DELETE FROM authtokens";
        try(Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.executeUpdate();
        }catch(SQLException e){
            throw new DataAccessException("Unable to clear authtokens", e);
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        String statement = """
                Insert INTO authtokens (authtoken, username)
                VALUES (?,?)
                """;
        try(Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(statement)){
            ps.setString(1, authData.authToken());
            ps.setString(2, authData.username());

            ps.executeUpdate();
        }catch(SQLException e){
            throw new DataAccessException("Unable to create new auth token", e);
        }
    }

    @Override
    public AuthData getAuth(String AuthToken) throws DataAccessException {
        String statement = """
                SELECT authtoken, username
                FROM authtokens
                WHERE authtoken = ?
                """;
        try(Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setString(1, AuthToken);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return readAuthData(rs);
            }else{
                return null;
            }
        }catch(SQLException e){
            throw new DataAccessException("Unable to retrieve user", e);
        }
    }

    @Override
    public void deleteAuth(String AuthToken) throws DataAccessException {
        String statement = """
                        DELETE FROM authtokens
                        WHERE authtoken = ?;
                        """;
        try(Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setString(1,AuthToken);
            ps.executeUpdate();
        }catch(SQLException e){
            throw new DataAccessException("Unable to delete authtoken", e);
        }
    }


    private AuthData readAuthData(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String authToken = rs.getString("authtoken");
        return new AuthData(authToken,username);
    }
}
