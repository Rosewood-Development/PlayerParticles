package com.esophose.playerparticles.styles;

import org.bukkit.Location;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStylePoint implements ParticleStyle {

	public PParticle[] getParticles(PPlayer pplayer, Location location) {
		return new PParticle[] { new PParticle(location.add(0.0, 1.5, 0.0)) };
	}

	public void updateTimers() {

	}

	public String getName() {
		return "point";
	}

	public boolean canBeFixed() {
		return true;
	}

}
