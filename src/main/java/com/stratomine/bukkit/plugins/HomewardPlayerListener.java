package com.stratomine.bukkit.plugins;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class HomewardPlayerListener extends org.bukkit.event.player.PlayerListener {
	
	private static final Action   HOME_ACTION = Action.RIGHT_CLICK_BLOCK;
	private static final Material HOME_BLOCK  = Material.DIAMOND_BLOCK;
	private static final Material HOME_ITEM   = Material.COMPASS;
	
	private HomewardPlugin plugin;
	
	public HomewardPlayerListener(HomewardPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		getPlugin().updatePlayerCompass(event.getPlayer());
	}

	@Override
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		getPlugin().updatePlayerCompass(event.getPlayer());
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if ((event.getAction() != HOME_ACTION) || (event.getClickedBlock().getType() != HOME_BLOCK) || (event.getMaterial() != HOME_ITEM)) {
			return;
		}

		getPlugin().setPlayerCompass(event.getPlayer(), event.getClickedBlock().getLocation());
	}
	
	public HomewardPlugin getPlugin() {
		return plugin;
	}
	
}
