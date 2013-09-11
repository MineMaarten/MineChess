package chessMod.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import chessMod.common.EntityBaseChessPiece;
import chessMod.common.EntityRook;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class RenderRook extends RenderLiving{
    protected ModelRook model;

    public RenderRook(ModelBase par1ModelBase, float par2){
        super(par1ModelBase, par2);
        model = (ModelRook)mainModel;
    }

    public void renderRook(EntityRook entity, double par2, double par4, double par6, float par8, float par9){
        super.doRenderLiving(entity, par2, par4, par6, par8, par9);
    }

    @Override
    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9){
        renderRook((EntityRook)par1EntityLiving, par2, par4, par6, par8, par9);
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9){
        renderRook((EntityRook)par1Entity, par2, par4, par6, par8, par9);
    }

    @Override
    protected ResourceLocation func_110775_a(Entity entity){
        return ((EntityBaseChessPiece)entity).getTexture();
    }
}
