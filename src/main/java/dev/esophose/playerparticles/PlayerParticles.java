/*
 * TODO:
 * + Add ability to create/manage fixed effects from the GUI
 * * Convert fixed effect ids into names
 */

package dev.esophose.playerparticles;

import dev.esophose.playerparticles.gui.hook.PlayerChatHook;
import dev.esophose.playerparticles.hook.ParticlePlaceholderExpansion;
import dev.esophose.playerparticles.hook.PlaceholderAPIHook;
import dev.esophose.playerparticles.hook.WorldGuardHook;
import dev.esophose.playerparticles.manager.CommandManager;
import dev.esophose.playerparticles.manager.ConfigurationManager;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.DataMigrationManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.Manager;
import dev.esophose.playerparticles.manager.ParticleGroupPresetManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.manager.ParticleStyleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.manager.PluginUpdateManager;
import dev.esophose.playerparticles.particles.listener.PPlayerCombatListener;
import dev.esophose.playerparticles.particles.listener.PPlayerMovementListener;
import dev.esophose.playerparticles.util.LegacyMetrics;
import dev.esophose.playerparticles.util.NMSUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Esophose
 */
public class PlayerParticles extends JavaPlugin {

    /**
     * The running instance of PlayerParticles on the server
     */
    private static PlayerParticles INSTANCE;

    /*
     * The plugin managers
     */
    private final Map<Class<?>, Manager> managers;

    public PlayerParticles() {
        INSTANCE = this;
        this.managers = new LinkedHashMap<>();
    }

    /**
     * Executes essential tasks for starting up the plugin
     */
    @Override
    public void onEnable() {
        if (!NMSUtil.isSpigot()) {
            this.getLogger().severe("This plugin is only compatible with Spigot and other forks. CraftBukkit is not supported. Disabling PlayerParticles.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.reload();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PPlayerMovementListener(), this);
        pm.registerEvents(new PPlayerCombatListener(), this);
        pm.registerEvents(new PlayerChatHook(), this);

        if (Setting.SEND_METRICS.getBoolean()) {
            if (NMSUtil.getVersionNumber() > 7) {
                new MetricsLite(this, 3531);
            } else {
                new LegacyMetrics(this);
            }
        }

        if (PlaceholderAPIHook.enabled())
            new ParticlePlaceholderExpansion(this).register();

        PlayerChatHook.setup();
    }

    @Override
    public void onDisable() {
        this.managers.values().forEach(Manager::disable);
        this.managers.clear();
    }

    @Override
    public void onLoad() {
        if (NMSUtil.isSpigot())
            WorldGuardHook.initialize();
    }
    
    /**
     * Gets a manager instance
     *
     * @param managerClass The class of the manager instance to get
     * @param <T> The manager type
     * @return The manager instance or null if one does not exist
     */
    @SuppressWarnings("unchecked")
    public <T extends Manager> T getManager(Class<T> managerClass) {
        if (this.managers.containsKey(managerClass))
            return (T) this.managers.get(managerClass);

        try {
            T manager = managerClass.getConstructor(this.getClass()).newInstance(this);
            this.managers.put(managerClass, manager);
            manager.reload();
            return manager;
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Reloads the plugin
     */
    public void reload() {
        this.managers.values().forEach(Manager::disable);
        this.managers.values().forEach(Manager::reload);

        this.getManager(ConfigurationManager.class);
        this.getManager(LocaleManager.class);
        this.getManager(DataManager.class);
        this.getManager(DataMigrationManager.class);
        this.getManager(PermissionManager.class);
        this.getManager(CommandManager.class);
        this.getManager(ParticleStyleManager.class);
        this.getManager(ParticleGroupPresetManager.class);
        this.getManager(ConfigurationManager.class);
        this.getManager(ParticleManager.class);
        this.getManager(PluginUpdateManager.class);
    }

    /**
     * Gets the instance of the plugin running on the server
     * 
     * @return The PlayerParticles plugin instance
     */
    public static PlayerParticles getInstance() {
        return INSTANCE;
    }

}
