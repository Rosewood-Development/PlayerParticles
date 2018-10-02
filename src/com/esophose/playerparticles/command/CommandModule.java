package com.esophose.playerparticles.command;

import java.util.List;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;

public interface CommandModule {

    /**
     * Called when this command gets executed
     * 
     * @param pplayer The PPlayer who executed this command
     * @param args The arguments to this command
     */
    public void onCommandExecute(PPlayer pplayer, String[] args);

    /**
     * Called when a player tries to tab complete this command
     * 
     * @param pplayer The PPlayer who is tab completing this command
     * @param args Arguments typed so far
     */
    public List<String> onTabComplete(PPlayer pplayer, String[] args);

    /**
     * Gets the name of this command
     * 
     * @return The name of this command
     */
    public String getName();

    /**
     * Gets the Lang description of this command
     * 
     * @return The description of this command
     */
    public Lang getDescription();

    /**
     * Gets any arguments this command has
     * 
     * @return The arguments this command has
     */
    public String getArguments();

    /**
     * True if this command requires the player to have any effects
     * 
     * @return If the player must have effects to use this command
     */
    public boolean requiresEffects();
    
    /**
     * Displays a command's usage to the player
     * 
     * @param pplayer The PPlayer to display the command usage to
     * @param command The command to display usage for
     */
    public static void printUsage(PPlayer pplayer, CommandModule command) {
        LangManager.sendCustomMessage(pplayer, String.format("/{0} {1}", command.getName(), command.getArguments()));
    }
    
    /**
     * Displays a command's sub-command usage to the player
     * 
     * @param pplayer The PPlayer to display the command usage to
     * @param command The command to display usage for
     * @param subCommandName The name of the command's sub-command to display usage for
     * @param subCommandArgs The sub-command's arguments
     */
    public static void printSubcommandUsage(PPlayer pplayer, CommandModule command, String subCommandName, String subCommandArgs) {
        LangManager.sendCustomMessage(pplayer, String.format("/{0} {1} {2}", command.getName(), subCommandName, subCommandArgs));
    }

}
