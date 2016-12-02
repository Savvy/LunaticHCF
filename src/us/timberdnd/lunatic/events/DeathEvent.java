package us.timberdnd.lunatic.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import us.timberdnd.lunatic.Methods;
import us.timberdnd.lunatic.account.Account;
import us.timberdnd.lunatic.account.AccountManager;
import us.timberdnd.lunatic.utils.BaseUtils;

/**
 * Created by DigitalCodex on Oct 24, 2016.
 */
public class DeathEvent extends Methods implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Account acc = AccountManager.getAccount(event.getEntity());
		acc.setLives(acc.getLives() - 1);
		if(acc.getLives() == 0 || kickOnDeath) {
			acc.getPlayer().kickPlayer(BaseUtils.translate(
                    getLanguageManager().getMessage("deathKick")));
		}
	}
}