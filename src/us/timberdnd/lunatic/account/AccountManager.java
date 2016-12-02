package us.timberdnd.lunatic.account;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Timber on 10/16/2016.
 */
public class AccountManager {

	private static Map<String, Account> accounts = new HashMap<String, Account>();

	public static Map<String, Account> getAccounts() {
		return accounts;
	}

	public static void addAccount(Player player) {
		if(!(accounts.containsKey(player.getName())))
			accounts.put(player.getName(), new Account(player));
	}

	public static void removeAccount(Player player) {
		if(accounts.containsKey(player.getName()))
		getAccount(player).dispose();
		accounts.remove(player.getName());
	}

	public static Account getAccount(Player player) {
		return accounts.get(player.getName());
	}

	public static Account getAcount(String string) {
		return accounts.get(string);
	}
}
