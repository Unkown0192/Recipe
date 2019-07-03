package org.CBPL.recipe.main;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public static FileConfiguration cf;
	public static Main instance;
	
	static File file = new File("plugins\\Recipe\\Recipe.yml");
	static YamlConfiguration recipe;
	
	@Override
	public void onEnable() {
		cf = getConfig();
		instance = this;
		 
	    this.saveResource("Recipe" + ".yml", false);
		recipe = YamlConfiguration.loadConfiguration(file);
			
		setCommand(new Commands(), "레시피");
		registerEvent(new Listeners());
		
		saveDefaultConfig();
		
		Bukkit.getConsoleSender().sendMessage("§e[ §fRecipe v1.0 §e] §f플러그인 활성화 / §aON");
	}
	
	@Override
	public void onDisable() {
		saveConfig();
		Bukkit.getConsoleSender().sendMessage("§e[ §fRecipe v1.0 §e] §f플러그인 비활성화 / §cOFF");
	}
	
	void registerEvent(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, this);
	}
	
	void setCommand(CommandExecutor commandExecutor, String cmd) {
		getCommand(cmd).setExecutor(commandExecutor);
	}
}
