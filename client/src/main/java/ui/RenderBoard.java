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
        String [] rank = {"a","b","c","d","e","f","g","h"};
        String boardString = " \u2003";
        //B[r][c]
        //true: WHITE ; false: BLACK
        boolean color = true;
        for(String k : rank){
            boardString += " "+k+"\u2003";
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
                    boardString += printSquare(pCol, BLACK, pType);
                }else{
                    boardString += printSquare(pCol, WHITE, pType);
                }
                color = !color;
            }
            color = !color;
            boardString += RESET_BG_COLOR+String.format("\u2003%d\n", i);
        }
        boardString += " \u2003";
        for(String k : rank){
            boardString += " "+k+"\u2003";
        }
        return boardString;
    }

    public String drawBoardT(ChessGame.TeamColor perspective){
        String [] rank = {"a","b","c","d","e","f","g","h"};
        String boardString = " \u2003";
        //B[r][c]
        //true: WHITE ; false: BLACK
        boolean color = true;
        for(String c : rank){
            boardString += " "+c+"\u2003";
        }
        boardString += "\n";
        for(int r = 1; r <= 8; r++){
            boardString += r+"\u2003";
            for(int c = 8; c >= 1; c--){
                ChessPiece.PieceType pType = null;
                ChessGame.TeamColor pCol = null;
                if(board.getPiece(new ChessPosition(r,c)) != null){
                    pType = board.getPiece(new ChessPosition(r,c)).getPieceType();
                    pCol = board.getPiece(new ChessPosition(r,c)).getTeamColor();
                }
                if(color){
                    boardString += printSquare(pCol, BLACK, pType);
                }else{
                    boardString += printSquare(pCol, WHITE, pType);
                }
                color = !color;
            }
            color = !color;
            boardString += RESET_BG_COLOR+String.format("\u2003%d\n",r);
        }
        boardString += " \u2003";
        for(String c : rank){
            boardString += " "+c+"\u2003";
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

