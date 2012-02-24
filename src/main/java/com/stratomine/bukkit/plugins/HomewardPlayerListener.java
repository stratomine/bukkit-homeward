package com.stratomine.bukkit.plugins;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class HomewardPlayerListener extends org.bukkit.event.player.PlayerListener {
	
	private static final Action   HOME_ACTION = Action.RIGHT_CLICK_BLOCK;
	private static final Material HOME_BLOCK  = Material.DIAMOND_BLOCK;
	private static final Material HOME_ITEM   = Material.COMPASS;
	
	private HomewardPlugin plugin;
	
	public HomewardPlayerListener(HomewardPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if ((event.getAction() != HOME_ACTION) || (event.getClickedBlock().getType() != HOME_BLOCK) || (event.getMaterial() != HOME_ITEM)) {
			return;
		}
		
		Location target = event.getClickedBlock().getLocation();
		Player player = event.getPlayer();
		player.setCompassTarget(target);
		player.sendMessage("Your compass is attuned to this location!");
	}
	
	public HomewardPlugin getPlugin() {
		return plugin;
	}
	
}
