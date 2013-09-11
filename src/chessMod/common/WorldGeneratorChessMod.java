package chessMod.common;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGeneratorChessMod implements IWorldGenerator{

    private final String[][][] boards = {{{"bR", "  ", "  ", "  ", "  ", "bR", "  ", "  "}, {"bP", "  ", "  ", "  ", "  ", "  ", "bP", "bK"}, {"  ", "bP", "  ", "wR", "  ", "  ", "  ", "  "}, {"  ", "  ", "bN", "  ", "  ", "bP", "  ", "  "}, {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "}, {"wP", "wB", "  ", "  ", "wR", "  ", "  ", "  "}, {"  ", "wP", "wP", "  ", "  ", "wP", "wP", "wP"}, {"  ", "  ", "  ", "  ", "  ", "  ", "wK", "  "}}, {{"bR", "  ", "bB", "  ", "bK", "bB", "  ", "bR"}, {"bP", "bP", "bQ", "bP", "  ", "bP", "bP", "bP"}, {"  ", "  ", "  ", "  ", "bP", "  ", "  ", "  "}, {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "}, {"  ", "  ", "wB", "bN", "wP", "  ", "bN", "  "}, {"  ", "  ", "wN", "  ", "  ", "wN", "  ", "wP"}, {"wP", "wP", "  ", "  ", "  ", "wP", "wP", "  "}, {"wR", "  ", "wB", "wQ", "  ", "wR", "wK", "  "}}};

    private final int[] mateInTimes = new int[]{1, 2};
    private final boolean[] isComputerBlack = {true, false};

    /*
     * private final String[][] board1 = { {"  ", "  ", "  ", "  ", "  ", "  ",
     * "  ", "  "}, {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "}, {"  ",
     * "  ", "  ", "  ", "  ", "  ", "  ", "  "}, {"  ", "  ", "  ", "  ", "  ",
     * "  ", "  ", "  "}, {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
     * {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "}, {"  ", "  ", "  ",
     * "  ", "  ", "  ", "  ", "  "}, {"  ", "  ", "  ", "  ", "  ", "  ", "  ",
     * "  "}};
     */

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider){
        if(ChessMod.DEBUG || !(chunkGenerator instanceof ChunkProviderFlat)) { //don't generate on flatworlds (unless testing)
            switch(world.provider.dimensionId){
                case 0:
                    generateSurface(world, random, chunkX * 16, chunkZ * 16);
                    break;
                case -1:
                    generateNether(world, random, chunkX * 16, chunkZ * 16);
                    break;
                case 1:
                    generateEnd(world, random, chunkX * 16, chunkZ * 16);
            }
        }
    }

    public void generateSurface(World world, Random rand, int chunkX, int chunkZ){

        if(ChessMod.configWorldgenPuzzleUnderground != 0 && rand.nextInt(ChessMod.configWorldgenPuzzleUnderground) == 0) {
            for(int i = 0; i < 10; i++) { // 10 generation attempts.
                int baseX = chunkX + rand.nextInt(8);
                int baseY = rand.nextInt(30) + 20;
                int baseZ = chunkZ + rand.nextInt(8);

                if(collidingWithCave(world, baseX, baseY, baseZ)) {
                    if(ChessMod.DEBUG) System.out.println("[MineChess] Generating puzzle at " + baseX + ", " + baseY + ", " + baseZ);
                    generateChessPuzzle(world, rand, baseX, baseY, baseZ);
                    break;
                }
            }
        }
    }

    public void generateNether(World world, Random rand, int chunkX, int chunkZ){

    }

    public void generateEnd(World world, Random rand, int chunkX, int chunkZ){

    }

    private void generateChessPuzzle(World world, Random rand, int baseX, int baseY, int baseZ){
        fillWithMetadataBlocks(world, baseX - 1, baseY + 1, baseZ - 1, baseX + 8, baseY + 4, baseZ + 8, 0, 0);
        fillWithMossyStone(world, baseX - 1, baseY, baseZ - 1, baseX + 8, baseY, baseZ + 8, rand);// floor, which mostly will be overwritten by the chessboard
        fillWithMossyStone(world, baseX - 2, baseY, baseZ - 2, baseX + 9, baseY + 4, baseZ - 2, rand);// -Z wall
        fillWithMossyStone(world, baseX - 2, baseY, baseZ + 9, baseX + 9, baseY + 4, baseZ + 9, rand);// +Z wall
        fillWithMossyStone(world, baseX - 2, baseY, baseZ - 2, baseX - 2, baseY + 4, baseZ + 9, rand);// -X wall
        fillWithMossyStone(world, baseX + 9, baseY, baseZ - 2, baseX + 9, baseY + 4, baseZ + 9, rand);// +X wall
        fillWithMossyStone(world, baseX - 2, baseY + 5, baseZ - 2, baseX + 9, baseY + 5, baseZ + 9, rand);// roof
        world.setBlock(baseX + 2, baseY + 5, baseZ + 2, Block.redstoneLampActive.blockID, 0, 2);
        world.setBlock(baseX + 5, baseY + 5, baseZ + 2, Block.redstoneLampActive.blockID, 0, 2);
        world.setBlock(baseX + 5, baseY + 5, baseZ + 5, Block.redstoneLampActive.blockID, 0, 2);
        world.setBlock(baseX + 2, baseY + 5, baseZ + 5, Block.redstoneLampActive.blockID, 0, 2);
        world.setBlock(baseX + 2, baseY + 6, baseZ + 2, Block.lever.blockID, 13, 2);
        world.setBlock(baseX + 5, baseY + 6, baseZ + 2, Block.lever.blockID, 13, 2);
        world.setBlock(baseX + 5, baseY + 6, baseZ + 5, Block.lever.blockID, 13, 2);
        world.setBlock(baseX + 2, baseY + 6, baseZ + 5, Block.lever.blockID, 13, 2);
        ChessModUtils.generateChessBoard(world, baseX, baseY, baseZ);
        int randomPuzzle = rand.nextInt(2);
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                String pieceString = boards[randomPuzzle][7 - j][7 - i];
                if(!pieceString.equals("  ")) {
                    EntityBaseChessPiece chessPiece;
                    switch(pieceString.charAt(1)){
                        case 'K':
                            chessPiece = new EntityKing(world);
                            break;
                        case 'Q':
                            chessPiece = new EntityQueen(world);
                            break;
                        case 'N':
                            chessPiece = new EntityKnight(world);
                            break;
                        case 'B':
                            chessPiece = new EntityBishop(world);
                            break;
                        case 'R':
                            chessPiece = new EntityRook(world);
                            break;
                        case 'P':
                            chessPiece = new EntityPawn(world);
                            break;
                        default:
                            continue;
                    }
                    chessPiece.setIsBlack(pieceString.charAt(0) == 'b');
                    chessPiece.computerPiece = pieceString.charAt(0) == 'b' == isComputerBlack[randomPuzzle];
                    chessPiece.isBlackTurn = !isComputerBlack[randomPuzzle];
                    chessPiece.setPosition((double)i + (double)baseX + 0.5D, baseY + 1.0D, (double)j + (double)baseZ + 0.5D);
                    chessPiece.setTargetPosition(i, j);
                    chessPiece.xOffset = baseX;
                    chessPiece.zOffset = baseZ;
                    chessPiece.mateInTimes = mateInTimes[randomPuzzle];
                    world.spawnEntityInWorld(chessPiece);
                }
            }
        }
    }

    private boolean collidingWithCave(World world, int baseX, int baseY, int baseZ){
        for(int i = baseX; i <= baseX + 8; i++) {
            for(int j = baseY; j <= baseY + 4; j++) {
                for(int k = baseZ; k <= baseZ + 8; k++) {
                    if(world.getBlockId(i, j, k) == 0) return true;
                }
            }
        }
        return false;
    }

    private void fillWithMetadataBlocks(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int blockID, int metadata){
        for(int i = minX; i <= maxX; i++) {
            for(int j = minY; j <= maxY; j++) {
                for(int k = minZ; k <= maxZ; k++) {
                    // if(world.getBlockId(i, j, k) != 0)
                    world.setBlock(i, j, k, blockID, metadata, 3);
                }
            }
        }
    }

    private void fillWithMossyStone(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Random rand){
        for(int i = minX; i <= maxX; i++) {
            for(int j = minY; j <= maxY; j++) {
                for(int k = minZ; k <= maxZ; k++) {
                    if(world.getBlockId(i, j, k) != 0) {
                        if(rand.nextInt(4) == 0) {
                            world.setBlock(i, j, k, Block.cobblestone.blockID, 0, 2);
                        } else {
                            world.setBlock(i, j, k, Block.cobblestoneMossy.blockID, 0, 2);
                        }
                    }
                }
            }
        }
    }

}
