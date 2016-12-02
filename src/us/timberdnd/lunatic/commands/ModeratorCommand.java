package us.timberdnd.lunatic.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.timberdnd.lunatic.account.Account;
import us.timberdnd.lunatic.account.AccountManager;
import us.timberdnd.lunatic.commands.handler.CommandHandler;
import us.timberdnd.lunatic.cooldown.Cooldown;

/**
 * Created by Timber on 10/16/2016.
 */
public class ModeratorCommand extends CommandHandler {

    public ModeratorCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(translate(getLanguageManager().getMessage("mustBePlayer")));
            return false;
        }
        if((!sender.hasPermission("luna.moderatormode")) && (!sender.isOp())) {
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
        acc.toggleModeratorMode();
        String toggled = acc.isModeratorMode()
                ? getInstance().getConfig().getString("color.enabled", "&a") + "ENABLED"
                : getInstance().getConfig().getString("color.disabled", "&c") + "DISABLED";
        sender.sendMessage(translate(
                getLanguageManager().getMessage("moderatorModeToggled").replace("{0}", toggled)));
        return true;
    }
}
