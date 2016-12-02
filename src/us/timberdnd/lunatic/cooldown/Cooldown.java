package us.timberdnd.lunatic.cooldown;

import org.bukkit.entity.Player;

import us.timberdnd.lunatic.account.Account;

public class Cooldown {
	
	private Account acc;
	private Player player;
	private String cooldownName;
	private int cooldownLength;
	private long currentTime;
	
	public Cooldown(Account acc, String cooldownName, int cooldownLength, long currentTime) {
		this.acc = acc;
		this.player = acc.getPlayer();
		setCooldownName(cooldownName);
		setCooldownLength(cooldownLength);
		setCurrentTime(currentTime);
	}
	
	public Player getPlayer() {
		return player;
	}

	public String getCooldownName() {
		return cooldownName;
	}

	public Cooldown setCooldownName(String cooldownName) {
		this.cooldownName = cooldownName;
		return this;
	}

	public int getCooldownLength() {
		return cooldownLength;
	}
	
	public long getTimeRemaining() {
		return getCurrentTime() / 1000L + getCooldownLength() 
		- System.currentTimeMillis() / 1000L;
	}

	public Cooldown setCooldownLength(int cooldownLength) {
		this.cooldownLength = cooldownLength;
		return this;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public Cooldown setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
		return this;
	}
	
	public void register() {
		acc.register(this);
	}
}
