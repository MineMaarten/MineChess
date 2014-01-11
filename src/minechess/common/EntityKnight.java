package minechess.common;

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

public class EntityKnight extends EntityBaseChessPiece{

    public EntityKnight(World par1World){
        super(par1World);
        setSize(0.6F, 1.56F);
    }

    @Override
    public Entity getMob(){
        return new EntitySkeleton(worldObj);
    }

    @Override
    public List<int[]> getPossibleMoves(){
        List<int[]> moves = new ArrayList<int[]>();
        for(int i = 0; i < 8; i++) {
            int[][] move = new int[8][2];
            switch(i){// go through each of the 8 possibilities to jump
                case 0:
                    move[i][0] = targetX + 2;
                    move[i][1] = targetZ + 1;
                    break;
                case 1:
                    move[i][0] = targetX + 2;
                    move[i][1] = targetZ - 1;
                    break;
                case 2:
                    move[i][0] = targetX - 2;
                    move[i][1] = targetZ + 1;
                    break;
                case 3:
                    move[i][0] = targetX - 2;
                    move[i][1] = targetZ - 1;
                    break;
                case 4:
                    move[i][0] = targetX + 1;
                    move[i][1] = targetZ + 2;
                    break;
                case 5:
                    move[i][0] = targetX - 1;
                    move[i][1] = targetZ + 2;
                    break;
                case 6:
                    move[i][0] = targetX + 1;
                    move[i][1] = targetZ - 2;
                    break;
                case 7:
                    move[i][0] = targetX - 1;
                    move[i][1] = targetZ - 2;
                    break;
            }
            if(move[i][0] < 8 && move[i][0] >= 0 && move[i][1] < 8 && move[i][1] >= 0) moves.add(move[i]);
        }
        return moves;
    }
}
