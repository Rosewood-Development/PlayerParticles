package com.esophose.playerparticles.command;

import java.util.List;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;

public class EffectCommandModule implements CommandModule {

	public void onCommandExecute(PPlayer pplayer, String[] args) {
		LangManager.sendMessage(pplayer, Lang.COMMAND_REMOVED);
	}

	public List<String> onTabComplete(PPlayer pplayer, String[] args) {
		return null;
	}

	public String getName() {
		return "effect";
	}

	public String getDescription() {
		return Lang.EFFECT_COMMAND_DESCRIPTION.get();
	}

	public String getArguments() {
		return "";
	}
	
	public boolean requiresEffects() {
		return true;
	}
	
}
