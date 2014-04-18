package minechess.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import minechess.common.ai.AIMain;
import minechess.common.ai.ChessPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class EntityKing extends EntityBaseChessPiece{
    private final List<EntityPlayer> playersInArea = new ArrayList<EntityPlayer>();
    public final List<ChessPosition> lastPositions = new ArrayList<ChessPosition>();
    private int listUpdateTicks = 0;
    private AIMain ai;
    public float lastPositionScore;

    public EntityKing(World par1World){
        super(par1World);
        listUpdateTicks = rand.nextInt(60);
    }

    @Override
    public Entity getMob(){
        EntityCreeper chargedCreeper = new EntityCreeper(worldObj);
        chargedCreeper.onStruckByLightning(null);
        return chargedCreeper;
    }

    @Override
    public List<int[]> getPossibleMoves(){
        List<int[]> moves = new ArrayList<int[]>();
        for(int i = targetX - 1; i <= targetX + 1; i++) {
            for(int j = targetZ - 1; j <= targetZ + 1; j++) {
                if(i >= 0 && i < 8 && j >= 0 && j < 8) {
                    int[] move = new int[2];
                    move[0] = i;
                    move[1] = j;

                    moves.add(move);
                }
            }
        }
        if(firstMove) {
            List<EntityBaseChessPiece> pieces = getChessPieces(true);
            for(int i = 0; i < 2; i++) {
                int[] castlingMove = new int[2];
                castlingMove[0] = targetX - 2 + i * 4;
                castlingMove[1] = targetZ;
                boolean unmovedRook = false;
                boolean piecesInBetween = false;
                for(int j = 0; j < pieces.size(); j++) {
                    if(pieces.get(j) instanceof EntityRook && pieces.get(j).firstMove && pieces.get(j).targetX == i * 7 && pieces.get(j).targetZ == targetZ) {// when a rook is on the right side and hasn't moved yet
                        unmovedRook = true;
                    }
                    if((pieces.get(j).targetX == i * 5 + 1 || pieces.get(j).targetX == i * 3 + 2) && pieces.get(j).targetZ == targetZ) {// when there is a piece (ally or not) next to the rook,
                        piecesInBetween = true; // set to true. More is not needed
                    }
                }
                if(unmovedRook && !piecesInBetween) moves.add(castlingMove); //The chess rules will be handled in the main (EntityBaseChessPiece) class.

            }
        }
        return moves;
    }

    @Override
    public void onEntityUpdate(){
        super.onEntityUpdate();
        if(!computerPiece || worldObj.isRemote) return;
        if(ai != null && ai.bQuit) ai = null;//kill not needed threads
        if(ticksExisted % 10 == 0 && ai == null && isBlack() == isBlackTurn && deathTimer < 0 && !isPieceCapturing()) {
            ai = new AIMain();//create an AI
            ai.aiCaller.go(this);//activate the AI
        }
        if(mateInTimes < 0) return;//only give info about the puzzle if this is a puzzle

        //--------Give information to nearby players about the objective of the current puzzle.----------
        listUpdateTicks++;
        if(listUpdateTicks < 60) return;
        listUpdateTicks = 0;
        for(int i = 0; i < playersInArea.size(); i++) {
            if(!getNotSoNearbyPlayers().contains(playersInArea.get(i))) playersInArea.remove(i); //Remove players in the list that aren't nearby anymore.
        }
        List<EntityPlayer> playersNearby = getNearbyPlayers();
        for(int i = 0; i < playersNearby.size(); i++) {
            EntityPlayer playerNearby = playersNearby.get(i);
            if(!playersInArea.contains(playerNearby)) {
                playersInArea.add(playerNearby);
                AchievementHandler.giveAchievement(playerNearby, "enterArena");
                MineChessUtils.sendUnlocalizedMessage(playerNearby, "message.broadcast.puzzleObjective" + (isBlack() ? "Black" : "White"), EnumChatFormatting.BLUE.toString(), mateInTimes + "");
            }
        }

    }

    public List<EntityPlayer> getNearbyPlayers(){
        AxisAlignedBB bbBox = AxisAlignedBB.getAABBPool().getAABB(xOffset - 1, (int)posY - 1, zOffset - 1, xOffset + 8, posY + 2, zOffset + 8);
        return worldObj.getEntitiesWithinAABB(EntityPlayer.class, bbBox);
    }

    public List<EntityPlayer> getNotSoNearbyPlayers(){
        AxisAlignedBB bbBox = AxisAlignedBB.getAABBPool().getAABB(xOffset - 3, (int)posY - 2, zOffset - 3, xOffset + 10, posY + 4, zOffset + 10);
        return worldObj.getEntitiesWithinAABB(EntityPlayer.class, bbBox);

    }

    @Override
    public void setDead(){
        if(solvedPuzzle && !worldObj.isRemote && computerPiece && mateInTimes >= 0) {
            int chestX = xOffset + targetX;
            int chestY = (int)Math.floor(posY);
            int chestZ = zOffset + targetZ;
            for(int i = 0; i < 40; i++)
                MineChessUtils.spawnParticle("explode", worldObj, chestX + 0.5D, chestY + 0.5D, chestZ + 0.5D, rand.nextDouble() / 5 - 0.1D, rand.nextDouble() / 5 - 0.1D, rand.nextDouble() / 5 - 0.1D);
            worldObj.setBlock(chestX, chestY, chestZ, Blocks.chest, 0, 3);
            TileEntityChest chest = (TileEntityChest)worldObj.getTileEntity(chestX, chestY, chestZ);
            WeightedRandomChestContent.generateChestContents(rand, ChestGenHooks.getItems(ChestGenHooks.MINESHAFT_CORRIDOR, rand), chest, ChestGenHooks.getCount(ChestGenHooks.MINESHAFT_CORRIDOR, rand));
            for(int i = 0; i < 50; i++) {
                int slot = rand.nextInt(chest.getSizeInventory());
                if(chest.getStackInSlot(slot) == null) {
                    chest.setInventorySlotContents(slot, new ItemStack(MineChess.itemPieceMover, 1, 4));
                    break;
                }
            }
        }
        super.setDead();
    }

    /**
     * Adds the current position, and returns true if the game is a draw (3x same position, last 50 positions no active move).
     * @param messagePlayer when true, nearby players will be informed about when they can ask for a draw.
     * @return
     */
    public boolean checkForDraw(boolean messagePlayer){
        //Check for active movement (movements that can't be reproduced like pawn movement). When found, clear the movement list.
        ChessPosition lastPos = null;
        if(lastPositions.size() > 1) {
            lastPos = lastPositions.get(lastPositions.size() - 1);
            if(lastPos.hasActiveDifference(lastPositions.get(lastPositions.size() - 2))) {
                lastPositions.clear();
                lastPositions.add(lastPos);
            }
        }

        if(lastPositions.size() >= 100) {
            sendChatToNearbyPlayers(null, "message.broadcast.inactiveDraw", EnumChatFormatting.GOLD.toString(), "" + lastPositions.size());
            return true;//50x no active move (a move being both the player and his opponent moved once).
        }

        if(lastPos != null) {
            //Check for 3x same position.
            int samePositionsFound = 0;
            for(ChessPosition testPos : lastPositions) {
                if(testPos.isSame(lastPos)) samePositionsFound++;
            }
            if(samePositionsFound >= 3) {
                sendChatToNearbyPlayers(null, "message.broadcast.samePositionThrice", EnumChatFormatting.GOLD.toString(), "" + samePositionsFound);
                return true;
            }
        }

        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag){
        super.writeEntityToNBT(tag);
        tag.setFloat("lastScore", lastPositionScore);

        NBTTagList tagList = new NBTTagList();
        for(int i = 0; i < lastPositions.size(); i++) {
            NBTTagCompound positionTag = new NBTTagCompound();
            positionTag.setByte("turn", (byte)i);
            lastPositions.get(i).writeToNBT(positionTag);
            tagList.appendTag(positionTag);
        }
        tag.setTag("lastPositions", tagList);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag){
        super.readEntityFromNBT(tag);
        lastPositionScore = tag.getFloat("lastScore");

        lastPositions.clear();
        NBTTagList tagList = tag.getTagList("lastPositions", 10);
        lastPositions.addAll(Arrays.asList(new ChessPosition[tagList.tagCount()])); //reserve a fixed space, so we can fill in this list in sequence.

        for(int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound positionTag = tagList.getCompoundTagAt(i);
            int turn = positionTag.getByte("turn");

            if(turn >= 0 && turn < lastPositions.size()) {
                ChessPosition position = new ChessPosition();
                position.readFromNBT(positionTag);
                lastPositions.set(turn, position);
            }
        }
    }
}
