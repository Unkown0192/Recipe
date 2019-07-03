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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Recipe {
	
	private String name;
	
	public Recipe(String name) {
		this.name = name;
	}
	
	public static void save() {
		try {
			Main.recipe.save(Main.file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void remove() {
		Main.recipe.set(name, null);
		save();
		removeList();
	}
	
	public static List<String> getRecipeList() {
		List<String> list = new ArrayList<>();
		list = Main.recipe.getStringList("목록");
		return list;
	}
	
	public void setList(List<String> recipeList) {
		Main.recipe.set("목록", recipeList);
		save();
	}
	
	public void addList() {
		List<String> list = getRecipeList();
		list.add(name);
		setList(list);
	}
	
	public void removeList() {
		List<String> list = getRecipeList();
		list.remove(name);
		setList(list);
	}
	
  	public ItemStack getRecipeBook() {
		ItemStack book = createItem(Material.BOOK, (byte) 0, "§e§l[ §f레시피북 : §6§l" + name + " §e§l]", Arrays.asList("§7우클릭 시 레시피를 습득합니다."), 1);
		ItemMeta bookMeta = book.getItemMeta();
		bookMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		bookMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		book.setItemMeta(bookMeta);
		return book;
	}
	
  	public static boolean isEmpty(List<ItemStack> list) {
  		boolean check = true;
  		
  		for (ItemStack item : list) {
  			if (!item.getType().equals(Material.AIR)) {
  				check = false;
  				break;
  			}
  		}
  		return check;
  	}
  	
	public List<ItemStack> getMaterial() { //재료
		return (List<ItemStack>) Main.recipe.getList(name + ".조합법");
	}
	
	public static List<ItemStack> getMaterialFromInventory(Inventory inv) { //레시피 조합대에서 재료칸에 있는 아이템 전부 가져오기
		List<ItemStack> material = new ArrayList<>();
		
		final List<Integer> exception = Arrays.asList(10, 11, 12, 13, 19, 20, 21, 22, 28, 29, 30, 31, 37, 38, 39, 40);
		
		for (int i : exception) {
			if (inv.getItem(i) == null) {
				material.add(new ItemStack(Material.AIR));
				continue;
			}
			material.add(inv.getItem(i));
		}
		return material;
	}
	
	public void setMaterial(List<ItemStack> material) {
		Main.recipe.set(name + ".조합법", material);
		save();
	}
	
	public List<ItemStack> getResult() {
		return (List<ItemStack>) Main.recipe.getList(name + ".결과");
	}
	
	public static List<ItemStack> getResultFromInventory(Inventory inv) {
		List<ItemStack> result = new ArrayList<>();
		
		final List<Integer> exception = Arrays.asList(24, 25, 33, 34);
		
		for (int i : exception) {
			if (inv.getItem(i) == null) {
				result.add(new ItemStack(Material.AIR));
				continue;
			}
			result.add(inv.getItem(i));
		}
		return result;
	}
	
	public void setResult(List<ItemStack> result) {
		Main.recipe.set(name + ".결과", result);
		save();
	}
	
	public static Inventory getRecipeGUI(String string) {
		Inventory inv = Bukkit.createInventory(null, 54, "§0§l레시피 조합대");
		
		ItemStack deco = createItem(Material.STAINED_GLASS_PANE, (byte) 7, " ", new ArrayList<>(), 1);
		ItemStack info = createItem(Material.SIGN, (byte) 0, "§f§l현재 선택 된 레시피: §6§l" + (PlayerRecipe.getRecipes(string).isEmpty() ? "§c§lX" : PlayerRecipe.getRecipes(string).get(0)), Arrays.asList("§7R : 다음 레시피", "§7Shift + R : 다음 레시피(5개씩 이동)", "§7L : 이전 레시피", "§7Shift + L : 이전 레시피(5개씩 이동)"), 1);
		
		final List<Integer> exception = Arrays.asList(10, 11, 12, 13, 19, 20, 21, 22, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40);
		
		for (int i = 0; i < 54; i++) {
			if (exception.contains(i)) continue;
			inv.setItem(i, deco);
		}
		
		inv.setItem(32, info);
		return inv;
	}
	
	public Inventory getSettingGUI() {
		Inventory inv = Bukkit.createInventory(null, 54, "§0§l레시피 생성 - " + name);
		
		ItemStack deco = createItem(Material.STAINED_GLASS_PANE, (byte) 7, " ", new ArrayList<>(), 1);
		ItemStack info = createItem(Material.SIGN, (byte) 0, "§f§l현재 선택 된 레시피: §c§lX", Arrays.asList("§7클릭 시 변경 가능"), 1);
		ItemStack mode = createItem(Material.BOOK_AND_QUILL, (byte) 0, "§6§l현재 레시피 생성 모드입니다.", Arrays.asList("§7클릭 시 레시피 생성을 완료합니다."), 1);
		
		final List<Integer> exception = Arrays.asList(8, 10, 11, 12, 13, 19, 20, 21, 22, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40);
		
		for (int i = 0; i < 54; i++) {
			if (exception.contains(i)) continue;
			inv.setItem(i, deco);
		}
		
		inv.setItem(8, mode);
		inv.setItem(32, info);
		return inv;
	}
	
	
	public static boolean clickAble(int slot) {
		final List<Integer> exception = Arrays.asList(10, 11, 12, 13, 19, 20, 21, 22, 24, 25, 28, 29, 30, 31, 33, 34, 37, 38, 39, 40, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89);
		
		return exception.contains(slot);
	}
	
	public static boolean isEqualsRecipe(List<ItemStack> list1, List<ItemStack> list2) {
		for (int i = 0; i < list1.size(); i++) {
			if (!list1.get(i).equals(list2.get(i))) {
				return false;
			}
		}
		return true;
	}
}
