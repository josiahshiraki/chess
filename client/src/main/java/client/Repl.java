package client;

import java.util.Arrays;
import java.util.Scanner;

import model.*;


import static ui.EscapeSequences.*;

public class Repl {

    private final ChessClient chessClient;
    public Repl(String serverUrl){
        this.chessClient = new ChessClient(serverUrl);
    }

    public void run(){
        System.out.println(BLACK_KING+"Welcome to 240 chess! Type Help to get started."+WHITE_KING);
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")){
            printPrompt();
            String userIn = scanner.nextLine();
            try{
                result = chessClient.eval(userIn);
                System.out.println(result);
            }catch (Throwable e){
                var msg = e.toString();
                System.out.println(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt(){
        System.out.println("["+chessClient.getState()+"] " + ">>>");
    }

}
