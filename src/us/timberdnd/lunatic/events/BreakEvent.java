package us.timberdnd.lunatic.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import us.timberdnd.lunatic.account.Account;
import us.timberdnd.lunatic.account.AccountManager;
import us.timberdnd.lunatic.utils.ConfigValues;

public class BreakEvent extends ConfigValues implements Listener {
	
	@EventHandler
	public void onDrop(BlockBreakEvent event) {
		Account acc = AccountManager.getAccount(event.getPlayer());
		if(acc.isModeratorMode() && (!allowBreakInMod)) event.setCancelled(true);
	}
}
