package minechess.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import minechess.client.GuiPawnPromotion;
import minechess.common.EntityPawn;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLCommonHandler;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class PacketOpenPromotionGUI extends AbstractPacket{
    private int pawnEntityID;

    public PacketOpenPromotionGUI(){}

    public PacketOpenPromotionGUI(EntityPawn pawn){
        pawnEntityID = pawn.getEntityId();
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
        buffer.writeInt(pawnEntityID);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
        pawnEntityID = buffer.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player){
        Entity entity = player.worldObj.getEntityByID(pawnEntityID);
        if(entity instanceof EntityPawn) {
            FMLCommonHandler.instance().showGuiScreen(new GuiPawnPromotion((EntityPawn)entity));
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player){}

}
