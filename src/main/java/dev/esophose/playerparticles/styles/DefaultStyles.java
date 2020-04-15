package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.event.ParticleStyleRegistrationEvent;
import dev.esophose.playerparticles.manager.ParticleStyleManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class DefaultStyles implements Listener {

    /**
     * All the styles that are available by default from this plugin
     */
    public static final ParticleStyle ARROWS = new ParticleStyleArrows();
    public static final ParticleStyle BATMAN = new ParticleStyleBatman();
    public static final ParticleStyle BEAM = new ParticleStyleBeam();
    public static final ParticleStyle BLOCKBREAK = new ParticleStyleBlockBreak();
    public static final ParticleStyle BLOCKPLACE = new ParticleStyleBlockPlace();
    public static final ParticleStyle CELEBRATION = new ParticleStyleCelebration();
    public static final ParticleStyle CHAINS = new ParticleStyleChains();
    public static final ParticleStyle COMPANION = new ParticleStyleCompanion();
    public static final ParticleStyle CUBE = new ParticleStyleCube();
    public static final ParticleStyle FEET = new ParticleStyleFeet();
    public static final ParticleStyle FISHING = new ParticleStyleFishing();
    public static final ParticleStyle HALO = new ParticleStyleHalo();
    public static final ParticleStyle HURT = new ParticleStyleHurt();
    public static final ParticleStyle INVOCATION = new ParticleStyleInvocation();
    public static final ParticleStyle MOVE = new ParticleStyleMove();
    public static final ParticleStyle NORMAL = new ParticleStyleNormal();
    public static final ParticleStyle ORBIT = new ParticleStyleOrbit();
    public static final ParticleStyle OVERHEAD = new ParticleStyleOverhead();
    public static final ParticleStyle POINT = new ParticleStylePoint();
    public static final ParticleStyle POPPER = new ParticleStylePopper();
    public static final ParticleStyle PULSE = new ParticleStylePulse();
    public static final ParticleStyle QUADHELIX = new ParticleStyleQuadhelix();
    public static final ParticleStyle RINGS = new ParticleStyleRings();
    public static final ParticleStyle SPHERE = new ParticleStyleSphere();
    public static final ParticleStyle SPIN = new ParticleStyleSpin();
    public static final ParticleStyle SPIRAL = new ParticleStyleSpiral();
    public static final ParticleStyle SWORDS = new ParticleStyleSwords();
    public static final ParticleStyle TELEPORT = new ParticleStyleTeleport();
    public static final ParticleStyle THICK = new ParticleStyleThick();
    public static final ParticleStyle TRAIL = new ParticleStyleTrail();
    public static final ParticleStyle TWINS = new ParticleStyleTwins();
    public static final ParticleStyle VORTEX = new ParticleStyleVortex();
    public static final ParticleStyle WHIRL = new ParticleStyleWhirl();
    public static final ParticleStyle WHIRLWIND = new ParticleStyleWhirlwind();
    public static final ParticleStyle WINGS = new ParticleStyleWings();

    /**
     * Initializes all the default styles
     */
    public static void initStyles() {
        // Register event
        Bukkit.getPluginManager().registerEvents(new DefaultStyles(), PlayerParticles.getInstance());

        // Register their events
        PluginManager pluginManager = Bukkit.getPluginManager();
        PlayerParticles playerParticles = PlayerParticles.getInstance();
        pluginManager.registerEvents((Listener) ARROWS, playerParticles);
        pluginManager.registerEvents((Listener) BLOCKBREAK, playerParticles);
        pluginManager.registerEvents((Listener) BLOCKPLACE, playerParticles);
        pluginManager.registerEvents((Listener) FISHING, playerParticles);
        pluginManager.registerEvents((Listener) HURT, playerParticles);
        pluginManager.registerEvents((Listener) MOVE, playerParticles);
        pluginManager.registerEvents((Listener) SWORDS, playerParticles);
        pluginManager.registerEvents((Listener) TELEPORT, playerParticles);
        pluginManager.registerEvents((Listener) TRAIL, playerParticles);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onParticleStyleRegistration(ParticleStyleRegistrationEvent event) {
        event.registerStyle(ARROWS);
        event.registerStyle(BATMAN);
        event.registerStyle(BEAM);
        event.registerEventStyle(BLOCKBREAK);
        event.registerEventStyle(BLOCKPLACE);
        event.registerStyle(CELEBRATION);
        event.registerStyle(CHAINS);
        event.registerStyle(COMPANION);
        event.registerStyle(CUBE);
        event.registerStyle(FEET);
        event.registerStyle(FISHING);
        event.registerStyle(HALO);
        event.registerEventStyle(HURT);
        event.registerStyle(INVOCATION);
        event.registerEventStyle(MOVE);
        event.registerStyle(NORMAL);
        event.registerStyle(ORBIT);
        event.registerStyle(OVERHEAD);
        event.registerStyle(POINT);
        event.registerStyle(POPPER);
        event.registerStyle(PULSE);
        event.registerStyle(QUADHELIX);
        event.registerStyle(RINGS);
        event.registerStyle(SPHERE);
        event.registerStyle(SPIN);
        event.registerStyle(SPIRAL);
        event.registerEventStyle(SWORDS);
        event.registerEventStyle(TELEPORT);
        event.registerStyle(THICK);
        event.registerEventStyle(TRAIL);
        event.registerStyle(TWINS);
        event.registerStyle(VORTEX);
        event.registerStyle(WHIRL);
        event.registerStyle(WHIRLWIND);
        event.registerStyle(WINGS);
    }

    /**
     * Reloads the settings for all default styles
     *
     * @param particleStyleManager The ParticleStyleManager instance
     */
    public static void reloadSettings(ParticleStyleManager particleStyleManager) {
        for (ParticleStyle style : particleStyleManager.getStylesWithDisabled())
            if (style instanceof DefaultParticleStyle)
                ((DefaultParticleStyle) style).loadSettings(true);
    }

}
