/*
 * TODO: v6.4+
 * + Add new style(s) 'wings_<type>', multiple new wing types: fairy, demon
 * + Add ability to create/manage fixed effects from the GUI (may get implemented later)
 */

/*
 * TODO: v6.3
 * + Add named colors to the color data
 * * Display effects/styles in the GUI formatted to be more readable
 * + Possibly add a couple new styles (tornado, doubleorbit)
 */

package com.esophose.playerparticles;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.esophose.playerparticles.command.ParticleCommandHandler;
import com.esophose.playerparticles.database.DatabaseConnector;
import com.esophose.playerparticles.database.MySqlDatabaseConnector;
import com.esophose.playerparticles.database.SqliteDatabaseConnector;
import com.esophose.playerparticles.gui.GuiHandler;
import com.esophose.playerparticles.gui.hook.PlayerChatHook;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.manager.SettingManager;
import com.esophose.playerparticles.manager.SettingManager.PSetting;
import com.esophose.playerparticles.particles.PPlayerMovementListener;
import com.esophose.playerparticles.particles.ParticleGroup;
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
     * Executes essential tasks for starting up the plugin
     */
    public void onEnable() {
        pluginInstance = Bukkit.getServer().getPluginManager().getPlugin("PlayerParticles");
        
        this.registerCommands();
        
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ParticleManager(), this);
        pm.registerEvents(new PluginUpdateListener(), this);
        pm.registerEvents(new GuiHandler(), this);
        pm.registerEvents(new PPlayerMovementListener(), this);
        pm.registerEvents(new PlayerChatHook(), this);

        saveDefaultConfig();
        double configVersion = PSetting.VERSION.getDouble();
        double currentVersion = Double.parseDouble(getDescription().getVersion());
        boolean updatePluginSettings = configVersion < currentVersion;
        if (updatePluginSettings) {
            configureDatabase(PSetting.DATABASE_ENABLE.getBoolean());
            DataUpdater.updateData(configVersion, currentVersion);
            databaseConnector.closeConnection();
            databaseConnector = null;
            
            File configFile = new File(getDataFolder(), "config.yml");
            if (configFile.exists()) {
                configFile.delete();
            }
            saveDefaultConfig();
            reloadConfig();
            getLogger().warning("The config.yml has been updated to v" + getDescription().getVersion() + "!");
        }

        if (PSetting.CHECK_UPDATES.getBoolean()) {
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
        
        this.reload(updatePluginSettings);
    }

    /**
     * Clean up database connection if it's open
     * Close all users with an open PlayerParticles GUI
     */
    public void onDisable() {
        databaseConnector.closeConnection();
        GuiHandler.forceCloseAllOpenGUIs();
    }
    
    /**
     * Registers the commands for the plugin
     */
    private void registerCommands() {
        ParticleCommandHandler pch = new ParticleCommandHandler();
        PluginCommand pp = this.getCommand("pp");
        PluginCommand ppo = this.getCommand("ppo");
        
        pp.setTabCompleter(pch);
        pp.setExecutor(pch);
        ppo.setTabCompleter(pch);
        ppo.setExecutor(pch);
    }
    
    /**
     * Reloads the settings of the plugin
     * 
     * @param updatePluginSettings True if the settings should be updated to the latest version of the plugin
     */
    public void reload(boolean updatePluginSettings) {
        this.reloadConfig();
        
        // If not null, plugin is already loaded
        if (particleTask != null) {
            particleTask.cancel();
            particleTask = null;
            databaseConnector.closeConnection();
            databaseConnector = null;
            GuiHandler.forceCloseAllOpenGUIs();
        } else {
            DefaultStyles.registerStyles(); // Only ever load styles once
        }
        
        // This runs before the SettingManager is reloaded, the credentials will not be stored in memory for more than a few milliseconds
        configureDatabase(PSetting.DATABASE_ENABLE.getBoolean());
        DataUpdater.tryCreateTables();
        
        SettingManager.reload();
        LangManager.reload(updatePluginSettings);
        ParticleGroup.reload();
        
        GuiHandler.setup();
        PlayerChatHook.setup();
        
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
     * Determines if we should use MySQL or SQLite based on the useMySql parameter and if we were able to connect successfully
     * 
     * @param useMySql If we should use MySQL as the database type, if false, uses SQLite
     */
    private void configureDatabase(boolean useMySql) {
        if (useMySql) {
            databaseConnector = new MySqlDatabaseConnector();
        } else {
            try {
                Class.forName("org.sqlite.JDBC"); // This is required to put here for Spigot 1.9 and 1.10 for some reason
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            databaseConnector = new SqliteDatabaseConnector(this.getDataFolder().getAbsolutePath());
        }

        if (!databaseConnector.isInitialized()) {
            getLogger().severe("Unable to connect to the MySQL database! Is your login information correct? Falling back to SQLite database instead.");
            configureDatabase(false);
            return;
        }
    }

    /**
     * Starts the task responsible for spawning particles
     * Run in the synchronous task so it starts after all plugins have loaded, including extensions
     */
    private void startParticleTask() {
        final Plugin playerParticles = this;
        new BukkitRunnable() {
            public void run() {
                long ticks = PSetting.TICKS_PER_PARTICLE.getLong();
                particleTask = new ParticleManager().runTaskTimer(playerParticles, 5, ticks);
            }
        }.runTaskLater(playerParticles, 1);
    }

}
