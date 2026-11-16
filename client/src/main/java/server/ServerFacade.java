package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exception.ResponseException;
import model.*;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public void clear() throws ResponseException {
        var request = buildRequest("DELETE", "/db", null, null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public RegisterResult register(UserData user) throws ResponseException{
        var request = buildRequest("POST", "/user", user, null);
        var response = sendRequest(request);
        //RegisterResult: username, authToken
        return handleResponse(response,RegisterResult.class);
    }

    public RegisterResult login(LoginRequest logInfo) throws ResponseException{
        var request = buildRequest("POST","/session", logInfo, null);
        var response = sendRequest(request);
        //RegisterResult: username, authToken
        return handleResponse(response,RegisterResult.class);
    }

    public void logout(String authToken) throws ResponseException{
        var request = buildRequest("DELETE", "/session", null, authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public ListGamesResult listGames(String authToken) throws ResponseException{
        var request = buildRequest("GET", "/game", null, authToken);
        var response = sendRequest(request);
        //ListGameResult: Collection <ListGameData{...}> games
        return handleResponse(response, ListGamesResult.class);
    }

    public CreateGameResult createGame(String authToken, NewGame game) throws ResponseException{
        var request = buildRequest("POST", "/game", game, authToken);
        var response = sendRequest(request);
        //CreateGameResult: int gameID
        return handleResponse(response, CreateGameResult.class);
    }

    public void joinGame(String authToken, JoinGameData joinData) throws ResponseException{

        var request = buildRequest("PUT", "/game", joinData, authToken);

        var response = sendRequest(request);
        handleResponse(response, null);
    }


    //method: POST,PUT,DELETE, or GET
    //path: user,game, auth
    //model obj that has the packaged info
    //authToken required for some endpoints, null if not required
    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));

    if(authToken != null) {//sets Authorization header
        request.setHeader("Authorization", authToken);
    }
    if(body != null){// sets Content-Type header
        request.setHeader("Content-Type", "application/json");
    }

    return request.build();
    }

    private BodyPublisher makeRequestBody(Object request){
        if (request != null){
            return BodyPublishers.ofString(new Gson().toJson(request));
        }else{
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try{
            return client.send(request, BodyHandlers.ofString()); //returns server response with body (could be empty)
        }catch (Exception e){
            throw new ResponseException(e.getMessage(), 500);
        }
    }

    // T is a java placeholder for a Class name e.g. AuthData
    // Mostly handles error catching, if no error detected returns the obj of specified class
    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();

        if(!isSuccessful(status)){ //handles if the status code != 200ish
            var body = response.body();
            String message = "Request failed: " + status;

            if(body != null && !body.isBlank()){
                JsonObject json = new Gson().fromJson(body, JsonObject.class);

                if(json != null && json.has("message")){
                    message = json.get("message").getAsString();
                }
            }
            throw new ResponseException(message, status);

        }
        if(responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }else{
            return null;
        }
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
