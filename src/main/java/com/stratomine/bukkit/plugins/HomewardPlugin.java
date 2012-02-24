package com.stratomine.bukkit.plugins;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class HomewardPlugin extends JavaPlugin {
	
	public void onEnable() {
		log("Hello world!");
	}
	
	public void onDisable() {
		// Do nothing
	}
	
	protected Logger getLogger() {
		return getServer().getLogger();
	}
	
	public void log(Level level, String message) {
		message = "[" + getDescription().getName() + "] " + message;
		getLogger().log(level, message);
	}
	
	public void log(String message) {
		log(Level.INFO, message);
	}
	
}
