package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;

public class HelpCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LangManager.sendMessage(pplayer, Lang.COMMAND_DESCRIPTIONS);
        List<CommandModule> cmds = ParticleCommandHandler.getCommands();
        for (CommandModule cmd : cmds)
            if (!(cmd instanceof DefaultCommandModule))
                CommandModule.printUsageWithDescription(pplayer, cmd);
        LangManager.sendSimpleMessage(pplayer, Lang.COMMAND_DESCRIPTIONS_HELP_OTHER);
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<String>();
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
