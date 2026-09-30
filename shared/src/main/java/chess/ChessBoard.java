package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {


    private ChessPiece[][] board; //board setup

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     *         if (piece.getPieceType() == ChessPiece.PieceType.KING) {
     *             //return;
     *         }
     *         first row starts at 1, must subtract
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if(board[position.getRow()-1][position.getColumn()-1] == null){
            return null;
        }
        return board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     * |8|  |  |  |  |  |  |  |
     * |7|  |  |  |BL|  |  |  |
     * |6|  |  |  |  |  |  |  |
     * |5|  |  |  |  |  |  |  |
     * |4|  |  |  |  |  |  |  |
     * |3|  |  |  |  |  |  |  |
     * |2|  |  |  |WH|  |  |  |
     * |1|2 |3 |4 | 5| 6| 7| 8|     *
     */
    public void resetBoard() {
        ChessPiece.PieceType [] backRank = {
                ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,ChessPiece.PieceType.ROOK
        };
        //BLACK PIECES
        for(int i = 0; i < 8; i++){
            addPiece(new ChessPosition(7,i+1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(8,i+1), new ChessPiece(ChessGame.TeamColor.BLACK, backRank[i]));
        }
        //WHITE PIECES
        for(int i = 0; i < 8; i++){
            addPiece(new ChessPosition(2,i+1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(1,i+1), new ChessPiece(ChessGame.TeamColor.WHITE, backRank[i]));
        }
    }

    @Override
    public String toString() {
        String retString = "";
        for(int i = board.length-1; i >= 0; i--){
            for(int j = 0; j < board[i].length; j++){
                retString += board[i][j] + " ";
            }
            retString += "\n";
        }
        return retString;
    }

}
