package org.CBPL.recipe.main;

import static org.CBPL.recipe.main.Util.createItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerRecipe {
	String name;
	
	public PlayerRecipe(String name) {
		this.name = name;
	}
	
	
	public static List<String> getPlayerList() {
		return Main.recipe.contains("플레이어.목록") ? Main.recipe.getStringList("플레이어.목록") : new ArrayList<>();
		
	}
	
  	public static ItemStack getRecipeListBook(String recipe) {
		ItemStack book = createItem(Material.ENCHANTED_BOOK, (byte) 0, "§e§l[ §f레시피북 : §6§l" + recipe + " §e§l]", Arrays.asList("§7클릭 시 레시피를 확인합니다."), 1);
		ItemMeta bookMeta = book.getItemMeta();
		bookMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		book.setItemMeta(bookMeta);
		return book;
	}
  	
	public Inventory getRecipeListGUI(int page) {
		Inventory inv = Bukkit.createInventory(null, 54, "§0§l레시피 목록 - " + name);
		
		ItemStack deco = createItem(Material.WOOL, (byte) 7, " ", new ArrayList<>(), 1);
		ItemStack arrow_next = createItem(Material.ARROW, (byte) 0, "§f§l다음 페이지", Arrays.asList("§7클릭 시 다음 페이지로 이동합니다.", "§7현재 페이지 : §f" + page), 1);
		ItemStack arrow_back = createItem(Material.ARROW, (byte) 0, "§f§l이전 페이지", Arrays.asList("§7클릭 시 이전 페이지로 이동합니다.", "§7현재 페이지 : §f" + page), 1);
		final List<Integer> exception = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 52, 51, 50, 49, 48, 47, 46);
		
		for (int i = 0; i < 54; i++) {
			if (!exception.contains(i)) continue;
			inv.setItem(i, deco);
		}
		
		
		List<String> list = new ArrayList<String>();
		List<String> recipes = getRecipes(name);
		
		inv.setItem(53, arrow_next);
		inv.setItem(45, arrow_back);
		
		if (recipes.isEmpty()) {
			return inv;
		}
		
 		if (recipes.size() % 36 != 0 && recipes.size() < 36*page) {
			for (int i = (page-1) * 36; i < recipes.size(); i++) {
				list.add(recipes.get(i));
			}
			
			for (int i = 0; i < list.size(); i++) {
				inv.setItem(i + 9, PlayerRecipe.getRecipeListBook(list.get(i)));
			}
			return inv;
		}
		for (int i = (page-1) * 36; i < page*36; i++) {
			list.add(recipes.get(i));
		}
		
		for (int i = 0; i < list.size(); i++) {
			inv.setItem(i + 9, PlayerRecipe.getRecipeListBook(list.get(i)));
		}
		return inv;
	}
	
	public static int changeMaterialSlot(int slot) {
		switch (slot) {
		case 0:
			return 10;
		case 1:
			return 11;
		case 2:
			return 12;
		case 3:
			return 13;
		case 4:
			return 19;
		case 5:
			return 20;
		case 6:
			return 21;
		case 7:
			return 22;
		case 8:
			return 28;
		case 9:
			return 29;
		case 10:
			return 30;
		case 11:
			return 31;
		case 12:
			return 37;
		case 13:
			return 38;
		case 14:
			return 39;
		case 15:
			return 40;
			
		}
		return 0;
	}
	
	public static int changeResultSlot(int slot, boolean indexToSlot) {
		if (indexToSlot) {
			switch (slot) {
			case 0:
				return 24;
			case 1:
				return 25;
			case 2:
				return 33;
			case 3:
				return 34;
			}
		}
		switch (slot) {
		case 24:
			return 0;
		case 25:
			return 1;
		case 33:
			return 2;
		case 34:
			return 3;
		}
		return 0;
	}
	
	public static Inventory getRecipeViewGUI(String recipeName) {
		Inventory inv = Bukkit.createInventory(null, 54, "§0§l레시피 확인 - " + recipeName);
		
		ItemStack deco = createItem(Material.WOOL, (byte) 7, " ", new ArrayList<>(), 1);
		ItemStack info = createItem(Material.SIGN, (byte) 0, "§f§l현재 선택 된 레시피: §c§lX", Arrays.asList("§7클릭 시 변경 가능"), 1);
		
		final List<Integer> exception = Arrays.asList(10, 11, 12, 13, 19, 20, 21, 22, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40);
		
		for (int i = 0; i < 54; i++) {
			if (exception.contains(i)) continue;
			inv.setItem(i, deco);
		}
		
		Recipe recipe = new Recipe(recipeName);
		List<ItemStack> material = recipe.getMaterial();
		List<ItemStack> result = recipe.getResult();
		
		for (int i = 0; i < material.size(); i++) {
			inv.setItem(changeMaterialSlot(i), material.get(i));
		}
		
		for (int i = 0; i < result.size(); i++) {
			inv.setItem(changeResultSlot(i, true), result.get(i));
		}
		
		inv.setItem(32, info);
		return inv;
	}
	
	public static List<String> getRecipes(String name) {
		return Main.recipe.contains("플레이어." + name) ? Main.recipe.getStringList("플레이어." + name) : new ArrayList<>();
	}
	
	public void addList(String recipe) {
		List<String> list = getRecipes(name);
		list.add(recipe);
		Main.recipe.set("플레이어." + name, list);
	}
	
}
