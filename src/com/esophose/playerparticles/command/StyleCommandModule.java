package com.esophose.playerparticles.command;

import java.util.List;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;

public class StyleCommandModule implements CommandModule {

	public void onCommandExecute(PPlayer pplayer, String[] args) {
		LangManager.sendMessage(pplayer, Lang.COMMAND_REMOVED);
	}

	public List<String> onTabComplete(PPlayer pplayer, String[] args) {
		return null;
	}

	public String getName() {
		return "style";
	}

	public String getDescription() {
		return Lang.STYLE_COMMAND_DESCRIPTION.get();
	}

	public String getArguments() {
		return "";
	}
	
	public boolean requiresEffects() {
		return true;
	}
	
}
