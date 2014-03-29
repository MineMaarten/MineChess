package minechess.client;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class SoundHandlerMineChess{

    @SubscribeEvent
    public void onSound(SoundLoadEvent event){
        try {
            //TODO add sounds
        } catch(Exception e) {
            System.err.println("Failed to register one or more sounds.");
        }

    }
}
