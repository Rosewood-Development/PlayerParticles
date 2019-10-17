/*
 * TODO: v6.4
 * + Add ability to create/manage fixed effects from the GUI
 * * Convert fixed effect ids into names
 * + Add command '/pp fixed teleport <id>' that requires the permission playerparticles.fixed.teleport
 * + Add named colors to the color data
 * * Clean up duplicated command parsing
 */

/*
 * TODO: v6.5
 * + Add effect/style name customization through config files
 * + Add effect/style settings folder that lets you disable effects/style and edit style properties
 */

package dev.esophose.playerparticles;

import java.io.File;

import dev.esophose.playerparticles.command.ParticleCommandHandler;
import dev.esophose.playerparticles.database.DatabaseConnector;
import dev.esophose.playerparticles.database.MySqlDatabaseConnector;
import dev.esophose.playerparticles.database.SqliteDatabaseConnector;
import dev.esophose.playerparticles.gui.GuiHandler;
import dev.esophose.playerparticles.gui.hook.PlayerChatHook;
import dev.esophose.playerparticles.manager.LangManager;
import dev.esophose.playerparticles.manager.ParticleGroupPresetManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.manager.SettingManager;
import dev.esophose.playerparticles.manager.SettingManager.PSetting;
import dev.esophose.playerparticles.particles.PPlayerMovementListener;
import dev.esophose.playerparticles.styles.DefaultStyles;
import dev.esophose.playerparticles.updater.DataUpdater;
import dev.esophose.playerparticles.updater.PluginUpdateListener;
import dev.esophose.playerparticles.updater.Updater;
import dev.esophose.playerparticles.util.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayerParticles extends JavaPlugin {

    /**
     * The running instance of PlayerParticles on the server
     */
    private static PlayerParticles pluginInstance;

    /**
     * The version a new update has, will be null if the config has it disabled
     * or if there is no new version
     */
    private String updateVersion = null;

    /**
     * The database connection manager
     */
    private DatabaseConnector databaseConnector = null;
    
    /**
     * The task that spawns the particles
     */
    private BukkitTask particleTask = null;

    /**
     * Executes essential tasks for starting up the plugin
     */
    public void onEnable() {
        pluginInstance = (PlayerParticles) Bukkit.getServer().getPluginManager().getPlugin("PlayerParticles");
        
        this.registerCommands();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ParticleManager(), this);
        pm.registerEvents(new PluginUpdateListener(), this);
        pm.registerEvents(new GuiHandler(), this);
        pm.registerEvents(new PPlayerMovementListener(), this);
        pm.registerEvents(new PlayerChatHook(), this);

        this.saveDefaultConfig();
        double configVersion = PSetting.VERSION.getDouble();
        double currentVersion = Double.parseDouble(this.getDescription().getVersion());
        boolean updatePluginSettings = configVersion < currentVersion;
        if (updatePluginSettings) {
            this.configureDatabase(PSetting.DATABASE_ENABLE.getBoolean());
            DataUpdater.updateData(configVersion, currentVersion);
            this.databaseConnector.closeConnection();
            this.databaseConnector = null;
            
            File configFile = new File(this.getDataFolder(), "config.yml");
            if (configFile.exists()) {
                configFile.delete();
            }
            this.saveDefaultConfig();
            this.reloadConfig();
            this.getLogger().warning("The config.yml has been updated to v" + this.getDescription().getVersion() + "!");
        }

        if (PSetting.CHECK_UPDATES.getBoolean()) {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                try { // This can throw an exception if the server has no internet connection or if the Curse API is down
                    Updater updater = new Updater(pluginInstance, 82823, getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
                    if (Double.parseDouble(updater.getLatestName().replaceAll("PlayerParticles v", "")) > Double.parseDouble(getPlugin().getDescription().getVersion())) {
                        updateVersion = updater.getLatestName().replaceAll("PlayerParticles v", "");
                        getLogger().info("An update (v" + updateVersion + ") is available! You are running v" + getPlugin().getDescription().getVersion());
                    }
                } catch (Exception e) {
                    getLogger().warning("An error occurred checking for an update. There is either no established internet connection or the Curse API is down.");
                }
            });
        }
        
        if (PSetting.SEND_METRICS.getBoolean()) {
            new Metrics(this);
        }
        
        this.reload(updatePluginSettings);
    }

    /**
     * Clean up database connection if it's open
     * Close all users with an open PlayerParticles GUI
     */
    public void onDisable() {
        this.databaseConnector.closeConnection();
        GuiHandler.forceCloseAllOpenGUIs();
    }
    
    /**
     * Registers the commands for the plugin
     */
    private void registerCommands() {
        ParticleCommandHandler pch = new ParticleCommandHandler();
        PluginCommand pp = this.getCommand("pp");
        PluginCommand ppo = this.getCommand("ppo");

        if (pp == null || ppo == null) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        
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
        if (this.particleTask != null) {
            this.particleTask.cancel();
            this.particleTask = null;
            this.databaseConnector.closeConnection();
            this.databaseConnector = null;
            GuiHandler.forceCloseAllOpenGUIs();
        } else {
            DefaultStyles.registerStyles(); // Only ever load styles once
        }
        
        // This runs before the SettingManager is reloaded, the credentials will not be stored in memory for more than a few milliseconds
        this.configureDatabase(PSetting.DATABASE_ENABLE.getBoolean());
        DataUpdater.tryCreateTables();
        
        SettingManager.reload();
        LangManager.reload(updatePluginSettings);
        ParticleGroupPresetManager.reload();
        
        GuiHandler.setup();
        PlayerChatHook.setup();
        
        ParticleManager.refreshData();
        this.startParticleTask();
    }

    /**
     * Gets the instance of the plugin running on the server
     * 
     * @return The PlayerParticles plugin instance
     */
    public static PlayerParticles getPlugin() {
        return pluginInstance;
    }
    
    /**
     * Gets the DatabaseConnector that allows querying the database
     * 
     * @return The DatabaseConnector
     */
    public DatabaseConnector getDBConnector() {
        return this.databaseConnector;
    }
    
    /**
     * Gets the latest available version of the plugin
     * Will be null if no update is available
     * 
     * @return The latest version available for the plugin
     */
    public String getUpdateVersion() {
        return this.updateVersion;
    }

    /**
     * Determines if we should use MySQL or SQLite based on the useMySql parameter and if we were able to connect successfully
     * 
     * @param useMySql If we should use MySQL as the database type, if false, uses SQLite
     */
    private void configureDatabase(boolean useMySql) {
        if (useMySql) {
            this.databaseConnector = new MySqlDatabaseConnector();
        } else {
            try {
                Class.forName("org.sqlite.JDBC"); // This is required to put here for Spigot 1.9 and 1.10 for some reason
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.databaseConnector = new SqliteDatabaseConnector(this.getDataFolder().getAbsolutePath());
        }

        if (!this.databaseConnector.isInitialized()) {
            this.getLogger().severe("Unable to connect to the MySQL database! Is your login information correct? Falling back to SQLite database instead.");
            this.configureDatabase(false);
        }
    }

    /**
     * Starts the task responsible for spawning particles
     * Run in the synchronous task so it starts after all plugins have loaded, including extensions
     */
    private void startParticleTask() {
        Bukkit.getScheduler().runTaskLater(pluginInstance, () -> {
            long ticks = PSetting.TICKS_PER_PARTICLE.getLong();
            this.particleTask = new ParticleManager().runTaskTimer(pluginInstance, 5, ticks);
        }, 1);
    }

}
