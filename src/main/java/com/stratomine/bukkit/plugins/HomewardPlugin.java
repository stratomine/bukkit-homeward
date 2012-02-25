package com.stratomine.bukkit.plugins;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HomewardPlugin extends JavaPlugin {
	
	private File compassesFile;
	private HashMap<String,HashMap<World,Location>> compasses = new HashMap<String,HashMap<World,Location>>();
	private HomewardPlayerListener playerListener;
	
	public void onEnable() {
		compassesFile = new File(getDataFolder(), "compasses.yml");
		playerListener = new HomewardPlayerListener(this);
		
		loadCompasses();
		
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		manager.registerEvent(Event.Type.PLAYER_CHANGED_WORLD, playerListener, Event.Priority.Normal, this);
		manager.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
		
		info("Loaded %s", getDescription().getFullName());
	}
	
	public void onDisable() {
		saveCompasses();
	}
	
	private void loadCompasses() {
		if (!getCompassesFile().canRead()) {
			info("Cannot read %s", getCompassesFile().getPath());
			return;
		}
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getCompassesFile());
		for (String playerName : config.getKeys(false)) {
			ConfigurationSection playerConfig = config.getConfigurationSection(playerName);
			for (String worldName : playerConfig.getKeys(false)) {
				ConfigurationSection worldConfig = playerConfig.getConfigurationSection(worldName);
				World world = getServer().getWorld(worldName);
				double x = worldConfig.getDouble("x");
				double y = worldConfig.getDouble("y");
				double z = worldConfig.getDouble("z");
				Location location = new Location(world, x, y, z);
				setPlayerCompass(playerName, location);
			}
		}
	}
	
	private void saveCompasses() {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getCompassesFile());
		
		for (Map.Entry<String,HashMap<World,Location>> player : getCompasses().entrySet()) {
			String playerName = player.getKey();
			for (Map.Entry<World,Location> compass : player.getValue().entrySet()) {
				String worldName = compass.getKey().getName();
				Location location = compass.getValue();
				String path = playerName + "." + worldName;
				config.set(path + ".x", location.getX());
				config.set(path + ".y", location.getY());
				config.set(path + ".z", location.getZ());
			}
		}
		
		try {
			if (!getDataFolder().exists()) {
				getDataFolder().mkdir();
			}
			config.save(getCompassesFile());
		} catch (IOException e) {
			error("Could not save %s: %s", getCompassesFile().getPath(), e.getMessage());
		}
	}
	
	protected void updatePlayerCompass(Player player) {
		Location location = getPlayerCompass(player);
		if (location != null) {
			player.setCompassTarget(location);
		}
	}
	
	protected void setPlayerCompass(Player player, Location location) {
		player.setCompassTarget(location);
		player.sendMessage("Your compass is now attuned to this location!");
		setPlayerCompass(player.getName(), location);
	}
	
	private void setPlayerCompass(String playerName, Location location) {
		HashMap<World,Location> compasses = getPlayerCompasses(playerName);
		compasses.put(location.getWorld(), location);
		info("Set %s' compass to %s", playerName, location.toString());
	}
	
	private Location getPlayerCompass(Player player) {
		return getPlayerCompass(player.getName(), player.getWorld());
	}
	
	private Location getPlayerCompass(String playerName, World world) {
		return getPlayerCompasses(playerName).get(world);
	}
	
	private HashMap<World,Location> getPlayerCompasses(String playerName) {
		if (!getCompasses().containsKey(playerName)) {
			HashMap<World,Location> hash = new HashMap<World,Location>();
			getCompasses().put(playerName, hash);
		}
		
		return getCompasses().get(playerName);
	}
	
	private HashMap<String,HashMap<World,Location>> getCompasses() {
		return compasses;
	}
	
	private File getCompassesFile() {
		return compassesFile;
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
