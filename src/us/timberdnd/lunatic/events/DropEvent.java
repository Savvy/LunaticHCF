package us.timberdnd.lunatic.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import us.timberdnd.lunatic.account.Account;
import us.timberdnd.lunatic.account.AccountManager;
import us.timberdnd.lunatic.utils.ConfigValues;

public class DropEvent extends ConfigValues implements Listener {
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Account acc = AccountManager.getAccount(event.getPlayer());
		if(acc.isModeratorMode() && (!allowDropInMod)) event.setCancelled(true);
	}
}