package server;


import handler.*;
import service.*;
import dataaccess.*;

import io.javalin.*;

public class Server {

    private final Javalin javalin;


    public Server() {
        var userDAO = new MemoryUserDAO();
        var gameDAO = new MemoryGameDAO();
        var authDAO = new MemoryAuthDAO();

        var clearService = new ClearService(userDAO, gameDAO, authDAO);
        var userService = new UserService(userDAO, authDAO);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        var userHandler = new UserHandler(userService);
        var clearHandler = new ClearHandler(clearService);
        var gameHandler = new GameHandler();

        javalin.delete("/db", ctx -> clearHandler.clear(ctx));
        javalin.post("/user", ctx -> userHandler.register(ctx));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
