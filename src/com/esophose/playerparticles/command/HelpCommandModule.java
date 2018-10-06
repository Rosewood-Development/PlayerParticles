package com.esophose.playerparticles.command;

import java.util.List;

import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;

public class HelpCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        List<CommandModule> cmds = ParticleCommandHandler.getCommands();
        for (CommandModule cmd : cmds) {
            CommandModule.printUsageWithDescription(pplayer, cmd);
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return null;
    }

    public String getName() {
        return "help";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_HELP;
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffects() {
        return false;
    }

}
