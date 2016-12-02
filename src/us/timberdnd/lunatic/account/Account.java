package us.timberdnd.lunatic.account;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import us.timberdnd.lunatic.LunaticHCF;
import us.timberdnd.lunatic.Methods;
import us.timberdnd.lunatic.cooldown.Cooldown;
import us.timberdnd.lunatic.utils.BaseUtils;
import us.timberdnd.lunatic.utils.ConfigValues;
import us.timberdnd.lunatic.utils.ScoreboardEntry;
import us.timberdnd.lunatic.utils.ScoreboardUtil;

/**
 * Created by DigitalCodex on 10/16/2016.
 */
public class Account extends Methods {

	private Map<String, Cooldown> cooldown = new HashMap<String, Cooldown>();
	private Player player;

	private int lives;

	private boolean moderatorMode;
	private boolean vanish;

	private File file;
	private FileConfiguration fc;

	ItemStack[] modItems;

	ScoreboardUtil boardUtil;

	private int scoreboardTask = 0;

	public Account(Player player) {
		this.player = player;
		this.file = new File(getInstance().getDataFolder() + "/data/"
				+ player.getUniqueId().toString() + ".yml");
		this.fc = YamlConfiguration.loadConfiguration(file);
		load();
	}
	public boolean isModeratorMode() {
		return moderatorMode;
	}

	public void toggleModeratorMode() {
		if(moderatorMode) { // FALSE
			if(modItems != null)
				player.getInventory().setContents(modItems);
			if(isVanished())
				toggleVanish();
			player.setGameMode(GameMode.SURVIVAL);
			setModeratorMode(false);
			return;
		} // TRUE
		modItems = player.getInventory().getContents();
		player.getInventory().clear();
		if(!isVanished())
			toggleVanish();
		player.setGameMode(GameMode.CREATIVE);
		for(int slot: LunaticHCF.moderatorItems.keySet()) {
			player.getInventory().setItem(slot, LunaticHCF.moderatorItems.get(slot));
		}
		setModeratorMode(true);
		return;
	}

	public void setModeratorMode(boolean moderatorMode) {
		this.moderatorMode = moderatorMode;
	}

	public boolean isVanished() {
		return vanish;
	}

	public void toggleVanish() {
		if(vanish) { // FALSE
			for(Player p: Bukkit.getOnlinePlayers()) {
				if(p.getName() != getPlayer().getName())
					p.showPlayer(getPlayer());	
			}
			setVanish(false);
			return;
		} // TRUE
		for(Player p: Bukkit.getOnlinePlayers()) {
			if(p.getName() != getPlayer().getName())
				p.hidePlayer(getPlayer());
		}
		setVanish(true);
		return;
	}

	public boolean hasLives() {
		return lives >= 1;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public void setVanish(boolean vanish) {
		this.vanish = vanish;
	}

	public Player getPlayer() {
		return player;
	}

	public File getFile() {
		return file;
	}

	public Map<String, Cooldown> getCooldowns() {
		return cooldown;
	}

	public Cooldown getCooldown(String cooldownName) {
		return cooldown.get(cooldownName);
	}

	public void removeCooldown(String cooldownName) {
		cooldown.remove(cooldownName);
	}

	public boolean hasCooldown(String cooldownName) {
		return getCooldowns().containsKey(cooldownName);
	}

	public void register(Cooldown cooldown) {
		getCooldowns().put(cooldown.getCooldownName(), cooldown);
	}

	public void load() {
		setModeratorMode(getConfig().isSet("modMode") && getConfig().getBoolean("modMode"));
		setVanish(false);
		lives = getConfig().getInt("lives", 1);
		if(getConfig().isSet("cooldowns")) {
			for(String cooldownName: getConfig().getConfigurationSection("cooldowns").getKeys(false)) {
				new Cooldown(this, getConfig().getString("cooldowns." + cooldownName), 
						getConfig().getInt("cooldowns." + cooldownName + ".length"),
						getConfig().getLong("cooldowns." + cooldownName + ".currentTime")).register();
			}
		}
		if(ConfigValues.isScoreboard) {
			boardUtil = new ScoreboardUtil(BaseUtils.translate(ConfigValues.scoreboardTitle));
			scoreboardTask = new BukkitRunnable() {
				ScoreboardEntry entry = boardUtil.add("key1", "Random int: " + 0, 30, true);
				int i = 0;
				public void run() {
					if(i == 200) {
						i = 0;
					}
					entry.update("Random int: " + i++);
				}
			}.runTaskTimerAsynchronously(getInstance(), 0, 15).getTaskId();
			boardUtil.add(getPlayer());
		}
	}

	public void dispose() {
		if(AccountManager.getAccount(player).isModeratorMode()) {
			AccountManager.getAccount(player).toggleModeratorMode();
		}
		getConfig().set("modMode", isModeratorMode()); /*  Mod Mode */
		for(String cooldownName: getCooldowns().keySet()) {
			Cooldown cooldown = this.cooldown.get(cooldownName);
			getConfig().set("cooldowns." + cooldownName + ".length", cooldown.getCooldownLength());
			getConfig().set("cooldowns." + cooldownName + ".currentTime", cooldown.getCurrentTime());
		}
		getConfig().set("lives", getLives());
		if(scoreboardTask != 0) getInstance().getServer().getScheduler().cancelTask(scoreboardTask); /* Cancel Scoreboard Task */
		scoreboardTask = 0; /* Reset Scoreboard task */
		save();
	}

	public FileConfiguration getConfig() {
		return fc;
	}

	public void save() {
		try {
			getConfig().save(getFile());
			getLogger().log(Level.INFO, "Successfully saved ('" + player.getName() + "')'s data");
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
