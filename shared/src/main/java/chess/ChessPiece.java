package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     * Will return an ArrayList full of valid moves (coord (r,c)) for specific piece
     * numbers.add(new int[]{3, 5});
     * |8|  |  |  |  |  |  |  |
     * |7|  |  |  |  |  |  |  |
     * |6|  |  |  |  |  |  |  |
     * |5|  |  |  |  |  |  |  |
     * |4|  |  |  |  |  |  |  |
     * |3|  |  |  |  |  |  |  |
     * |2|  |  |  |  |  |  |  |
     * |1|2 |3 |4 | 5| 6| 7| 8|
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<int[]> moves = new ArrayList<>();
        //throw new RuntimeException("Not implemented");
        switch(this.type){
            case KING:
                king_moves(moves, myPosition);
                break;
            case QUEEN:
                break;
            case ROOK:
                break;
            case BISHOP:
                break;
            case KNIGHT:
                break;
            case PAWN:
                break;
        }

        return null;
    }
    /**
     *helper for diagonal moves (queen and bishop)
     *      * |8| *|  |  |  |  |  | *|
     *      * |7|  | *|  |  |  | *|  |
     *      * |6|  |  |* |  | *|  |  |
     *      * |5|  |  |  | B|  |  |  |
     *      * |4|  |  | *|  | *|  |  |
     *      * |3|  | *|  |  |  | *|  |
     *      * |2| *|  |  |  |  |  | *|
     *      * |1|2 |3 |4 | 5| 6| 7| 8|
     * use 4 while loops, each traveling in the NE, SE, SW, and NW direction
     * for loops will break for the following conditions: if it reaches the end of the board or reaches another piece (if enemy, include that square too)
     *
     * @param pos (current chess position)
     * @return collection of valid diagonal moves
     */
    private ArrayList<int[]> diagonal_moves(ChessBoard board, ChessPosition pos){//
        ArrayList<int[]> moves = new ArrayList<>();
        int r = pos.getRow();
        int c = pos.getColumn();

        //determine the enemy color for capture
        ChessGame.TeamColor enemyColor = ChessGame.TeamColor.BLACK;
        if (this.pieceColor == ChessGame.TeamColor.BLACK) enemyColor = ChessGame.TeamColor.WHITE;

        while(true){
            if((r+1 == 9) || (c+1 == 9)){ //check if on edge
                break;
            }else if(board.getPiece(pos) != null){
                if(board.getPiece(pos).pieceColor == enemyColor){
                    moves.add(new int[]{r,c});
                }
                break;
            }else{
                moves.add(new int[]{r,c});
            }
            r++;
            c++;
        }
        return moves;
    }

    //mutator for king_move board
    private void king_moves(ArrayList<int[]> moves, ChessPosition pos){
        int r = pos.getRow();
        int c = pos.getColumn();
        int [][] adj_squares = {{r+1,c-1},{r+1,c},{r+1,c+1},
                                {r,c-1},          {r,c+1},
                                {r-1,c-1},{r-1,c},{r-1,c+1}};
        for(int[] square: adj_squares){
            if((square[0] == 0) || (square[1] == 0) || (square[0] == 9) || (square[1] == 9))continue; //check bounds
            //if(same-color piece in adj_squares)continue;
            //if(king will be in check if move to that square)continue;

            moves.add(square);
        }

    }
}


//            if(square[1] == -1)continue;
//            if(square[0] == 8)continue;
//            if(square[1] == 8)continue;