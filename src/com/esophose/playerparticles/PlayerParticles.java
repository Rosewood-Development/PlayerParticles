/*
 * TODO: v5.3
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

import com.esophose.playerparticles.command.ParticleCommandHandler;
import com.esophose.playerparticles.database.DatabaseConnector;
import com.esophose.playerparticles.database.MySqlDatabaseConnector;
import com.esophose.playerparticles.database.SqliteDatabaseConnector;
import com.esophose.playerparticles.gui.PlayerParticlesGui;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.styles.DefaultStyles;
import com.esophose.playerparticles.updater.PluginUpdateListener;
import com.esophose.playerparticles.updater.Updater;

public class PlayerParticles extends JavaPlugin {

    private static Plugin pluginInstance;

    /**
     * The version a new update has, will be null if the config has it disabled
     * or if there is no new version
     */
    public static String updateVersion = null;

    /**
     * The database connection manager
     */
    public static DatabaseConnector databaseConnector = null;

    /**
     * Registers all the styles available by default
     * Saves the default config if it doesn't exist
     * Registers the tab completer and the event listeners
     * Checks if the config needs to be updated to the new version
     * Makes sure the database is accessible
     * Updates the map and styleMap @see ParticleCreator
     * Starts the particle spawning task
     * Registers the command executor
     * Checks for any updates if checking is enabled in the config
     */
    public void onEnable() {
        pluginInstance = Bukkit.getServer().getPluginManager().getPlugin("PlayerParticles");

        getCommand("pp").setTabCompleter(new ParticleCommandHandler());
        getCommand("pp").setExecutor(new ParticleCommandHandler());

        Bukkit.getPluginManager().registerEvents(new ParticleManager(), this);
        Bukkit.getPluginManager().registerEvents(new PluginUpdateListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerParticlesGui(), this);

        saveDefaultConfig();
        double configVersion = getConfig().getDouble("version");
        if (configVersion < Double.parseDouble(getDescription().getVersion())) {
            File configFile = new File(getDataFolder(), "config.yml");
            if (configFile.exists()) {
                configFile.delete();
            }
            saveDefaultConfig();
            reloadConfig();
            getLogger().warning("The config.yml has been updated to v" + getDescription().getVersion() + "!");
        }

        DefaultStyles.registerStyles();
        LangManager.setup();

        configureDatabase(getConfig().getBoolean("database-enable"));
        startParticleTask();

        if (shouldCheckUpdates()) {
            new BukkitRunnable() {
                public void run() {
                    try { // This can throw an exception if the server has no internet connection or if the Curse API is down
                        Updater updater = new Updater(pluginInstance, 82823, getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
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
     * Clean up database connection if it's open
     * Close all users with an open PlayerParticles GUI
     */
    public void onDisable() {
        databaseConnector.closeConnection();
        PlayerParticlesGui.forceCloseAllOpenGUIs();
    }

    /**
     * Gets the instance of the plugin running on the server
     * 
     * @return The PlayerParticles plugin instance
     */
    public static Plugin getPlugin() {
        return pluginInstance;
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
     * Checks if database-enable is true in the config, if it is then uses MySql
     * Gets the database connection information from the config and tries to connect to the server
     * Removes old table from previous versions of the plugin
     * Creates new tables if they don't exist
     */
    private void configureDatabase(boolean useMySql) {
        if (useMySql) {
            databaseConnector = new MySqlDatabaseConnector(this.getConfig());
        } else {
            databaseConnector = new SqliteDatabaseConnector(this.getDataFolder().getAbsolutePath());
        }

        if (!databaseConnector.isInitialized()) {
            getLogger().severe("Unable to connect to the MySQL database! Is your login information correct? Falling back to SQLite database instead.");
            configureDatabase(false);
            return;
        }

        databaseConnector.connect((connection) -> {
            // Check if pp_users exists, if it does, this is an old database schema that needs to be deleted
            try { // @formatter:off
    			try (Statement statement = connection.createStatement()) {
    			    String pp_usersQuery;
    			    if (useMySql) {
    			        pp_usersQuery = "SHOW TABLES LIKE 'pp_users'";
    			    } else {
    			        pp_usersQuery = "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'pp_users'";
    			    }
        			ResultSet result = statement.executeQuery(pp_usersQuery);
        			if (result.next()) {
        			    statement.close();
        			    
        				Statement dropStatement = connection.createStatement();
        				dropStatement.addBatch("DROP TABLE IF EXISTS pp_users");
        				dropStatement.addBatch("DROP TABLE IF EXISTS pp_fixed");
        				dropStatement.addBatch("DROP TABLE IF EXISTS pp_data_item");
        				dropStatement.addBatch("DROP TABLE IF EXISTS pp_data_block");
        				dropStatement.addBatch("DROP TABLE IF EXISTS pp_data_color");
        				dropStatement.addBatch("DROP TABLE IF EXISTS pp_data_note");
        				dropStatement.executeBatch();
        				getLogger().warning("Deleted old " + (useMySql ? "MySQL" : "SQLite") + " database schema, it was out of date.");
        			}
        		}
    			
    			// Check if pp_group exists, if it doesn't, we need to create all the tables
        		try (Statement statement = connection.createStatement()) {
        		    String pp_groupQuery;
                    if (useMySql) {
                        pp_groupQuery = "SHOW TABLES LIKE 'pp_group'";
                    } else {
                        pp_groupQuery = "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'pp_group'";
                    }
        			ResultSet result = statement.executeQuery(pp_groupQuery);
        			if (!result.next()) {
        			    statement.close();
        			    
        				Statement createStatement = connection.createStatement();
        				createStatement.addBatch("CREATE TABLE IF NOT EXISTS pp_group (uuid VARCHAR(36), owner_uuid VARCHAR(36), name VARCHAR(100))");
        				createStatement.addBatch("CREATE TABLE IF NOT EXISTS pp_fixed (owner_uuid VARCHAR(36), id SMALLINT, particle_uuid VARCHAR(36), world VARCHAR(100), xPos DOUBLE, yPos DOUBLE, zPos DOUBLE)");
        				createStatement.addBatch("CREATE TABLE IF NOT EXISTS pp_particle (uuid VARCHAR(36), group_uuid VARCHAR(36), id SMALLINT, effect VARCHAR(100), style VARCHAR(100), item_material VARCHAR(100), block_material VARCHAR(100), note SMALLINT, r SMALLINT, g SMALLINT, b SMALLINT)");
        				createStatement.executeBatch();
        				getLogger().warning("Created new " + (useMySql ? "MySQL" : "SQLite") + " database schema.");
        			}
        		}
    		} catch (SQLException ex) {
    		    ex.printStackTrace();
    			if (useMySql) {
    			    getLogger().severe("Unable to connect to the MySQL database! Is your login information correct? Falling back to SQLite database instead.");
    			    configureDatabase(false);
    			} else {
    			    getLogger().severe("Unable to connect to the SQLite database! This is a critical error, the plugin will be unable to save any data.");
    			}
    		}
    	}); // @formatter:on
    }

    /**
     * Starts the task responsible for spawning particles
     * Run in the synchronous task so it starts after all plugins have loaded, including extensions
     */
    private void startParticleTask() {
        final Plugin playerParticles = this;
        new BukkitRunnable() {
            public void run() {
                ParticleManager.refreshPPlayers(); // Add any online players who have particles
                PlayerParticlesGui.setup();

                long ticks = getConfig().getLong("ticks-per-particle");
                new ParticleManager().runTaskTimer(playerParticles, 0, ticks);
            }
        }.runTaskLater(playerParticles, 1);
    }

}
