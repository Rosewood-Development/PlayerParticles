package com.esophose.playerparticles.styles;

import org.bukkit.Location;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleSphere implements ParticleStyle {

	@Override
	public PParticle[] getParticles(PPlayer pplayer, Location location) {
		int particleCount = 15;
		float radius = 1.5f;
		PParticle[] particles = new PParticle[particleCount];

		for (int i = 0; i < particleCount; i++) {
			double u = Math.random();
			double v = Math.random();
			double theta = 2 * Math.PI * u;
			double phi = Math.acos(2 * v - 1);
			double x = location.getX() + (radius * Math.sin(phi) * Math.cos(theta));
			double y = location.getY() + (radius * Math.sin(phi) * Math.sin(theta));
			double z = location.getZ() + (radius * Math.cos(phi));
			particles[i] = new PParticle(new Location(location.getWorld(), x, y, z));
		}

		return particles;
	}

	@Override
	public void updateTimers() {

	}

	@Override
	public String getName() {
		return "sphere";
	}

	@Override
	public boolean canBeFixed() {
		return true;
	}

}
