package minechess.common.network;

import io.netty.buffer.ByteBuf;
import minechess.common.EntityPawn;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class PacketPromotePawn extends AbstractPacket<PacketPromotePawn>{

    private int pawnEntityID;
    private String promotedPiece;

    public PacketPromotePawn(){}

    public PacketPromotePawn(EntityPawn promotedPawn, String promotedPiece){
        pawnEntityID = promotedPawn.getEntityId();
        this.promotedPiece = promotedPiece;
    }

    @Override
    public void toBytes(ByteBuf buffer){
        buffer.writeInt(pawnEntityID);
        ByteBufUtils.writeUTF8String(buffer, promotedPiece);
    }

    @Override
    public void fromBytes(ByteBuf buffer){
        pawnEntityID = buffer.readInt();
        promotedPiece = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    public void handleClientSide(PacketPromotePawn message, EntityPlayer player){}

    @Override
    public void handleServerSide(PacketPromotePawn message, EntityPlayer player){
        Entity entity = player.worldObj.getEntityByID(message.pawnEntityID);
        if(entity instanceof EntityPawn) {
            ((EntityPawn)entity).promote(player, message.promotedPiece);
        }
    }

}
