/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 * 
 * TODO: Make sure copyright notice is on all files
 * TODO: Make sure all the comments are properly formatted still
 * TODO: Add option to config to show particles in spectator mode or not - Disabled by default
 * TODO: Add message configuration for data usage
 */

package com.esophose.playerparticles;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.esophose.playerparticles.library.MySQL;
import com.esophose.playerparticles.manager.MessageManager;
import com.esophose.playerparticles.styles.DefaultStyles;
import com.esophose.playerparticles.updater.PluginUpdateListener;
import com.esophose.playerparticles.updater.Updater;

public class PlayerParticles extends JavaPlugin {

	/**
	 * The version a new update has, will be null if the config has it disabled
	 * or if there is no new version
	 */
	public static String updateVersion = null;

	/**
	 * The MySQL connection
	 */
	public static MySQL mySQL = null;

	/**
	 * Whether or not to use MySQL as determined in the config
	 */
	public static boolean useMySQL = false;

	/**
	 * Registers all the styles available by default
	 * Saves the default config if it doesn't exist
	 * Registers the tab completer and the event listeners
	 * Checks if the config needs to be updated to the new version
	 * Makes sure the database is accessable
	 * Updates the map and styleMap @see ParticleCreator
	 * Starts the particle spawning task
	 * Registers the command executor
	 * Checks for any updates if checking is enabled in the config
	 */
	public void onEnable() {
		DefaultStyles.registerStyles();
		MessageManager.setup();
		saveDefaultConfig();
		getCommand("pp").setTabCompleter(new ParticleCommandCompleter());
		getCommand("pp").setExecutor(new ParticleCommandExecutor());
		Bukkit.getPluginManager().registerEvents(new ParticleCreator(), this);
		Bukkit.getPluginManager().registerEvents(new PluginUpdateListener(), this);
		if (getConfig().getDouble("version") < Double.parseDouble(getDescription().getVersion())) {
			File configFile = new File(getDataFolder(), "config.yml");
			configFile.delete();
			saveDefaultConfig();
			reloadConfig();
			getLogger().warning("[PlayerParticles] config.yml has been updated!");
		}
		checkDatabase();
		ParticleCreator.refreshPPlayers();
		startTask();

		if (shouldCheckUpdates()) {
			Updater updater = new Updater(this, 82823, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
			if (Double.parseDouble(updater.getLatestName().replaceAll("PlayerParticles v", "")) > Double.parseDouble(getPlugin().getDescription().getVersion())) {
				updateVersion = updater.getLatestName().replaceAll("PlayerParticles v", "");
				getLogger().info("[PlayerParticles] An update (v" + updateVersion + ") is available! You are running v" + getPlugin().getDescription().getVersion());
			}
		}
	}

	/**
	 * Gets the instance of the plugin running on the server
	 * 
	 * @return The PlayerParticles plugin instance
	 */
	public static Plugin getPlugin() {
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
	 * Removes old table from previous versions of the plugin
	 * Creates new tables if they don't exist
	 * Sets useMySQL to true if it connects successfully, and false if it fails or isn't enabled
	 */
	private void checkDatabase() {
		if (getConfig().getBoolean("database-enable")) {
			String hostname = getConfig().getString("database-hostname");
			String port = getConfig().getString("database-port");
			String database = getConfig().getString("database-name");
			String user = getConfig().getString("database-user-name");
			String pass = getConfig().getString("database-user-password");
			mySQL = new MySQL(hostname, port, database, user, pass);
			try (ResultSet res = mySQL.querySQL("SHOW TABLES LIKE 'playerparticles'")) { // Clean up the old mess
				if (res.next()) {
					mySQL.updateSQL("DROP TABLE playerparticles");
				}
			} catch (ClassNotFoundException | SQLException e1) {
				getLogger().info("[PlayerParticles] Failed to connect to the MySQL Database! Check to see if your login information is correct!");
				useMySQL = false;
				return;
			}
			try (ResultSet res = mySQL.querySQL("SHOW TABLES LIKE 'pp_users'")) {
				if (!res.next()) { // @formatter:off
					mySQL.updateSQL("CREATE TABLE pp_users (player_uuid VARCHAR(36), effect VARCHAR(32), style VARCHAR(32));" + 
									"CREATE TABLE pp_data_item (player_uuid VARCHAR(36), material VARCHAR(32), data SMALLINT);" + 
									"CREATE TABLE pp_data_block (player_uuid VARCHAR(36), material VARCHAR(32), data SMALLINT);" +
									"CREATE TABLE pp_data_color (player_uuid VARCHAR(36), r SMALLINT, g SMALLINT, b SMALLINT);" + 
									"CREATE TABLE pp_data_note (player_uuid VARCHAR(36), note SMALLINT);"
					); // @formatter:on
				}
				useMySQL = true;
			} catch (ClassNotFoundException | SQLException e) {
				getLogger().info("[PlayerParticles] Failed to connect to the MySQL Database! Check to see if your login information is correct!");
				getLogger().info("Additional information: " + e.getMessage());
				useMySQL = false;
			}
		} else {
			useMySQL = false;
		}
	}

	/**
	 * Starts the task reponsible for spawning particles
	 */
	private void startTask() {
		double ticks = getConfig().getInt("ticks-per-particle");
		new ParticleCreator().runTaskTimer(this, 20, (long) ticks);
	}

}
