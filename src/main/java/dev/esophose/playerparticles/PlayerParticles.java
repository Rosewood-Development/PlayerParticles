/*
 * TODO: v7.0
 * + Add ability to create/manage fixed effects from the GUI
 * * Convert fixed effect ids into names
 * + Add effect/style settings folder that lets you disable effects/style and edit style properties
 */

 /*
 * + Added setting to disable particles while in combat
 * * /ppo now uses your permissions instead of the player you are targetting
 * + Added effect/style name customization through config files
 * * Fixed the 'swords' style so you have to be holding a sword/trident
 * * Fixed several styles ignoring the disabled worlds setting
 * + Added a setting 'dust-size' to change the size of dust particles in 1.13+
 * * Cleaned up duplicated command parsing
 * + Added command '/pp fixed teleport <id>' that requires the permission playerparticles.fixed.teleport
 * + Added named colors to the color data autocomplete
 * + Added an API, accessible through the dev.esophose.playerparticles.api.PlayerParticlesAPI class
 * * Refactored the DataManager to only handle saving/loading data
 * * Refactored and cleaned up code to remove static abuse
 * * Changed the package names
 * + Config and lang files will no longer reset every update
 * + Added PlaceholderAPI support
 * * The style 'normal' is no longer granted permission by default
 * * Fixed an issue where 'random' data would not parse properly in preset_groups.yml
 * * Fixed an issue where preset groups would not display in the GUI even if the player has permission for them
 * + Added permission playerparticles.override for /ppo
 * * Changed how * permissions are handled, negative permissions should work now
 */

 /*
  * UPDATED LOCALE MESSAGES:
  * Renamed command-error-no-effects to command-error-missing-effects-or-styles
  * Changed message for command-error-missing-effects-or-styles
  * Added message gui-no-permission
  * Added messages for fixed particles under the section #23.5
 */

package dev.esophose.playerparticles;

import dev.esophose.playerparticles.gui.hook.PlayerChatHook;
import dev.esophose.playerparticles.hook.ParticlePlaceholderExpansion;
import dev.esophose.playerparticles.hook.PlaceholderAPIHook;
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
import dev.esophose.playerparticles.util.Metrics;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
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
    private Map<Class<?>, Manager> managers;

    public PlayerParticles() {
        INSTANCE = this;
        this.managers = new LinkedHashMap<>();
    }

    /**
     * Executes essential tasks for starting up the plugin
     */
    @Override
    public void onEnable() {
        this.reload();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PPlayerMovementListener(), this);
        pm.registerEvents(new PPlayerCombatListener(), this);
        pm.registerEvents(new PlayerChatHook(), this);

        if (Setting.SEND_METRICS.getBoolean())
            new Metrics(this);

        if (PlaceholderAPIHook.enabled())
            new ParticlePlaceholderExpansion(this).register();

        PlayerChatHook.setup();
    }

    @Override
    public void onDisable() {
        this.managers.values().forEach(Manager::disable);
        this.managers.clear();
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
     * Returns the file which contains this plugin
     * Exposes the JavaPlugin.getFile() method
     *
     * @return File containing this plugin
     */
    public File getJarFile() {
        return this.getFile();
    }
    
    /**
     * Reloads the plugin
     */
    public void reload() {
        this.managers.values().forEach(Manager::disable);
        this.managers.values().forEach(Manager::reload);

        this.getManager(CommandManager.class);
        this.getManager(ParticleStyleManager.class);
        this.getManager(ParticleGroupPresetManager.class);
        this.getManager(ConfigurationManager.class);
        this.getManager(DataManager.class);
        this.getManager(DataMigrationManager.class);
        this.getManager(ParticleManager.class);
        this.getManager(LocaleManager.class);
        this.getManager(ConfigurationManager.class);
        this.getManager(PermissionManager.class);
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
