package minechess.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class AchievementHandler{
    public static final List<Achievement> achieveList = new ArrayList<Achievement>();

    public static void init(){
        achieveList.add(new Achievement("movePiece", "movePiece", 1, 3, new ItemStack(MineChess.itemPieceMover, 1, 0), null).setIndependent());
        achieveList.add(new Achievement("castling", "castling", 1, -1, new ItemStack(MineChess.itemPieceMover, 1, 0), getAchieveFromID("movePiece")).setIndependent().setSpecial());
        achieveList.add(new Achievement("enterArena", "enterArena", 3, 5, new ItemStack(MineChess.itemPieceMover, 1, 4), null).setIndependent());
        achieveList.add(new Achievement("puzzleFailCreepy", "puzzleFailCreepy", 5, 2, new ItemStack(Items.skull), getAchieveFromID("enterArena")).setIndependent());
        achieveList.add(new Achievement("puzzleFailPotion", "puzzleFailPotion", 5, 4, new ItemStack(Items.potionitem), getAchieveFromID("enterArena")).setIndependent());
        achieveList.add(new Achievement("puzzleFailTransform", "puzzleFailTransform", 5, 6, new ItemStack(Items.egg), getAchieveFromID("enterArena")).setIndependent());
        achieveList.add(new Achievement("puzzleFailFire", "puzzleFailFire", 5, 8, new ItemStack(Items.flint_and_steel), getAchieveFromID("enterArena")).setIndependent());
        achieveList.add(new Achievement("enPassant", "enPassant", 1, 7, new ItemStack(MineChess.itemPieceMover, 1, 0), getAchieveFromID("movePiece")).setIndependent().setSpecial());
        achieveList.add(new Achievement("puzzleWin", "puzzleWin", 3, 0, new ItemStack(Blocks.chest), getAchieveFromID("enterArena")).setIndependent());
        achieveList.add(new Achievement("stalemate", "stalemate", -1, 1, new ItemStack(MineChess.itemPieceMover, 1, 0), getAchieveFromID("movePiece")).setIndependent().setSpecial());
        achieveList.add(new Achievement("checkmate", "checkmate", -1, 3, new ItemStack(MineChess.itemPieceMover, 1, 0), getAchieveFromID("movePiece")).setIndependent());
        achieveList.add(new Achievement("check", "check", -2, 4, new ItemStack(MineChess.itemPieceMover, 1, 0), getAchieveFromID("movePiece")).setIndependent());
        achieveList.add(new Achievement("lose", "lose", -1, 5, new ItemStack(MineChess.itemPieceMover, 1, 0), getAchieveFromID("movePiece")).setIndependent());

        for(Achievement achieve : achieveList) {
            achieve.registerStat();
        }
        AchievementPage.registerAchievementPage(new AchievementPage("MineChess", achieveList.toArray(new Achievement[achieveList.size()])));

    }

    public static void giveAchievement(EntityPlayer player, String id){
        player.triggerAchievement(getAchieveFromID(id));
    }

    public static Achievement getAchieveFromID(String id){
        for(Achievement achieve : achieveList) {
            if(achieve.statId.equals(id)) return achieve;
        }
        throw new IllegalArgumentException("[MineChess] No existing achievement id: " + id);
    }
}
