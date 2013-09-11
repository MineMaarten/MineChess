package chessMod.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import chessMod.client.ChessModDrawBlockHighlightHandler;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandlerChessMod implements IPacketHandler{

    private static final int GIVE_ACHIEVEMENT_ID = 1;
    private static final int PREVIEW_UPDATE_ID = 2;
    private static final int SEND_CHAT_ID = 3;
    private static final int PLAY_SOUND_ID = 4;
    private static final int SPAWN_PARTICLE_ID = 5;

    @Override
    public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player){
        ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
        World world = ChessMod.proxy.getClientWorld();
        EntityPlayer entityPlayer = (EntityPlayer)player;
        int packetID = dat.readInt();
        // System.out.println("Packet received, id = " + packetID);
        switch(packetID){
            case GIVE_ACHIEVEMENT_ID:
                /*
                 * int achievement = dat.readInt();
                 * System.out.println("achievement get: " + achievement);
                 * EntityPlayer entityPlayer = (EntityPlayer)player;
                 * if(achievement < 5){
                 * entityPlayer.addStat(minesweeperMod.achieveTilesCleared
                 * [achievement], 1); }else if(achievement < 9){
                 * entityPlayer.addStat
                 * (minesweeperMod.achieveDifficultyCleared[achievement - 5],
                 * 1); }
                 */
                break;
            case PREVIEW_UPDATE_ID:
                List<int[]> renderPositions = new ArrayList<int[]>();
                // entityPlayer.addChatMessage("Moves: ");
                ChessModDrawBlockHighlightHandler.renderHeight = dat.readInt();
                int listSize = dat.readInt();
                for(int i = 0; i < listSize; i++) {
                    int[] move = new int[2];
                    move[0] = dat.readInt();
                    move[1] = dat.readInt();
                    renderPositions.add(move);
                }
                ChessModDrawBlockHighlightHandler.pulse = 0;

                if(entityPlayer.getCurrentEquippedItem().getItem().itemID == ChessMod.itemPieceMover.itemID) {
                    ItemPieceMover pieceMover = (ItemPieceMover)entityPlayer.getCurrentEquippedItem().getItem();
                    pieceMover.renderPositions = renderPositions;
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

    public static Packet getPreviewUpdatePacket(List<int[]> renderPositions, int renderHeight){
        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeInt(PREVIEW_UPDATE_ID);
            dos.writeInt(renderHeight);
            dos.writeInt(renderPositions.size());

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

}
