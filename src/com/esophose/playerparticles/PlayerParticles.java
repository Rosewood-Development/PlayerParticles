/**
 * Copyright Esophose 2017
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

/*
 TODO: v4.4 
 + Add new style 'tornado'
 + Add new style 'atom'
 + Add new style 'wings'
 + GUI for styles and effects - Requires no additional permissions
   /pp gui - Shows GUI that tells you your current effect, style, and data and lets you choose new ones
   /pp gui effect - Shows GUI that lets you select a new effect, also shows your current one
   /pp gui style - Shows GUI that lets you select a new style, also shows your current one
   /pp gui data - Shows GUI that lets you choose from preset data based on your current effect, also shows your current data
*/

package com.esophose.playerparticles;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.esophose.playerparticles.library.MySQL;
import com.esophose.playerparticles.manager.MessageManager;
import com.esophose.playerparticles.manager.ParticleManager;
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
		Bukkit.getPluginManager().registerEvents(new ParticleManager(), this);
		Bukkit.getPluginManager().registerEvents(new PluginUpdateListener(), this);
		if (getConfig().getDouble("version") < Double.parseDouble(getDescription().getVersion().substring(0, 3))) {
			File configFile = new File(getDataFolder(), "config.yml");
			configFile.delete();
			saveDefaultConfig();
			reloadConfig();
			getLogger().warning("The config.yml has been updated to v" + getDescription().getVersion() + "!");
		}
		checkDatabase();
		startTask();

		if (shouldCheckUpdates()) {
			Updater updater = new Updater(this, 82823, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
			if (Double.parseDouble(updater.getLatestName().replaceAll("PlayerParticles v", "").replaceAll("\\.", "")) > Double.parseDouble(getPlugin().getDescription().getVersion().replaceAll("\\.", ""))) {
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
			
			useMySQL = true; // If something goes wrong this will be set to false

			// @formatter:off
			try (ResultSet res = mySQL.querySQL("SHOW TABLES LIKE 'pp_users'")) {
				if (res.next()) { // Add the new fixed table and rename some columns
					try (ResultSet res2 = mySQL.querySQL("SHOW TABLES LIKE 'pp_fixed'")) {
						if (!res2.next()) { // Using an old database, update to the new one
							mySQL.updateSQL("CREATE TABLE pp_fixed (uuid VARCHAR(36), player_uuid VARCHAR(36), id SMALLINT, effect VARCHAR(32), style VARCHAR(32), worldName VARCHAR(50), xPos DOUBLE, yPos DOUBLE, zPos DOUBLE);" +
										    "ALTER TABLE pp_data_item CHANGE player_uuid uuid VARCHAR(36);" +
										    "ALTER TABLE pp_data_block CHANGE player_uuid uuid VARCHAR(36);" +
										    "ALTER TABLE pp_data_color CHANGE player_uuid uuid VARCHAR(36);" +
										    "ALTER TABLE pp_data_note CHANGE player_uuid uuid VARCHAR(36);");
						}
					} catch (Exception e) {
						throw e; // Try-catch block here is just for auto closure
					}
				} else { // No database is set up yet, create it
					mySQL.updateSQL("CREATE TABLE pp_users (player_uuid VARCHAR(36), effect VARCHAR(32), style VARCHAR(32));" + 
									"CREATE TABLE pp_fixed (uuid VARCHAR(36), player_uuid VARCHAR(36), id SMALLINT, effect VARCHAR(32), style VARCHAR(32), worldName VARCHAR(50), xPos DOUBLE, yPos DOUBLE, zPos DOUBLE);" +
									"CREATE TABLE pp_data_item (uuid VARCHAR(36), material VARCHAR(32), data SMALLINT);" + 
									"CREATE TABLE pp_data_block (uuid VARCHAR(36), material VARCHAR(32), data SMALLINT);" +
									"CREATE TABLE pp_data_color (uuid VARCHAR(36), r SMALLINT, g SMALLINT, b SMALLINT);" + 
									"CREATE TABLE pp_data_note (uuid VARCHAR(36), note SMALLINT);"
							       );
				}
			} catch (ClassNotFoundException | SQLException e) {
				getLogger().info("[PlayerParticles] Failed to connect to the MySQL Database! Check to see if your login information is correct!");
				getLogger().info("Additional information: " + e.getMessage());
				useMySQL = false;
				return;
			} // @formatter:on
		} else {
			useMySQL = false;
		}
	}

	/**
	 * Starts the task reponsible for spawning particles
	 * Run in the synchronous task so it starts after all plugins have loaded, including extensions
	 */
	private void startTask() {
		final Plugin playerParticles = this;
		new BukkitRunnable() {
			public void run() {
				ParticleManager.refreshPPlayers(); // Add any online players who have particles
				ParticleManager.addAllFixedEffects(); // Add all fixed effects
				
				double ticks = getConfig().getInt("ticks-per-particle");
				new ParticleManager().runTaskTimer(playerParticles, 20, (long) ticks);
			}
		}.runTaskLater(playerParticles, 20);
	}

}
