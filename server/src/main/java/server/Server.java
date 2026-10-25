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

        javalin.exception(BadRequestException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.contentType("application/json");
            ctx.result(new Gson().toJson(
                    Map.of("message",e.getMessage())
            ));
        });

        javalin.exception(UnauthorizedException.class, (e, ctx) -> {
            ctx.status(401);
            ctx.contentType("application/json");
            ctx.result(new Gson().toJson(
                    Map.of("message",e.getMessage())
            ));
        });

        javalin.exception(Exception.class, (e, ctx) -> {
            ctx.status(500);
            ctx.contentType("application/json");
            ctx.result(new Gson().toJson(
                    Map.of("message", "Error: " + e.getMessage())
            ));
        });

        //DAO OBJS FOR DEPENDENCY INJECTION
        var userDAO = new MemoryUserDAO();
        var gameDAO = new MemoryGameDAO();
        var authDAO = new MemoryAuthDAO();

        //service objects
        var clearService = new ClearService(userDAO, gameDAO, authDAO);
        var userService = new UserService(userDAO, authDAO);
        var gameService = new GameService(gameDAO, authDAO);

        //handler objects
        var userHandler = new UserHandler(userService);
        var clearHandler = new ClearHandler(clearService);
        var gameHandler = new GameHandler(gameService);

        //endpoints
        javalin.delete("/db", ctx -> clearHandler.clear(ctx));
        javalin.post("/user", ctx -> userHandler.register(ctx));
        javalin.post("/session", ctx -> userHandler.login(ctx));
        javalin.delete("/session", ctx -> userHandler.logout(ctx));
        javalin.get("/game", ctx -> gameHandler.listGames(ctx));
        javalin.post("/game", ctx -> gameHandler.createGame(ctx));
        javalin.put("/game", ctx -> gameHandler.joinGame(ctx));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
