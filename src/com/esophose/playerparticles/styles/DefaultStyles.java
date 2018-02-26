package com.esophose.playerparticles.styles;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.styles.api.ParticleStyleManager;

public class DefaultStyles {

    /**
     * All the styles that are available by default from this plugin
     */
    public static final ParticleStyle NONE = new ParticleStyleNone();
    public static final ParticleStyle BEAM = new ParticleStyleBeam();
    public static final ParticleStyle HALO = new ParticleStyleHalo();
    public static final ParticleStyle POINT = new ParticleStylePoint();
    public static final ParticleStyle MOVE = new ParticleStyleMove();
    public static final ParticleStyle SPIN = new ParticleStyleSpin();
    public static final ParticleStyle QUADHELIX = new ParticleStyleQuadhelix();
    public static final ParticleStyle ORBIT = new ParticleStyleOrbit();
    public static final ParticleStyle FEET = new ParticleStyleFeet();
    public static final ParticleStyle CUBE = new ParticleStyleCube();
    public static final ParticleStyle ARROWS = new ParticleStyleArrows();
    public static final ParticleStyle SPIRAL = new ParticleStyleSpiral();
    public static final ParticleStyle THICK = new ParticleStyleThick();
    public static final ParticleStyle WINGS = new ParticleStyleWings();
    public static final ParticleStyle SPHERE = new ParticleStyleSphere();

    /**
     * Registers all the default styles to the ParticleStyleManager
     */
    public static void registerStyles() {
        ParticleStyleManager.registerStyle(NONE);
        ParticleStyleManager.registerStyle(BEAM);
        ParticleStyleManager.registerStyle(HALO);
        ParticleStyleManager.registerStyle(POINT);
        ParticleStyleManager.registerCustomHandledStyle(MOVE);
        ParticleStyleManager.registerStyle(SPIN);
        ParticleStyleManager.registerStyle(QUADHELIX);
        ParticleStyleManager.registerStyle(ORBIT);
        ParticleStyleManager.registerStyle(FEET);
        ParticleStyleManager.registerStyle(CUBE);
        ParticleStyleManager.registerStyle(ARROWS);
        ParticleStyleManager.registerStyle(SPIRAL);
        ParticleStyleManager.registerStyle(THICK);
        ParticleStyleManager.registerStyle(WINGS);
        ParticleStyleManager.registerStyle(SPHERE);

        Bukkit.getPluginManager().registerEvents((Listener) MOVE, PlayerParticles.getPlugin());
        Bukkit.getPluginManager().registerEvents((Listener) ARROWS, PlayerParticles.getPlugin());
    }

}
