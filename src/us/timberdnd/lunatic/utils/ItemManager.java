package us.timberdnd.lunatic.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.timberdnd.lunatic.Methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager extends Methods implements Listener {

    private static boolean listener = false;
    private static final Map<String, PotionEffect> effects = new HashMap<String, PotionEffect>();

    private ItemStack is;

    public ItemManager(final Material mat) {
	is = new ItemStack(mat);
    }

    public ItemManager(final ItemStack is) {
	this.is = is;
    }

    public ItemManager addItemFlags(ItemFlag... itemf) {
	is.getItemMeta().addItemFlags(itemf);
	return this;
    }

    public ItemManager amount(final int amount) {
	is.setAmount(amount);
	return this;
    }

    public String getName() {
	return is.getItemMeta().getDisplayName();
    }
    
    public Material getType() {
	return is.getType();
    }
    
    public ItemMeta getItemMeta() {
	return is.getItemMeta();
    }
    
    public ItemManager setItemMeta(ItemMeta meta) {
	is.setItemMeta(meta);
	return this;
    }

    public ItemManager name(final String name) {
	final ItemMeta meta = is.getItemMeta();
	meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
	is.setItemMeta(meta);
	return this;
    }

    public ItemManager lore(final String name) {
	final ItemMeta meta = is.getItemMeta();
	List<String> lore = meta.getLore();
	if (lore == null) {
	    lore = new ArrayList<String>();
	}
	lore.add(ChatColor.translateAlternateColorCodes('&', name));
	meta.setLore(lore);
	is.setItemMeta(meta);
	return this;
    }

    public ItemManager durability(final int durability) {
	is.setDurability((short) durability);
	return this;
    }
    
    public Short getDurability() {
	return is.getDurability();
    }

    @SuppressWarnings("deprecation")
    public ItemManager data(final int data) {
	is.setDurability((short) data);
	is.setData(new MaterialData(is.getType(), (byte) data));
	return this;
    }

    public ItemManager addEnchantment(Enchantment enchantment, int arg, boolean arg2) {
	is.getItemMeta().addEnchant(enchantment, arg, arg2);
	return this;
    }

    public ItemManager enchantment(final Enchantment enchantment, final int level) {
	is.addUnsafeEnchantment(enchantment, level);
	return this;
    }

    public ItemManager enchantment(final Enchantment enchantment) {
	is.addUnsafeEnchantment(enchantment, 1);
	return this;
    }
    
    public ItemManager removeEnchantment(Enchantment enchantment) {
	is.removeEnchantment(enchantment);
	return this;
    }

    public ItemManager type(final Material material) {
	is.setType(material);
	return this;
    }

    public ItemManager clearLore() {
	final ItemMeta meta = is.getItemMeta();
	meta.setLore(new ArrayList<String>());
	is.setItemMeta(meta);
	return this;
    }

    public ItemManager clearEnchantments() {
	for (final Enchantment e : is.getEnchantments().keySet()) {
	    is.removeEnchantment(e);
	}
	return this;
    }

    public ItemManager color(Color color) {
	if ((is.getType() == Material.LEATHER_BOOTS) || (is.getType() == Material.LEATHER_CHESTPLATE) || (is.getType() == Material.LEATHER_HELMET) || (is.getType() == Material.LEATHER_LEGGINGS)) {
	    LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
	    meta.setColor(color);
	    is.setItemMeta(meta);
	    return this;
	} else {
	    throw new IllegalArgumentException("color() only applicable for leather armor!");
	}
    }

    public ItemManager effect(PotionEffectType type, int duration, int amplifier, boolean ambient) {
	effect(new PotionEffect(type, duration, amplifier, ambient));
	return this;
    }

    public ItemManager effect(PotionEffect effect) {
	if (!listener) {
	    Bukkit.getPluginManager().registerEvents(this, getInstance());
	    listener = true;
	}
	String name = is.getItemMeta().getDisplayName();
	while (effects.containsKey(name)) {
	    name = name + "#";
	}
	effects.put(name, effect);
	return this;
    }

    public ItemManager effect(PotionEffectType type, int duration, int amplifier) {
	effect(new PotionEffect(type, duration == -1 ? 1000000 : duration, amplifier));
	return this;
    }

    public ItemManager effect(PotionEffectType type, int duration) {
	effect(new PotionEffect(type, duration == -1 ? 1000000 : duration, 1));
	return this;
    }

    public ItemStack build() {
	return is;
    }
}