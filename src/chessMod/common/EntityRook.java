package chessMod.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.world.World;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class EntityRook extends EntityBaseChessPiece{

    public EntityRook(World par1World){
        super(par1World);
        setSize(0.4F, 1.125F);
    }

    @Override
    public Entity getMob(){
        return new EntityCaveSpider(worldObj);
    }

    @Override
    public List<int[]> getPossibleMoves(){
        List<int[]> moves = new ArrayList<int[]>();
        for(int i = 0; i < 8; i++) {
            int[] move1 = new int[2];
            move1[0] = i;
            move1[1] = targetZ;
            int[] move2 = new int[2];
            move2[0] = targetX;
            move2[1] = i;
            moves.add(move1);
            moves.add(move2);
        }
        return moves;
    }

}
