package us.timberdnd.lunatic.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import us.timberdnd.lunatic.LunaticHCF;
import us.timberdnd.lunatic.Methods;
import us.timberdnd.lunatic.account.Account;
import us.timberdnd.lunatic.account.AccountManager;
import us.timberdnd.lunatic.actions.Action;
import us.timberdnd.lunatic.actions.Actions;
import us.timberdnd.lunatic.utils.BaseUtils;	

/**
 * Created by DigitalCodex on Oct 22, 2016.
 */
public class InteractEvent extends Methods implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Account acc = AccountManager.getAccount(event.getPlayer());
		if(!(acc.isModeratorMode())) {
			return;
		}

		int slot = event.getPlayer().getInventory().getHeldItemSlot();
		ItemStack item = event.getItem();
		if(item == null || event.getPlayer().getInventory().getItem(slot).getType() != item.getType() 
				|| (!item.hasItemMeta()) || (!item.getItemMeta().hasDisplayName()) || (LunaticHCF.moderatorItems.isEmpty())
				|| (!LunaticHCF.moderatorItems.containsKey(slot))) {
			return;
		}
		if(!(getInstance().getConfig().isSet("modItems." + String.valueOf(slot) + ".action"))) {
			return;
		}
		String configName = getInstance().getConfig().getString("modItems." + String.valueOf(slot) + ".name");
		String action = getInstance().getConfig().getString("modItems." + String.valueOf(slot) + ".action");
		if(action.equalsIgnoreCase("invsee")) {
			return;
		}
		if(item.getItemMeta().getDisplayName().equalsIgnoreCase(BaseUtils.translate(configName))) {
				Actions actions = Action.getActions().get(action);
				actions.callAction(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		if(!(event.getRightClicked() instanceof Player)) {
			return;
		}
		Account acc = AccountManager.getAccount(event.getPlayer());
		if(!(acc.isModeratorMode())) {
			return;
		}

		int slot = event.getPlayer().getInventory().getHeldItemSlot();
		ItemStack item = event.getPlayer().getItemInHand();
		if(item == null || event.getPlayer().getInventory().getItem(slot).getType() != item.getType() 
				|| (!item.hasItemMeta()) || (!item.getItemMeta().hasDisplayName()) || (LunaticHCF.moderatorItems.isEmpty())
				|| (!LunaticHCF.moderatorItems.containsKey(slot))) {
			return;
		}
		if(!(getInstance().getConfig().isSet("modItems." + String.valueOf(slot) + ".action"))) {
			return;
		}
		String configName = getInstance().getConfig().getString("modItems." + String.valueOf(slot) + ".name");
		String action;
		action = getInstance().getConfig().getString("modItems." + String.valueOf(slot) + ".action");
		if((!action.equalsIgnoreCase("invsee"))) {
			return;
		}
		if(item.getItemMeta().getDisplayName().equalsIgnoreCase(BaseUtils.translate(configName))) {
				Actions actions = Action.getActions().get(action);
				actions.callAction(event.getPlayer());
		}
	}
}
