package minechess.common;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.WorldServer;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class CommandKillPiece extends CommandBase{

    @Override
    public String getCommandName(){
        return "killpiece";
    }

    @Override
    public int getRequiredPermissionLevel(){
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender){
        return "/killpiece <x> <y> <z> <black/white>";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) throws CommandException{
        if(astring.length > 3 && (astring[3].equals("white") || astring[3].equals("black"))) {
            int x = parseInt(astring[0]);
            int y = parseInt(astring[1]);
            int z = parseInt(astring[2]);
            boolean isBlack = astring[3].equals("black");
            AxisAlignedBB bbBox = new AxisAlignedBB(x, y - 2, z, x + 1, y + 2, z + 1);
            for(WorldServer worldServer : MinecraftServer.getServer().worldServers) {
                List<EntityBaseChessPiece> pieces = worldServer.getEntitiesWithinAABB(EntityBaseChessPiece.class, bbBox);
                for(EntityBaseChessPiece piece : pieces) {
                    if(piece instanceof EntityKing) {
                        piece.setDeathTimer(piece.isBlack());
                    }
                    if(piece.isBlack() == isBlack) piece.kill();
                }
            }
            notifyOperators(icommandsender, this, "Killed any " + astring[3] + " pieces at " + x + ", " + y + ", " + z, new Object[]{0});
        } else {
            throw new WrongUsageException(getCommandUsage(icommandsender), new Object[0]);
        }
    }
}
