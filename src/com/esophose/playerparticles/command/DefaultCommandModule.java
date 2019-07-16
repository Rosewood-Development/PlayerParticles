package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.StringUtil;

import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;

public class DefaultCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        // The default command just opens the GUI, execute the GUICommandModule
        ParticleCommandHandler.findMatchingCommand("gui").onCommandExecute(pplayer, new String[] { "_byDefault_" });
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        List<String> matches = new ArrayList<>();
        List<String> commandNames = ParticleCommandHandler.getCommandNames();
        
        if (args.length == 0) return commandNames;
        
        StringUtil.copyPartialMatches(args[0], commandNames, matches);

        return matches;
    }

    public String getName() {
        return "";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_DEFAULT;
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffects() {
        return false;
    }

    public boolean canConsoleExecute() {
        return false;
    }

}
