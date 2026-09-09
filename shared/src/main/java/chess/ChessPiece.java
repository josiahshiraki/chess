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
        String [] edges = check_edge(r,c);

    }

    private String[] check_edge(int r, int c){
        String [] ret_edges  = new String[2]; // first is top or bot flank, second is left or right flank, if none, NULL

        if(r-1 == -1){
            ret_edges[0] = "top";
        }else if(r+1 == 8){
            ret_edges[0] = "bot";
        }
        if(c-1 == -1){
            ret_edges[1] = "left";
        }else if(c+1 == 8){
            ret_edges[1] = "right";
        }

        return ret_edges;
    }
}
