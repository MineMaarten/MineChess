package chessMod.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.world.World;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class EntityBishop extends EntityBaseChessPiece{

    public EntityBishop(World par1World){
        super(par1World);
    }

    @Override
    public Entity getMob(){
        return new EntitySkeleton(worldObj);
    }

    @Override
    public List<int[]> getPossibleMoves(){
        List<int[]> moves = new ArrayList<int[]>();
        int x = targetX;
        int z = targetZ;
        while(x < 7 && z < 7) {
            x++;
            z++;
        }
        while(x >= 0 && z >= 0) {
            int[] move = new int[2];
            move[0] = x;
            move[1] = z;
            moves.add(move);
            x--;
            z--;
        }
        int X = targetX;
        int Z = targetZ;
        while(X < 7 && Z > 0) {
            X++;
            Z--;
        }
        while(X >= 0 && Z < 8) {
            int[] move = new int[2];
            move[0] = X;
            move[1] = Z;
            moves.add(move);
            X--;
            Z++;
        }

        return moves;
    }

}
