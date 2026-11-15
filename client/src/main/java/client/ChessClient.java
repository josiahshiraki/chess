package client;

import exception.ResponseException;
import model.*;
import server.ServerFacade;

import java.util.Arrays;

import static client.State.SIGNED_OUT;

public class ChessClient {
    private final ServerFacade server;
    private State state = State.SIGNED_OUT;
    private String authToken;

    public ChessClient(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public State getState(){
        return this.state;
    }

    public String eval(String userIn){
        //enforce SIGNED_IN and SIGNED_OUT checks, throw error is mismatch execution
        try{
            //[toExecute, arg1, arg2, ...]
            String[] tokens = userIn.split(" ");
            String execute = tokens[0].toLowerCase();
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch(execute){
                case "register":
                    return register(params);
                case "login":
                    return "NI";
                case "quit":
                    return "NI";
                case "create":
                    return "NI";
                case "list":
                    return "NI";
                case "join":
                    return "NI";
                case "observe":
                    return "NI";
                case "logout":
                    return "NI";
                default:
                    return printHelp();

            }
        }catch (ResponseException e){
            return e.getMessage();
        }
    }

    private String register(String [] params) throws ResponseException{
        if(params.length == 3){
            RegisterResult registered = server.register(new UserData(params[0],params[1],params[2]));
            this.authToken = registered.authToken();
            state = State.SIGNED_IN;
            return "registration successful, currently logged in as " + registered.username();
        }
        throw new ResponseException("expected 3 inputs <USERNAME> <PASSWORD> <EMAIL>", 400);
    }

    private String login(String [] params) throws ResponseException{
        return null;
    }


    private String printHelp() {
        if (state == SIGNED_OUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD>
                    quit - playing chess
                    help - display possible commands
                    """;
        } else {
            return """
                    create <NAME> - a game
                    list - games
                    join <ID> - a game
                    observe <ID> [WHITE|BLACK]- a game
                    logout - when you are done
                    quit - playing chess
                    help - display possible commands
                    """;
        }
    }

    private void assertSignedIn() throws ResponseException{
        if (state == SIGNED_OUT){
            throw new ResponseException("You must be signed in", 400);
        }
    }
}
