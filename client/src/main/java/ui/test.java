
import chess.ChessBoard;
import chess.ChessGame;
import client.Repl;

import static ui.EscapeSequences.*;
public static void main(String[] args) {
    Repl repl = new Repl("http://localhost:8080");
    repl.run();
}

//public class test {
//    public static void main(String[] args) {
//        ChessBoard board = new ChessBoard();
//        board.resetBoard();
//
//        RenderBoard renderer = new RenderBoard(board);
//
//        System.out.println(renderer.drawBoard(ChessGame.TeamColor.WHITE));
//        System.out.println();
//        System.out.println(renderer.drawBoardT(ChessGame.TeamColor.BLACK));
//        //System.out.println();
//        //System.out.println(renderer.drawBoard(ChessGame.TeamColor.BLACK));
//    }
//}
