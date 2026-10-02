package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }
    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }

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
     * Use arraylists function .remove(a) to remove 1
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if(piece == null){
            return null;
        }
        Collection <ChessMove> valid = piece.pieceMoves(board,startPosition);;
        if (isInCheck(piece.getTeamColor()) && (piece.getPieceType() != ChessPiece.PieceType.KING)){
            valid.removeIf(check -> simulateBoard(check, piece.getTeamColor()));
            return valid;
        }
        //I think if a knight is pinned, it cannot move in any direction so we would only have to check once
        // if a piece is pinned in specific direction? -> the resulting move check will put the king in check, so we remove from valid
        valid.removeIf(check -> simulateBoard(check, piece.getTeamColor()));

        return valid;
    }

    //method to check ONE SINGLE move to see if moving that piece will put the king in check
    //returns a boolean that indicates whether that move puts teamColor King in check
    private boolean simulateBoard(ChessMove check, TeamColor teamColor){
        ChessBoard simulate = copyBoard();

        //simulates a move
        ChessPiece pieceToMove = simulate.getPiece(check.getStartPosition());
        simulate.addPiece(check.getEndPosition(), pieceToMove); //set piece to its new position
        simulate.addPiece(check.getStartPosition(), null); //remove object from original position

        System.out.println(check.getEndPosition() + " || " + simulate.getPiece(check.getEndPosition()) + " || " + isInCheckSim(teamColor, simulate));
        return isInCheckSim(teamColor, simulate);
    }

    //has to simulate a board so we don't mutate the actual game board which could lead to problems
    public boolean isInCheckSim(TeamColor teamColor, ChessBoard simulate) {
        TeamColor enemy = (teamColor == TeamColor.BLACK) ? TeamColor.WHITE : TeamColor.BLACK;
        ArrayList <ChessPosition> enemyPieces = scanForPieces(enemy);
        ChessPosition kingPos = findKingSim(teamColor, simulate);
        //System.out.println("kingPos: " + kingPos);
        assert kingPos != null;

        for(ChessPosition check : enemyPieces){
            ChessPiece enemyPiece = simulate.getPiece(check);

            if(enemyPiece.getPieceType() == ChessPiece.PieceType.PAWN){
                if(pawnThreat(check, kingPos, enemy)) return true;
                continue;
            }

            Collection <ChessMove> moves = enemyPiece.pieceMoves(simulate, check);
            for(ChessMove move : moves){
                ChessPosition threat = move.getEndPosition();
                if(kingPos.equals(threat)) {
                    // System.out.println("threatened by: " + enemyPiece);
                    return true;
                }
            }
        }
        return false;
    }

    private ChessBoard copyBoard(){
        ChessBoard simulate = new ChessBoard();
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                ChessPiece toAdd = board.getPiece(new ChessPosition(r+1,c+1));
                if(toAdd != null) simulate.addPiece(new ChessPosition(r+1,c+1), toAdd);
            }
        }
        return simulate;
    }

    /**
     * Makes a move in the chess game
     *
     * throws exception if an invalid move
     * might get a chess move that is illegal, not necceasirly from possible moves, so get valid moves first then check if move inside, if not throw error
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece pieceToMove = board.getPiece(move.getStartPosition());

        if(move.getPromotionPiece() != null){
            pieceToMove = new ChessPiece(teamTurn, move.getPromotionPiece());
        }


        if(pieceToMove == null){
            throw new InvalidMoveException("Moved no Piece");
        }
        // THROW ERRORS
        Collection <ChessMove> valid = validMoves(start);
        if (!valid.contains(move)){
            throw new InvalidMoveException("Invalid Move");
        }else if(pieceToMove.getTeamColor() != teamTurn){
            throw new InvalidMoveException("not your turn");
        }


        this.board.addPiece(move.getEndPosition(), pieceToMove); //set piece to its new position
        this.board.addPiece(move.getStartPosition(), null); //remove object from original position
        this.teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK: TeamColor.WHITE; //switches the color after every turn


    }

    /**
     * Determines if the given team is in check
     * if you change this method make sure to change the simulation isInCheck method too :)
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor enemy = (teamColor == TeamColor.BLACK) ? TeamColor.WHITE : TeamColor.BLACK;
        ChessPosition kingPos = findKing(teamColor);
        assert kingPos != null;
        ArrayList <ChessPosition> enemies = scanForPieces(enemy);
        for(ChessPosition check : enemies){
            ChessPiece enemyPiece = board.getPiece(check);

            if(enemyPiece.getPieceType() == ChessPiece.PieceType.PAWN){
                if(pawnThreat(check, kingPos, enemy)) return true;
                continue;
            }

            for(ChessMove threat : enemyPiece.pieceMoves(board, check)){
                if(kingPos.equals(threat.getEndPosition()))return true;
            }
        }
        return false;
    }

    //returns true if a pawn is threatening check
    private boolean pawnThreat(ChessPosition pawnPos, ChessPosition kingPos, TeamColor pawnC){
        //check pawns immediate diagonals instead of front moves because pieceMoves only returns a diagonal if a enemy piece is there
        int r = pawnPos.getRow();
        int c = pawnPos.getColumn();
        int advance = (pawnC == TeamColor.WHITE) ? 1 : -1; //black is -1

        ChessPosition checkL = new ChessPosition(r+advance, c+1);
        ChessPosition checkR = new ChessPosition(r+advance, c-1);

        if(checkL.equals(kingPos)){
            return true;
        }else{
            return checkR.equals(kingPos);
        }
    }

    private ChessPosition findKingSim(TeamColor teamColor, ChessBoard simulate){
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                ChessPiece piece = simulate.getPiece(new ChessPosition(r+1,c+1));
                if(piece == null) continue;
                if(piece.getPieceType() == ChessPiece.PieceType.KING){
                    if(piece.getTeamColor() == teamColor) {
                        return new ChessPosition(r + 1, c + 1);
                    }
                }
            }
        }
        return null;
    }

    private ChessPosition findKing(TeamColor teamColor){
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                ChessPiece piece = this.board.getPiece(new ChessPosition(r+1,c+1));
                if(piece == null) continue;
                if(piece.getPieceType() == ChessPiece.PieceType.KING){
                    if(piece.getTeamColor() == teamColor) {
                        return new ChessPosition(r + 1, c + 1);
                    }
                }
            }
        }
        return null;
    }

    //scans for pieces of a given color
    private ArrayList<ChessPosition> scanForPieces(TeamColor color){
        ArrayList <ChessPosition> enemyPieces = new ArrayList<>();
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                ChessPiece toCheck = this.board.getPiece(new ChessPosition(r+1,c+1));
                if(toCheck == null)continue;
                if(toCheck.getTeamColor() == color){
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
    public boolean isInCheckmate(TeamColor teamColor){
        if (!isInCheck(teamColor))return false;
        ChessPosition kingPos = findKing(teamColor);

        //have to check if an Ally piece can block a check -> simulate every ally piece
        for (ChessPosition ally : scanForPieces(teamColor)){
            for (ChessMove check : validMoves(ally)){
                if(!simulateBoard(check, teamColor)) return false;
            }
        }

        //if the king is in check, and he has no valid moves left then the game is over, enemy has checkmated you XD
        return validMoves(kingPos).isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheckmate(teamColor)) return false; //if in checkmate, not in stalemate
        for (ChessPosition ally : scanForPieces(teamColor)){
            if(!validMoves(ally).isEmpty()) return false;
        }
        //returns true if no pieces (including the king) has no valid moves
        return true;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
