package us.timberdnd.lunatic.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.timberdnd.lunatic.account.Account;
import us.timberdnd.lunatic.account.AccountManager;
import us.timberdnd.lunatic.commands.handler.CommandHandler;
import us.timberdnd.lunatic.cooldown.Cooldown;
import us.timberdnd.lunatic.utils.ConfigValues;

/**
 * Created by DigitalCodex on Oct 29, 2016.
 */
public class SOTWCommand extends CommandHandler {

	public SOTWCommand(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String s, String[] args) {
		/*if(!(sender instanceof Player)) {
            sender.sendMessage(translate(getLanguageManager().getMessage("mustBePlayer")));
            return false;
        }*/
		if((!sender.hasPermission("luna.sotw")) && (!sender.isOp())) {
			sender.sendMessage(translate(getLanguageManager().getMessage("noPermission")));
			return false;
		}
		Account acc = AccountManager.getAccount((Player) sender);
		if(commandHasCooldown()) {
			if(acc.hasCooldown(getName())) {
				sender.sendMessage(translate(
						getLanguageManager().getMessage("commandCooldown").replace("{0}", String.valueOf(acc.getCooldown(getName()).getTimeRemaining()))));
				return false;
			}else{
				new Cooldown(acc, getName(), getCooldownLength(), System.currentTimeMillis()).register();
			}
		}
		switch(args.length) {
		case 0:
			String toggled = ConfigValues.SOTW
			? getInstance().getConfig().getString("color.enabled", "&a") + "ENABLED"
					: getInstance().getConfig().getString("color.disabled", "&c") + "DISABLED";
			sender.sendMessage(translate(
					getLanguageManager().getMessage("SOTWView").replace("{0}", toggled)));
			break;
		case 1:
			if((!sender.hasPermission("luna.sotw.admin")) && (!sender.isOp())) {
				sender.sendMessage(translate(getLanguageManager().getMessage("noPermission")));
				return false;
			}
			String string = args[0];
			if ("start".equals(string)) {
				if(ConfigValues.SOTW) {
					sender.sendMessage(translate(
							getLanguageManager().getMessage("SOTWAlreadyEnabled")));
				}else{
					ConfigValues.SOTW = true;
					sender.sendMessage(translate(
							getLanguageManager().getMessage("SOTWEnabled")));
				}
			} else if ("stop".equals(string)) {
				if(!(ConfigValues.SOTW)) {
					sender.sendMessage(translate(
							getLanguageManager().getMessage("SOTWAlreadyDisabled")));
				}else{
					ConfigValues.SOTW = false;
					sender.sendMessage(translate(
							getLanguageManager().getMessage("SOTWDisabled")));
				}
			} else {
				sender.sendMessage(translate(
						getLanguageManager().getMessage("incorrectSOTWCommand")));
			}
			break;
		}
		return true;
	}
}