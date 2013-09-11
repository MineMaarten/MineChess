package chessMod.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class ItemPieceMover extends Item{
    public EntityBaseChessPiece entitySelected; //TODO move selected entity to the NBT of the item.
    public List<int[]> renderPositions; // used in the blockhighlighthandler
    private final Icon[] texture;

    public ItemPieceMover(int par1){
        super(par1);
        renderPositions = new ArrayList<int[]>();
        texture = new Icon[5];
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List){
        for(int i = 0; i < 5; i++)
            par3List.add(new ItemStack(par1, 1, i));
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack){
        String name = super.getUnlocalizedName() + ".";
        switch(par1ItemStack.getItemDamage()){
            case 0:
                return name + "blackPieceMover";
            case 1:
                return name + "whitePieceMover";
            case 2:
                return name + "chessBoardGenerator";
            case 3:
                return name + "chessBoardColumn";
            default:// == 4
                return name + "chessAI";
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister){
        texture[0] = par1IconRegister.registerIcon("chessMod:blackMover");
        texture[1] = par1IconRegister.registerIcon("chessMod:whiteMover");
        texture[2] = par1IconRegister.registerIcon("chessMod:chessBoard");
        texture[3] = par1IconRegister.registerIcon("chessMod:chessBoardColumn");
        texture[4] = par1IconRegister.registerIcon("chessMod:AIInjector");
    }

    @Override
    public Icon getIconFromDamage(int par1){
        if(par1 < 5) return texture[par1];
        return texture[2];
    }

    @Override
    public boolean onItemUse(ItemStack iStack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10){
        if(iStack.getItemDamage() < 2) {
            if(entitySelected != null && !entitySelected.isDead && !world.isRemote) {
                try {
                    if(!entitySelected.getEnemyPiece().computerPiece) entitySelected.setLosingPlayer(player, entitySelected.isBlack());
                    entitySelected.tryToGoTo(x - entitySelected.xOffset, z - entitySelected.zOffset, player);
                } catch(Exception e) {

                }
                entitySelected = null;
                return true;
            } else {
                renderPositions.clear();
            }
        } else if(iStack.getItemDamage() == 2) {
            if(world.isRemote) return false;
            int orientation = ChessModUtils.determineOrientation(player);
            int startX = x;
            int startZ = z;
            switch(orientation){
                case 0:
                    startX -= 7;
                    break;
                case 1:
                    startX -= 7;
                    startZ -= 7;
                    break;
                case 2:
                    startZ -= 7;
            }
            AxisAlignedBB bbBox = AxisAlignedBB.getAABBPool().getAABB(startX - 1, y - 1, startZ - 1, startX + 9, y + 2, startZ + 9);
            List<EntityBaseChessPiece> pieces = world.getEntitiesWithinAABB(EntityBaseChessPiece.class, bbBox);
            if(pieces.size() > 0) {
                ChessModUtils.sendUnlocalizedMessage(player, "message.error.targetOccupied", EnumChatFormatting.DARK_RED.toString());
                return false;
            }

            generateChessEntities(world, startX, y, startZ, false);
            ChessModUtils.generateChessBoard(world, startX, y, startZ);
            iStack.stackSize--;
            return true;
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack iStack, EntityPlayer player, List list, boolean par4){
        if(iStack.getItemDamage() < 2) {
            if(entitySelected != null) {
                list.add(LocalizationHandler.getStringFromUnlocalizedParts(entitySelected.isBlack() ? "tooltip.selectedBlackPiece" : "tooltip.selectedWhitePiece", EnumChatFormatting.GRAY.toString(), "entity." + EntityList.getEntityString(entitySelected) + ".name", entitySelected.getColumnName(entitySelected.targetX) + (entitySelected.targetZ + 1)));
            } else {
                list.add(LanguageRegistry.instance().getStringLocalization("tooltip.noSelectedPiece"));
            }
        } else if(iStack.getItemDamage() == 4) {
            list.add(LanguageRegistry.instance().getStringLocalization("tooltip.aiInjector"));
        }
    }

    private void generateChessEntities(World world, int x, int y, int z, boolean computerNeeded){
        if(!world.isRemote) {
            for(int side = 0; side < 2; side++) {
                EntityKing king = new EntityKing(world);
                king.setPositionAndOffset(x + 0.5D, y + 1.0D, z + 0.5D);
                king.setTargetPosition(3, side * 7);
                king.setIsBlack(side == 1);
                king.computerPiece = computerNeeded;
                world.spawnEntityInWorld(king);

                EntityQueen queen = new EntityQueen(world);
                queen.setPositionAndOffset(x + 0.5D, y + 1.0D, z + 0.5D);
                queen.setTargetPosition(4, side * 7);
                queen.setIsBlack(side == 1);
                queen.computerPiece = computerNeeded;
                world.spawnEntityInWorld(queen);

                for(int i = 0; i < 2; i++) {
                    EntityRook rook = new EntityRook(world);
                    rook.setPositionAndOffset(x + 0.5D, y + 1.0D, z + 0.5D);
                    rook.setTargetPosition(i * 7, side * 7);
                    rook.setIsBlack(side == 1);
                    rook.computerPiece = computerNeeded;
                    world.spawnEntityInWorld(rook);
                }
                for(int i = 0; i < 2; i++) {
                    EntityKnight knight = new EntityKnight(world);
                    knight.setPositionAndOffset(x + 0.5D, y + 1.0D, z + 0.5D);
                    knight.targetX = 1 + i * 5;
                    knight.targetY = y + 1;
                    knight.targetZ = side * 7;
                    knight.setIsBlack(side == 1);
                    knight.computerPiece = computerNeeded;
                    world.spawnEntityInWorld(knight);
                }
                for(int i = 0; i < 2; i++) {
                    EntityBishop bishop = new EntityBishop(world);
                    bishop.setPositionAndOffset(x + 0.5D, y + 1.0D, z + 0.5D);
                    bishop.setTargetPosition(2 + i * 3, side * 7);
                    bishop.setIsBlack(side == 1);
                    bishop.computerPiece = computerNeeded;
                    world.spawnEntityInWorld(bishop);
                }
                for(int i = 0; i < 8; i++) {
                    EntityPawn pawn = new EntityPawn(world);
                    pawn.setPositionAndOffset(x + 0.5D, y + 1.0D, z + 0.5D);
                    pawn.setTargetPosition(i, 1 + side * 5);
                    pawn.setIsBlack(side == 1);
                    pawn.computerPiece = computerNeeded;
                    world.spawnEntityInWorld(pawn);
                }
            }
        }

    }
}
