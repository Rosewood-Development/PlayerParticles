package com.esophose.playerparticles.particles;

import java.util.List;

public class ParticleGroup {

	private String name;
	private List<ParticlePair> particles;
	
	public ParticleGroup(String name, List<ParticlePair> particles) {
		this.name = name;
		this.particles = particles;
	}
	
	/**
	 * Get the player-given name of this ParticleGroup
	 * This will be null if it's the player's active ParticleGroup
	 * 
	 * @return The name of this group
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Get the List<ParticlePair> of particles in this group
	 * 
	 * @return The particles in this group
	 */
	public List<ParticlePair> getParticles() {
		return this.particles;
	}
	
}
