package handler;

import io.javalin.http.Context;
import model.*;
import service.GameService;

public class GameHandler {

    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void listGames(Context ctx) {
        String authToken = ctx.header("authorization");
        ListGamesResult games = gameService.listGames(authToken);

        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result("{}");
    }
}
