package dev.esophose.playerparticles.command;

import java.util.List;
import org.bukkit.command.CommandSender;

public interface CommandModuleSecondary {

    /**
     * Called when this command gets executed
     * 
     * @param sender The CommandSender who executed this command
     * @param args The arguments to this command
     */
    void onCommandExecute(CommandSender sender, String[] args);

    /**
     * Called when a player tries to tab complete this command
     * 
     * @param sender The CommandSender who is tab completing this command
     * @param args Arguments typed so far
     * @return A list of possible argument values
     */
    List<String> onTabComplete(CommandSender sender, String[] args);

}
