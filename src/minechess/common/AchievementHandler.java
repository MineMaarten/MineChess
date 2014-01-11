package minechess.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class AchievementHandler{
    public static final List<Achievement> achieveList = new ArrayList<Achievement>();
    private static final int ID_OFFSET = 1345;
    public static final int MOVE_PIECE_ID = ID_OFFSET + 0;
    public static final int CASTLING_ID = ID_OFFSET + 1;
    public static final int ENTER_ARENA_ID = ID_OFFSET + 2;
    public static final int PUZZLE_FAIL_CREEPER = ID_OFFSET + 3;
    public static final int PUZZLE_FAIL_POTION = ID_OFFSET + 4;
    public static final int PUZZLE_FAIL_TRANSFORM = ID_OFFSET + 5;
    public static final int PUZZLE_FAIL_FIRE = ID_OFFSET + 6;
    public static final int EN_PASSANT_ID = ID_OFFSET + 7;
    public static final int PUZZLE_WIN_ID = ID_OFFSET + 8;
    public static final int STALEMATE_ID = ID_OFFSET + 9;
    public static final int CHECKMATE_ID = ID_OFFSET + 10;
    public static final int CHECK_ID = ID_OFFSET + 11;
    public static final int LOSE_ID = ID_OFFSET + 12;

    public static void init(){
        achieveList.add(new Achievement(MOVE_PIECE_ID, "movePiece", 1, 3, new ItemStack(MineChess.itemPieceMover, 1, 0), null).setIndependent());
        achieveList.add(new Achievement(CASTLING_ID, "castling", 1, -1, new ItemStack(MineChess.itemPieceMover, 1, 0), getAchieveFromID(MOVE_PIECE_ID)).setIndependent().setSpecial());
        achieveList.add(new Achievement(ENTER_ARENA_ID, "enterArena", 3, 5, new ItemStack(MineChess.itemPieceMover, 0, 4), null).setIndependent());
        achieveList.add(new Achievement(PUZZLE_FAIL_CREEPER, "puzzleFailCreepy", 5, 2, new ItemStack(Block.skull), getAchieveFromID(ENTER_ARENA_ID)).setIndependent());
        achieveList.add(new Achievement(PUZZLE_FAIL_POTION, "puzzleFailPotion", 5, 4, new ItemStack(Item.potion), getAchieveFromID(ENTER_ARENA_ID)).setIndependent());
        achieveList.add(new Achievement(PUZZLE_FAIL_TRANSFORM, "puzzleFailTransform", 5, 6, new ItemStack(Item.egg), getAchieveFromID(ENTER_ARENA_ID)).setIndependent());
        achieveList.add(new Achievement(PUZZLE_FAIL_FIRE, "puzzleFailFire", 5, 8, new ItemStack(Block.fire), getAchieveFromID(ENTER_ARENA_ID)).setIndependent());
        achieveList.add(new Achievement(EN_PASSANT_ID, "enPassant", 1, 7, new ItemStack(MineChess.itemPieceMover, 1, 0), getAchieveFromID(MOVE_PIECE_ID)).setIndependent().setSpecial());
        achieveList.add(new Achievement(PUZZLE_WIN_ID, "puzzleWin", 3, 0, new ItemStack(Block.chest), getAchieveFromID(ENTER_ARENA_ID)).setIndependent());
        achieveList.add(new Achievement(STALEMATE_ID, "stalemate", -1, 1, new ItemStack(MineChess.itemPieceMover, 1, 0), getAchieveFromID(MOVE_PIECE_ID)).setIndependent().setSpecial());
        achieveList.add(new Achievement(CHECKMATE_ID, "checkmate", -1, 3, new ItemStack(MineChess.itemPieceMover, 1, 0), getAchieveFromID(MOVE_PIECE_ID)).setIndependent());
        achieveList.add(new Achievement(CHECK_ID, "check", -2, 4, new ItemStack(MineChess.itemPieceMover, 1, 0), getAchieveFromID(MOVE_PIECE_ID)).setIndependent());
        achieveList.add(new Achievement(LOSE_ID, "lose", -1, 5, new ItemStack(MineChess.itemPieceMover, 1, 0), getAchieveFromID(MOVE_PIECE_ID)).setIndependent());

        AchievementPage.registerAchievementPage(new AchievementPage("MineChess", achieveList.toArray(new Achievement[achieveList.size()])));

    }

    public static void giveAchievement(EntityPlayer player, int achieve){
        if(player.worldObj.isRemote) {
            player.addStat(getAchieveFromID(achieve), 1);
        } else {
            PacketDispatcher.sendPacketToPlayer(PacketHandler.getAchievementPacket(achieve), (Player)player);
        }
    }

    public static Achievement getAchieveFromID(int id){
        for(Achievement achieve : achieveList) {
            if(achieve.statId == id + 5242880) return achieve;
        }
        return null;
    }

}
