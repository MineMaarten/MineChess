package chessMod.client;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class SoundHandlerChessMod{

    @ForgeSubscribe
    public void onSound(SoundLoadEvent event){
        try {
            //TODO add sounds
        } catch(Exception e) {
            System.err.println("Failed to register one or more sounds.");
        }

    }
}
