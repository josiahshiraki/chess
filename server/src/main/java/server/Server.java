package server;


import com.google.gson.Gson;
import handler.*;
import service.*;
import dataaccess.*;
import exception.*;


import io.javalin.*;

import java.util.Map;

public class Server {

    private final Javalin javalin;


    public Server() {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        //ERROR HANDLING
        javalin.exception(AlreadyTakenException.class, (e, ctx) -> {
            ctx.status(403);
            ctx.contentType("application/json");
            ctx.result(new Gson().toJson(
                    Map.of("message", e.getMessage())
            ));
        });

        //DAO OBJS FOR DEPENDENCY INJECTION
        var userDAO = new MemoryUserDAO();
        var gameDAO = new MemoryGameDAO();
        var authDAO = new MemoryAuthDAO();

        //service objects
        var clearService = new ClearService(userDAO, gameDAO, authDAO);
        var userService = new UserService(userDAO, authDAO);

        //handler objects
        var userHandler = new UserHandler(userService);
        var clearHandler = new ClearHandler(clearService);
        var gameHandler = new GameHandler();

        //endpoints
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
