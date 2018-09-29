package com.esophose.playerparticles.command;

import java.util.List;

import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;

public class DefaultCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        // The default command just opens the GUI, execute the GUICommandModule
        ParticleCommandHandler.findMatchingCommand("gui").onCommandExecute(pplayer, args);
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return null;
    }

    public String getName() {
        return "";
    }

    public Lang getDescription() {
        return Lang.DEFAULT_COMMAND_DESCRIPTION;
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffects() {
        return true;
    }

}
