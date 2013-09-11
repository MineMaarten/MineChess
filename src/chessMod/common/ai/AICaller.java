package chessMod.common.ai;

import org.apache.commons.lang3.time.StopWatch;

import chessMod.common.EntityBaseChessPiece;
import chessMod.common.EntityKing;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 * This class came from pate's ChessMate, found at https://github.com/pate/chessmate. Many thanks to him for allowing
 * the usage of his code by others. These classes are a bit modified to be able to have more AI's running at the same time.
 */

public class AICaller extends Thread{
    /**
     * A reference to the parent Chess class.
     * @see class Chess
     */
    Chess chess;
    public EntityBaseChessPiece entityPiece;

    boolean bStart = false;

    /**
     * This method starts the AI
     * @param entityPiece piece that the board is associated with. Always use a piece that is a computerpiece!
     */
    public void go(EntityBaseChessPiece entityPiece){
        if(!bStart) {
            this.entityPiece = entityPiece;
            chess.pos = new ChessPosition(entityPiece);
            if(!entityPiece.isBlack()) {//when the computerpiece color is white, we need to inform the AI logic about that.
                chess.HUMAN = chess.BLACK;
                chess.PROGRAM = chess.WHITE;
            }
            entityPiece.sendChatToNearbyPlayers(null, AIPersonality.getPreTurnMessage(chess.positionEvaluation(chess.pos, chess.PROGRAM), ((EntityKing)entityPiece).lastPositionScore, entityPiece.getRNG()));

            ((EntityKing)entityPiece).lastPositionScore = chess.positionEvaluation(chess.pos, chess.PROGRAM);
            bStart = true;
            //System.out.println("AI going to run! Board received: ");
            // chess.pos.printBoard();
        }
    }

    public void cancel(){
        bStart = false;
    }

    public void exit(){
        bRunning = false;
    }

    /**
     * A boolean variable indicating whether the AICaller class is running.
     * This prevents the possibility of two threads simultaneously manipulating the
     * chess board.  This should never happen, but one can never be too safe.
     */
    private boolean bRunning = false;

    public AICaller(Chess chess){
        this.chess = chess;
        //Thread.currentThread().setPriority(MAX_PRIORITY);
    }

    /**
     * This function is called when a player moves a piece and it is the AI's turn to
     * make a move.  It simply processes the search in a separate thread so the application is not
     * locked up.
     * The thread will exit if chess.bThinking is falsified.
     */
    @Override
    public void run(){
        if(bRunning) return;
        bRunning = true;
        // System.out.println("AI initialized, waiting for a call...");

        while(!chess.main.bQuit && bRunning) {
            if(bStart) {
                bStart = false;
                try {
                    Thread.currentThread().sleep(1000);
                } catch(InterruptedException ex) {
                    System.out.println("AICaller Thread Sleep Error");
                }
                StopWatch stopwatch = new StopWatch();
                stopwatch.start();

                chess.bThinking = true;

                ChessMove n = chess.playGame(chess.pos, chess.PROGRAM);
                if(chess.bThinking) {
                    chess.bThinking = false; // consider removing the bRunning?
                    entityPiece.executeMove(n);

                    chess.pos.makeMove(n);
                    entityPiece.sendChatToNearbyPlayers(null, AIPersonality.getPostTurnMessage(chess.positionEvaluation(chess.pos, chess.PROGRAM), ((EntityKing)entityPiece).lastPositionScore, (int)(stopwatch.getTime() / 1000), entityPiece.getRNG()));

                    //System.out.println("AI executed the move " + (n != null ? n.toString() : "NO MOVE FOUND"));
                    chess.main.bQuit = true;
                }
            }

            try {
                Thread.currentThread().sleep(50);
            } catch(InterruptedException ex) {
                System.out.println("AICaller Thread Sleep Error");
            }

        }

        bRunning = false;

    }
}