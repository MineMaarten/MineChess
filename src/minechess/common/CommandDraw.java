package minechess.common;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class CommandDraw extends CommandBase{

    @Override
    public String getName(){
        return "draw";
    }

    @Override
    public int getRequiredPermissionLevel(){
        return -10; //every value below zero should do to make it useable for every player.
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender){
        return "/draw";
    }

    @Override
    public void execute(ICommandSender icommandsender, String[] astring) throws CommandException{
        BlockPos coords = icommandsender.getPosition();
        AxisAlignedBB aabb = new AxisAlignedBB(coords.getX() - 2, coords.getY() - 2, coords.getZ() - 2, coords.getX() + 2, coords.getY() + 2, coords.getZ());
        List<EntityBaseChessPiece> pieces = icommandsender.getEntityWorld().getEntitiesWithinAABB(EntityBaseChessPiece.class, aabb);
        if(pieces.size() > 0) {
            pieces = pieces.get(0).getChessPieces(true);
            for(EntityBaseChessPiece piece : pieces) {
                if(piece instanceof EntityKing && piece.isBlack()) {
                    EntityKing king = (EntityKing)piece;
                    if(king.checkForDraw(false)) {
                        king.setDeathTimer(true);
                        king.sendChatToNearbyPlayers(null, "message.broadcast.drawRequestAllowed", EnumChatFormatting.GOLD.toString(), icommandsender.getName());
                    } else {
                        king.sendChatToNearbyPlayers(null, "message.broadcast.drawRequestRejected", EnumChatFormatting.GOLD.toString(), icommandsender.getName());
                    }
                    return;
                }
            }
        } else {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(icommandsender.getName());
            if(player != null) {
                MineChessUtils.sendUnlocalizedMessage(player, "message.error.noChessboardsNearby", EnumChatFormatting.RED.toString());
            }
        }
    }
}
