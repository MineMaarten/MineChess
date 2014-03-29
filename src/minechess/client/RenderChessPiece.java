package minechess.client;

import minechess.common.EntityBaseChessPiece;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class RenderChessPiece extends RenderLiving{

    public RenderChessPiece(ModelBase par1ModelBase, float par2){
        super(par1ModelBase, par2);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity){
        return ((EntityBaseChessPiece)entity).getTexture();
    }
}
