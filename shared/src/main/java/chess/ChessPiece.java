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
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ChessGame.TeamColor enemyColor = (this.pieceColor == ChessGame.TeamColor.BLACK) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        ArrayList<ChessMove> moves = new ArrayList<>();
        switch(this.type){
            case KING:
                king_move(board,moves, myPosition, enemyColor);
                break;
            case QUEEN:
                int [][] qDir = {{1,0},{0,1},{-1,0},{0,-1},{1,1},{-1,1},{-1,-1},{1,-1}};
                for(int [] coord : qDir) getMovesInDirection(moves, board, myPosition, coord[0],coord[1], enemyColor); //N Direction
                break;
            case ROOK:
                int [][] rDir = {{1,0},{0,1},{-1,0},{0,-1}};
                for(int[] coord : rDir) getMovesInDirection(moves, board, myPosition, coord[0],coord[1], enemyColor);
                break;
            case BISHOP:
                int [][] bDir = {{1,1},{-1,1},{-1,-1},{1,-1}};
                for(int[] coord : bDir) getMovesInDirection(moves, board, myPosition, coord[0],coord[1], enemyColor);
                break;
            case KNIGHT:
                knight_move(board, moves, myPosition, enemyColor);
                break;
            case PAWN:
                break;
        }
        return moves;
    }

    /**
     * @param board get pieces
     * @param moves mutate for possible moves
     * @param pos current positon of the pawn
     */

    private void pawnMoveWhite(ChessBoard board, ArrayList<ChessMove> moves, ChessPosition pos, ChessGame.TeamColor enemyColor){
        int r = pos.getRow();
        int c = pos.getColumn();
//        ArrayList<int[]> possibleMoves = new ArrayList<>();

        //to check possible move squares if there is a piece to capture or your own piece in the way
        ChessPiece captureL = (c-1 == 0) ? null : board.getPiece(new ChessPosition(r+1,c-1));
        ChessPiece captureR = (c+1 == 9) ? null : board.getPiece(new ChessPosition(r+1, c+1));
        ChessPiece move1 = board.getPiece(new ChessPosition(r+1, c));

        //move2
        if(board.getPiece(new ChessPosition(r+1, c)) != null){
            if(board.getPiece(new ChessPosition(r+2, c)) != null){
                moves.add(new ChessMove(pos, new ChessPosition(r+2, c), null));
            }
        }

        PieceType [] bigBoys = {PieceType.BISHOP, PieceType.ROOK, PieceType.KNIGHT, PieceType.QUEEN};

        if(captureL != null && captureL.getTeamColor() == enemyColor){
            for(PieceType toPromote : bigBoys){
                ChessMove toAdd = new ChessMove(pos, new ChessPosition(r+1, r-1), toPromote);
                moves.add(toAdd);
            }

        }
        if(captureR != null && captureR.getTeamColor() == enemyColor){
            for(PieceType toPromote : bigBoys){
                ChessMove toAdd = new ChessMove(pos, new ChessPosition(r+1, r-1), toPromote);
                moves.add(toAdd);
            }
        }
        if(move1 == null) {
            for(PieceType toPromote : bigBoys){
                ChessMove toAdd = new ChessMove(pos, new ChessPosition(r+1, r-1), toPromote);
                moves.add(toAdd);
            }
        }
    }

    private void knight_move(ChessBoard board, ArrayList<ChessMove> moves, ChessPosition pos, ChessGame.TeamColor enemyColor){
        int r = pos.getRow();
        int c = pos.getColumn();
        int [][] possibleMoves = {{r+2,c-1},{r+2,c+1}, {r+1,c+2},{r-1,c+2}, {r-2,c+1},{r-2,c-1}, {r-1,c-2},{r+1,c-2}};
        get_valid_moves(board, possibleMoves, moves, enemyColor, pos);
    }

    private void king_move(ChessBoard board, ArrayList<ChessMove> moves, ChessPosition pos,ChessGame.TeamColor enemyColor){
        int r = pos.getRow();
        int c = pos.getColumn();
        int [][] possibleMoves = {{r+1,c-1}, {r+1,c},{r+1,c+1},{r,c+1},{r-1,c+1},{r-1,c},{r-1,c-1},{r,c-1}};
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

    private void getMovesInDirection(ArrayList<ChessMove> moves, ChessBoard board, ChessPosition pos, int rDir, int cDir, ChessGame.TeamColor enemyColor){
        int r = pos.getRow();
        int c = pos.getColumn();

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
