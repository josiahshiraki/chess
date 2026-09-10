package chess;

import java.util.ArrayList;
import java.util.Collection;

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
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<int[]> moves = new ArrayList<>();
        //throw new RuntimeException("Not implemented");
        switch(this.type){
            case KING:
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
    //mutator for king_move board
    private void king_moves(ArrayList<int[]> moves, ChessPosition pos){
        int r = pos.getRow()-1;
        int c = pos.getColumn()-1;
        int [][] adj_squares = {{r-1,c-1},{r-1,c},{r-1,c+1},
                                {r,c-1},          {r,c+1},
                                {r+1,c-1},{r+1,c},{r+1,c+1}};
        for(int[] square: adj_squares){
            if(square[0] == -1)continue;
            if(square[1] == -1)continue;
            if(square[0] == 8)continue;
            if(square[1] == 8)continue;

            moves.add(square);

        }

    }
}
