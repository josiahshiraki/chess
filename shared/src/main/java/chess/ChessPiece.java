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

    @Override
    public String toString() {
        if(pieceColor == ChessGame.TeamColor.BLACK){
            return "B" + type;
        }
        return "" + type;
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
                king_move(board,moves, myPosition);
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
                knight_move(board, moves, myPosition);
                break;
            case PAWN:
                break;
        }
        System.out.println("Size: " + moves.size());
        return moves;
    }

    /**
     *
     * |8|  | B|LA|CK|  |  |  |
     * |7|  |  |  |  |  |  |  |
     * |6|  |  |  |  |  |  |  |
     * |5|  |  |  |  |  |  |  |
     * |4|  |  | E| *| E|  | *|
     * |3|  |  |  | p|  |  | *|
     * |2|  | W|HI|TE|  |  | p|
     * |1|2 |3 |4 | 5| 6| 7| 8|
     *
     * @param board get pieces
     * @param moves mutate for possible moves
     * @param pos current positon of the pawn
     */
    private void pawnMoveWhite(ChessBoard board, ArrayList<ChessMove> moves, ChessPosition pos){
        int r = pos.getRow();
        int c = pos.getColumn();
        ArrayList<int[]> possibleMoves = new ArrayList<>();
        possibleMoves.add(new int[]{r+1,c});
        if(r==2)possibleMoves.add(new int[]{r+2,c});

        //int[][] possibleMoves = {{r+1,c},{r+2,c},{r+1,c-1},{r+1,c+1}};
    }

    /**
     * |8|  |  |  |  |  |  |  |
     * |7|  |  | *|  | *|  |  |
     * |6|  | *|  |  |  | *|  |
     * |5|  |  |  | k|  |  |  |
     * |4|  | *|  |  |  | *|  |
     * |3|  |  | *|  | *|  |  |
     * |2|  |  |  |  |  |  |  |
     * |1|2 |3 |4 | 5| 6| 7| 8|
     * @param moves list to mutate
     * @param pos current chess position on board
     */
    private void knight_move(ChessBoard board, ArrayList<ChessMove> moves, ChessPosition pos){
        int r = pos.getRow();
        int c = pos.getColumn();
        int [][] possibleMoves =    {
                                    {r+2,c-1},{r+2,c+1},
                                    {r+1,c+2},{r-1,c+2},
                                    {r-2,c+1},{r-2,c-1},
                                    {r-1,c-2},{r+1,c-2}
                                    };

        ChessGame.TeamColor enemyColor = ChessGame.TeamColor.BLACK; // maybe could become a private in class var because always be opposite of current color
        if (this.pieceColor == ChessGame.TeamColor.BLACK) enemyColor = ChessGame.TeamColor.WHITE; // make this into query

        get_valid_moves(board, possibleMoves, moves, enemyColor, pos);
    }

    private void king_move(ChessBoard board, ArrayList<ChessMove> moves, ChessPosition pos){
        int r = pos.getRow();
        int c = pos.getColumn();
        int [][] possibleMoves = {{r+1,c-1}, {r+1,c},{r+1,c+1},{r,c+1},{r-1,c+1},{r-1,c},{r-1,c-1},{r,c-1}};
        for(int i = 0; i < possibleMoves.length; i++){
            System.out.println("possible moves -> "+ "row: " + possibleMoves[i][0] + " col: " +possibleMoves[i][1]);
        }
        ChessGame.TeamColor enemyColor = ChessGame.TeamColor.BLACK; // maybe could become a private in class var because always be opposite of current color
        if (this.pieceColor == ChessGame.TeamColor.BLACK) enemyColor = ChessGame.TeamColor.WHITE;

        get_valid_moves(board,possibleMoves, moves, enemyColor, pos);
    }


    // abstracted method to use for knight, pawn, king with one set of moves, not continuous
    private void get_valid_moves(ChessBoard board, int[][] possibleMoves, ArrayList<ChessMove> moves, ChessGame.TeamColor enemyColor, ChessPosition pos){
        for(int i = 0; i < possibleMoves.length; i++){
            if((possibleMoves[i][0] <= 0) || (possibleMoves[i][0] > 8))continue; //rejects all off board moves
            if((possibleMoves[i][1] <= 0) || (possibleMoves[i][1] > 8))continue;
            ChessPosition check = new ChessPosition(possibleMoves[i][0], possibleMoves[i][1]);
            if(board.getPiece(check) != null){ //checks if there is a piece on a square
                if (board.getPiece(check).pieceColor == enemyColor){ //checks if that piece is an enemy piece
                    moves.add(new ChessMove(pos, check, null));
                }
            }else{
                moves.add(new ChessMove(pos, check, null));
            }
        }
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

        int vertBound = 0; //make into query, eliminate the if statements
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
