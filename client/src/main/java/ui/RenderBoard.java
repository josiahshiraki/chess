package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static chess.ChessGame.TeamColor.*;
import static ui.EscapeSequences.*;

public class RenderBoard {
    private ChessBoard board;
    public RenderBoard(ChessBoard board){
        this.board = board;
    }

    public String drawBoard(ChessGame.TeamColor perspective){

        String [] fileW = {"a","b","c","d","e","f","g","h"};
        String [] fileB = {"h","g","f","e","d","c","b","a"};
        String boardString = " \u2003";
        //B[r][c]
        //true: WHITE ; false: BLACK
        boolean color = true;
        if(perspective == WHITE) {
            for (String k : fileW) {
                boardString += " " + k + "\u2003";
            }
        }else{
            for (String k : fileB) {
                boardString += " " + k + "\u2003";
            }
        }
        boolean isWhite = perspective == WHITE;

        int rowStart = isWhite ? 8 : 1;
        int rowStep = isWhite ? -1 : 1;

        int colStart = isWhite ? 1 : 8;
        int colStep = isWhite ? 1 : -1;

        boardString += "\n";
        for(int i=rowStart; i>=1 && i<=8;i+=rowStep){
            boardString += i +"\u2003";
            for(int j=colStart; j<=8 && j>=1; j+=colStep){

                ChessPiece.PieceType pType = null;
                ChessGame.TeamColor pCol = null;
                if(board.getPiece(new ChessPosition(i, j)) != null){
                    pType = board.getPiece(new ChessPosition(i, j)).getPieceType();
                    pCol = board.getPiece(new ChessPosition(i, j)).getTeamColor();
                }
                if(color){
                    boardString += printSquare(pCol, WHITE, pType);
                }else{
                    boardString += printSquare(pCol, BLACK, pType);
                }
                color = !color;
            }
            color = !color;
            boardString += RESET_BG_COLOR+String.format("\u2003%d\n", i);
        }
        boardString += " \u2003";
        if(perspective == WHITE) {
            for (String k : fileW) {
                boardString += " " + k + "\u2003";
            }
        }else{
            for (String k : fileB) {
                boardString += " " + k + "\u2003";
            }
        }
        return boardString;
    }



    private String printSquare(ChessGame.TeamColor pieceCol, ChessGame.TeamColor squareCol,ChessPiece.PieceType pieceT){
        String piece = getPiece(pieceCol, pieceT);
        if(squareCol == WHITE) {
            return SET_BG_COLOR_LIGHT_GREY + piece;
        }
        return SET_BG_COLOR_DARK_GREY + piece;

    }

    private String getPiece(ChessGame.TeamColor col, ChessPiece.PieceType pieceT){
        String piece;
        if(pieceT == null){
            return EMPTY;
        }
        piece = switch (pieceT) {
            case PAWN -> (col == ChessGame.TeamColor.WHITE) ? WHITE_PAWN : BLACK_PAWN;
            case KING -> (col == ChessGame.TeamColor.WHITE) ? WHITE_KING : BLACK_KING;
            case QUEEN -> (col == ChessGame.TeamColor.WHITE) ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> (col == ChessGame.TeamColor.WHITE) ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> (col == ChessGame.TeamColor.WHITE) ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> (col == ChessGame.TeamColor.WHITE) ? WHITE_ROOK : BLACK_ROOK;
        };
        return piece;
    }

}

