package us.timberdnd.lunatic.utils;

import org.bukkit.ChatColor;

public class BaseUtils {
	
	public static String translate(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch(NumberFormatException ex) {
			return false;
		}
	}
}
