package com.esophose.playerparticles.styles;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.styles.api.ParticleStyleManager;

public class DefaultStyles {

    /**
     * All the styles that are available by default from this plugin
     */
    public static final ParticleStyle ARROWS = new ParticleStyleArrows();
    public static final ParticleStyle BATMAN = new ParticleStyleBatman();
    public static final ParticleStyle BEAM = new ParticleStyleBeam();
    public static final ParticleStyle BLOCKBREAK = new ParticleStyleBlockBreak();
    public static final ParticleStyle BLOCKEDIT = new ParticleStyleBlockEdit();
    public static final ParticleStyle BLOCKPLACE = new ParticleStyleBlockPlace();
    public static final ParticleStyle CELEBRATION = new ParticleStyleCelebration();
    public static final ParticleStyle CHAINS = new ParticleStyleChains();
    public static final ParticleStyle COMPANION = new ParticleStyleCompanion();
    public static final ParticleStyle CUBE = new ParticleStyleCube();
    public static final ParticleStyle FEET = new ParticleStyleFeet();
    public static final ParticleStyle HALO = new ParticleStyleHalo();
    public static final ParticleStyle HURT = new ParticleStyleHurt();
    public static final ParticleStyle INVOCATION = new ParticleStyleInvocation();
    public static final ParticleStyle MOVE = new ParticleStyleMove();
    public static final ParticleStyle NORMAL = new ParticleStyleNormal();
    public static final ParticleStyle ORBIT = new ParticleStyleOrbit();
    public static final ParticleStyle OVERHEAD = new ParticleStyleOverhead();
    public static final ParticleStyle POINT = new ParticleStylePoint();
    public static final ParticleStyle QUADHELIX = new ParticleStyleQuadhelix();
    public static final ParticleStyle RINGS = new ParticleStyleRings();
    public static final ParticleStyle SPHERE = new ParticleStyleSphere();
    public static final ParticleStyle SPIN = new ParticleStyleSpin();
    public static final ParticleStyle SPIRAL = new ParticleStyleSpiral();
    public static final ParticleStyle SWORDS = new ParticleStyleSwords();
    public static final ParticleStyle THICK = new ParticleStyleThick();
    public static final ParticleStyle VORTEX = new ParticleStyleVortex();
    public static final ParticleStyle WINGS = new ParticleStyleWings();

    /**
     * Registers all the default styles to the ParticleStyleManager
     * Registered in alphabetical order
     */
    public static void registerStyles() {
        ParticleStyleManager.registerStyle(ARROWS);
        ParticleStyleManager.registerStyle(BATMAN);
        ParticleStyleManager.registerStyle(BEAM);
        ParticleStyleManager.registerCustomHandledStyle(BLOCKBREAK);
        ParticleStyleManager.registerCustomHandledStyle(BLOCKEDIT);
        ParticleStyleManager.registerCustomHandledStyle(BLOCKPLACE);
        ParticleStyleManager.registerStyle(CELEBRATION);
        ParticleStyleManager.registerStyle(CHAINS);
        ParticleStyleManager.registerStyle(COMPANION);
        ParticleStyleManager.registerStyle(CUBE);
        ParticleStyleManager.registerStyle(FEET);
        ParticleStyleManager.registerStyle(HALO);
        ParticleStyleManager.registerCustomHandledStyle(HURT);
        ParticleStyleManager.registerStyle(INVOCATION);
        ParticleStyleManager.registerCustomHandledStyle(MOVE);
        ParticleStyleManager.registerStyle(NORMAL);
        ParticleStyleManager.registerStyle(ORBIT);
        ParticleStyleManager.registerStyle(OVERHEAD);
        ParticleStyleManager.registerStyle(POINT);
        ParticleStyleManager.registerStyle(QUADHELIX);
        ParticleStyleManager.registerStyle(RINGS);
        ParticleStyleManager.registerStyle(SPHERE);
        ParticleStyleManager.registerStyle(SPIN);
        ParticleStyleManager.registerStyle(SPIRAL);
        ParticleStyleManager.registerCustomHandledStyle(SWORDS);
        ParticleStyleManager.registerStyle(THICK);
        ParticleStyleManager.registerStyle(VORTEX);
        ParticleStyleManager.registerStyle(WINGS);

        // Register their events
        PluginManager manager = Bukkit.getPluginManager();
        Plugin playerParticles = PlayerParticles.getPlugin();
        manager.registerEvents((Listener) ARROWS, playerParticles);
        manager.registerEvents((Listener) BLOCKBREAK, playerParticles);
        manager.registerEvents((Listener) BLOCKPLACE, playerParticles);
        manager.registerEvents((Listener) BLOCKEDIT, playerParticles);
        manager.registerEvents((Listener) HURT, playerParticles);
        manager.registerEvents((Listener) MOVE, playerParticles);
        manager.registerEvents((Listener) SWORDS, playerParticles);
    }

}
