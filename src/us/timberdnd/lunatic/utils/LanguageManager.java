package us.timberdnd.lunatic.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import us.timberdnd.lunatic.Methods;

public class LanguageManager extends Methods {

	private Plugin plugin;
	private String filePath;
	private File file;
	private FileConfiguration fc;
	private Map<String, String> langMessage = new HashMap<String, String>();
	
	private Map<String, List<String>> listMessages = new HashMap<String, List<String>>();
	public LanguageManager(Plugin plugin, String filePath) {
		this.plugin = plugin;
		this.filePath = filePath;
		this.file = new File(plugin.getDataFolder() + "/" + filePath);
		this.fc = YamlConfiguration.loadConfiguration(this.file);
	}

	public Map<String, String> getLanguageMessage() {
		return langMessage;
	}

	public String getMessage(String key) {
		if(langMessage.containsKey(key)) {
			return langMessage.get(key);
		}else if (fc.isSet(key)) {
			return fc.getString(key);
		}else{
			getLogger().log(Level.INFO, "The message: " + key + ", does not exist.");
			return "Invalid Message";
		}
	}
	
	public List<String> getMessageList(String key) {
		if(listMessages.containsKey(key)) {
			return listMessages.get(key);
		}else if (fc.isSet(key)) {
			return fc.getStringList(key);
		}else{
			getLogger().log(Level.INFO, "The message list: " + key + ", does not exist.");
			return new ArrayList<String>();
		}
	}
	
	public LanguageManager removeMessageList(String key) {
		listMessages.remove(key);
		getLogger().log(Level.INFO, "Successfully removed message: " + key);
		return this;
	}
	
	public LanguageManager addMessageList(String key, List<String> defaultMessage) {
		if(!(listMessages.containsKey(key))) {
			listMessages.put(key, defaultMessage);
			getLogger().log(Level.INFO, "Successfully Registered message list: " + key);
		}else{
			getLogger().log(Level.INFO, "Could not register message list: " + key);
			getLogger().log(Level.INFO, "Already Exists.");
		}
		return this;
	}

	public LanguageManager addMessage(String key, String defaultMessage) {
		if(!(langMessage.containsKey(key))) {
			langMessage.put(key, defaultMessage);
			getLogger().log(Level.INFO, "Successfully Registered message: " + key);
		}else{
			getLogger().log(Level.INFO, "Could not register message: " + key);
			getLogger().log(Level.INFO, "Already Exists.");
		}
		return this;
	}

	public LanguageManager removeMessage(String key) {
		langMessage.remove(key);
		getLogger().log(Level.INFO, "Successfully removed message: " + key);
		return this;
	}

	public void save() {
		try {
			for(String s: langMessage.keySet()) {
				if(!(fc.isSet(s))) {
					fc.set(s, langMessage.get(s));
				}
			}
			
			for(String s: listMessages.keySet()) {
				if(!(fc.isSet(s))) {
					fc.set(s, listMessages.get(s));
				}
			}
			fc.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileConfiguration getConfig() {
		return fc;
	}

	public String getPath() {
		return filePath;
	}

	public Plugin getPlugin() {
		return plugin;
	}
}
