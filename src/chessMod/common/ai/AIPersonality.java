package chessMod.common.ai;

import java.util.Random;

import chessMod.common.ChessMod;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 *
 */

public class AIPersonality{
    public static String getPreTurnMessage(float aiPoints, float prevPoints, Random rand){
        if(ChessMod.DEBUG) {
            return "[Pre] Actual points: " + aiPoints + ", previous points: " + prevPoints;
        } else {
            while(true) {
                switch(rand.nextInt(4)){
                    case 0:
                        return "message.ai.preTurn.thinking";
                    case 1:
                        if(aiPoints < -10F) return "message.ai.preTurn.tough";
                    case 2:
                        if(prevPoints - aiPoints < -5F) return "message.ai.preTurn.goodMove";
                }
            }

        }
    }

    public static String getPostTurnMessage(float aiPoints, float prevPoints, int secsElapsed, Random rand){
        if(ChessMod.DEBUG) {
            return "[Post] Actual points: " + aiPoints + ", previous points: " + prevPoints + ". sec past: " + secsElapsed;
        } else {
            while(true) {
                switch(rand.nextInt(4)){
                    case 0:
                        return "message.ai.postTurn.yourTurn";
                    case 1:
                        if(secsElapsed < 5) return "message.ai.postTurn.quickMove";
                        if(secsElapsed > 60) return "message.ai.postTurn.slowMove";
                    case 2:
                        if(prevPoints - aiPoints > 5F) return "message.ai.postTurn.aiGoodMove";
                    case 3:
                        if(aiPoints > 10F) return "message.ai.postTurn.advantage";

                }
            }
        }
    }
}
