package com.esophose.playerparticles.command;

import java.util.List;

import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;

public class HelpCommandModule implements CommandModule {
	
	public void onCommandExecute(PPlayer pplayer, String[] args) {

	}

	public List<String> onTabComplete(PPlayer pplayer, String[] args) {
		return null;
	}

	public String getName() {
		return "help";
	}

	public String getDescription() {
		return Lang.HELP_COMMAND_DESCRIPTION.get();
	}

	public String getArguments() {
		return "";
	}
	
	public boolean requiresEffects() {
		return false;
	}
	
}
