package us.timberdnd.lunatic.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import us.timberdnd.lunatic.LunaticHCF;

public class ConfigValues {
	
	public static boolean SOTW = LunaticHCF.getInstance().getConfig().getBoolean("sotw.enabled", false);
	
	public String serverName = LunaticHCF.getInstance().getConfig().getString("serverName", "&cServerName &7&l>> ");

	public String mapVersion = LunaticHCF.getInstance().getConfig().getString("mapVersion", "1");

	public boolean allowDropInMod = LunaticHCF.getInstance().getConfig().getBoolean("allowDropInMod", false);
	public boolean allowBreakInMod = LunaticHCF.getInstance().getConfig().getBoolean("allowBreakInMod", false);
	public boolean allowPlaceInMod = LunaticHCF.getInstance().getConfig().getBoolean("allowPlaceInMod", false);
	
	public boolean kickOnDeath = LunaticHCF.getInstance().getConfig().getBoolean("kickOnDeath");
	
	public boolean enderPearlCooldownEnabled = LunaticHCF.getInstance().getConfig().getBoolean("enderpearlCooldown.enabled");
	public int enderPearlCooldownLength = LunaticHCF.getInstance().getConfig().getInt("enderpearlCooldown.length");
	
	public static boolean isScoreboard = LunaticHCF.getInstance().getConfig().getBoolean("scoreboard.enabled");
	public static int scoreboardUpdate = LunaticHCF.getInstance().getConfig().getInt("scoreboard.update");
	public static String scoreboardTitle = LunaticHCF.getInstance().getConfig().getString("scoreboard.title");

	public static ItemStack getItem(ConfigurationSection cs) {
		ItemStack item = new ItemStack(Material.AIR);
		if((!cs.isSet("material")) || (!cs.isSet("name")) ) {
			throw new NullPointerException("Could not correctly parse " + cs.getName());
		}
		if(Material.valueOf(cs.getString("material").toUpperCase()) == null) {
			throw new NullPointerException(cs.getString("material") + " is not a valid item type!");
		}
		item.setType(Material.valueOf(cs.getString("material").toUpperCase()));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(BaseUtils.translate(cs.getString("name")));
		if(cs.isSet("lore")) {
			List<String> lore = new ArrayList<String>();
			for(String s: cs.getStringList("lore")) {
				lore.add(BaseUtils.translate(s));
			}
			meta.setLore(lore);
		}
		if(cs.isSet("durability")) {
			item.setDurability((short) cs.getInt("durability"));
		}
		if(cs.isSet("amount")) {
			item.setAmount(cs.getInt("amount"));
		}
		item.setItemMeta(meta);
		return item;
	}
}
