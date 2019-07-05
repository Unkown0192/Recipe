package org.CBPL.recipe.main;

import static org.CBPL.recipe.main.Util.createItem;
import static org.CBPL.recipe.main.Util.sendMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Listeners implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		String playerName = p.getName();
		Inventory inv = e.getInventory();
		String title = inv.getTitle().replace("§8§l", "");
		
		if (title.contains("레시피 생성")) {
			if (!Recipe.clickAble(e.getRawSlot())) e.setCancelled(true);
			
			if (e.getRawSlot() == 8) {
				List<ItemStack> material = Recipe.getMaterialFromInventory(inv);
				List<ItemStack> result = Recipe.getResultFromInventory(inv);
				
				if (Recipe.isEmpty(material) || Recipe.isEmpty(result)) {
					p.closeInventory();
					sendMessage(p, "재료나 결과가 설정되지 않아서 레시피 생성이 취소되었습니다.");
					return;
				}
				
				String recipeName = title.replace("레시피 생성 - ", "").replace("§0§l", "");
				Recipe recipe = new Recipe(recipeName);
				recipe.setMaterial(material);
				recipe.setResult(result);
				recipe.addList();
				Recipe.save();
				p.closeInventory();
				sendMessage(p, "성공적으로 레시피가 생성되었습니다. : §6§l" + recipeName);
				return;
			}
			return;
		}
		
		if (title.contains("레시피 조합대")) {
			if (e.getRawSlot() == 32) {
				e.setCancelled(true);
				ItemStack sign = e.getCurrentItem();
				List<String> recipes = PlayerRecipe.getRecipes(playerName);
				
				
				int max = recipes.size() - 1;
				String recipe = sign.getItemMeta().getDisplayName().contains("X") ? sign.getItemMeta().getDisplayName().replace("§f§l현재 선택 된 레시피: §6§l§c§l", "") : sign.getItemMeta().getDisplayName().replace("§f§l현재 선택 된 레시피: §6§l", "");
				if (recipe.equals("X")) return;
				int now = recipe.equals("X") ? 0 : recipes.indexOf(recipe);
				
				ItemStack newSign = createItem(Material.SIGN, (byte) 0, "§f§l현재 선택 된 레시피: §6§l" + recipes.get(0), Arrays.asList("§7R : 다음 레시피", "§7Shift + R : 다음 레시피(5개씩 이동)", "§7L : 이전 레시피", "§7Shift + L : 이전 레시피(5개씩 이동)"), 1);
				
				ClickType c = e.getClick();
				if (c.equals(ClickType.RIGHT)) { //좌클 (다음)
					if (now == max) return; 
					ItemMeta newMeta= newSign.getItemMeta();
					newMeta.setDisplayName("§f§l현재 선택 된 레시피: §6§l" + recipes.get(now + 1));
					newSign.setItemMeta(newMeta);
					
					inv.setItem(32, newSign);
					return;
				}
				
				if (c.equals(ClickType.SHIFT_RIGHT)) { //Shift + 좌클(5개 이동)
					Bukkit.broadcastMessage(max + " " + now + " " + (max-now)/5);
					if ((max-now)/5 < 1) return;
					//newSign.getItemMeta().setDisplayName("§f§l현재 선택 된 레시피: §6§l" + recipes.get(now + 5));
					
					ItemMeta newMeta= newSign.getItemMeta();
					newMeta.setDisplayName("§f§l현재 선택 된 레시피: §6§l" + recipes.get(now + 5));
					newSign.setItemMeta(newMeta);
					
					inv.setItem(32, newSign);
					return;
				}
				
				if (c.equals(ClickType.LEFT)) { //우클 (이전)
					if (now == 0) return;
					//newSign.getItemMeta().setDisplayName("§f§l현재 선택 된 레시피: §6§l" + recipes.get(now - 1));
					
					ItemMeta newMeta= newSign.getItemMeta();
					newMeta.setDisplayName("§f§l현재 선택 된 레시피: §6§l" + recipes.get(now - 1));
					newSign.setItemMeta(newMeta);
					
					inv.setItem(32, newSign);
					return;
				}
				
				if (c.equals(ClickType.SHIFT_LEFT)) { //Shift + 좌클(5개 이동)
					if ((now - 5) < 0) return;
					//newSign.getItemMeta().setDisplayName("§f§l현재 선택 된 레시피: §6§l" + recipes.get(now - 5));
					
					ItemMeta newMeta= newSign.getItemMeta();
					newMeta.setDisplayName("§f§l현재 선택 된 레시피: §6§l" + recipes.get(now - 5));
					newSign.setItemMeta(newMeta);
					
					inv.setItem(32, newSign);
					return;
				}
				return;
			}
			
			
			final List<ItemStack> empty = Arrays.asList(new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR));
			
			if (Recipe.materialSlot.contains(e.getRawSlot()) || Recipe.resultSlot.contains(e.getRawSlot()) || e.getRawSlot() == 32) {
				
				List<ItemStack> result_ = Recipe.getResultFromInventory(inv);
				
				if (!Recipe.isEqualsRecipe(empty, result_)) return;
				//조합 구현
				ItemStack info = inv.getItem(32);
				String recipe = info.getItemMeta().getDisplayName().contains("X") ? info.getItemMeta().getDisplayName().replace("§f§l현재 선택 된 레시피: §6§l§c§l", "") : info.getItemMeta().getDisplayName().replace("§f§l현재 선택 된 레시피: §6§l", "");
				
				if (recipe.equals("X")) return;
				Recipe r = new Recipe(recipe);
				List<ItemStack> material = r.getMaterial();
				List<ItemStack> result = r.getResult();
				
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, () -> {
	                List<ItemStack> nowMaterial = Recipe.getMaterialFromInventory(inv);
	                
					if (Recipe.isEqualsRecipe(material, nowMaterial)) {
						for (int i : Recipe.materialSlot) 
							inv.setItem(i, new ItemStack(Material.AIR));
						for (int i : Recipe.resultSlot) 
							inv.setItem(i, result.get(PlayerRecipe.changeResultSlot(i, false)));
						
					}
					return; 
	            }, 1L);
				
			}
			if (!Recipe.clickAble(e.getRawSlot())) 
				e.setCancelled(true);
			
		}
		
		if (title.contains("레시피 목록")) {
			e.setCancelled(true);
			PlayerRecipe pr = new PlayerRecipe(playerName);
			
			List<String> recipes = PlayerRecipe.getRecipes(playerName);
			
			for (int i = 0; i < recipes.size(); i++) 
				if (!Main.recipe.contains(recipes.get(i)))
					recipes.remove(i);
			
			
			int nowPage = Integer.parseInt(inv.getItem(45).getItemMeta().getLore().get(1).replace("§7현재 페이지 : §f", ""));
			int maxPage = (recipes.size() / 36) + ((recipes.size() % 36 == 0) ? 0 : 1); 
					
			if (e.getRawSlot() == 45) { //이전 페이지
				if (nowPage==1) return;
				
				p.closeInventory();
				p.openInventory(pr.getRecipeListGUI(nowPage-1));
				
				return;
			}
			if (e.getRawSlot() == 53) { //다음 페이지
				if (nowPage == maxPage || maxPage == 0) return;
				
				p.closeInventory();
				p.openInventory(pr.getRecipeListGUI(nowPage+1));
				
				return;
			}
			if (e.getCurrentItem().getType().equals(Material.ENCHANTED_BOOK)) {
				String recipe = e.getCurrentItem().getItemMeta().getDisplayName().replace("§e§l[ §f레시피북 : §6§l", "").replace(" §e§l]", "");
				p.closeInventory();
				p.openInventory(PlayerRecipe.getRecipeViewGUI(recipe));
				return;
			}
		}
		
		if (title.contains("레시피 확인")) 
			e.setCancelled(true);
		
	}
	
	@EventHandler
	public void onItemClick(PlayerInteractEvent e) { //레시피북 사용
		Player p = e.getPlayer();
		String playerName = p.getName();
		ItemStack item = e.getItem();
		Action a = e.getAction();
		
		if (item != null && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null) {
			if (item.getType().equals(Material.ENCHANTED_BOOK) && item.getItemMeta().getDisplayName().contains("§e§l[ §f레시피북 ")) {
				if (a.equals(Action.RIGHT_CLICK_AIR) || a.equals(Action.RIGHT_CLICK_BLOCK)) {
					PlayerRecipe pr = new PlayerRecipe(playerName);
					List<String> recipes = PlayerRecipe.getRecipes(playerName);
					
					String recipe = item.getItemMeta().getDisplayName().replace("§e§l[ §f레시피북 : §6§l", "").replace(" §e§l]", "");
					
					if (!Main.recipe.contains(recipe)) return;
					if (recipes.contains(recipe)) return;
					
					if (!PlayerRecipe.getPlayerList().contains(playerName)) {
						List<String> playerList = Main.recipe.contains("플레이어.목록") ? PlayerRecipe.getPlayerList() : new ArrayList<>();
						playerList.add(playerName);
						Main.recipe.set("플레이어.목록", playerList);
					}
					
					ItemStack item_ = p.getItemInHand();
					item_.setAmount(item.getAmount()-1);
					
					p.setItemInHand(item_);
					pr.addList(item.getItemMeta().getDisplayName().replace("§e§l[ §f레시피북 : §6§l", "").replace(" §e§l]", ""));
					Recipe.save();
						
					sendMessage(p, "레시피를 습득하였습니다. : §6§l" + item.getItemMeta().getDisplayName().replace("§e§l[ §f레시피북 : §6§l", "").replace(" §e§l]", ""));
					p.playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
				}
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		Inventory inv = e.getInventory();
		String title = inv.getTitle();
		
		if (title.contains("레시피 조합대")) {
			List<ItemStack> material = Recipe.getMaterialFromInventory(inv);
			List<ItemStack> result = Recipe.getResultFromInventory(inv);
			
			if (!Recipe.isEmpty(material)) 
				for (ItemStack item : material) {
					if (item.getType().equals(Material.AIR)) continue;
					p.getWorld().dropItem(p.getLocation(), item);
				}
			
			
			if (!Recipe.isEmpty(result))  
				for (ItemStack item : result) {
					if (item.getType().equals(Material.AIR)) continue;
					p.getWorld().dropItem(p.getLocation(), item);
				}
			
		}
	}
}
