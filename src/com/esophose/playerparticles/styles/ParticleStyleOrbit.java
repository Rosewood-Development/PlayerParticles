package com.esophose.playerparticles.styles;

import org.bukkit.Location;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleOrbit implements ParticleStyle {

	private float step = 0;

	public PParticle[] getParticles(PPlayer pplayer, Location location) {
		int orbs = 3;
		PParticle[] particles = new PParticle[orbs];
		for (int i = 0; i < orbs; i++) {
			double dx = -(Math.cos((step / 120) * (Math.PI * 2) + (((Math.PI * 2) / orbs) * i)));
			double dz = -(Math.sin((step / 120) * (Math.PI * 2) + (((Math.PI * 2) / orbs) * i)));
			particles[i] = new PParticle(new Location(location.getWorld(), location.getX() + dx, location.getY(), location.getZ() + dz));
		}
		return particles;
	}

	public void updateTimers() {
		step++;
		if (step > 120) {
			step = 0;
		}
	}

	public String getName() {
		return "orbit";
	}
	
	public boolean canBeFixed() {
		return true;
	}

}
