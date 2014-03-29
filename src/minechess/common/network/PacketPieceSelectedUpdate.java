package minechess.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

import minechess.client.MineChessDrawBlockHighlightHandler;
import minechess.common.ItemPieceMover;
import minechess.common.MineChess;
import net.minecraft.entity.player.EntityPlayer;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class PacketPieceSelectedUpdate extends AbstractPacket{

    private List<int[]> renderPositions;
    private int pieceEntityID, renderHeight;

    public PacketPieceSelectedUpdate(){}

    public PacketPieceSelectedUpdate(List<int[]> renderPositions, int pieceEntityID, int renderHeight){
        this.renderPositions = renderPositions;
        this.pieceEntityID = pieceEntityID;
        this.renderHeight = renderHeight;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
        buffer.writeByte(renderPositions.size());
        for(int[] position : renderPositions) {
            buffer.writeInt(position[0]);
            buffer.writeInt(position[1]);
        }
        buffer.writeInt(pieceEntityID);
        buffer.writeInt(renderHeight);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
        renderPositions = new ArrayList<int[]>();
        int listSize = buffer.readInt();
        for(int i = 0; i < listSize; i++) {
            int[] move = new int[2];
            move[0] = buffer.readInt();
            move[1] = buffer.readInt();
            renderPositions.add(move);
        }
        pieceEntityID = buffer.readInt();
        renderHeight = buffer.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player){
        MineChessDrawBlockHighlightHandler.pulse = 0;

        if(player.getCurrentEquippedItem().getItem() == MineChess.itemPieceMover) {
            ItemPieceMover.setRenderTiles(renderPositions, renderHeight, player.getCurrentEquippedItem());
            ItemPieceMover.setEntitySelected(pieceEntityID, player.getCurrentEquippedItem());
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player){}

}
