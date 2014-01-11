package minechess.client;

import java.util.HashMap;
import java.util.Map.Entry;

import minechess.common.EntityPawn;
import minechess.common.PacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiPawnPromotion extends GuiScreen{
    private final EntityPawn promotedPawn;
    private static final HashMap<Integer, String> promotedPieces = new HashMap<Integer, String>();

    static {
        promotedPieces.put(0, "MineChess.Bishop");
        promotedPieces.put(1, "MineChess.Knight");
        promotedPieces.put(2, "MineChess.Queen");
        promotedPieces.put(3, "MineChess.Rook");
    }

    public GuiPawnPromotion(EntityPawn promotedPawn){
        this.promotedPawn = promotedPawn;
    }

    @Override
    public void initGui(){
        int minX = width / 2 - 110;
        int minY = height / 2 - 50;
        for(Entry<Integer, String> entry : promotedPieces.entrySet())
            buttonList.add(new GuiButton(entry.getKey(), minX + 5, minY + 5 + 25 * entry.getKey(), LocalizationHandler.getStringLocalization("entity." + entry.getValue() + ".name")));
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks){
        drawDefaultBackground();
        super.drawScreen(x, y, partialTicks);
    }

    @Override
    public void actionPerformed(GuiButton button){
        EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
        if(player != null) {
            player.closeScreen();
        }
        PacketDispatcher.sendPacketToServer(PacketHandler.promotePawn(promotedPawn, promotedPieces.get(button.id)));
    }
}
