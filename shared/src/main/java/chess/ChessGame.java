package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;


    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceToMove = board.getPiece(move.getStartPosition());
        this.board.addPiece(move.getEndPosition(), pieceToMove); //set piece to its new position
        this.board.addPiece(move.getStartPosition(), null); //remove object from original position
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor enemy = (teamColor == TeamColor.BLACK) ? TeamColor.WHITE : TeamColor.BLACK;
        ArrayList <ChessPosition> enemyPieces = scanForEnemy(enemy);

        for(ChessPosition check : enemyPieces){
            ChessPiece enemyPiece = board.getPiece(check);
            Collection <ChessMove> moves = enemyPiece.pieceMoves(board, check);
            ChessPosition kingPos = findKing(teamColor);
            for(ChessMove move : moves){
                ChessPosition threat = move.getEndPosition();
                if(kingPos == threat) {
                    return true;
                }
            }
        }
        return false;
    }

    private ChessPosition findKing(TeamColor teamColor){
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                if(this.board.getPiece(new ChessPosition(r+1,c+1)).getPieceType() == ChessPiece.PieceType.KING){
                    if(this.board.getPiece(new ChessPosition(r+1,c+1)).getTeamColor() == teamColor) {
                        return new ChessPosition(r + 1, c + 1);
                    }
                }
            }
        }
        return null;
    }

    private ArrayList<ChessPosition> scanForEnemy(TeamColor enemy){
        ArrayList <ChessPosition> enemyPieces = new ArrayList<>();
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                ChessPiece toCheck = this.board.getPiece(new ChessPosition(r+1,c+1));
                if(toCheck.getTeamColor() == enemy){
                    enemyPieces.add(new ChessPosition(r+1,c+1));
                }
            }
        }
        return enemyPieces;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        throw new RuntimeException("Not implemented");
    }
}
