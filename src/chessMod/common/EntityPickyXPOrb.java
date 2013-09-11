package chessMod.common;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class EntityPickyXPOrb extends EntityXPOrb{

    private EntityPlayer lostPlayer;

    public EntityPickyXPOrb(World par1World){
        super(par1World);
    }

    public EntityPickyXPOrb(World par1World, double par2, double par4, double par6, int par8, EntityPlayer playerLost){
        super(par1World, par2, par4, par6, par8);
        lostPlayer = playerLost;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate(){
        super.onUpdate();

        double d0 = 8.0D;
        if(lostPlayer != null) {
            double d1 = (lostPlayer.posX - posX) / d0;
            double d2 = (lostPlayer.posY + lostPlayer.getEyeHeight() - posY) / d0;
            double d3 = (lostPlayer.posZ - posZ) / d0;
            double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            double d5 = 1.0D - d4;

            if(d5 > 0.0D) {
                d5 *= d5;
                motionX -= d1 / d4 * d5 * 0.8D; // repelling strength is twice as strong as the attracting strength.
                motionY -= d2 / d4 * d5 * 0.8D;
                motionZ -= d3 / d4 * d5 * 0.8D;
            }
        }
    }

}
