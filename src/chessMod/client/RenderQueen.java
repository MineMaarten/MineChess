package chessMod.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import chessMod.common.EntityBaseChessPiece;
import chessMod.common.EntityQueen;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class RenderQueen extends RenderLiving{
    protected ModelQueen model;

    public RenderQueen(ModelBase par1ModelBase, float par2){
        super(par1ModelBase, par2);
        model = (ModelQueen)mainModel;
    }

    public void renderQueen(EntityQueen entity, double par2, double par4, double par6, float par8, float par9){
        super.doRenderLiving(entity, par2, par4, par6, par8, par9);
    }

    @Override
    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9){
        renderQueen((EntityQueen)par1EntityLiving, par2, par4, par6, par8, par9);
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9){
        renderQueen((EntityQueen)par1Entity, par2, par4, par6, par8, par9);
    }

    @Override
    protected ResourceLocation func_110775_a(Entity entity){
        return ((EntityBaseChessPiece)entity).getTexture();
    }
}
