package chessMod.common;

import net.minecraft.block.Block;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import chessMod.common.ai.Chess;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

// TODO increase version
@Mod(modid = "Minemaarten_Chess Mod", name = "MineChess", version = "1.3.3")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {"chessMod"}, packetHandler = PacketHandlerChessMod.class)
public class ChessMod{

    @SidedProxy(clientSide = "chessMod.client.ClientProxyChessMod", serverSide = "chessMod.common.CommonProxyChessMod")
    public static CommonProxyChessMod proxy;

    @Instance("Minemaarten_Chess Mod")
    public static ChessMod instance = new ChessMod();

    public static final boolean DEBUG = false;

    public static Item itemPieceMover;

    int ItemPieceMoverID;

    public static boolean configRenderMovement;
    public static int configWorldgenPuzzleUnderground;

    public static Property propertyAIDepth; //globally declared so we can the change the config with the /aidepth command.
    public static Configuration config;

    @EventHandler
    public void PreInit(FMLPreInitializationEvent event){
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        Property PropertyRenderMovement = config.get(Configuration.CATEGORY_GENERAL, "Display possible moves", true);
        PropertyRenderMovement.comment = "Setting this to false doesn't show the green tiles indicating the possible moves of the current selected chesspiece.";
        configRenderMovement = PropertyRenderMovement.getBoolean(false);

        Property PropertyPuzzleUnderground = config.get(Configuration.CATEGORY_GENERAL, "Spawnrate of underground Chess Puzzles", 40);
        PropertyPuzzleUnderground.comment = "Determines the spawnrate of the underground chess puzzles. The lesser the number, the more spawns. Set to 0 to completely disable the spawns.";
        configWorldgenPuzzleUnderground = PropertyPuzzleUnderground.getInt();

        propertyAIDepth = config.get("AI settings", "AI depth", 5);
        propertyAIDepth.comment = "Determines the difficulty of the AI. This can also be changed in-game by using the command \"aidepth <depth>\". The default is quite a challenge and takes about 30 seconds before making a move. Decreasing the number lowers the diffuculty quite severely. Increasing it is also possible, but take more time in account.";
        Chess.maxDepthSetting = propertyAIDepth.getInt();

        // item ID's
        ItemPieceMoverID = config.getItem("Black/White Piece Mover & Chessboard Generator & Chessboard Column ID", 5090).getInt();

        config.save();

        itemPieceMover = new ItemPieceMover(ItemPieceMoverID).setCreativeTab(CreativeTabs.tabTools).setUnlocalizedName("Piece Mover");
        gameRegisters();
        proxy.registerHandlers();
    }

    @EventHandler
    public void load(FMLInitializationEvent event){
        proxy.registerRenders();
        LocalizationHandler.init();
    }

    public void gameRegisters(){
        GameRegistry.registerItem(itemPieceMover, "Piece Mover");

        ItemStack chessColumn = new ItemStack(itemPieceMover, 1, 3);
        ItemStack blackWool = new ItemStack(Block.cloth.blockID, 1, 15);
        ItemStack whiteWool = new ItemStack(Block.cloth.blockID, 1, 0);
        // crafting recipes
        // piece movers
        for(int i = 0; i < 2; i++) {
            GameRegistry.addRecipe(new ItemStack(itemPieceMover, 1, 1 - i), " ww", " ww", "s  ", 'w', new ItemStack(Block.cloth.blockID, 1, i * 15), 's', new ItemStack(Item.stick));
        }
        GameRegistry.addShapelessRecipe(new ItemStack(itemPieceMover, 1, 2), chessColumn, chessColumn, chessColumn, chessColumn, chessColumn, chessColumn, chessColumn, chessColumn);
        GameRegistry.addShapelessRecipe(chessColumn, blackWool, blackWool, blackWool, blackWool, whiteWool, whiteWool, whiteWool, whiteWool);
        // Entities
        // parms: entity class, mobname (for spawners), id, modclass, max player
        // distance for update, update frequency, boolean keep server updated
        // about velocities.
        EntityRegistry.registerModEntity(EntityKing.class, "King", 0, this, 80, 1, true);
        EntityRegistry.registerModEntity(EntityRook.class, "Rook", 1, this, 80, 1, true);
        EntityRegistry.registerModEntity(EntityPawn.class, "Pawn", 2, this, 80, 1, true);
        EntityRegistry.registerModEntity(EntityKnight.class, "Knight", 3, this, 80, 1, true);
        EntityRegistry.registerModEntity(EntityQueen.class, "Queen", 4, this, 80, 1, true);
        EntityRegistry.registerModEntity(EntityBishop.class, "Bishop", 5, this, 80, 1, true);
        EntityRegistry.registerModEntity(EntityPickyXPOrb.class, "Picky XP Orb", 6, this, 80, 1, true);

        // worldgenerators
        GameRegistry.registerWorldGenerator(new WorldGeneratorChessMod());
    }

    @EventHandler
    public void onServerStart(FMLServerStartingEvent event){
        ServerCommandManager comManager = (ServerCommandManager)MinecraftServer.getServer().getCommandManager();
        comManager.registerCommand(new CommandAIDepth());
        comManager.registerCommand(new CommandKillPiece());
        comManager.registerCommand(new CommandDraw());
    }
}
