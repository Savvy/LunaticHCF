package us.timberdnd.lunatic.actions;

import org.bukkit.entity.Player;

public abstract interface Actions {
    
    public String getName();
    
    public void callAction(Player player);
    
    public void callAction(Player player, Player target);
    
}
