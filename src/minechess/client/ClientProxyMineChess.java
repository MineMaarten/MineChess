package minechess.client;

import minechess.common.CommonProxyMineChess;
import minechess.common.EntityBishop;
import minechess.common.EntityKing;
import minechess.common.EntityKnight;
import minechess.common.EntityPawn;
import minechess.common.EntityPickyXPOrb;
import minechess.common.EntityQueen;
import minechess.common.EntityRook;
import minechess.common.MineChess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * MineChess
 * 
 * @author MineMaarten www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class ClientProxyMineChess extends CommonProxyMineChess{

    @Override
    public void registerRenders(){
        super.registerRenders();
        RenderingRegistry.registerEntityRenderingHandler(EntityRook.class, new RenderChessPiece(new ModelRook(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(EntityKing.class, new RenderChessPiece(new ModelKing(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(EntityPawn.class, new RenderChessPiece(new ModelPawn(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(EntityKnight.class, new RenderChessPiece(new ModelKnight(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(EntityQueen.class, new RenderChessPiece(new ModelQueen(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(EntityBishop.class, new RenderChessPiece(new ModelBishop(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(EntityPickyXPOrb.class, new RenderXPOrb(Minecraft.getMinecraft().getRenderManager()));
        MinecraftForge.EVENT_BUS.register(new MineChessDrawBlockHighlightHandler());

        String[] names = new String[5];
        for(int i = 0; i < 5; i++) {
            String name = "chessmod:" + new ItemStack(MineChess.itemPieceMover, 1, i).getUnlocalizedName().substring(5);
            names[i] = name;
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MineChess.itemPieceMover, i, new ModelResourceLocation(name, "inventory"));
        }
        ModelBakery.addVariantName(MineChess.itemPieceMover, names);
    }

    @Override
    public void registerHandlers(){

    }

    @Override
    public World getClientWorld(){
        return FMLClientHandler.instance().getClient().theWorld;
    }
}
