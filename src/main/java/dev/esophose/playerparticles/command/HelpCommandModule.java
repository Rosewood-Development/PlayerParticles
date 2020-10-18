package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.CommandManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.particles.PPlayer;
import java.util.ArrayList;
import java.util.List;

public class HelpCommandModule implements CommandModule {

    @Override
    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        boolean isConsole = pplayer.getPlayer() == null;

        localeManager.sendMessage(pplayer, "command-descriptions");
        List<CommandModule> cmds = PlayerParticles.getInstance().getManager(CommandManager.class).getCommands();
        for (CommandModule cmd : cmds)
            if (!(cmd instanceof DefaultCommandModule) && (!isConsole || cmd.canConsoleExecute()))
                CommandModule.printUsageWithDescription(pplayer, cmd);

        localeManager.sendSimpleMessage(pplayer, "command-descriptions-help-other");
    }

    @Override
    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescriptionKey() {
        return "command-description-help";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public boolean requiresEffectsAndStyles() {
        return false;
    }

    @Override
    public boolean canConsoleExecute() {
        return true;
    }

}
