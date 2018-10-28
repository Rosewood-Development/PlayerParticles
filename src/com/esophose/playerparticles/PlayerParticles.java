/*
 * TODO: v5.3
 * + Add new style 'tornado'
 * * Setting in config.yml for max number of particle groups, default 10
 * * Permission to allow players to overrule the max particle groups allowed in the config playerparticles.groups.unlimited
 * * Setting in config.yml to disable non-event styles while the player is moving
 * * Setting in config.yml for max particles allowed per player, default 3
 * * Permission to allow players to overrule the max particles allowed playerparticles.particle.max
 * * Permissions for the following:
 *     - playerparticles.particles.max.1
 *     - playerparticles.particles.max.2
 *     - playerparticles.particles.max.3
 *     - playerparticles.particles.max.4
 *     - playerparticles.particles.max.5
 *     - playerparticles.particles.max.6
 *     - playerparticles.particles.max.7
 *     - playerparticles.particles.max.8
 *     - playerparticles.particles.max.9
 *     - playerparticles.particles.max.10
 *     - playerparticles.particles.max.unlimited
 *     Note: The default max particles in the config.yml is used instead if the playerparticles.particles.max.# is lower
 *     Note: The highest number the user has permission for is how many they are able to use
 *           Ex. they have 4 and 7, they will have a max of 7
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
import org.bukkit.scheduler.BukkitTask;

import com.esophose.playerparticles.command.ParticleCommandHandler;
import com.esophose.playerparticles.database.DatabaseConnector;
import com.esophose.playerparticles.database.MySqlDatabaseConnector;
import com.esophose.playerparticles.database.SqliteDatabaseConnector;
import com.esophose.playerparticles.gui.PlayerParticlesGui;
import com.esophose.playerparticles.manager.DataManager;
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
    private static DatabaseConnector databaseConnector = null;
    
    /**
     * The task that spawns the particles
     */
    private static BukkitTask particleTask = null;

    /**
     * Registers all the styles available by default
     * Saves the default config if it doesn't exist
     * Registers the tab completer and the event listeners
     * Checks if the config needs to be updated to the new version
     * Makes sure the database is accessible
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
        
        this.reload();
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
     * Reloads the settings of the plugin
     */
    public void reload() {
        this.reloadConfig();
        
        // If not null, plugin is already loaded
        if (particleTask != null) {
            particleTask.cancel();
            particleTask = null;
            databaseConnector.closeConnection();
            databaseConnector = null;
            PlayerParticlesGui.forceCloseAllOpenGUIs();
        } else {
            DefaultStyles.registerStyles(); // Only ever load styles once
        }
        
        configureDatabase(getConfig().getBoolean("database-enable"));
        
        DataManager.reload();
        LangManager.reload();
        
        PlayerParticlesGui.setup();
        
        ParticleManager.refreshData();
        startParticleTask();
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
     * Gets the DatabaseConnector that allows querying the database
     * 
     * @return The DatabaseConnector
     */
    public static DatabaseConnector getDBConnector() {
        return databaseConnector;
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
     * 
     * @param useMySql If we should use MySQL as the database type, if false, uses SQLite
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
    			
    			// Try to create the tables just in case they don't exist
    			try (Statement createStatement = connection.createStatement()) {
    			    createStatement.addBatch("CREATE TABLE IF NOT EXISTS pp_group (uuid VARCHAR(36), owner_uuid VARCHAR(36), name VARCHAR(100), PRIMARY KEY(uuid))");
                    createStatement.addBatch("CREATE TABLE IF NOT EXISTS pp_fixed (owner_uuid VARCHAR(36), id SMALLINT, particle_uuid VARCHAR(36), world VARCHAR(100), xPos DOUBLE, yPos DOUBLE, zPos DOUBLE, PRIMARY KEY(owner_uuid, id), FOREIGN KEY(particle_uuid) REFERENCES pp_particle(uuid))");
                    createStatement.addBatch("CREATE TABLE IF NOT EXISTS pp_particle (uuid VARCHAR(36), group_uuid VARCHAR(36), id SMALLINT, effect VARCHAR(100), style VARCHAR(100), item_material VARCHAR(100), block_material VARCHAR(100), note SMALLINT, r SMALLINT, g SMALLINT, b SMALLINT, PRIMARY KEY(uuid))");
                    int[] results = createStatement.executeBatch();
                    if (results[0] + results[1] + results[2] > 0) {
                        getLogger().warning("Updated " + (useMySql ? "MySQL" : "SQLite") + " database schema.");
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
                long ticks = getConfig().getLong("ticks-per-particle");
                particleTask = new ParticleManager().runTaskTimer(playerParticles, 0, ticks);
            }
        }.runTaskLater(playerParticles, 1);
    }

}
