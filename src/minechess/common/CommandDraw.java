package minechess.common;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class CommandDraw extends CommandBase{

    @Override
    public String getCommandName(){
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
    public void processCommand(ICommandSender icommandsender, String[] astring){
        ChunkCoordinates coords = icommandsender.getPlayerCoordinates();
        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(coords.posX - 2, coords.posY - 2, coords.posZ - 2, coords.posX + 2, coords.posY + 2, coords.posZ);
        List<EntityBaseChessPiece> pieces = icommandsender.getEntityWorld().getEntitiesWithinAABB(EntityBaseChessPiece.class, aabb);
        if(pieces.size() > 0) {
            pieces = pieces.get(0).getChessPieces(true);
            for(EntityBaseChessPiece piece : pieces) {
                if(piece instanceof EntityKing && piece.isBlack()) {
                    EntityKing king = (EntityKing)piece;
                    if(king.checkForDraw(false)) {
                        king.setDeathTimer(true);
                        king.sendChatToNearbyPlayers(null, "message.broadcast.drawRequestAllowed", EnumChatFormatting.GOLD.toString(), icommandsender.getCommandSenderName());
                    } else {
                        king.sendChatToNearbyPlayers(null, "message.broadcast.drawRequestRejected", EnumChatFormatting.GOLD.toString(), icommandsender.getCommandSenderName());
                    }
                    return;
                }
            }
        } else {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(icommandsender.getCommandSenderName());
            if(player != null) {
                MineChessUtils.sendUnlocalizedMessage(player, "message.error.noChessboardsNearby", EnumChatFormatting.RED.toString());
            }
        }
    }
}
