package chessMod.client;

import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import chessMod.common.CommonProxyChessMod;
import chessMod.common.EntityBishop;
import chessMod.common.EntityKing;
import chessMod.common.EntityKnight;
import chessMod.common.EntityPawn;
import chessMod.common.EntityPickyXPOrb;
import chessMod.common.EntityQueen;
import chessMod.common.EntityRook;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class ClientProxyChessMod extends CommonProxyChessMod{

    @Override
    public void registerRenders(){
        super.registerRenders();
        RenderingRegistry.registerEntityRenderingHandler(EntityRook.class, new RenderRook(new ModelRook(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(EntityKing.class, new RenderKing(new ModelKing(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(EntityPawn.class, new RenderPawn(new ModelPawn(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(EntityKnight.class, new RenderKnight(new ModelKnight(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(EntityQueen.class, new RenderQueen(new ModelQueen(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(EntityBishop.class, new RenderBishop(new ModelBishop(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(EntityPickyXPOrb.class, new RenderXPOrb());
        MinecraftForge.EVENT_BUS.register(new ChessModDrawBlockHighlightHandler());

    }

    @Override
    public void registerHandlers(){
        MinecraftForge.EVENT_BUS.register(new SoundHandlerChessMod());
    }

    @Override
    public World getClientWorld(){
        return FMLClientHandler.instance().getClient().theWorld;
    }
}
