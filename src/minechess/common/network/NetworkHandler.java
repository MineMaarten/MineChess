package minechess.common.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler{

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("MineChess");
    private static int discriminant;

    /*
     * The integer is the ID of the message, the Side is the side this message will be handled (received) on!
     */
    public static void init(){
        INSTANCE.registerMessage(PacketSpawnParticle.class, PacketSpawnParticle.class, discriminant++, Side.CLIENT);
        INSTANCE.registerMessage(PacketAddChatMessage.class, PacketAddChatMessage.class, discriminant++, Side.CLIENT);
        INSTANCE.registerMessage(PacketOpenPromotionGUI.class, PacketOpenPromotionGUI.class, discriminant++, Side.CLIENT);
        INSTANCE.registerMessage(PacketPieceSelectedUpdate.class, PacketPieceSelectedUpdate.class, discriminant++, Side.CLIENT);
        INSTANCE.registerMessage(PacketPromotePawn.class, PacketPromotePawn.class, discriminant++, Side.SERVER);
    }

    /* public static void INSTANCE.registerMessage(Class<? extends AbstractPacket<? extends IMessage>> clazz){
         INSTANCE.registerMessage(clazz, clazz, discriminant++, Side.SERVER, discriminant++, Side.SERVER);
     }*/

    public static void sendToAll(IMessage message){
        INSTANCE.sendToAll(message);
    }

    public static void sendTo(IMessage message, EntityPlayerMP player){
        INSTANCE.sendTo(message, player);
    }

    public static void sendToAllAround(LocationIntPacket message, World world, double distance){
        sendToAllAround(message, message.getTargetPoint(world, distance));
    }

    public static void sendToAllAround(LocationIntPacket message, World world){
        sendToAllAround(message, message.getTargetPoint(world));
    }

    public static void sendToAllAround(LocationDoublePacket message, World world){
        sendToAllAround(message, message.getTargetPoint(world));
    }

    public static void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point){
        INSTANCE.sendToAllAround(message, point);
    }

    public static void sendToDimension(IMessage message, int dimensionId){
        INSTANCE.sendToDimension(message, dimensionId);
    }

    public static void sendToServer(IMessage message){
        INSTANCE.sendToServer(message);
    }
}
