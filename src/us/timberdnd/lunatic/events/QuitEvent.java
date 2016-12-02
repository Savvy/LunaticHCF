package us.timberdnd.lunatic.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

import us.timberdnd.lunatic.account.AccountManager;

/**
 * Created by Timber on 10/16/2016.
 */
public class QuitEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerQuitEvent event) {
        AccountManager.removeAccount(event.getPlayer());
    }
    
    @EventHandler
    public void onDisable(PluginDisableEvent event) {
    	for(Player player: Bukkit.getOnlinePlayers()) {
    		AccountManager.removeAccount(player);
    	}
    }
}
