package minechess.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import minechess.client.LocalizationHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import cpw.mods.fml.common.network.ByteBufUtils;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class PacketAddChatMessage extends AbstractPacket{

    private String message;
    private String[] replacements;

    public PacketAddChatMessage(){}

    public PacketAddChatMessage(String message, String... replacements){
        this.message = message;
        this.replacements = replacements;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
        ByteBufUtils.writeUTF8String(buffer, message);
        if(replacements != null) {
            buffer.writeInt(replacements.length);
            for(String replacement : replacements) {
                ByteBufUtils.writeUTF8String(buffer, replacement);
            }
        } else {
            buffer.writeInt(0);
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
        message = ByteBufUtils.readUTF8String(buffer);
        replacements = new String[buffer.readInt()];
        for(int i = 0; i < replacements.length; i++) {
            replacements[i] = ByteBufUtils.readUTF8String(buffer);
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player){
        player.addChatComponentMessage(new ChatComponentTranslation(LocalizationHandler.getStringFromUnlocalizedParts(message, replacements), new Object[0]));
    }

    @Override
    public void handleServerSide(EntityPlayer player){}

}
