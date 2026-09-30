package chess;


import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;
/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {


    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;


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
    }


    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int r = myPosition.getRow();
        int c = myPosition.getColumn();


        ArrayList <ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor enemyColor = (this.getTeamColor() == ChessGame.TeamColor.BLACK) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;


        switch(this.type){
            case PieceType.KING:
                int [][] kingMoves = {{r+1,c-1},{r+1,c},{r+1,c+1},{r,c+1},{r-1,c+1},{r-1,c},{r-1,c-1},{r,c-1}};
                getValid(moves,board,myPosition,enemyColor,kingMoves);
                break;
            case PieceType.QUEEN:
                int [][] qDir = {{1,0},{0,1},{-1,0},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};
                for (int [] dir : qDir) getDir(moves,board,myPosition,enemyColor,dir);
                break;
            case PieceType.BISHOP:
                int [][] bDir = {{1,1},{1,-1},{-1,1},{-1,-1}};
                for (int [] dir : bDir) getDir(moves,board,myPosition,enemyColor,dir);
                break;
            case PieceType.ROOK:
                int [][] rDir = {{1,0},{-1,0},{0,1},{0,-1}};
                for (int [] dir : rDir) getDir(moves,board,myPosition,enemyColor,dir);
                break;
            case PieceType.KNIGHT:
                int [][] kMoves = {{r+2,c-1},{r+2,c+1},{r-1,c+2},{r+1,c+2},{r-2,c-1},{r-2,c+1},{r-1,c-2},{r+1,c-2}};
                getValid(moves,board,myPosition,enemyColor,kMoves);
                break;
            case PieceType.PAWN:
                if(this.getTeamColor() == ChessGame.TeamColor.WHITE){
                    pawnWhite(moves,board,myPosition,enemyColor);
                }else{
                    pawnBlack(moves,board,myPosition,enemyColor);
                }
                break;
        }
        return moves;
    }


    private void pawnBlack(ArrayList<ChessMove> moves, ChessBoard board, ChessPosition pos, ChessGame.TeamColor enemyColor){
        int r = pos.getRow();
        int c = pos.getColumn();


        ChessPiece capL = (c == 8) ? null : board.getPiece(new ChessPosition(r-1,c+1));
        ChessPiece capR = (c == 1) ? null : board.getPiece(new ChessPosition(r-1,c-1));




        //move2
        if(r == 7 && board.getPiece(new ChessPosition(r-1,c)) == null){
            if(board.getPiece(new ChessPosition(r-2,c)) == null){
                moves.add(new ChessMove(pos, new ChessPosition(r-2,c),null));
            }
        }


        //move1
        if(board.getPiece(new ChessPosition(r-1,c)) == null){
            promotions(moves, pos, r-1,c, 1);
        }
        if(capL != null && capL.getTeamColor() == enemyColor){
            promotions(moves, pos, r-1,c+1, 1);
        }
        if(capR != null && capR.getTeamColor() == enemyColor){
            promotions(moves, pos, r-1,c-1, 1);
        }
    }


    private void pawnWhite(ArrayList<ChessMove> moves, ChessBoard board, ChessPosition pos, ChessGame.TeamColor enemyColor){
        int r = pos.getRow();
        int c = pos.getColumn();


        ChessPiece capL = (c == 1) ? null : board.getPiece(new ChessPosition(r+1,c-1));
        ChessPiece capR = (c == 8) ? null : board.getPiece(new ChessPosition(r+1,c+1));


        //move2
        if(r == 2 && board.getPiece(new ChessPosition(r+1,c)) == null){
            if(board.getPiece(new ChessPosition(r+2,c)) == null){
                moves.add(new ChessMove(pos, new ChessPosition(r+2,c),null));
            }
        }


        //move1
        if(board.getPiece(new ChessPosition(r+1,c)) == null){
            promotions(moves, pos, r+1,c, 8);
        }
        if(capL != null && capL.getTeamColor() == enemyColor){
            promotions(moves, pos, r+1,c-1, 8);
        }
        if(capR != null && capR.getTeamColor() == enemyColor){
            promotions(moves, pos, r+1,c+1, 8);
        }
    }


    private void promotions(ArrayList<ChessMove> moves, ChessPosition pos, int r, int c, int backRank){
        ChessPiece.PieceType [] bigBoys = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP};
        if(r == backRank) {
            for (ChessPiece.PieceType promote: bigBoys) {
                moves.add(new ChessMove(pos, new ChessPosition(r, c), promote));
            }
        }else {
            moves.add(new ChessMove(pos, new ChessPosition(r, c), null));
        }
    }


    private void getValid(ArrayList<ChessMove> moves, ChessBoard board, ChessPosition pos, ChessGame.TeamColor enemyColor, int [][] possible){


        for(int [] move : possible){
            if(move[0] <= 0 || move[0] >= 9)continue;
            if(move[1] <= 0 || move[1] >= 9)continue;


            ChessPosition toMove = new ChessPosition(move[0], move[1]);
            if(board.getPiece(toMove) != null){
                if(board.getPiece(toMove).getTeamColor() == enemyColor){
                    moves.add(new ChessMove(pos, toMove, null));
                }
            }else{
                moves.add(new ChessMove(pos, toMove, null));
            }
        }
    }


    private void getDir (ArrayList<ChessMove> moves, ChessBoard board, ChessPosition pos, ChessGame.TeamColor enemyColor, int [] dir){
        int r = pos.getRow();
        int c = pos.getColumn();


        int vB = (dir[0] > 0) ? 9 : 0;
        int hB = (dir[1] > 0) ? 9 : 0;


        while(true){
            if(r+dir[0] == vB || c+dir[1] == hB){
                break;
            }


            ChessPosition toMove = new ChessPosition(r+dir[0], c+dir[1]);
            if(board.getPiece(toMove) != null){
                if(board.getPiece(toMove).getTeamColor() == enemyColor){
                    moves.add(new ChessMove(pos,toMove,null));
                }
                break;
            }
            moves.add(new ChessMove(pos,toMove,null));
            r+=dir[0];
            c+=dir[1];


        }
    }


}

