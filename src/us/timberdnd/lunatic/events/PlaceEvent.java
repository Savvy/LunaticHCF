package us.timberdnd.lunatic.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import us.timberdnd.lunatic.account.Account;
import us.timberdnd.lunatic.account.AccountManager;
import us.timberdnd.lunatic.utils.ConfigValues;

public class PlaceEvent extends ConfigValues implements Listener {
	
	@EventHandler
	public void onDrop(BlockPlaceEvent event) {
		Account acc = AccountManager.getAccount(event.getPlayer());
		if(acc.isModeratorMode() && (!allowPlaceInMod)) event.setCancelled(true);
	}
}
