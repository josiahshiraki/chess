package client;

import chess.ChessBoard;
import chess.ChessGame;
import exception.ResponseException;
import model.*;
import server.ServerFacade;
import ui.RenderBoard;

import java.util.Arrays;

import static chess.ChessGame.TeamColor.BLACK;
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
        assertSignedOut();
        if(params.length == 3){
            try {
                RegisterResult registered = server.register(new UserData(params[0], params[1], params[2]));
                this.authToken = registered.authToken();
                state = State.SIGNED_IN;
                return "CHESSへよこそう！ currently logged in as " + registered.username();
            }catch(ResponseException e){
                if(e.getStatusCode()==403){
                    return "Username is already taken. Please choose another username.";
                }
            }
        }
        throw new ResponseException("expected three inputs <USERNAME> <PASSWORD> <EMAIL>", 400);
    }

    private String login(String [] params) throws ResponseException{
        assertSignedOut();
        if(params.length == 2){
            try {
                RegisterResult loggedIn = server.login(new LoginRequest(params[0], params[1]));
                this.authToken = loggedIn.authToken();
                state = State.SIGNED_IN;
                return "CHESSへよこそう！ currently logged in as " + loggedIn.username();
            }catch(ResponseException e){
                if(e.getStatusCode()==401){
                    return "Incorrect Username or Password";
                }
            }

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
            return "Successfully created game!";
        }
        throw new ResponseException("Expected one input <NAME>", 400);
    }

    private String list() throws ResponseException{
        //Collection<ListGameData> games
        //ListGameData = {int gameID, String whiteUsername, String blackUsername, String gameName}
        assertSignedIn();
        ListGamesResult result =  server.listGames(authToken);
        StringBuilder retString = new StringBuilder();
        int i = 1;
        //print empty not null
        for(ListGameData game : result.games()){
            String usernameW = game.whiteUsername();
            String usernameB = game.blackUsername();
            if(game.whiteUsername() == null){
                usernameW = "[EMPTY]";
            }
            if(game.blackUsername() == null){
                usernameB = "[EMPTY]";
            }
            retString.append(String.format("%d - Game Name: %s, White Username: %s, BlackUsername: %s\n",
                    i,
                    game.gameName(),
                    usernameW,
                    usernameB));
            i++;
        }
        return retString.toString();
    }

    private int findGame(String [] params){
        int index;
        try {
            index = Integer.parseInt(params[0]) - 1;
        }catch(NumberFormatException e){
            throw new ResponseException("must be a number", 400);
        }
        try {
            var listG = server.listGames(authToken).games();
            ListGameData[] newList = new ListGameData[listG.size()];
            int i = 0;
            for (ListGameData game : listG) {
                newList[i] = game;
                i++;
            }
            return newList[index].gameID();
        }catch(ArrayIndexOutOfBoundsException e){
            throw new ResponseException("Game not be found",400);
        }
    }

    private String join(String [] params) throws ResponseException{
        assertSignedIn();
        if(params.length == 2){
            checkNumber(params[0]);
            checkColor(params[1]);
            int gameID = findGame(params);
            server.joinGame(authToken, new JoinGameData(params[1].toUpperCase(), gameID));
            state = INGAME;
            ChessBoard joined = new ChessBoard();
            joined.resetBoard();
            ChessGame.TeamColor perspective;

            if(params[1].equalsIgnoreCase("WHITE")){
                perspective = WHITE;
            }else{
                perspective = BLACK;
            }
            String board = new RenderBoard(joined).drawBoard(perspective);
            return board+"\nSuccessfully joined game!";
        }
        throw new ResponseException("Expected two inputs <ID> [WHITE|BLACK]", 400);
    }

    private String observe(String [] params) throws ResponseException{
        //join on the list param index
        assertSignedIn();
        if(params.length == 1){
            try{
                int gameNum = findGame(params);
                //server.joinGame(authToken, new JoinGameData(null, gameNum));
                state = State.INGAME;
                ChessBoard observe = new ChessBoard();
                observe.resetBoard();
                return new RenderBoard(observe).drawBoard(WHITE);
            }catch(ArrayIndexOutOfBoundsException e){
                throw new ResponseException("Game could not be found", 400);
            }
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
    private void assertSignedOut() throws ResponseException{
        if (state == SIGNED_IN){
            throw new ResponseException("You must be signed OUT", 400);
        }
    }
}
