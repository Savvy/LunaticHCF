package us.timberdnd.lunatic.commands.handler;

import org.bukkit.ChatColor;
import org.bukkit.command.defaults.BukkitCommand;
import us.timberdnd.lunatic.LunaticHCF;
import us.timberdnd.lunatic.Methods;
import us.timberdnd.lunatic.utils.LanguageManager;

import java.util.List;

public abstract class CommandHandler extends BukkitCommand {
	
	private boolean commandCooldown;
	
	private int cooldownLength;
    public CommandHandler(String name) {
        super(name);
        this.getAliases().clear();
        this.getAliases().addAll(getInstance().commandFileConfig.getStringList(name + ".aliases"));
        cooldownLength = getInstance().commandFileConfig.getInt(name + ".cooldown");
        commandCooldown = cooldownLength == 0 ? false : true;
    }

    public CommandHandler(String name, String description, String usage, List<String> aliases) {
        super(name, description, usage, aliases);
    }
    
    public boolean commandHasCooldown() {
    	return commandCooldown;
    }
    
    public int getCooldownLength() {
    	return cooldownLength;
    }

    public LunaticHCF getInstance() {
        return LunaticHCF.getInstance();
    }

    public LanguageManager getLanguageManager() {
        return Methods.getInstance().getLangManager();
    }

    protected String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}