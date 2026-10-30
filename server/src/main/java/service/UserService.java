package service;
import exception.*;

import dataaccess.*;
import model.*;

import java.util.Objects;
import java.util.UUID;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }


    public RegisterResult register(UserData user){
        try {
            if(userDAO.getUser(user.username()) != null){
                throw new AlreadyTakenException("Error: already taken");
            }else if(checkMissingReg(user)){
                throw new BadRequestException("Error: bad request");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            userDAO.createUser(user);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        String authToken = UUID.randomUUID().toString();
        try {
            authDAO.createAuth(new AuthData(authToken, user.username()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return new RegisterResult(user.username(), authToken);
    }

    private boolean checkMissingReg(UserData user){
        return (user == null ||
                user.username() == null || user.username().isBlank() ||
                user.password() == null || user.password().isBlank() ||
                user.email() == null || user.email().isBlank());
    }


    public RegisterResult login(LoginRequest user) {
        if(checkMissingLog(user)){
            throw new BadRequestException("Error: bad request");
        }
        if(checkUnauthorized(user)){
            throw new UnauthorizedException("Error: unauthorized");
        }

        String authToken = UUID.randomUUID().toString();
        try {
            authDAO.createAuth(new AuthData(authToken, user.username()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return new RegisterResult(user.username(), authToken);
    }

    private boolean checkMissingLog(LoginRequest user){
        return (user == null ||
                user.username() == null || user.username().isBlank() ||
                user.password() == null || user.password().isBlank());
    }

    private boolean checkUnauthorized(LoginRequest loginUser){
        //first check if username in database, next check if password of that user matches
        try {
            if(userDAO.getUser(loginUser.username()) != null){
                String comparePass = userDAO.getUser(loginUser.username()).password();
                return !Objects.equals(loginUser.password(), comparePass); // returns true if password does not match
            }else{
                return true; //returns true if username not found
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout(String authToken) {
        try {
            if(authDAO.getAuth(authToken) == null){
                throw new UnauthorizedException("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            authDAO.deleteAuth(authToken);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
