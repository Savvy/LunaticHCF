package us.timberdnd.lunatic.events;

import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import us.timberdnd.lunatic.Methods;
import us.timberdnd.lunatic.account.Account;
import us.timberdnd.lunatic.account.AccountManager;
import us.timberdnd.lunatic.cooldown.Cooldown;
import us.timberdnd.lunatic.utils.BaseUtils;

/**
 * Created by DigitalCodex on Oct 24, 2016.
 */
public class ProjectileEvent extends Methods implements Listener {
	
	@EventHandler
	public void onThrow(ProjectileLaunchEvent event) {
		if(!(event.getEntity() instanceof EnderPearl)) {
			return;
		}
		if(!(enderPearlCooldownEnabled)) {
			return;
		}
		if(!(event.getEntity().getShooter() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		Account acc = AccountManager.getAccount(player);
		if(acc.hasCooldown("enderpearl")) {
			player.sendMessage(BaseUtils.translate(
                    getLanguageManager().getMessage("enderPearlCooldown").replace("{0}", String.valueOf(acc.getCooldown("enderpearl").getTimeRemaining()))));
			event.setCancelled(true);
		}else{
			new Cooldown(acc, "enderpearl", enderPearlCooldownLength, System.currentTimeMillis()).register();
		}
	}
}
