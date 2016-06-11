/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

// Fixed worlds missing from /pp help
// Add style "feet"

package com.esophose.playerparticles;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.esophose.playerparticles.library.database.MySQL;
import com.esophose.playerparticles.updater.PluginUpdateListener;
import com.esophose.playerparticles.updater.Updater;

public class PlayerParticles extends JavaPlugin {
	
	/**
	 * The version a new update has, will be null if the config has it disabled or if there is no new version
	 */
	public static String updateVersion = null;
	
	/**
	 * The MySQL connection
	 */
	public static MySQL mySQL = null;
	public static Connection c = null;
	
	/**
	 * Whether or not to use MySQL as determined in the config
	 */
	public static boolean useMySQL = false;
	
	/**
	 * Saves the default config if it doesn't exist
	 * Registers the tab completer and the event listeners
	 * Checks if the config needs to be updated to the new version
	 * Makes sure the database is accessable
	 * Updates the map and styleMap @see ParticleCreator
	 * Starts the particle spawning task
	 * Registers the command executor
	 * Checks for any updates if checking is enabled in the config
	 */
	public void onEnable(){
		saveDefaultConfig();
		getCommand("pp").setTabCompleter(new ParticleCommandCompleter());
		Bukkit.getPluginManager().registerEvents(new ParticleCreator(), this);
		Bukkit.getPluginManager().registerEvents(new PluginUpdateListener(), this);
		if(getConfig().getDouble("version") < Double.parseDouble(getDescription().getVersion())) {
			File configFile = new File(getDataFolder(), "config.yml");
			configFile.delete();
			saveDefaultConfig();
			reloadConfig();
			getLogger().warning("[PlayerParticles] config.yml has been updated!");
		}
		checkDatabase();
		ParticleCreator.updateMap();
		ParticleCreator.updateStyleMap();
		startTasks();
		
		getCommand("pp").setExecutor(new ParticleCommandExecutor());
		
		if(shouldCheckUpdates()) {
			getLogger().info("[PlayerParticles] Checking for an update...");
			Updater updater = new Updater(this, 82823, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
			if(Double.parseDouble(updater.getLatestName().replaceAll("PlayerParticles v", "")) > Double.parseDouble(getPlugin().getDescription().getVersion())) {
				updateVersion = updater.getLatestName().replaceAll("PlayerParticles v", "");
				getLogger().info("[PlayerParticles] An update (v" + updateVersion + ") is available! You are running v" + getPlugin().getDescription().getVersion());
			} else {
				getLogger().info("[PlayerParticles] No update was found");
			}
		}
	}
	
	/**
	 * Gets the instance of the plugin running on the server
	 * 
	 * @return The PlayerParticles plugin instance
	 */
	public static Plugin getPlugin(){
		return Bukkit.getPluginManager().getPlugin("PlayerParticles");
	}
	
	/**
	 * Checks the config if the plugin can look for updates
	 * 
	 * @return True if check-updates is set to true in the config
	 */
	public boolean shouldCheckUpdates() {
		return getConfig().getBoolean("check-updates");
	}
	
	/**
	 * Checks if database-enable is true in the config, if it is then continue
	 * Gets the database connection information from the config and tries to connect to the server
	 * Creates a new table if it doesn't exist called playerparticles
	 * Sets useMySQL to true if it connects successfully, and false if it fails or isn't enabled
	 */
	private void checkDatabase() {
		if(getConfig().getBoolean("database-enable")) {
			String hostname = getConfig().getString("database-hostname");
			String port = getConfig().getString("database-port");
			String database = getConfig().getString("database-name");
			String user = getConfig().getString("database-user-name");
			String pass = getConfig().getString("database-user-password");
			mySQL = new MySQL(hostname, port, database, user, pass);
			try {
				c = mySQL.openConnection();
				Statement statement = c.createStatement();
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS playerparticles (player_name VARCHAR(32), particle VARCHAR(32), style VARCHAR(32));");
				useMySQL = true;
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				getLogger().info("[PlayerParticles] Failed to connect to MySQL Database! Check to see if your config is correct!");
				useMySQL = false;
			}
		} else {
			useMySQL = false;
		}
		getLogger().info("[PlayerParticles] Using mySQL for data storage: " + useMySQL);
	}
	
	/**
	 * Starts the task reponsible for spawning particles
	 * Starts two with 1 tick delay if ticks-per-particle is set to 0.5
	 */
	private void startTasks() {
		double ticks = getConfig().getDouble("ticks-per-particle");
		if(ticks == 0.5) {
			new ParticleCreator().runTaskTimer(this, 20, 1);
			new ParticleCreator().runTaskTimer(this, 20, 1);
		} else new ParticleCreator().runTaskTimer(this, 20, (long) ticks);
	}
	
}
