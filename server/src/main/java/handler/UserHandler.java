package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
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
}
