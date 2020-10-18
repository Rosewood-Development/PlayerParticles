package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.CommandManager;
import dev.esophose.playerparticles.particles.PPlayer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.util.StringUtil;

public class DefaultCommandModule implements CommandModule {

    @Override
    public void onCommandExecute(PPlayer pplayer, String[] args) {
        // The default command just opens the GUI, execute the GUICommandModule
        ((GUICommandModule) PlayerParticles.getInstance().getManager(CommandManager.class).findMatchingCommand("gui"))
                .onCommandExecute(pplayer, new String[0], false);
    }

    @Override
    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        List<String> matches = new ArrayList<>();
        List<String> commandNames = PlayerParticles.getInstance().getManager(CommandManager.class).getCommandNames();
        
        if (args.length == 0)
            return commandNames;
        
        StringUtil.copyPartialMatches(args[0], commandNames, matches);

        return matches;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getDescriptionKey() {
        return "command-description-default";
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
        return false;
    }

}
