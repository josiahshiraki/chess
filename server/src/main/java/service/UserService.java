package service;
import exception.*;

import dataaccess.*;
import model.*;

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
            throw new AlreadyTakenException("already taken");
        }

        userDAO.createUser(user);
        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, user.username()));

        return new RegisterResult(user.username(), authToken);
    }

//    private boolean checkMissing(UserData user){
//        if
//    }

}
