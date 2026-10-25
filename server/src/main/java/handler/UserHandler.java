package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import service.UserService;

import model.*;

public class UserHandler {

    private final UserService userService;

    public UserHandler(UserService userService){
        this.userService = userService;
    }

    public void register(Context ctx){
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);

        RegisterResult result =  userService.register(user);

        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(new Gson().toJson(result));
    }

    public void login(Context ctx){
        LoginRequest loginUser = new Gson().fromJson(ctx.body(), LoginRequest.class);
        RegisterResult result = userService.login(loginUser);

        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(new Gson().toJson(result));
;    }

    public void logout(Context ctx) {
        String authToken = ctx.header("authorization");
        userService.logout(authToken);

        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result("{}");

    }
}
