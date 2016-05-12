/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.esophose.playerparticles.libraries.particles.ParticleEffect.ParticleType;

public class ConfigManager {
	
	/**
	 * The instance of the ConfigManager used for effect data
	 */
	private static ConfigManager instance = new ConfigManager("effectData");
	/**
	 * The instance of the ConfigManager used for style data
	 */
	private static ConfigManager styleInstance = new ConfigManager("styleData");
	/**
	 * The file the data is located in for the instance
	 */
	private File file;
	/**
	 * The configuration used to edit the .yaml file
	 */
	private FileConfiguration config;
	
	/**
	 * @return The instance of the config for effects
	 */
	public static ConfigManager getInstance() {
		return instance;
	}
	
	/**
	 * @return The instance of the config for styles
	 */
	public static ConfigManager getStyleInstance() {
		return styleInstance;
	}
	
	/**
	 * @param fileName Will either be "effectData" or "styleData"
	 */
	private ConfigManager(String fileName) {		
		if (!PlayerParticles.getPlugin().getDataFolder().exists()) PlayerParticles.getPlugin().getDataFolder().mkdir();
		
		file = new File(PlayerParticles.getPlugin().getDataFolder(), fileName + ".yml");
		
		if (!file.exists()) {
			try { file.createNewFile(); }
			catch (Exception e) { e.printStackTrace(); }
		}
		
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	/**
	 * Removes any data contained within the current config instance
	 * Never used
	 */
	public void flushData() {
		for(String key : config.getKeys(false)) {
			config.set(key, null);
		}
		try {config.save(file);}
		catch (IOException e) {e.printStackTrace();}
	}
	
	/**
	 * Removes all the player, effect, and style data from the connected database
	 * Never used
	 */
	public static void flushDatabase() {
		if(PlayerParticles.useMySQL) {
			Statement statement;
			try {
				statement = PlayerParticles.c.createStatement();
				statement.executeUpdate("TRUNCATE playerparticles;");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Saves the particle effect to the player's name in either the database or config
	 * Should only be called from the effectData instance
	 * 
	 * @param type The type of the particle
	 * @param player The player to save the particle to
	 */
	public void setParticle(ParticleType type, Player player){
		if(PlayerParticles.useMySQL) {
			Statement statement;
			try {
				statement = PlayerParticles.c.createStatement();
				statement.executeUpdate("UPDATE playerparticles SET particle = '" + type.toString().toLowerCase().replace("_", "") + "' WHERE player_name = '" + player.getName() + "';");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			config.set(player.getName(), type.toString().toLowerCase().replace("_", ""));
			try {config.save(file);}
			catch (IOException e) {e.printStackTrace();}
		}
	}
	
	/**
	 * Removes the particle effect from the player's name in either the database or config
	 * Should only be called from the effectData instance
	 * 
	 * @param player The player to clear the particle effect from
	 */
	public void resetParticle(Player player){
		if(PlayerParticles.useMySQL) {
			Statement statement;
			try {
				statement = PlayerParticles.c.createStatement();
				statement.executeUpdate("UPDATE playerparticles SET particle = NULL WHERE player_name = '" + player.getName() + "';");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			config.set(player.getName(), ParticleStyle.NONE.toString().toLowerCase().replace("_", ""));
			try {config.save(file);}
			catch (IOException e) {e.printStackTrace();}
		}
	}
	
	/**
	 * Gets the particle effect saved in either the database or config for the player
	 * 
	 * @param player The player to get the particle effect data for
	 * @return The particle effect for the player
	 */
	public ParticleType getParticle(Player player){
		if(PlayerParticles.useMySQL) {
			Statement statement;
			try {
				statement = PlayerParticles.c.createStatement();
				ResultSet res = statement.executeQuery("SELECT * FROM playerparticles WHERE player_name = '" + player.getName() + "';");
				res.next();
				return ParticleCreator.particleFromString(res.getString("particle"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			String effectToLowerCase = (String) config.getString(player.getName());
			return ParticleCreator.particleFromString(effectToLowerCase);
		}
		return null;
	}
	
	/**
	 * Saves the style effect to the player's name in either the database or config
	 * Should only be called from the effectData instance
	 * 
	 * @param style The style to save for the player
	 * @param player The player to save the style to
	 */
	public void setStyle(ParticleStyle style, Player player) {
		if(PlayerParticles.useMySQL) {
			Statement statement;
			try {
				statement = PlayerParticles.c.createStatement();
				statement.executeUpdate("UPDATE playerparticles SET style = '" + style.toString().toLowerCase().replace("_", "") + "' WHERE player_name = '" + player.getName() + "';");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			config.set(player.getName(), style.toString().toLowerCase().replace("_", ""));
			try {config.save(file);}
			catch (IOException e) {e.printStackTrace();}
		}
	}
	
	/**
	 * Removes the particle effect from the player's name in either the database or config
	 * Should only be called from the effectData instance
	 * 
	 * @param player The player to reset the style for
	 */
	public void resetStyle(Player player) {
		if(PlayerParticles.useMySQL) {
			Statement statement;
			try {
				statement = PlayerParticles.c.createStatement();
				statement.executeUpdate("UPDATE playerparticles SET style = '" + ParticleStyle.NONE.toString().toLowerCase().replace("_", "") + "' WHERE player_name = '" + player.getName() + "';");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			config.set(player.getName(), ParticleStyle.NONE.toString().toLowerCase().replace("_", ""));
			try {config.save(file);}
			catch (IOException e) {e.printStackTrace();}
		}
	}
	
	/**
	 * Gets the particle effect saved in either the database or config for the player
	 * 
	 * @param player The player to get the particle style for
	 * @return The particle style for the player
	 */
	public ParticleStyle getStyle(Player player) {
		if(PlayerParticles.useMySQL) {
			Statement statement;
			try {
				statement = PlayerParticles.c.createStatement();
				ResultSet res = statement.executeQuery("SELECT * FROM playerparticles WHERE player_name = '" + player.getName() + "';");
				res.next();
				return ParticleStyle.styleFromString(res.getString("style"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			String styleToLowerCase = (String) config.getString(player.getName());
			return ParticleStyle.styleFromString(styleToLowerCase);
		}
		return ParticleStyle.NONE;
	}
	
	/**
	 * Checks if a world is disabled for particles to spawn in
	 * 
	 * @param world The world name to check
	 * @return True if the world is disabled
	 */
	@SuppressWarnings("unchecked")
	public boolean isWorldDisabled(String world) {
		if(PlayerParticles.getPlugin().getConfig().get("disabled-worlds") != null && ((ArrayList<String>) PlayerParticles.getPlugin().getConfig().get("disabled-worlds")).contains(world)) {
			return true;
		}else return false;
	}
	
	/**
	 * Gets all the worlds that are disabled
	 * 
	 * @return All world names that are disabled
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getDisabledWorlds() {
		if(PlayerParticles.getPlugin().getConfig().get("disabled-worlds") != null) {
			return ((ArrayList<String>) PlayerParticles.getPlugin().getConfig().get("disabled-worlds"));
		}else return null;
	}
	
}
