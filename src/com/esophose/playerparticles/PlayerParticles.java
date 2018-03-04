/**
 * Copyright Esophose 2018
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

/*
 * TODO: v5.1
 * + Command to force set an effect/style for a player
 * + Tab completion for fixed effects
 * + Add new style 'tornado'
 * + Add new style 'companion'
 * + Add new style 'atom'
 * + Add new style 'rings'
 */

package com.esophose.playerparticles;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.esophose.playerparticles.gui.PlayerParticlesGui;
import com.esophose.playerparticles.manager.DatabaseManager;
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
     * The MySQL database connection manager
     */
    public static DatabaseManager mySQL = null;

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
        Bukkit.getPluginManager().registerEvents(new PlayerParticlesGui(), this);
        if (getConfig().getDouble("version") < Double.parseDouble(getDescription().getVersion())) {
            File configFile = new File(getDataFolder(), "config.yml");
            if (configFile.exists()) configFile.delete();
            saveDefaultConfig();
            reloadConfig();
            getLogger().warning("The config.yml has been updated to v" + getDescription().getVersion() + "!");
        }
        checkDatabase();
        startTask();

        if (shouldCheckUpdates()) {
            new BukkitRunnable() {
                public void run() {
                    final File file = getFile();
                    try { // This can throw an exception if the server has no internet connection or if the Curse API is down
                        Updater updater = new Updater(getPlugin(), 82823, file, Updater.UpdateType.NO_DOWNLOAD, false);
                        if (Double.parseDouble(updater.getLatestName().replaceAll("PlayerParticles v", "")) > Double.parseDouble(getPlugin().getDescription().getVersion())) {
                            updateVersion = updater.getLatestName().replaceAll("PlayerParticles v", "");
                            getLogger().info("An update (v" + updateVersion + ") is available! You are running v" + getPlugin().getDescription().getVersion());
                        }
                    } catch (Exception e) {
                        getLogger().warning("An error occurred checking for an update. There is either no established internet connection or the Curse API is down.");
                    }
                }
            }.runTaskAsynchronously(this);
        }
    }

    /**
     * Clean up MySQL connection if it's open
     */
    public void onDisable() {
        if (useMySQL) {
            mySQL.closeConnection();
        }

        PlayerParticlesGui.forceCloseAllOpenGUIs();
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
            mySQL = new DatabaseManager(getConfig());

            useMySQL = mySQL.isInitialized(); // If something goes wrong, this will be set to false
            if (!useMySQL) return; // Break out, couldn't set up the database connection

            // Queries are purposely not run in an asynchronous task, this is on plugin startup and shouldn't affect the end users
            mySQL.connect((connection) -> { // @formatter:off
                try (Statement statement = connection.createStatement();
                     ResultSet res = statement.executeQuery("SHOW TABLES LIKE 'pp_users'")) {
                    
                    if (res.next()) { // Database is already created
                        try (Statement statement2 = connection.createStatement();
                             ResultSet res2 = statement2.executeQuery("SHOW TABLES LIKE 'pp_fixed'")) {
                            
                            if (!res2.next()) { // Is the current database of an old version?
                                mySQL.updateSQL("CREATE TABLE pp_fixed (uuid VARCHAR(36), player_uuid VARCHAR(36), id SMALLINT, effect VARCHAR(32), style VARCHAR(32), worldName VARCHAR(50), xPos DOUBLE, yPos DOUBLE, zPos DOUBLE);" +
                                                "ALTER TABLE pp_data_item CHANGE player_uuid uuid VARCHAR(36);" +
                                                "ALTER TABLE pp_data_block CHANGE player_uuid uuid VARCHAR(36);" +
                                                "ALTER TABLE pp_data_color CHANGE player_uuid uuid VARCHAR(36);" +
                                                "ALTER TABLE pp_data_note CHANGE player_uuid uuid VARCHAR(36);");
                            }
                        }
                    } else { // Database isn't created yet
                        mySQL.updateSQL("CREATE TABLE pp_users (player_uuid VARCHAR(36), effect VARCHAR(32), style VARCHAR(32));" + 
                                        "CREATE TABLE pp_fixed (uuid VARCHAR(36), player_uuid VARCHAR(36), id SMALLINT, effect VARCHAR(32), style VARCHAR(32), worldName VARCHAR(50), xPos DOUBLE, yPos DOUBLE, zPos DOUBLE);" +
                                        "CREATE TABLE pp_data_item (uuid VARCHAR(36), material VARCHAR(32), data SMALLINT);" + 
                                        "CREATE TABLE pp_data_block (uuid VARCHAR(36), material VARCHAR(32), data SMALLINT);" +
                                        "CREATE TABLE pp_data_color (uuid VARCHAR(36), r SMALLINT, g SMALLINT, b SMALLINT);" + 
                                        "CREATE TABLE pp_data_note (uuid VARCHAR(36), note SMALLINT);");
                    }
                    
                } catch (SQLException e) {
                    getLogger().info("Failed to connect to the MySQL Database! Check to see if your login information is correct!");
                    getLogger().info("Additional information: " + e.getMessage());
                    useMySQL = false;
                }
            });
        } else {
            useMySQL = false;
        } // @formatter:on
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
                PlayerParticlesGui.setup();

                long ticks = getConfig().getLong("ticks-per-particle");
                new ParticleManager().runTaskTimer(playerParticles, 0, ticks);
            }
        }.runTaskLater(playerParticles, 1);
    }

}
