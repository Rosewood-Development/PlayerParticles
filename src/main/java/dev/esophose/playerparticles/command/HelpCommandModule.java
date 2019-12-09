package dev.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LangManager;
import dev.esophose.playerparticles.manager.LangManager.Lang;
import dev.esophose.playerparticles.manager.CommandManager;
import dev.esophose.playerparticles.manager.ParticleGroupPresetManager;
import dev.esophose.playerparticles.particles.PPlayer;

public class HelpCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LangManager.sendMessage(pplayer, Lang.COMMAND_DESCRIPTIONS);
        List<CommandModule> cmds = PlayerParticles.getInstance().getManager(CommandManager.class).getCommands();
        for (CommandModule cmd : cmds)
            if (!(cmd instanceof DefaultCommandModule))
                CommandModule.printUsageWithDescription(pplayer, cmd);
        LangManager.sendSimpleMessage(pplayer, Lang.COMMAND_DESCRIPTIONS_HELP_OTHER);
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return "help";
    }

    public String getDescriptionKey() {
        return "command-description-help";
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
