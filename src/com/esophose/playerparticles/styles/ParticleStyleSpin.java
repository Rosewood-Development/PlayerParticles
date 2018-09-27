package com.esophose.playerparticles.styles;

import java.util.Collections;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleSpin implements ParticleStyle {

	private int step = 0;

	public List<PParticle> getParticles(ParticlePair particle, Location location) {
		int points = 15;
		double radius = .5;
		double slice = 2 * Math.PI / points;
		double angle = slice * (step % 15);
		double newX = location.getX() + radius * Math.cos(angle);
		double newY = location.getY() + 1.5;
		double newZ = location.getZ() + radius * Math.sin(angle);
		return Collections.singletonList(new PParticle(new Location(location.getWorld(), newX, newY, newZ)));
	}

	public void updateTimers() {
		step++;
		if (step > 30) {
			step = 0;
		}
	}

	public String getName() {
		return "spin";
	}
	
	public boolean canBeFixed() {
		return true;
    }

}
