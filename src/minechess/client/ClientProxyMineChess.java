package minechess.client;

import minechess.common.CommonProxyMineChess;
import minechess.common.EntityBishop;
import minechess.common.EntityKing;
import minechess.common.EntityKnight;
import minechess.common.EntityPawn;
import minechess.common.EntityPickyXPOrb;
import minechess.common.EntityQueen;
import minechess.common.EntityRook;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
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
        RenderingRegistry.registerEntityRenderingHandler(EntityPickyXPOrb.class, new RenderXPOrb());
        MinecraftForge.EVENT_BUS.register(new MineChessDrawBlockHighlightHandler());

    }

    @Override
    public void registerHandlers(){
        MinecraftForge.EVENT_BUS.register(new SoundHandlerMineChess());
    }

    @Override
    public World getClientWorld(){
        return FMLClientHandler.instance().getClient().theWorld;
    }
}
