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
        ArrayList<ChessMove> moves = new ArrayList<>();
        //throw new RuntimeException("Not implemented");
        switch(this.type){
            case KING:
                break;
            case QUEEN:
                getMovesInDirection(moves, board, myPosition, 1,0); //N Direction
                getMovesInDirection(moves, board, myPosition, 0,1); //E Direction
                getMovesInDirection(moves, board, myPosition, -1,0); //S Direction
                getMovesInDirection(moves, board, myPosition, 0,-1); //W Direction

                getMovesInDirection(moves, board, myPosition, 1,1); //NE Direction
                getMovesInDirection(moves, board, myPosition, -1,1); //SE Direction
                getMovesInDirection(moves, board, myPosition, -1,-1); //SW Direction
                getMovesInDirection(moves, board, myPosition, 1,-1); //NW Direction
                print_directions(moves);
                break;
            case ROOK:
                getMovesInDirection(moves, board, myPosition, 1,0); //N Direction
                getMovesInDirection(moves, board, myPosition, 0,1); //E Direction
                getMovesInDirection(moves, board, myPosition, -1,0); //S Direction
                getMovesInDirection(moves, board, myPosition, 0,-1); //W Direction
                break;
            case BISHOP:
                getMovesInDirection(moves, board, myPosition, 1,1); //NE Direction
                getMovesInDirection(moves, board, myPosition, -1,1); //SE Direction
                getMovesInDirection(moves, board, myPosition, -1,-1); //SW Direction
                getMovesInDirection(moves, board, myPosition, 1,-1); //NW Direction
                break;
            case KNIGHT:
                break;
            case PAWN:
                break;
        }
        return moves;
    }



    private void print_directions(ArrayList<ChessMove> moves){
        for(int i = 0; i < moves.size();i++){
            //System.out.println("start row: " +moves.get(i).getStartPosition().getRow() + " move col: " + moves.get(i).getStartPosition().getColumn());
            System.out.println("move row: " +moves.get(i).getEndPosition().getRow() + " move col: " + moves.get(i).getEndPosition().getColumn());
        }
        System.out.println(moves.size());
    }

    /**
     * @param moves mutates that move arraylist
     * @param board check piece positions
     * @param pos check curr piece position
     * @param rDir up +1 or down -1 direction
     * @param cDir left -1 or right +1 direction
     */
    private void getMovesInDirection(ArrayList<ChessMove> moves, ChessBoard board, ChessPosition pos, int rDir, int cDir){
        int r = pos.getRow();
        int c = pos.getColumn();

        ChessGame.TeamColor enemyColor = ChessGame.TeamColor.BLACK; // maybe could become a private in class var because always be opposite of current color
        if (this.pieceColor == ChessGame.TeamColor.BLACK) enemyColor = ChessGame.TeamColor.WHITE;

        int vertBound = 0;
        int horBound = 0;
        if (rDir > 0)vertBound = 9;
        if(cDir > 0)horBound = 9;

        while(true) {
            if ((r+rDir == vertBound) || (c+cDir == horBound)) {
                break;

            } else if (board.getPiece(new ChessPosition(r+rDir, c+cDir)) != null) {
                if (board.getPiece(new ChessPosition(r+rDir, c+cDir)).pieceColor == enemyColor) {
                    moves.add(new ChessMove(pos, new ChessPosition(r+rDir, c+cDir), null));
                }
                break;
            } else {
                moves.add(new ChessMove(pos, new ChessPosition(r+rDir, c+cDir), null));
            }
            r+=rDir;
            c+=cDir;
        }
    }
}
