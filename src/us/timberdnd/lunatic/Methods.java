package us.timberdnd.lunatic;


import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import us.timberdnd.lunatic.account.Account;
import us.timberdnd.lunatic.utils.ConfigValues;
import us.timberdnd.lunatic.utils.LanguageManager;

/**
 * Created by Timber on 10/16/2016.
 */
public class Methods extends ConfigValues {

    public static LunaticHCF getInstance() {
        return LunaticHCF.getInstance();
    }

    public Logger getLogger() {
        return getInstance().getLogger();
    }

    public LanguageManager getLanguageManager() {
        return getInstance().getLangManager();
    }
    
    public Chat getChat() {
    	return LunaticHCF.chat;
    }
    
    public String placeHolder(Account account, String string) {
    	return string
    			.replace("{lunatic_servername}", serverName)
    			.replace("{lunatic_mapversion}", mapVersion)
    			.replaceAll("{lunatic_mod}", String.valueOf(account.isModeratorMode()).toUpperCase());
    }
}
