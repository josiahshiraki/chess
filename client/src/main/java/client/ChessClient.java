package client;

import exception.ResponseException;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Objects;

import static client.State.SIGNED_OUT;

public class ChessClient {
    private final ServerFacade server;
    private State state = State.SIGNED_OUT;

    public ChessClient(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public State getState(){
        return this.state;
    }

    public String eval(String userIn){
        try{
            String result = "";
            //[toExecute, arg1, arg2, ...]
            String[] tokens = userIn.split(" ");
            String execute = tokens[0].toLowerCase();
            ArrayList<String> params = getParam(tokens);
            switch(execute){
                case "register":
                    return "NI";
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


    private ArrayList<String> getParam (String [] tokens){
        ArrayList<String> params = new ArrayList<>();
        for(int i = 1; i < tokens.length; i++){
                params.add(tokens[i]);
        }
        return params;
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
}
