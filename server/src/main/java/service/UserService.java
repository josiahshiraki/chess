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
        if(userDAO.getUser(user.username()) != null){
            throw new AlreadyTakenException("Error: already taken");
        }else if(checkMissingReg(user)){
            throw new BadRequestException("Error: bad request");
        }

        userDAO.createUser(user);
        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, user.username()));

        return new RegisterResult(user.username(), authToken);
    }

    private boolean checkMissingReg(UserData user){
        return (user == null ||
                user.username() == null || user.username().isBlank() ||
                user.password() == null || user.password().isBlank() ||
                user.email() == null || user.email().isBlank());
    }


    public RegisterResult login(LoginRequest user) {
        if(checkUnauthorized(user)){
            throw new UnauthorizedException("Error: unauthorized");
        }else if(checkMissingLog(user)){
            throw new BadRequestException("Error: bad request");
        }

        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, user.username()));

        return new RegisterResult(user.username(), authToken);
    }

    private boolean checkMissingLog(LoginRequest user){
        return (user == null ||
                user.username() == null || user.username().isBlank() ||
                user.password() == null || user.password().isBlank());
    }

    private boolean checkUnauthorized(LoginRequest loginUser){
        //first check if username in database, next check if password of that user matches
        if(userDAO.getUser(loginUser.username()) != null){
            String comparePass = userDAO.getUser(loginUser.username()).password();
            return !Objects.equals(loginUser.password(), comparePass); // returns true if password does not match
        }else{
            return true; //returns true if username not found
        }
    }

    public void logout(String authToken) {
        if(authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }
}
