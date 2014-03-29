package minechess.common;

import java.util.List;
import java.util.Random;

import minechess.common.network.PacketAddChatMessage;
import minechess.common.network.PacketPlaySound;
import minechess.common.network.PacketSpawnParticle;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class MineChessUtils{

    // private Potion[] badPotions = {Potion.blindness, Potion.confusion,
    // Potion.digSlowdown, Potion.hunger, Potion.moveSlowdown, Potion.poison,
    // Potion.weakness};

    public static int determineOrientation(EntityPlayer par4EntityPlayer){
        return MathHelper.floor_double(par4EntityPlayer.rotationYaw * 4.0F / 360.0F) & 3;
    }

    public static void generateChessBoard(World world, int x, int y, int z){
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                boolean even = (i + j) % 2 == 0;
                world.setBlock(i + x, y, j + z, Blocks.wool, even ? 0 : 15, 3);
            }
        }
    }

    public static void spawnParticle(String particleName, World world, double x, double y, double z, double velX, double velY, double velZ){
        MineChess.packetPipeline.sendToAllAround(new PacketSpawnParticle(particleName, x, y, z, velX, velY, velZ), world);
    }

    public static void onPuzzleFail(World world, EntityPlayer player, EntityBaseChessPiece piece, int x, int y, int z, Random rand){
        int randEffect = 0;
        do {
            randEffect = rand.nextInt(4);
        } while(randEffect == 3 && player == null);//Only allow the potion effect punishment when we have a player to apply the effects to.
        switch(randEffect){
            case 0:
                // generate fire underneath the chessboard, under each of the 64
                // tiles.
                for(int i = 0; i < 8; i++) {
                    for(int j = 0; j < 8; j++) {
                        world.setBlock(x + i, y - 2, z + j, Blocks.fire, 1, 3);
                    }
                }
                if(player != null) AchievementHandler.giveAchievement(player, "puzzleFailFire");
                return;
            case 1:
                // play a creepy sound. A maximum of 5 creepers spawn around the
                // chessboard.
                MineChess.packetPipeline.sendToAllAround(new PacketPlaySound("ambient.cave.cave", x, y, z, 1.0F, 1.0F, true), world);
                int entityCount = 0;
                boolean firstCreeper = true;
                for(int i = 0; i < 50; i++) {
                    int randX = x + rand.nextInt(20) - 12;
                    if(randX >= x - 2 && randX <= x + 9) randX += 12;
                    int randZ = z + rand.nextInt(20) - 10;
                    if(randZ >= z - 2 && randZ <= z + 9) randZ += 12;
                    int randY = y + rand.nextInt(6) - 3;
                    if(world.isAirBlock(randX, randY, randZ) && world.isAirBlock(randX, randY + 1, randZ)) {
                        EntityCreeper creeper = new EntityCreeper(world);

                        creeper.setPosition(randX + 0.5D, randY, randZ + 0.5D);
                        creeper.setTarget(player); // make the creeper already
                                                   // knowing where the player
                                                   // is.
                        if(firstCreeper) creeper.onStruckByLightning(null);
                        firstCreeper = false;
                        world.spawnEntityInWorld(creeper);
                        entityCount++;
                        if(entityCount >= 5) return;
                    }
                }
                if(player != null) AchievementHandler.giveAchievement(player, "puzzleFailCreepy");
                return;
            case 2:
                // Turn every dying enemy chesspiece into mobs.
                List<EntityBaseChessPiece> pieces = piece.getChessPieces(false);
                for(EntityBaseChessPiece chessPiece : pieces) {
                    if(chessPiece.isBlack() != piece.isBlack()) chessPiece.turnToMobOnDeath = true;
                }
                if(player != null) AchievementHandler.giveAchievement(player, "puzzleFailTransform");
                return;
            case 3:
                // give the player random potion effects.
                int potionAmount = 2 + rand.nextInt(3); // 2-4 potion effects
                for(int i = 0; i < potionAmount; i++) {
                    Potion randomPotion;
                    do {
                        randomPotion = Potion.potionTypes[rand.nextInt(20) + 1];
                    } while(!isPotionBadEffect(randomPotion.id) || randomPotion == Potion.harm);
                    player.addPotionEffect(new PotionEffect(randomPotion.id, 200 + rand.nextInt(400))); //make the potion effect last for 10-30 secs.
                }
                if(player != null) AchievementHandler.giveAchievement(player, "puzzleFailPotion");
                return;
        }

    }

    public static boolean isPotionBadEffect(int potionID){
        switch(potionID){
            case 2:
            case 4:
            case 9:
            case 15:
            case 17:
            case 18:
            case 19:
            case 20:
                return true;
        }
        return false;
    }

    public static void sendUnlocalizedMessage(EntityPlayer player, String message){
        sendUnlocalizedMessage(player, message, EnumChatFormatting.WHITE.toString());
    }

    public static void sendUnlocalizedMessage(EntityPlayer player, String message, String... replacements){
        MineChess.packetPipeline.sendTo(new PacketAddChatMessage(message, replacements), (EntityPlayerMP)player);
    }

}
