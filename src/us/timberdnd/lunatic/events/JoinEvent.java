package us.timberdnd.lunatic.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.server.PluginEnableEvent;

import us.timberdnd.lunatic.Methods;
import us.timberdnd.lunatic.account.Account;
import us.timberdnd.lunatic.account.AccountManager;
import us.timberdnd.lunatic.utils.BaseUtils;

/**
 * Created by Timber on 10/16/2016.
 */
public class JoinEvent extends Methods implements Listener {

    @EventHandler
    public void onEnable(PluginEnableEvent event) {
    	for(Player player: Bukkit.getOnlinePlayers()) {
    		AccountManager.addAccount(player);
    	}
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
            AccountManager.addAccount(event.getPlayer());
    }
    
    public void onLogin(PlayerLoginEvent event) {
    	Player player = (Player) event.getPlayer();
    	Account acc = AccountManager.getAccount(player);
    	if(!(acc.hasLives())) {
    		event.disallow(Result.KICK_OTHER, BaseUtils.translate(
                    getLanguageManager().getMessage("playerHasNoLives")));
    	}
    }
}
