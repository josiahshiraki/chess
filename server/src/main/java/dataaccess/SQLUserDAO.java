package dataaccess;

import java.sql.*;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        String statement = """
            CREATE TABLE IF NOT EXISTS users (
            username varchar(256) PRIMARY KEY,
            password varchar(256) NOT NULL,
            email varchar(256) NOT NULL
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
        String statement = "DELETE FROM users";
        try(Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.executeUpdate();
        }catch(SQLException e){
            throw new DataAccessException("Unable to clear users", e);
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        String statement = """
                INSERT INTO users (username, password, email)
                VALUES (?,?,?)
                """;

        //may need to handle race conditions and throwing specific duplicate error for duplicate primary key (users)
        try(Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(statement)){
            ps.setString(1,user.username());

            String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
            ps.setString(2,hashedPassword);
            ps.setString(3,user.email());

            ps.executeUpdate();

        }catch(SQLException e){
            throw new DataAccessException("Unable to create new user", e);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String statement = """
                SELECT username, password, email
                FROM users
                WHERE username = ?
                """;
        try(Connection conn = DatabaseManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return readUserData(rs);
            }else{
                return null;
            }
        }catch(SQLException e){
            throw new DataAccessException("Unable to retrieve user", e);
        }
    }

    private UserData readUserData(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        return new UserData(username,password,email);
    }
}
