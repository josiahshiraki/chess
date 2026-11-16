package client;

import chess.ChessBoard;
import exception.ResponseException;
import model.*;
import server.ServerFacade;
import ui.RenderBoard;

import java.util.Arrays;
import java.util.Locale;

import static chess.ChessGame.TeamColor.WHITE;
import static client.State.*;

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
        //default should be -> enter valid command, case help
        try{
            //[toExecute, arg1, arg2, ...]
            String[] tokens = userIn.split(" ");
            String execute = tokens[0].toLowerCase();
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (execute) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                case "create" -> createGame(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                default -> printHelp();
            };
        }catch (ResponseException e){
            return e.getMessage();
        }
    }

    private String register(String [] params) throws ResponseException{
        if(params.length == 3){
            RegisterResult registered = server.register(new UserData(params[0],params[1],params[2]));
            this.authToken = registered.authToken();
            state = State.SIGNED_IN;
            return "CHESSへよこそう！ currently logged in as " + registered.username();
        }
        throw new ResponseException("expected three inputs <USERNAME> <PASSWORD> <EMAIL>", 400);
    }

    private String login(String [] params) throws ResponseException{
        if(params.length == 2){
            RegisterResult loggedIn = server.login(new LoginRequest(params[0], params[1]));
            this.authToken = loggedIn.authToken();
            state = State.SIGNED_IN;
            return "CHESSへよこそう！ currently logged in as " + loggedIn.username();
        }
        throw new ResponseException("expected two inputs <USERNAME> <PASSWORD>", 400);
    }

    private String logout() throws ResponseException{
        assertSignedIn();
        server.logout(authToken);
        this.authToken = "";
        state = State.SIGNED_OUT;
        return "Logout successful バイバイ！";
    }

    private String createGame (String [] params) throws ResponseException{
        assertSignedIn();
        if(params.length == 1){
            CreateGameResult res = server.createGame(authToken, new NewGame(params[0]));
            return "Successfully created game! gameID: " + res.gameID();
        }
        throw new ResponseException("Expected one input <NAME>", 400);
    }

    private String list() throws ResponseException{
        //Collection<ListGameData> games
        //ListGameData = {int gameID, String whiteUsername, String blackUsername, String gameName}
        assertSignedIn();
        ListGamesResult result =  server.listGames(authToken);
        StringBuilder retString = new StringBuilder();
        int i = 0;
        for(ListGameData game : result.games()){
            retString.append(String.format("%d - Game Name: %s, GameID: %d, White Username: %s, BlackUsername: %s\n",
                    i,
                    game.gameName(),
                    game.gameID(),
                    game.whiteUsername(),
                    game.blackUsername()));
            i++;
        }
        return retString.toString();
    }

    private String join(String [] params) throws ResponseException{
        assertSignedIn();
        if(params.length == 2){
            checkNumber(params[0]);
            checkColor(params[1]);
            server.joinGame(authToken, new JoinGameData(params[1].toUpperCase(), Integer.parseInt(params[0])));
            return "Successfully joined game! gameID: " + params[1];
        }
        throw new ResponseException("Expected two inputs <ID> [WHITE|BLACK]", 400);
    }

    private String observe(String [] params) throws ResponseException{
        assertSignedIn();
        if(params.length == 1){
            ListGamesResult result =  server.listGames(authToken);
            for(ListGameData game : result.games()){
                int gameID = checkNumber(params[0]);
                if(game.gameID() == gameID){
                    server.joinGame(authToken, new JoinGameData(null, game.gameID()));
                    state = State.INGAME;
                    ChessBoard observe = new ChessBoard();
                    observe.resetBoard();
                    return new RenderBoard(observe).drawBoard(WHITE);
                }
            }
            throw new ResponseException("Could not find game with gameID: " + params[0], 400);
        }
        throw new ResponseException("Expected one input <ID>", 400);
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
                    join <ID> [WHITE|BLACK] - a game
                    observe <ID> - a game
                    logout - when you are done
                    quit - playing chess
                    help - display possible commands
                    """;
        }
    }

    private void checkColor(String color){
        if(!color.equalsIgnoreCase("WHITE") && !color.equalsIgnoreCase("BLACK")){
            throw new ResponseException("Color must be WHITE or BLACK", 400);
        }
    }

    private int checkNumber(String toCheck){
        int gameNumber;
        try {
            gameNumber = Integer.parseInt(toCheck);
            return gameNumber;
        } catch (NumberFormatException e) {
            throw new ResponseException("Game number must be an number", 400);
        }
    }

    private void assertSignedIn() throws ResponseException{
        if (state == SIGNED_OUT){
            throw new ResponseException("You must be signed in", 400);
        }
    }
}
