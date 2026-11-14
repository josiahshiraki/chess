package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exception.ResponseException;
import model.*;

import java.lang.ref.ReferenceQueue;
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

    public RegisterResult register(UserData user) throws ResponseException{
        var request = buildRequest("POST", "/user", user);
        var response = sendRequest(request);
        return handleResponse(response,RegisterResult.class);
    }

    public  RegisterResult login(LoginRequest logInfo){
        var request = buildRequest("POST","/session", logInfo);
        var response = sendRequest(request);
        return handleResponse(response,RegisterResult.class);
    }


    //method: POST,PUT,DELETE, or GET
    //path: user,game, auth
    //model obj that has the packaged info
    private HttpRequest buildRequest(String method, String path, Object body){
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null){
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
            return client.send(request, BodyHandlers.ofString());
        }catch (Exception e){
            throw new ResponseException(e.getMessage(), 500);
        }
    }

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
