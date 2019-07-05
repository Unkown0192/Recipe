package org.CBPL.recipe.main;

import static org.CBPL.recipe.main.Util.emptySlot;
import static org.CBPL.recipe.main.Util.sendMessage;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor {
	
	public boolean onCommand(CommandSender cs, Command cmd, String l, String[] args) {
		
		if (l.equals("레시피")) {
			if (!(cs instanceof Player)) {
				Bukkit.getConsoleSender().sendMessage("§e[ §fRecipe §e] §f버킷에서는 명령어 사용이 불가능합니다.");
				return false;
			}
			
			Player p = (Player) cs;
			
			if (args.length == 0) { // /레시피
				p.openInventory(Recipe.getRecipeGUI(p.getName()));
				return false;
			}
			
			if (args[0].equals("도움말")) { // /레시피 도움말
				if (args.length != 1) {
					sendMessage(p, "/레시피 도움말");
					return false;
				}
				
				sendMessage(p, "§e§l━━━━━━━━━━━━━━[ §6§lRecipe v1.0 §e§l]━━━━━━━━━━━━━━", false);
				sendMessage(p, "", false);
				sendMessage(p, "§6/§e레시피 도움말 §6- §f플러그인 사용법을 확인합니다.", false);
				sendMessage(p, "", false);
				sendMessage(p, "§6/§e레시피 생성 §6<§e레시피 이름§6> - §f레시피를 생성합니다. (GUI로 설정)", false);
				sendMessage(p, "§6/§e레시피 삭제 §6<§e레시피 이름§6> - §f레시피를 삭제합니다.", false);
				sendMessage(p, "§6/§e레시피 생성목록 §6 - §f생성 된 레시피를 확인합니다.", false);
				sendMessage(p, "§6/§e레시피 지급 §6<§e레시피 이름§6> <§e닉네임§6> - §f레시피북을 플레이어에게 지급합니다.", false);
				sendMessage(p, "", false);
				sendMessage(p, "§6/§e레시피 §6- §f레시피를 조합할 수 있는 조합대를 오픈합니다. (GUI 형태로 열림)", false);
				sendMessage(p, "§6/§e레시피 목록 §6- §f습득한 레시피 목록을 확인합니다. (GUI 형태로 열림)", false);
				sendMessage(p, "", false);
				sendMessage(p, "§e§l━━━━━━━━━━━━━━[ §6§lRecipe v1.0 §e§l]━━━━━━━━━━━━━━", false);
				return false;
			}
		
			if (args[0].equals("생성")) { // /레시피 생성 <레시피 이름>
				if (args.length != 2) {
					sendMessage(p, "/레시피 생성 <레시피 이름>");
					return false;
				}
				
				if (Recipe.getRecipeList().contains(args[1])) {
					sendMessage(p, "이미 동일한 이름의 레시피가 존재합니다.");
					return false;
				}
				
				Recipe recipe = new Recipe(args[1]);
				
				p.openInventory(recipe.getSettingGUI());
				return false;
			}
			
			if (args[0].equals("삭제")) {
				if (args.length != 2) {
					sendMessage(p, "/레시피 삭제 <레시피 이름>");
					return false;
				}
				
				if (!Recipe.getRecipeList().contains(args[1])) {
					sendMessage(p, "존재하지 않는 레시피 이름입니다.");
					return false;
				}
				
				for (String name : PlayerRecipe.getPlayerList()) {
					List<String> list = PlayerRecipe.getRecipes(name);
					if (!list.contains(args[1])) continue;
					list.remove(args[1]);
					Main.recipe.set("플레이어." + name, list);
				}
				
				Recipe.save();
				
				Recipe recipe = new Recipe(args[1]);
				recipe.remove();
				sendMessage(p, "성공적으로 레시피가 삭제되었습니다 : §6§l" + args[1]);
				return false;
			}
			
			if (args[0].equals("생성목록")) {
				if (args.length != 1) {
					sendMessage(p, "/레시피 생성목록");
					return false;
				}
				
				List<String> list = Main.recipe.contains("목록") ? Recipe.getRecipeList() : new ArrayList<>();
				sendMessage(p, "생성 된 레시피 §6§l: §f" + list.toString());
				return false;
			}
			
			if (args[0].equals("지급")) {
				if (args.length != 3) {
					sendMessage(p, "/레시피 지급 <레시피 이름> <닉네임>");
					return false;
				}
				
				if (!Recipe.getRecipeList().contains(args[1])) {
					sendMessage(p, "존재하지 않는 레시피 이름입니다.");
					return false;
				}
				
				if (Bukkit.getPlayer(args[2]) == null) {
					sendMessage(p, "플레이어가 존재하지 않거나 오프라인 상태입니다.");
					return false;
				}
				
				
				Player target = Bukkit.getPlayer(args[2]);
				
				if (emptySlot(target.getInventory()) == 0) {
					sendMessage(p, "해당 플레이어의 인벤토리가 비어있지 않습니다.");
					return false;
				}
				
				Recipe recipe = new Recipe(args[1]);
				ItemStack book = recipe.getRecipeBook();
				
				target.getInventory().addItem(book);
				sendMessage(p, "성공적으로 " + args[2] + "에게 레시피북을 지급했습니다.");
				return false;
			}
			
			if (args[0].equals("목록")) {
				if (args.length != 1) {
					sendMessage(p, "/레시피 목록");
					return false;
				}
				
				PlayerRecipe pr = new PlayerRecipe(p.getName());
				
				if (PlayerRecipe.getRecipes(p.getName()).equals(new ArrayList<>())) {
					sendMessage(p, "습득한 레시피가 없습니다.");
					return false;
				}
				p.openInventory(pr.getRecipeListGUI(1));
				return false;
			}
			
			sendMessage(p, "올바르지 않은 명령어 사용법입니다. /레시피 도움말");
			return false;
		}
		return false; 
	}
}
