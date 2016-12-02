package us.timberdnd.lunatic.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.timberdnd.lunatic.account.Account;
import us.timberdnd.lunatic.account.AccountManager;
import us.timberdnd.lunatic.commands.handler.CommandHandler;
import us.timberdnd.lunatic.utils.BaseUtils;

/**
 * Created by Timber on 10/16/2016.
 */
public class LivesCommand extends CommandHandler {

	public LivesCommand(String name) {
		super(name);
	}

	/*
	 *
	 *  /lives - Displays amount of lives player has (Permissible)
	 *  /lives <name> - Displays amount of lives player has (Permissible)
	 *  /lives transfer <player> <amount> - Gives another player your lives (Permissible)
	 *  /lives give <player> <amount> - Gives players lives (Admin Command) (Permissible)
	 *  /lives take <player> <amount> - Takes players lives (Admin Command) (Permissible)
	 *  /lives help - Displays help menu
	 * 
	 */
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(translate(getLanguageManager().getMessage("mustBePlayer")));
			return false;
		}
		if((!sender.hasPermission("luna.lives")) && (!sender.isOp())) {
			sender.sendMessage(translate(getLanguageManager().getMessage("noPermission")));
			return false;
		}
		Account senderAcc = AccountManager.getAccount((Player) sender);
		Player player;
		switch(args.length) {
		case 0:
			senderAcc.getPlayer().sendMessage(BaseUtils.translate(getLanguageManager()
					.getMessage("playerLives").replace("{0}", String.valueOf(senderAcc.getLives()))));
			return true;
		case 1:
			String string = args[0];
			if ("help".equals(string)) {
				for(String help: getLanguageManager().getMessageList("livesHelp")) {
					sender.sendMessage(translate(help));
				}
				return true;
			} else {
				player = (Player) Bukkit.getPlayer(args[0]);
				if(player != null && player.isOnline()) {
					Account pAcc = AccountManager.getAccount(player);
					senderAcc.getPlayer().sendMessage(BaseUtils.translate(getLanguageManager()
							.getMessage("targetLives").replace("{0}", player.getName()).replaceAll("{1}", String.valueOf(pAcc.getLives()))));
					return true;
				}else{
					sender.sendMessage(translate(getLanguageManager().getMessage("playerNotOnline")));
					return false;
				}
			}
		case 3:
			if(args[0].equalsIgnoreCase("transfer")) {
				if((!sender.hasPermission("luna.lives.transfer")) && (!sender.isOp())) {
					sender.sendMessage(translate(getLanguageManager().getMessage("noPermission")));
					return false;
				}
				if(!(BaseUtils.isInt(args[2]))) {
					sender.sendMessage(translate(getLanguageManager().getMessage("mustBeNumber")));
					return false;
				}
				if(!(senderAcc.getLives() >= Integer.parseInt(args[2]))) {
					sender.sendMessage(translate(getLanguageManager().getMessage("notEnoughLives")));
					return false;
				} 

				player = (Player) Bukkit.getPlayer(args[1]);
				if(player != null && player.isOnline()) {
					Account pAcc = AccountManager.getAccount(player);
					pAcc.setLives(pAcc.getLives() + Integer.parseInt(args[2]));
					senderAcc.setLives(pAcc.getLives() - Integer.parseInt(args[2]));
					senderAcc.getPlayer().sendMessage(BaseUtils.translate(getLanguageManager()
							.getMessage("transferLives").replace("{1}", player.getName()).replaceAll("{0}", args[2])));
					pAcc.getPlayer().sendMessage(BaseUtils.translate(getLanguageManager()
							.getMessage("livesTransferred").replace("{1}", player.getName()).replaceAll("{0}", args[2])));
					return true;
				}else{
					sender.sendMessage(translate(getLanguageManager().getMessage("playerNotOnline")));
					return false;
				}
			} else if(args[0].equalsIgnoreCase("give")) {
				if((!sender.hasPermission("luna.lives.give")) && (!sender.isOp())) {
					sender.sendMessage(translate(getLanguageManager().getMessage("noPermission")));
					return false;
				}
				if(!(BaseUtils.isInt(args[2]))) {
					sender.sendMessage(translate(getLanguageManager().getMessage("mustBeNumber")));
					return false;
				}
				player = (Player) Bukkit.getPlayer(args[1]);
				if(player != null && player.isOnline()) {
					Account pAcc = AccountManager.getAccount(player);
					pAcc.setLives(pAcc.getLives() + Integer.parseInt(args[2]));
					senderAcc.getPlayer().sendMessage(BaseUtils.translate(getLanguageManager()
							.getMessage("givenLives").replace("{1}", player.getName()).replaceAll("{0}", args[2])));

					pAcc.getPlayer().sendMessage(BaseUtils.translate(getLanguageManager()
							.getMessage("livesGiven").replace("{1}", player.getName())));
					return true;
				}else{
					sender.sendMessage(translate(getLanguageManager().getMessage("playerNotOnline")));
					return false;
				}
			} else if(args[0].equalsIgnoreCase("take")) {
				if((!sender.hasPermission("luna.lives.take")) && (!sender.isOp())) {
					sender.sendMessage(translate(getLanguageManager().getMessage("noPermission")));
					return false;
				}
				if(!(BaseUtils.isInt(args[2]))) {
					sender.sendMessage(translate(getLanguageManager().getMessage("mustBeNumber")));
					return false;
				}
				player = (Player) Bukkit.getPlayer(args[1]);
				if(player != null && player.isOnline()) {
					Account pAcc = AccountManager.getAccount(player);
					pAcc.setLives(pAcc.getLives() - Integer.parseInt(args[2]));
					senderAcc.getPlayer().sendMessage(BaseUtils.translate(getLanguageManager()
							.getMessage("takenLives").replace("{1}", player.getName()).replaceAll("{0}", args[2])));

					pAcc.getPlayer().sendMessage(BaseUtils.translate(getLanguageManager()
							.getMessage("livesTaken").replace("{1}", player.getName())));
					return true;
				}else{
					sender.sendMessage(translate(getLanguageManager().getMessage("playerNotOnline")));
					return false;
				}
			} else {
				for(String help: getLanguageManager().getMessageList("livesHelp")) {
					sender.sendMessage(translate(help));
				}
			}
		}
		return true;
	}
}
