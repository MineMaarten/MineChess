package minechess.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class PacketSpawnParticle extends LocationDoublePacket{

    private double dx, dy, dz;
    private String particleName;

    public PacketSpawnParticle(){}

    public PacketSpawnParticle(String particleName, double x, double y, double z, double dx, double dy, double dz){
        super(x, y, z);
        this.particleName = particleName;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
        super.encodeInto(ctx, buffer);
        ByteBufUtils.writeUTF8String(buffer, particleName);
        buffer.writeDouble(dx);
        buffer.writeDouble(dy);
        buffer.writeDouble(dz);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
        super.decodeInto(ctx, buffer);
        particleName = ByteBufUtils.readUTF8String(buffer);
        dx = buffer.readDouble();
        dy = buffer.readDouble();
        dz = buffer.readDouble();
    }

    @Override
    public void handleClientSide(EntityPlayer player){
        player.worldObj.spawnParticle(particleName, x, y, z, dx, dy, dz);
    }

    @Override
    public void handleServerSide(EntityPlayer player){}

}
