package chessMod.common.ai;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 * This class came from pate's ChessMate, found at https://github.com/pate/chessmate. Many thanks to him for allowing
 * the usage of his code by others. These classes are a bit modified to be able to have more AI's running at the same time.
 */

public class ChessMove// implements Comparator
{
    public int from;
    public int to;

    private static final char[] rankNames = {'h', 'g', 'f', 'e', 'd', 'c', 'b', 'a', '/', '/'};

    public String squareString(int square_index){
        return new String("" + rankNames[square_index % 10] + (square_index / 10 + 1));
    }

    @Override
    public String toString(){
        if(from == 0 && to == 0) return "..";
        return new String(squareString(from) + squareString(to));
    }

    public ChessMove(){}

    public ChessMove(int from, int to){
        this.from = from;
        this.to = to;
    }

    public ChessMove(ChessMove m){
        from = m.from;
        to = m.to;
    }
}