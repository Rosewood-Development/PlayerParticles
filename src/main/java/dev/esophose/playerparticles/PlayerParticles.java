/*
 * TODO:
 * + Add ability to create/manage fixed effects from the GUI
 * * Convert fixed effect ids into names
 */

package dev.esophose.playerparticles;

import dev.esophose.playerparticles.config.Settings;
import dev.esophose.playerparticles.hook.ParticlePlaceholderExpansion;
import dev.esophose.playerparticles.hook.WorldGuardHook;
import dev.esophose.playerparticles.manager.CommandManager;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.ParticleGroupPresetManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.manager.ParticlePackManager;
import dev.esophose.playerparticles.manager.ParticleStyleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.listener.PPlayerCombatListener;
import dev.esophose.playerparticles.particles.listener.PPlayerMovementListener;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.SettingHolder;
import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import dev.rosewood.rosegarden.manager.Manager;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

/**
 * @author Esophose
 */
public class PlayerParticles extends RosePlugin {

    /**
     * The running instance of PlayerParticles on the server
     */
    private static PlayerParticles INSTANCE;

    public PlayerParticles() {
        super(40261, 3531, DataManager.class, LocaleManager.class, null);
        INSTANCE = this;
    }

    /**
     * Executes essential tasks for starting up the plugin
     */
    @Override
    protected void enable() {
        if (!this.isSpigot()) {
            this.getLogger().severe("This plugin is only compatible with Spigot and other forks. CraftBukkit is not supported. Disabling PlayerParticles.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PPlayerMovementListener(), this);
        pm.registerEvents(new PPlayerCombatListener(), this);

        if (PlaceholderAPIHook.enabled())
            new ParticlePlaceholderExpansion(this).register();

        Settings.GuiIcon.reset();
    }

    @Override
    protected void disable() {

    }

    @Override
    public void onLoad() {
        if (this.isSpigot())
            WorldGuardHook.initialize();
    }

    @Override
    protected List<Class<? extends Manager>> getManagerLoadPriority() {
        return Arrays.asList(
                ParticlePackManager.class,
                PermissionManager.class,
                CommandManager.class,
                ParticleStyleManager.class,
                ParticleGroupPresetManager.class,
                ParticleManager.class
        );
    }

    @Override
    protected SettingHolder getRoseConfigSettingHolder() {
        return Settings.INSTANCE;
    }

    @Override
    protected String[] getRoseConfigHeader() {
        return new String[] {
                "     _________ __                           __________                __   __        __",
                "     \\______  \\  | _____  ___ __  __________\\______   \\_____ ________/  |_|__| ____ |  |   ____   ______",
                "     |     ___/  | \\__  \\<   |  |/ __ \\_  __ \\     ___/\\__  \\\\_  __ \\   __\\  |/ ___\\|  | _/ __ \\ /  ___/",
                "     |    |   |  |__/ __ \\\\___  \\  ___/|  | \\/    |     / __ \\|  | \\/|  | |  \\  \\___|  |_\\  ___/ \\___ \\",
                "     |____|   |____(____  / ____|\\___  >__|  |____|    (____  /__|   |__| |__|\\___  >____/\\___  >____  >",
                "                        \\/\\/         \\/                     \\/                    \\/          \\/     \\/"
        };
    }

    /**
     * Gets the instance of the plugin running on the server
     * 
     * @return The PlayerParticles plugin instance
     */
    public static PlayerParticles getInstance() {
        return INSTANCE;
    }

    /**
     * @return true if the server is running Spigot or a fork, false otherwise
     */
    private boolean isSpigot() {
        try {
            Class.forName("org.spigotmc.SpigotConfig");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


}
