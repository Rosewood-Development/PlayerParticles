package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.CommandManager;
import dev.esophose.playerparticles.particles.PPlayer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.util.StringUtil;

public class DefaultCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        // The default command just opens the GUI, execute the GUICommandModule
        ((GUICommandModule) PlayerParticles.getInstance().getManager(CommandManager.class).findMatchingCommand("gui"))
                .onCommandExecute(pplayer, new String[0], false);
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        List<String> matches = new ArrayList<>();
        List<String> commandNames = PlayerParticles.getInstance().getManager(CommandManager.class).getCommandNames();
        
        if (args.length == 0)
            return commandNames;
        
        StringUtil.copyPartialMatches(args[0], commandNames, matches);

        return matches;
    }

    public String getName() {
        return "";
    }

    public String getDescriptionKey() {
        return "command-description-default";
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffectsAndStyles() {
        return false;
    }

    public boolean canConsoleExecute() {
        return false;
    }

}
