package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import model.*;
import org.jetbrains.annotations.NotNull;
import service.GameService;

public class GameHandler {

    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void listGames(Context ctx) {
        String authToken = ctx.header("authorization");
        ListGamesResult result = gameService.listGames(authToken);

        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(new Gson().toJson(result));
    }

    public void createGame( Context ctx) {
        String authToken = ctx.header("authorization");
        NewGame newGame = new Gson().fromJson(ctx.body(), NewGame.class);
        CreateGameResult result = gameService.createGame(authToken, newGame);

        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(new Gson().toJson(result));
    }

    public void joinGame(Context ctx) {
        String authToken = ctx.header("authorization");
        JoinGameData joinData = new Gson().fromJson(ctx.body(), JoinGameData.class);

        gameService.joinGame(authToken, joinData);

        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result("{}");
    }
}
