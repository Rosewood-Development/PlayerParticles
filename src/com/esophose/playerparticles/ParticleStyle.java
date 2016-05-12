/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles;

public enum ParticleStyle {

	NONE,
	SPIRAL,
	HALO,
	POINT,
	MOVE,
	SPIN,
	QUADHELIX,
	ORB;
	
	/**
	 * Gets the ParticleStyle with the name given, returns null if not found
	 * 
	 * @param particle The string of the style to search for
	 * @return The ParticleStyle with the name requested
	 */
	public static ParticleStyle styleFromString(String particle){
		for(ParticleStyle style : ParticleStyle.values()){
			if(style.toString().toLowerCase().replace("_", "").equals(particle)) return style;
		}
		return ParticleStyle.NONE;
	}
		
}
