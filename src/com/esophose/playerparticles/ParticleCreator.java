/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.esophose.playerparticles.libraries.particles.ParticleEffect;
import com.esophose.playerparticles.libraries.particles.ParticleEffect.ParticleType;
import com.esophose.playerparticles.manager.ConfigManager;
import com.esophose.playerparticles.manager.PermissionManager;

public class ParticleCreator extends BukkitRunnable implements Listener {
	
	/**
	 * The map containing all the effects for players
	 */
	private static HashMap<String, ParticleType> map = new HashMap<String, ParticleType>();
	/**
	 * The map containing all the styles for players
	 */
	private static HashMap<String, ParticleStyle> styleMap = new HashMap<String, ParticleStyle>();
	
	/**
	 * The timing system used for the styles HALO and SPIRAL
	 */
	private double step = 0;
	
	/**
	 * The timing system used for the styles QUAD_HELIX and ORB
	 */
	private double helixStep = 0;
	private double helixYStep = 0;
	private boolean reverse = false;
	
	/**
	 * Used to check for the database timing out
	 */
	private double mysqltimer = 0;
	
	/**
	 * First checks if the player is in the database (if it is enabled), if they are not then add them to the database
	 * Checks to see if that player has any effects or styles saved in either the database or config
	 * If so add the values to the map and/or styleMap
	 * 
	 * Problematically clears the map and style map every time a player joins and refills the values
	 * Why does it do this?
	 * Figure out why or remove updateMap() and updateStyleMap()
	 * 
	 * @param e The event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		if(PlayerParticles.useMySQL) {
			Statement s = null;
			Statement statement = null;
			ResultSet res = null;
			try {
				s = PlayerParticles.c.createStatement();
				res = s.executeQuery("SELECT * FROM playerparticles WHERE player_name = '" + e.getPlayer().getName() + "';");
				if(!res.next()) {
					statement = PlayerParticles.c.createStatement();
					statement.executeUpdate("INSERT INTO playerparticles SET player_name = '" + e.getPlayer().getName() + "', particle = NULL, style = 'none';");
					PlayerParticles.getPlugin().getLogger().info("[PlayerParticles] New player added to database: " + e.getPlayer().getName());
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			} finally {
				if(s != null) try { s.close(); } catch (SQLException e1) { e1.printStackTrace(); }
				if(statement != null) try { statement.close(); } catch (SQLException e1) { e1.printStackTrace(); }
				if(res != null) try { res.close(); } catch (SQLException e1) { e1.printStackTrace(); }
			}
		}
		if(ConfigManager.getInstance().getParticle(e.getPlayer()) == null) return;
		map.put(e.getPlayer().getName(), ConfigManager.getInstance().getParticle(e.getPlayer()));
		styleMap.put(e.getPlayer().getName(), ConfigManager.getStyleInstance().getStyle(e.getPlayer()));
		updateMap();
		updateStyleMap();
	}
	
	/**
	 * Removes the player from the map and styleMap if they have any values in them
	 * Prevents spawning particles at a null location
	 * 
	 * @param e The event
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		if(map.containsKey(e.getPlayer().getName())){
			map.remove(e.getPlayer().getName());
		}
		if(styleMap.containsKey(e.getPlayer().getName())) {
			styleMap.remove(e.getPlayer().getName());
		}
	}
	
	/**
	 * A somewhat costly solution to updating the MOVE style and displaying the appropriate particles
	 * 
	 * @param e The event
	 */
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if(map.containsKey(e.getPlayer().getName()) && styleMap.get(e.getPlayer().getName()) == ParticleStyle.MOVE) {
			if(PermissionManager.hasStylePermission(e.getPlayer(), ParticleStyle.MOVE)) {
				Location loc = e.getPlayer().getLocation();
				loc.setY(loc.getY() + 1);
				handleStyleNone(map.get(e.getPlayer().getName()), loc);
			}
		}
	}
	
	/**
	 * Adds the player with the given effect to the map
	 * 
	 * @param player The player to add the effect to
	 * @param effect The effect
	 */
	public static void addMap(Player player, ParticleType effect){
		map.remove(player.getName());
		map.put(player.getName(), effect);
	}
	
	/**
	 * Removes the player from the map
	 * 
	 * @param player The player to remove
	 */
	public static void removeMap(Player player){
		map.remove(player.getName());
	}
	
	/**
	 * Clears the map then adds everybody on the server if they have effects saved
	 * Used for when the server reloads and we can't rely on players rejoining
	 */
	public static void updateMap(){
		map.clear();
		for(Player player : Bukkit.getOnlinePlayers()){
			if(ConfigManager.getInstance().getParticle(player) == null) continue;
			map.put(player.getName(), ConfigManager.getInstance().getParticle(player));
		}
	}
	
	/**
	 * Adds the player with the given style to the styleMap
	 * 
	 * @param player The player to add the style to
	 * @param style The style
	 */
	public static void addStyleMap(Player player, ParticleStyle style) {
		styleMap.remove(player.getName());
		styleMap.put(player.getName(), style);
	}
	
	/**
	 * Removes the player from the styleMap
	 * 
	 * @param player The player to remove
	 */
	public static void removeStyleMap(Player player){
		styleMap.remove(player.getName());
	}
	
	/**
	 * Clears the styleMap then adds everybody on the server if they have effects saved
	 * Used for when the server reloads and we can't rely on the players rejoining
	 */
	public static void updateStyleMap(){
		styleMap.clear();
		for(Player player : Bukkit.getOnlinePlayers()){
			styleMap.put(player.getName(), ConfigManager.getStyleInstance().getStyle(player));
		}
	}
	
	/**
	 * Gets a particle type from a string, used for getting ParticleType's from the saved data
	 * 
	 * @param particle The name of the particle to check for
	 * @return The ParticleType with the given name, will be null if name was not found
	 */
	public static ParticleType particleFromString(String particle) {
		for(ParticleType effect : ParticleType.values()){
			if(effect.toString().toLowerCase().replace("_", "").equals(particle)) return effect;
		}
		return null;
	}

	/**
	 * The main loop to display all the particles
	 * Updates all the timing variables
	 * Refreshes the database connection if it is enabled and it has been 30 seconds since last refresh
	 * Displays the particles for all players on the server
	 */
	public void run() {
		step++;
		if(step > 30) {
			step = 0;
		}
		helixStep++;
		if(helixStep > 90) {
			helixStep = 0;
		}
		if(reverse) {
			helixYStep++;
			if(helixYStep > 60) reverse = false;
		}else{
			helixYStep--;
			if(helixYStep < -60) reverse = true;
		}
		if(PlayerParticles.useMySQL) {
			mysqltimer++;
			if(mysqltimer > 600) {
				try {
					if(PlayerParticles.c != null && PlayerParticles.c.isClosed()) {
						PlayerParticles.c = PlayerParticles.mySQL.openConnection();
						if(PlayerParticles.c.isClosed()) {
							PlayerParticles.getPlugin().getLogger().info("[PlayerParticles] Cannot connect to database! Is the database available and is your connection information correct?");
						}
					}
				} catch (SQLException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				mysqltimer = 0;
			}
		}
		for(Player player : Bukkit.getOnlinePlayers()){
			if(!map.containsKey(player.getName()) || ConfigManager.getInstance().isWorldDisabled(player.getWorld().getName())) continue;
			ParticleType effect = map.get(player.getName());
			if(PermissionManager.hasPermission(player, effect)){
				Location loc = player.getLocation();
				loc.setY(loc.getY() + 1);
				displayParticle(effect, styleMap.get(player.getName()), loc);
			}
		}
	}
	
	/**
	 * Displays particles at the given player location with the effect and style given
	 * Checks all the effects and styles to make sure we display what is requested
	 * 
	 * @param effect The effect to display
	 * @param style The style to display 
	 * @param location The location to display at
	 */
	private void displayParticle(ParticleType effect, ParticleStyle style, Location location){
		if(style == null || style == ParticleStyle.NONE) {
			handleStyleNone(effect, location);
		}else if(style == ParticleStyle.SPIRAL) {
			ParticleEffect particle = null;
			if(effect == ParticleType.RAINBOW || effect == ParticleType.NOTE) particle = new ParticleEffect(effect, 0.0F, 0.0F, 0.0F, 1.0F, 1);
			else particle = new ParticleEffect(effect, 0.0F, 0.0F, 0.0F, 0.0F, 1);
			int points = 16;
			double radius = 1;
			double slice = 2 * Math.PI / points;
			for(int i = 0; i < points; i++) {
				double angle = slice * i;
				double newX = location.getX() + radius * Math.cos(angle);
				double newY = location.getY() + (step / 10) - 1;
				double newZ = location.getZ() + radius * Math.sin(angle);
				Location newLocation = new Location(location.getWorld(), newX, newY, newZ);
				particle.display(newLocation);
			}
		}else if(style == ParticleStyle.HALO) {
			if(step % 2 == 0) return;
			ParticleEffect particle = null;
			if(effect == ParticleType.RAINBOW || effect == ParticleType.NOTE) particle = new ParticleEffect(effect, 0.0F, 0.0F, 0.0F, 1.0F, 1);
			else particle = new ParticleEffect(effect, 0.0F, 0.0F, 0.0F, 0.0F, 1);
			int points = 16;
			double radius = .65;
			double slice = 2 * Math.PI / points;
			for(int i = 0; i < points; i++) {
				double angle = slice * i;
				double newX = location.getX() + radius * Math.cos(angle);
				double newY = location.getY() + 1.5;
				double newZ = location.getZ() + radius * Math.sin(angle);
				Location newLocation = new Location(location.getWorld(), newX, newY, newZ);
				particle.display(newLocation);
			}
		}else if(style == ParticleStyle.POINT) {
			ParticleEffect particle = null;
			if(effect == ParticleType.RAINBOW || effect == ParticleType.NOTE) particle = new ParticleEffect(effect, 0.0F, 0.0F, 0.0F, 1.0F, 1);
			else particle = new ParticleEffect(effect, 0.0F, 0.0F, 0.0F, 0.0F, 1);
			particle.display(location.add(0.0, 1.5, 0.0));
		}else if(style == ParticleStyle.SPIN) {
			ParticleEffect particle = null;
			if(effect == ParticleType.RAINBOW || effect == ParticleType.NOTE) particle = new ParticleEffect(effect, 0.0F, 0.0F, 0.0F, 1.0F, 1);
			else particle = new ParticleEffect(effect, 0.0F, 0.0F, 0.0F, 0.0F, 1);
			int points = 15;
			double radius = .5;
			double slice = 2 * Math.PI / points;
			double angle = slice * (step % 15);
			double newX = location.getX() + radius * Math.cos(angle);
			double newY = location.getY() + 1.5;
			double newZ = location.getZ() + radius * Math.sin(angle);
			Location newLocation = new Location(location.getWorld(), newX, newY, newZ);
			particle.display(newLocation);
		}else if(style == ParticleStyle.QUADHELIX) {
			ParticleEffect particle = null;
			if(effect == ParticleType.RAINBOW || effect == ParticleType.NOTE) particle = new ParticleEffect(effect, 0.0F, 0.0F, 0.0F, 1.0F, 1);
			else particle = new ParticleEffect(effect, 0.0F, 0.0F, 0.0F, 0.0F, 1);
			for(int i = 0; i < 4; i++) {
				double dx = -(Math.cos((helixStep / 90) * (Math.PI * 2) + ((Math.PI / 2) * i))) * ((60 - Math.abs(helixYStep)) / 60);
				double dy = ((helixYStep) / 60) * 1.5;
				double dz = -(Math.sin((helixStep / 90) * (Math.PI * 2) + ((Math.PI / 2) * i))) * ((60 - Math.abs(helixYStep)) / 60);
				particle.display(new Location(location.getWorld(), location.getX() + dx, location.getY() + dy, location.getZ() + dz));
			}
		}else if(style == ParticleStyle.ORB) {
			ParticleEffect particle = null;
			if(effect == ParticleType.RAINBOW || effect == ParticleType.NOTE) particle = new ParticleEffect(effect, 0.0F, 0.0F, 0.0F, 1.0F, 1);
			else particle = new ParticleEffect(effect, 0.0F, 0.0F, 0.0F, 0.0F, 1);
			for(int i = 0; i < 4; i++) {
				double dx = -(Math.cos((helixStep / 90) * (Math.PI * 2) + ((Math.PI / 2) * i)));
				double dz = -(Math.sin((helixStep / 90) * (Math.PI * 2) + ((Math.PI / 2) * i)));
				particle.display(new Location(location.getWorld(), location.getX() + dx, location.getY(), location.getZ() + dz));
			}
		}
	}
	
	/**
	 * Displays particles at the given location with the default spread out style, NONE
	 * Only check against every type to make sure they look nice, it isn't completely required
	 * 
	 * @param effect The effect to display as
	 * @param location The locatio to display at
	 */
	public void handleStyleNone(ParticleType effect, Location location) {
		if(effect == null || location == null) return;
		if(effect.equals(ParticleType.ANGRY_VILLAGER)){
			ParticleEffect particle = new ParticleEffect(effect, 0.6F, 0.6F, 0.6F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.BUBBLE)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.CLOUD)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.CRIT)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.DEPTH_SUSPEND)){
			ParticleEffect particle = new ParticleEffect(effect, 0.5F, 0.5F, 0.5F, 0.0F, 5);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.DRIP_LAVA)){
			ParticleEffect particle = new ParticleEffect(effect, 0.6F, 0.6F, 0.6F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.DRIP_WATER)){
			ParticleEffect particle = new ParticleEffect(effect, 0.6F, 0.6F, 0.6F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.ENCHANTMENT_TABLE)){
			ParticleEffect particle = new ParticleEffect(effect, 0.6F, 0.6F, 0.6F, 0.05F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.EXPLODE)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.FIREWORKS_SPARK)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.FLAME)){
			ParticleEffect particle = new ParticleEffect(effect, 0.1F, 0.1F, 0.1F, 0.05F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.FOOTSTEP)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.0F, 0.4F, 0.0F, 1);
			particle.display(location.subtract(0, 0.98, 0));
			return;
		}else
		if(effect.equals(ParticleType.HAPPY_VILLAGER)){
			ParticleEffect particle = new ParticleEffect(effect, 0.5F, 0.5F, 0.5F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.HEART)){
			ParticleEffect particle = new ParticleEffect(effect, 0.6F, 0.6F, 0.6F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.HUGE_EXPLOSION)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.INSTANT_SPELL)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.LARGE_EXPLODE)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.LARGE_SMOKE)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.LAVA)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.MAGIC_CRIT)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.MOB_SPELL)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.MOB_SPELL_AMBIENT)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.NOTE)){
			ParticleEffect particle = new ParticleEffect(effect, 0.6F, 0.6F, 0.6F, 1.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.PORTAL)){
			ParticleEffect particle = new ParticleEffect(effect, 0.5F, 0.5F, 0.5F, 0.05F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.RAINBOW)){
			ParticleEffect particle = new ParticleEffect(effect, 0.5F, 0.5F, 0.5F, 1.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.RED_DUST)){
			ParticleEffect particle = new ParticleEffect(effect, 0.5F, 0.5F, 0.5F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.SLIME)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.SMOKE)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.SNOW_SHOVEL)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.SNOWBALL_POOF)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.SPELL)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.SUSPENDED)){
			ParticleEffect particle = new ParticleEffect(effect, 0.8F, 0.8F, 0.8F, 0.0F, 5);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.WAKE)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 3);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.WITCH_MAGIC)){
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.BARRIER)){
			ParticleEffect particle = new ParticleEffect(effect, 1.2F, 1.2F, 1.2F, 0.0F, 1);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.DROPLET)){
			ParticleEffect particle = new ParticleEffect(effect, 0.8F, 0.8F, 0.8F, 0.0F, 5);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.DRAGON_BREATH)) {
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 5);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.END_ROD)) {
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 5);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.DAMAGE_INDICATOR)) {
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 5);
			particle.display(location);
			return;
		}else
		if(effect.equals(ParticleType.SWEEP_ATTACK)) {
			ParticleEffect particle = new ParticleEffect(effect, 0.4F, 0.4F, 0.4F, 0.0F, 5);
			particle.display(location);
			return;
		}
	}

}
