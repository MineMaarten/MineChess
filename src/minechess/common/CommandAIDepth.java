package minechess.common;

import minechess.common.ai.Chess;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class CommandAIDepth extends CommandBase{

    @Override
    public String getName(){
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
    public void execute(ICommandSender icommandsender, String[] astring) throws CommandException{
        if(astring.length > 0) {
            int newDepth = parseInt(astring[0], 1);
            Chess.maxDepthSetting = newDepth;
            MineChess.propertyAIDepth.set(newDepth);
            MineChess.config.save();
            notifyOperators(icommandsender, this, String.format("Set MineChess AI search depth to %s", newDepth), new Object[]{Integer.valueOf(newDepth)}); // LanguageRegistry.instance().getStringLocalization("message.command.setAIDepth")
        } else {
            throw new WrongUsageException(getCommandUsage(icommandsender), new Object[0]);
        }
    }
}
