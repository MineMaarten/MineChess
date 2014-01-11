package minechess.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import minechess.client.GuiPawnPromotion;
import minechess.client.LocalizationHandler;
import minechess.client.MineChessDrawBlockHighlightHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler{

    private static final int GIVE_ACHIEVEMENT_ID = 1;
    private static final int SELECTED_PIECE_UPDATE_ID = 2;
    private static final int SEND_CHAT_ID = 3;
    private static final int PLAY_SOUND_ID = 4;
    private static final int SPAWN_PARTICLE_ID = 5;
    private static final int PROMOTE_PAWN_ID = 6;
    private static final int OPEN_PROMOTION_GUI = 7;

    @Override
    public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player){
        ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
        EntityPlayer entityPlayer = (EntityPlayer)player;
        World world = entityPlayer.worldObj;
        int packetID = dat.readInt();
        // System.out.println("Packet received, id = " + packetID);
        switch(packetID){
            case GIVE_ACHIEVEMENT_ID:
                int achieve = dat.readInt();
                entityPlayer.addStat(AchievementHandler.getAchieveFromID(achieve), 1);
                break;
            case SELECTED_PIECE_UPDATE_ID:
                List<int[]> renderPositions = new ArrayList<int[]>();
                // entityPlayer.addChatMessage("Moves: ");
                int renderHeight = dat.readInt();
                int listSize = dat.readInt();
                int entityID = dat.readInt();
                for(int i = 0; i < listSize; i++) {
                    int[] move = new int[2];
                    move[0] = dat.readInt();
                    move[1] = dat.readInt();
                    renderPositions.add(move);
                }
                MineChessDrawBlockHighlightHandler.pulse = 0;

                if(entityPlayer.getCurrentEquippedItem().getItem().itemID == MineChess.itemPieceMover.itemID) {
                    ItemPieceMover.setRenderTiles(renderPositions, renderHeight, entityPlayer.getCurrentEquippedItem());
                    ItemPieceMover.setEntitySelected(entityID, entityPlayer.getCurrentEquippedItem());
                }
                break;
            case SEND_CHAT_ID:
                String chatMessage = dat.readUTF();
                int replacementCount = dat.readByte();
                String[] replacements = new String[replacementCount];
                for(int i = 0; i < replacementCount; i++) {
                    replacements[i] = dat.readUTF();
                }
                entityPlayer.addChatMessage(LocalizationHandler.getStringFromUnlocalizedParts(chatMessage, replacements));
                break;
            case PLAY_SOUND_ID:
                world.playSound(dat.readDouble(), dat.readDouble(), dat.readDouble(), dat.readUTF(), dat.readFloat(), dat.readFloat(), dat.readBoolean());
                break;
            case SPAWN_PARTICLE_ID:
                world.spawnParticle(dat.readUTF(), dat.readDouble(), dat.readDouble(), dat.readDouble(), dat.readDouble(), dat.readDouble(), dat.readDouble());
                break;
            case PROMOTE_PAWN_ID:
                Entity entity = world.getEntityByID(dat.readInt());
                if(entity instanceof EntityPawn) {
                    ((EntityPawn)entity).promote((EntityPlayer)player, dat.readUTF());
                }
                break;
            case OPEN_PROMOTION_GUI:
                Entity entity2 = world.getEntityByID(dat.readInt());
                if(entity2 instanceof EntityPawn) {
                    FMLCommonHandler.instance().showGuiScreen(new GuiPawnPromotion((EntityPawn)entity2));
                }
                break;
        }
    }

    public static Packet getAchievementPacket(int achievement){
        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeInt(GIVE_ACHIEVEMENT_ID);
            dos.writeInt(achievement);
        } catch(IOException e) {

        }
        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "chessMod";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet getPieceSelectedUpdatePacket(List<int[]> renderPositions, int renderHeight, int entityID){
        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeInt(SELECTED_PIECE_UPDATE_ID);
            dos.writeInt(renderHeight);
            dos.writeInt(renderPositions.size());
            dos.writeInt(entityID);

            for(int i = 0; i < renderPositions.size(); i++) {
                dos.writeInt(renderPositions.get(i)[0]);
                dos.writeInt(renderPositions.get(i)[1]);
            }
        } catch(IOException e) {

        }
        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "chessMod";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet getChatMessagePacket(String chatMessage){
        return getChatMessagePacket(chatMessage, new String[0]);
    }

    public static Packet getChatMessagePacket(String chatMessage, String[] replacements){
        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeInt(SEND_CHAT_ID);
            dos.writeUTF(chatMessage);
            dos.writeByte((byte)replacements.length);
            for(String replacement : replacements) {
                dos.writeUTF(replacement);
            }
        } catch(IOException e) {

        }
        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "chessMod";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }

    public static Packet spawnParticle(String particleName, double spawnX, double spawnY, double spawnZ, double spawnMotX, double spawnMotY, double spawnMotZ){
        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeInt(SPAWN_PARTICLE_ID);
            dos.writeUTF(particleName);
            dos.writeDouble(spawnX);
            dos.writeDouble(spawnY);
            dos.writeDouble(spawnZ);
            dos.writeDouble(spawnMotX);
            dos.writeDouble(spawnMotY);
            dos.writeDouble(spawnMotZ);
        } catch(IOException e) {

        }
        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "chessMod";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;

        return pkt;
    }

    public static Packet playSound(double x, double y, double z, String soundName, float volume, float pitch, boolean bool){
        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeInt(PLAY_SOUND_ID);
            dos.writeDouble(x);
            dos.writeDouble(y);
            dos.writeDouble(z);
            dos.writeUTF(soundName);

            dos.writeFloat(volume);
            dos.writeFloat(pitch);
            dos.writeBoolean(bool);
        } catch(IOException e) {

        }
        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "chessMod";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;

        return pkt;
    }

    public static Packet promotePawn(EntityPawn pawn, String promotedPiece){
        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeInt(PROMOTE_PAWN_ID);
            dos.writeInt(pawn.entityId);
            dos.writeUTF(promotedPiece);
        } catch(IOException e) {}
        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "chessMod";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;

        return pkt;
    }

    public static Packet openPromotionGUI(EntityPawn pawn){
        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeInt(OPEN_PROMOTION_GUI);
            dos.writeInt(pawn.entityId);
        } catch(IOException e) {}
        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "chessMod";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;

        return pkt;
    }

}
