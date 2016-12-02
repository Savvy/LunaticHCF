package us.timberdnd.lunatic.actions.action;

import org.bukkit.entity.Player;

import us.timberdnd.lunatic.actions.Actions;

public class InvSeeAction implements Actions {
    
    String name;
    
    public InvSeeAction(String name) {
	this.name = name;
    }
    @Override
    public String getName() {
	return name;
    }

    @Override
    public void callAction(Player player) {
	
    }
	@Override
	public void callAction(Player player, Player target) {
		player.closeInventory();
		player.openInventory(target.getInventory());
	}
}