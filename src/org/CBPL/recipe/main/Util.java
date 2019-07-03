package org.CBPL.recipe.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Util {
	
	public static String prefix = "§6§l[ §e§l레시피 §6§l] §f";
	
	public static void sendMessage(CommandSender cs, String msg, boolean isPrefix) {
		cs.sendMessage(isPrefix ? prefix : "" + msg);
	}
	
	public static void sendMessage(CommandSender cs, String msg) {
		cs.sendMessage(prefix + msg);
	}
	
	public static ItemStack createItem(Material type, byte data, String display, List<String> lore, int amount) {
		ItemStack item = new ItemStack(type, amount, data);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(display);
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		return item;
	}
	
	public static int emptySlot(PlayerInventory inv) {
		int empty = 0;
		
		List<Integer> exception = new ArrayList<>();
		
		for (int i = 0; i < 36; i++) {
			exception.add(i);
		}
		
		for (int i = 0; i < inv.getSize(); i++) {
			if (!exception.contains(i)) continue;
			if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) empty++;
		}
		
		return empty;
	}
	
	public void I_Love_It() {
	}
}
