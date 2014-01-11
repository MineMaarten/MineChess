package minechess.client;

import net.minecraft.util.StatCollector;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class LocalizationHandler{
    private static final String[] REPLACE_CHARS = new String[]{"@", "#", "$", "^", "&", "*"};

    /**
     * Returns a formatted string (injected with the replacements) from a unlocalized message. To add color to this string add /u00ax as first replacement.
     * @param chatMessage
     * @param replacements
     * @return
     */
    public static String getStringFromUnlocalizedParts(String chatMessage, String... replacements){
        chatMessage = getStringLocalization(chatMessage);
        for(int i = 0; i < replacements.length; i++) {
            String replacement = replacements[i];
            if(!getStringLocalization(replacement).equals("")) {
                replacements[i] = getStringLocalization(replacement);
                if(replacement.startsWith("entity.")) {
                    for(String replaceChar : REPLACE_CHARS)
                        chatMessage = chatMessage.replace(replaceChar, getStringLocalization(replacement.replace("name", "replacement." + replaceChar)));
                }
            } else {
                replacements[i] = replacement;
            }
        }
        return String.format("%s" + chatMessage, (Object[])replacements);
    }

    public static String getStringLocalization(String unlocalized){
        return StatCollector.translateToLocal(unlocalized);
    }
}
