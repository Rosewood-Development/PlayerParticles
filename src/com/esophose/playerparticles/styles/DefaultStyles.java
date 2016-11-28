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
	public static ParticleStyle NONE = new ParticleStyleNone();
	public static ParticleStyle SPIRAL = new ParticleStyleSpiral();
	public static ParticleStyle HALO = new ParticleStyleHalo();
	public static ParticleStyle POINT = new ParticleStylePoint();
	public static ParticleStyle MOVE = new ParticleStyleMove();
	public static ParticleStyle SPIN = new ParticleStyleSpin();
	public static ParticleStyle QUADHELIX = new ParticleStyleQuadhelix();
	public static ParticleStyle ORBIT = new ParticleStyleOrbit();
	public static ParticleStyle FEET = new ParticleStyleFeet();
	public static ParticleStyle CUBE = new ParticleStyleCube();

	/**
	 * Registers all the default styles to the ParticleStyleManager
	 */
	public static void registerStyles() {
		ParticleStyleManager.registerStyle(NONE);
		ParticleStyleManager.registerStyle(SPIRAL);
		ParticleStyleManager.registerStyle(HALO);
		ParticleStyleManager.registerStyle(POINT);
		ParticleStyleManager.registerCustomHandledStyle(MOVE);
		ParticleStyleManager.registerStyle(SPIN);
		ParticleStyleManager.registerStyle(QUADHELIX);
		ParticleStyleManager.registerStyle(ORBIT);
		ParticleStyleManager.registerStyle(FEET);
		ParticleStyleManager.registerStyle(CUBE);
		
		Bukkit.getPluginManager().registerEvents((Listener) MOVE, PlayerParticles.getPlugin());
	}

}
