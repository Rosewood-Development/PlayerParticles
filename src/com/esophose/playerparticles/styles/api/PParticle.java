package com.esophose.playerparticles.styles.api;

import org.bukkit.Location;

public class PParticle {

	private Location location;
	private float speed;
	private float xOff, yOff, zOff;

	/**
	 * The constructor with all the fancy parameters for customization
	 * 
	 * @param location The location to display the particle at
	 * @param xOff The offset for the x-axis
	 * @param yOff The offset for the y-axis
	 * @param zOff The offset for the z-axis
	 * @param speed The speed the particle will move at
	 */
	public PParticle(Location location, float xOff, float yOff, float zOff, float speed) {
		this.location = location;
		this.xOff = xOff;
		this.yOff = yOff;
		this.zOff = zOff;
		this.speed = speed;
	}

	/**
	 * The constructor used if you just want stand-still particles
	 * Useful for making shapes with the styles
	 * 
	 * @param location The location to display the particles at
	 */
	public PParticle(Location location) {
		this(location, 0.0F, 0.0F, 0.0F, 0.0F);
	}

	/**
	 * Gets the location that the particle will be displayed at
	 * Offsets must be applied manually if this is a colorable effect
	 * 
	 * @param colorable Whether or not this effect is colorable and we need to manually
	 *            adjust for the offsets or not
	 * @return The location, either as normal or modified with the offsets
	 */
	public Location getLocation(boolean colorable) {
		if (!colorable) {
			return this.location;
		} else {
			double x = this.location.getX();
			double y = this.location.getY();
			double z = this.location.getZ();

			x += this.xOff * 1.75D * (Math.random() > 0.5 ? Math.random() : -Math.random());
			y += this.yOff * 1.75D * (Math.random() > 0.5 ? Math.random() : -Math.random());
			z += this.zOff * 1.75D * (Math.random() > 0.5 ? Math.random() : -Math.random());

			return new Location(this.location.getWorld(), x, y, z);
		}
	}

	/**
	 * Gets the speed of the particle
	 * 
	 * @return The particle's speed
	 */
	public float getSpeed() {
		return this.speed;
	}

	/**
	 * Gets the offset on the x axis for the particle
	 * 
	 * @return The x-axis offset
	 */
	public float getXOff() {
		return this.xOff;
	}

	/**
	 * Gets the offset on the y-axis for the particle
	 * 
	 * @return The y-axis offset
	 */
	public float getYOff() {
		return this.yOff;
	}

	/**
	 * Gets the offset on the z-axis for the particle
	 * 
	 * @return The z-axis offset
	 */
	public float getZOff() {
		return this.zOff;
	}

}
