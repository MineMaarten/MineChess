package chessMod.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import chessMod.common.ai.AIMain;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class EntityKing extends EntityBaseChessPiece{
    private final List<EntityPlayer> playersInArea;
    private int listUpdateTicks = 0;
    private AIMain ai;
    public float lastPositionScore;

    public EntityKing(World par1World){
        super(par1World);
        listUpdateTicks = rand.nextInt(60);
        playersInArea = new ArrayList<EntityPlayer>();
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
                ChessModUtils.sendUnlocalizedMessage(playerNearby, "message.broadcast.puzzleObjective" + (isBlack() ? "Black" : "White"), EnumChatFormatting.BLUE.toString(), mateInTimes + "");
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
                ChessModUtils.spawnParticle("explode", chestX + 0.5D, chestY + 0.5D, chestZ + 0.5D, rand.nextDouble() / 5 - 0.1D, rand.nextDouble() / 5 - 0.1D, rand.nextDouble() / 5 - 0.1D);
            worldObj.setBlock(chestX, chestY, chestZ, Block.chest.blockID, 0, 3);
            TileEntityChest chest = (TileEntityChest)worldObj.getBlockTileEntity(chestX, chestY, chestZ);
            WeightedRandomChestContent.generateChestContents(rand, ChestGenHooks.getItems(ChestGenHooks.MINESHAFT_CORRIDOR, rand), chest, ChestGenHooks.getCount(ChestGenHooks.MINESHAFT_CORRIDOR, rand));
            for(int i = 0; i < 50; i++) {
                int slot = rand.nextInt(chest.getSizeInventory());
                if(chest.getStackInSlot(slot) == null) {
                    chest.setInventorySlotContents(slot, new ItemStack(ChessMod.itemPieceMover, 1, 4));
                    break;
                }
            }
        }
        super.setDead();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag){
        super.writeEntityToNBT(tag);
        tag.setFloat("lastScore", lastPositionScore);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag){
        super.readEntityFromNBT(tag);
        lastPositionScore = tag.getFloat("lastScore");
    }
}
