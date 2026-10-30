package dataaccess;

import java.sql.*;
import model.UserData;


public class SQLUserDAO implements UserDAO{
    private String sql = """
            CREATE TABLE IF NOT EXISTS users (
            username varchar(256) PRIMARY KEY,
            password varchar(256) NOT NULL,
            email varchar(256) NOT NULL
            )
            """;

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){

            ps.execute();

        }catch(SQLException e){
            throw new DataAccessException("Unable to configure database", e);
        }
    }

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }
}
