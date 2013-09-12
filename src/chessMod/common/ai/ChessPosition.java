package chessMod.common.ai;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import chessMod.common.EntityBaseChessPiece;
import chessMod.common.EntityBishop;
import chessMod.common.EntityKing;
import chessMod.common.EntityKnight;
import chessMod.common.EntityPawn;
import chessMod.common.EntityQueen;
import chessMod.common.EntityRook;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 * This class came from pate's ChessMate, found at https://github.com/pate/chessmate. Many thanks to him for allowing
 * the usage of his code by others. These classes are a bit modified to be able to have more AI's running at the same time.
 */

public class ChessPosition{
    final static public int BLANK = 0;
    final static public int PAWN = 1;
    final static public int KNIGHT = 2;
    final static public int BISHOP = 3;
    final static public int ROOK = 4;
    final static public int QUEEN = 5;
    final static public int KING = 6;

    /**
     * An array of board squares.
     */
    public int[] board = new int[80];
    /**
     * Stores the index of pieces on the board.  Exists for faster move generation.
     */
    //   public Vector pieces = new Vector();

    boolean bWhiteKingMoved = false;
    boolean bBlackKingMoved = false;

    boolean bWhiteChecked = false;
    boolean bBlackChecked = false;

    int enPassantSquare = 0;

    /**
     * Applies the given move parameter to the board position saved in this instance of the class.
     */
    public void makeMove(ChessMove move){
        /* 	pieces.remove(move.from);
         	if ( !pieces.contains( new Integer(move.to) ) )
         		pieces.add( new Integer(move.to) );

        	pieces.set( pieces.indexOf( new Integer(move.from)) , new Integer(move.to) );
        */

        board[move.to] = board[move.from];
        board[move.from] = 0;

        if(move.to >= 70) {
            if(board[move.to] == PAWN) board[move.to] = QUEEN;
        } else if(move.to < 8) {
            if(board[move.to] == -PAWN) board[move.to] = -QUEEN;
        } else if(board[move.to] == KING && !bWhiteKingMoved) {
            bWhiteKingMoved = true;
        } else if(board[move.to] == -KING && !bBlackKingMoved) {
            bBlackKingMoved = true;
        }// else
        /*		if ( enPassantSquare > 0 )
        		{
        			if ( board[ move.to ] == PAWN && move.to-10 == enPassantSquare )
        			{
        				board[move.to-10] = 0;
        				enPassantSquare = 0;
        			} else
        			if ( board[ move.to ] == -PAWN && move.to+10 == enPassantSquare )
        			{
        				board[move.to+10] = 0;
        				enPassantSquare = 0;
        			}
        		}*/
    }

    /**
     * Instantiates the board position by mirroring another board position.
     * Used extensively during alpha-beta search.
     */
    public ChessPosition(ChessPosition p){
        System.arraycopy(p.board, 0, board, 0, 80);
        //eval = p.eval;
        bWhiteKingMoved = p.bWhiteKingMoved;
        bBlackKingMoved = p.bBlackKingMoved;

        bWhiteChecked = p.bWhiteChecked;
        bBlackChecked = p.bBlackChecked;
    }

    /**
     * Constructs an empty chess board.
     */
    public ChessPosition(){}

    public ChessPosition(EntityBaseChessPiece entityPiece){
        List<EntityBaseChessPiece> pieces = entityPiece.getChessPieces(true);
        for(EntityBaseChessPiece piece : pieces) {
            int pieceValue = 0;
            if(piece instanceof EntityPawn) pieceValue = PAWN;
            else if(piece instanceof EntityBishop) pieceValue = BISHOP;
            else if(piece instanceof EntityKnight) pieceValue = KNIGHT;
            else if(piece instanceof EntityRook) pieceValue = ROOK;
            else if(piece instanceof EntityKing) {
                pieceValue = KING;
                if(!piece.firstMove) {
                    if(piece.isBlack()) {
                        bBlackKingMoved = true;
                    } else {
                        bWhiteKingMoved = true;
                    }
                }
            } else if(piece instanceof EntityQueen) pieceValue = QUEEN;
            if(piece.targetX + piece.targetZ * 10 < board.length) board[piece.targetX + piece.targetZ * 10] = piece.isBlack() ? -pieceValue : pieceValue;

        }
        for(int i = 0; i < 8; i++) {
            board[i * 10 + 8] = 7;
            board[i * 10 + 9] = 7;
        }

    }

    public void printBoard(){
        for(int i = 0; i < 80; i++) {
            System.out.print("[" + board[i] + "] ");
            if(i % 10 == 9) System.out.println();
        }
    }

    public void writeToNBT(NBTTagCompound tag){
        for(int i = 0; i < board.length; i++) {
            tag.setInteger("tile" + i, board[i]);
        }
        tag.setBoolean("blackChecked", bBlackChecked);
        tag.setBoolean("blackKingMoved", bBlackKingMoved);
        tag.setBoolean("whiteChecked", bWhiteChecked);
        tag.setBoolean("whiteKingMoved", bWhiteKingMoved);
        tag.setByte("enPassantSquare", (byte)enPassantSquare);
    }

    public void readFromNBT(NBTTagCompound tag){
        for(int i = 0; i < board.length; i++) {
            board[i] = tag.getInteger("tile" + i);
        }
        bBlackChecked = tag.getBoolean("blackChecked");
        bBlackKingMoved = tag.getBoolean("blackKingMoved");
        bWhiteChecked = tag.getBoolean("whiteChecked");
        bWhiteKingMoved = tag.getBoolean("whiteKingMoved");
        enPassantSquare = tag.getByte("enPassantSquare");
    }

    /**
     * Returns true if the pieces are at the same place as the comparison, and when the piece movement booleans are the same.
     * @param compare
     * @return
     */
    public boolean isSame(ChessPosition compare){
        for(int i = 0; i < board.length; i++) {
            if(board[i] != compare.board[i]) return false;
        }
        if(bBlackChecked != compare.bBlackChecked) return false;
        if(bBlackKingMoved != compare.bBlackKingMoved) return false;
        if(bWhiteChecked != compare.bWhiteChecked) return false;
        if(bWhiteKingMoved != compare.bWhiteKingMoved) return false;
        if(enPassantSquare != compare.enPassantSquare) return false;
        return true;
    }

    /**
     * Returns true when the given position has a difference in active pieces from this position (unreproducable moves, e.g. pawns moved)
     * @param compare
     * @return
     */
    public boolean hasActiveDifference(ChessPosition compare){
        //check for pawn change and captures
        int thisPieceCount = 0;
        int comparePieceCount = 0;
        for(int i = 0; i < board.length; i++) {
            if(board[i] == 1 && compare.board[i] != 1) return true;
            if(board[i] == -1 && compare.board[i] != -1) return true;
            if(board[i] != 0) thisPieceCount++;
            if(compare.board[i] != 0) comparePieceCount++;
        }
        if(thisPieceCount != comparePieceCount) return true;

        //check for losing the ability to castle e.g.
        if(bBlackChecked != compare.bBlackChecked) return true;
        if(bBlackKingMoved != compare.bBlackKingMoved) return true;
        if(bWhiteChecked != compare.bWhiteChecked) return true;
        if(bWhiteKingMoved != compare.bWhiteKingMoved) return true;
        if(enPassantSquare != compare.enPassantSquare) return true;
        return false;
    }
}