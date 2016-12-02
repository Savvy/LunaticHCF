package us.timberdnd.lunatic;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;
import us.timberdnd.lunatic.actions.Action;
import us.timberdnd.lunatic.commands.LivesCommand;
import us.timberdnd.lunatic.commands.ModeratorCommand;
import us.timberdnd.lunatic.commands.SOTWCommand;
import us.timberdnd.lunatic.commands.handler.CommandHandler;
import us.timberdnd.lunatic.events.BreakEvent;
import us.timberdnd.lunatic.events.DropEvent;
import us.timberdnd.lunatic.events.InteractEvent;
import us.timberdnd.lunatic.events.JoinEvent;
import us.timberdnd.lunatic.events.PlaceEvent;
import us.timberdnd.lunatic.events.QuitEvent;
import us.timberdnd.lunatic.utils.BaseUtils;
import us.timberdnd.lunatic.utils.ConfigValues;
import us.timberdnd.lunatic.utils.LanguageManager;

/**
 * Created by Timber on 10/16/2016.
 */
public class LunaticHCF extends JavaPlugin {

	private static LunaticHCF instance;
	public static Chat chat = null;
	private LanguageManager langManager;

	private File commandFile;
	public FileConfiguration commandFileConfig;

	public static Map<Integer, ItemStack> moderatorItems = new HashMap<Integer, ItemStack>();
	public static List<String> scoreboardLines = new ArrayList<String>();
	
	public static LunaticHCF getInstance() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		load();
	}

	public void load() {
		getServer().getPluginManager().registerEvents(new JoinEvent(), this);
		getServer().getPluginManager().registerEvents(new QuitEvent(), this);
		getServer().getPluginManager().registerEvents(new DropEvent(), this);
		getServer().getPluginManager().registerEvents(new InteractEvent(), this);
		getServer().getPluginManager().registerEvents(new BreakEvent(), this);
		getServer().getPluginManager().registerEvents(new PlaceEvent(), this);
		commandFile = new File(getDataFolder() + "/commands.yml");
		commandFileConfig = YamlConfiguration.loadConfiguration(commandFile);
		scoreboardLines = getConfig().getStringList("scoreboard.lines");
		langManager = new LanguageManager(instance, "language.yml");
		List<String> livesHelp = new ArrayList<String>();
		livesHelp.add("&7--&cLives Help&7--");
		livesHelp.add("&c/lives - Displays amount of lives player has.");
		livesHelp.add("&c/lives <name> - Displays amount of lives player has");
		livesHelp.add("&c/lives transfer <player> <amount> - Gives another player your lives");
		livesHelp.add("&c/lives give <player> <amount> - Gives players lives (Admin Command)");
		livesHelp.add("&c/lives take <player> <amount> - Takes players lives (Admin Command)");
		livesHelp.add("&c/lives help - Displays this menu.");

		langManager
		.addMessage("mustBePlayer", "&cThis command is only available to players!")
		.addMessage("playerNotOnline", "&cThat player is not online.")
		.addMessage("mustBeNumber", "&cAmount must be a number!")
		.addMessage("noPermission", "&cSorry you do not have permission for this command.")
		.addMessage("deathKick", "&cYou have been kicked because you died.")
		.addMessage("commandCooldown", "&cThis command is on cooldown for another {0} seconds.")
		.addMessage("vanishModeToggled", "&cVanish mode {0}")
		.addMessage("moderatorModeToggled", "&cModerator mode {0}")
		.addMessage("enderPearlCooldown", "&cThis object is on cooldown for another {0} seconds.")
		.addMessage("playerHasNoLives", "&cYou have no lives left.")
		.addMessage("playerLives", "&cYou have {0} lives")
		.addMessage("targetLives", "&c{0} has {1} lives.")
		.addMessage("notEnoughLives", "You do not have enough lives to transfer")
		.addMessage("transferLives", "&cYou have transfered {0} lives to {1}.")
		.addMessage("livesTransferred", "&cYou have recieved {0} lives from {1}.")
		.addMessage("givenLives", "&cYou have given {0} lives to {1}")
		.addMessage("livesGiven", "&cYou have recieved {0} lives.")
		.addMessage("takenLives", "&cYou have taken {0} lives from {1}")
		.addMessage("livesTaken", "&cYou have lost {0} lives.")
		.addMessage("SOTWView", "&cSOTW is currently {0}")
		.addMessage("SOTWAlreadyEnabled", "&cSOTW is already enabled")
		.addMessage("SOTWEnabled", "&cSOTW has been enabled")
		.addMessage("SOTWAlreadyDisabled", "&cSOTW is already disabled")
		.addMessage("SOTWDisabled", "&cSOTW has been disabled")
		.addMessage("incorrectSOTWCommand", "Incorrect usage please try: /sotw <enable/disable/help>.")
		.addMessageList("livesHelp", livesHelp);
		langManager.save();
		for(String s: getInstance().getConfig().getConfigurationSection("modItems").getKeys(false)) {
			ItemStack item = ConfigValues.getItem(getInstance().getConfig().getConfigurationSection("modItems." + s));
			if(BaseUtils.isInt(s)) {
				moderatorItems.put(Integer.parseInt(s), item);
			}else{
				throw new NullPointerException(s + " is not a valid slot integer, please use an integer.");
			}
		}
		Action.registerActions();
		registerAll(
				new SOTWCommand("sotw"),
				new LivesCommand("lives"),
				new ModeratorCommand("moderator"));
		if(setupChat()) {
			getLogger().log(Level.INFO, "Successfully hooked into Vault");
		}else{
			getLogger().log(Level.SEVERE, "Could not hook into Vault");
		}
	}

	public void registerAll(CommandHandler... cmds) {
		for(CommandHandler cmd: cmds) {
			if(commandFileConfig.isSet(cmd.getName())) {
				if(commandFileConfig.getBoolean(cmd.getName() + ".enabled")) {
					register(cmd);
				}
			}else{
				commandFileConfig.set(cmd.getName() + ".enabled", true);
				commandFileConfig.set(cmd.getName() + ".cooldown", 0);
				commandFileConfig.set(cmd.getName() + ".aliases", new ArrayList<String>());
				try {
					commandFileConfig.save(commandFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				register(cmd);
			}
		}
	}
	
	public void onDisable() {
		getConfig().set("SOTW.enabled", ConfigValues.SOTW);
	}

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null) {
			chat = chatProvider.getProvider();
		}

		return (chat != null);
	}

	public LanguageManager getLangManager() {
		return langManager;
	}

	public void register(CommandHandler cmd) {
		try {
			Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			if (!f.isAccessible()) {
				f.setAccessible(true);
			}
			CommandMap map = (CommandMap) f.get(getServer());
			map.register(cmd.getName(), "LunaticHCF", cmd);
			getLogger().log(Level.INFO, "Successfully registered command " + cmd.getName());
		} catch (Exception ex) {
		}
	}
}
