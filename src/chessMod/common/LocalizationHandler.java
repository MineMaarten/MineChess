package chessMod.common;

import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */

public class LocalizationHandler{
    private static final String[] LANG_FILES = new String[]{"en_US", "es_ES", "nl_NL"};
    private static final String LANGUAGE_LOCATION = "/assets/chessmod/lang/";
    private static final String[] REPLACE_CHARS = new String[]{"@", "#", "$", "^", "&", "*"};

    public static void init(){
        for(String localizationFile : LANG_FILES) {
            LanguageRegistry.instance().loadLocalization(LANGUAGE_LOCATION + localizationFile + ".xml", localizationFile, true);
        }
    }

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
        if(LanguageRegistry.instance().getStringLocalization(unlocalized).equals("")) {
            return LanguageRegistry.instance().getStringLocalization(unlocalized, "en_US");
        } else {
            return LanguageRegistry.instance().getStringLocalization(unlocalized);
        }
    }
}
