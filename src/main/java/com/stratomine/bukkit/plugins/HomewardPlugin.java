package com.stratomine.bukkit.plugins;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HomewardPlugin extends JavaPlugin {
	
	private HomewardPlayerListener playerListener;
	
	public void onEnable() {
		playerListener = new HomewardPlayerListener(this);
		
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
		
		info("Loaded %s", getDescription().getFullName());
	}
	
	public void onDisable() {
		// Do nothing
	}
	
	protected Logger getLogger() {
		return getServer().getLogger();
	}
	
	protected void log(Level level, String message, Object... objects) {
		message = "[" + getDescription().getName() + "] " + String.format(message, objects);
		getLogger().log(level, message);
	}
	
	protected void info(String message, Object... objects) {
		log(Level.INFO, message, objects);
	}
	
	protected void error(String message, Object... objects) {
		log(Level.SEVERE, message, objects);
	}
	
}
