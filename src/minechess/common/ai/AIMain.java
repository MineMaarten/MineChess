package minechess.common.ai;

import java.util.Vector;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 * This class came from pate's ChessMate, found at https://github.com/pate/chessmate. Many thanks to him for allowing
 * the usage of his code by others. These classes are a bit modified to be able to have more AI's running at the same time.
 */

public class AIMain{

    /**
     * The omni-present Chess reference, used nearly everywhere for calculations
     * and drawing.
     */
    public Chess chess;

    int moveTime = 0;

    /**
     * A list of moves by the players to be displayed in a table.
     * @see moveTable
     * @see moveTable_dataModel
     */
    Vector moveList = new Vector();

    /**
     * The AICaller class handles AI threading so nothing gets stucky.
     */
    public AICaller aiCaller;

    /**
     * If bQuit is true, all threads will exit and the application will close.
     */
    public boolean bQuit = false;

    /**
     * bPlaying is true when the game is in progress - not for example when the board
     * is being set up.
     */
    public boolean bPlaying = true; // are we playing
    /**
     * bSetPosition is true when the player is setting up the board.  Kind of self-explanatory.
     */
    public boolean bSetPosition = false;

    /**
     * Alert Displays a dialog containing useful information.
     */
    public void alert(String title, String message){
        //String[] SaveOptionNames = {"Continue"};
        // JLabel label = new JLabel(message);

        //JOptionPane.showOptionDialog(this, label, title, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, SaveOptionNames, SaveOptionNames[0]);
    }

    /**
     * When the player starts a new game or loads an old game, this function is invoked
     * to clear the moveList, graph and playing area of scattered pieces.  The traditional
     * chess board position is also loaded.
     */
    public void NewGame(){
        chess.bThinking = false;

        //chess.NewGame();

        // if(chess.PROGRAM == chess.WHITE) aiCaller.go();
    }

    /**
     * Main constructor. Called upon application entry. Adds a window listener to the frame
     * to listen for close messages.
     * Initiates the program by loading images, adding menu items, toolbar items and connecting
     * to the data source.
     */
    public AIMain(){

        chess = new Chess(this);
        aiCaller = new AICaller(chess);
        aiCaller.start();
        NewGame();
    }

}
