package chessMod.common;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import chessMod.common.ai.Chess;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class CommandAIDepth extends CommandBase{

    @Override
    public String getCommandName(){
        return "aidepth";
    }

    @Override
    public int getRequiredPermissionLevel(){
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender){
        return "/aidepth <" + "depth" + ">";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring){
        if(astring.length > 0) {
            int newDepth = parseIntWithMin(icommandsender, astring[0], 1);
            Chess.maxDepthSetting = newDepth;
            ChessMod.propertyAIDepth.set(newDepth);
            ChessMod.config.save();
            notifyAdmins(icommandsender, String.format("Set MineChess AI search depth to %s", newDepth), new Object[]{Integer.valueOf(newDepth)}); // LanguageRegistry.instance().getStringLocalization("message.command.setAIDepth")
        } else {
            throw new WrongUsageException(getCommandUsage(icommandsender), new Object[0]);
        }
    }
}
