package us.timberdnd.lunatic.actions.action;

import org.bukkit.entity.Player;

import us.timberdnd.lunatic.Methods;
import us.timberdnd.lunatic.account.Account;
import us.timberdnd.lunatic.account.AccountManager;
import us.timberdnd.lunatic.actions.Actions;
import us.timberdnd.lunatic.utils.BaseUtils;

/**
 * Created by DigitalCodex on Oct 22, 2016.
 */
public class VanishAction extends Methods implements Actions {
    
    String name;
    
    public VanishAction(String name) {
	this.name = name;
    }
    @Override
    public String getName() {
	return name;
    }

    @Override
    public void callAction(Player player) {
	Account acc = AccountManager.getAccount(player);
	acc.toggleVanish();
	   String toggled = acc.isVanished()
               ? getInstance().getConfig().getString("color.enabled", "&a") + "ENABLED"
               : getInstance().getConfig().getString("color.disabled", "&c") + "DISABLED";
       player.sendMessage(BaseUtils.translate(
               getLanguageManager().getMessage("vanishModeToggled").replace("{0}", toggled)));
    }
	@Override
	public void callAction(Player player, Player target) {}
}