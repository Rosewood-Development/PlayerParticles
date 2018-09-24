package com.esophose.playerparticles.command;

import com.esophose.playerparticles.particles.PPlayer;

public interface CommandModule {

	public void onCommandExecute(PPlayer pplayer, String[] args);
	
	public void onTabComplete(PPlayer pplayer, String[] args);
	
	public String getName();
	
	public String getArguments();
	
}
