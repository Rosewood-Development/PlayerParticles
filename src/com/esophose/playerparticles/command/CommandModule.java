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
    void onCommandExecute(PPlayer pplayer, String[] args);

    /**
     * Called when a player tries to tab complete this command
     * 
     * @param pplayer The PPlayer who is tab completing this command
     * @param args Arguments typed so far
     * @return A list of possible argument values
     */
    List<String> onTabComplete(PPlayer pplayer, String[] args);

    /**
     * Gets the name of this command
     * 
     * @return The name of this command
     */
    String getName();

    /**
     * Gets the Lang description of this command
     * 
     * @return The description of this command
     */
    Lang getDescription();

    /**
     * Gets any arguments this command has
     * 
     * @return The arguments this command has
     */
    String getArguments();

    /**
     * True if this command requires the player to have any effects
     * 
     * @return If the player must have effects to use this command
     */
    boolean requiresEffects();

    /**
     * @return true if this command can be executed from console, otherwise false
     */
    boolean canConsoleExecute();
    
    /**
     * Displays a command's usage to the player
     * 
     * @param pplayer The PPlayer to display the command usage to
     * @param command The command to display usage for
     */
    static void printUsage(PPlayer pplayer, CommandModule command) {
        LangManager.sendMessage(pplayer, Lang.COMMAND_DESCRIPTIONS_USAGE, command.getName(), command.getArguments());
    }
    
    /**
     * Displays a command's usage (with its description) to the player
     * 
     * @param pplayer The PPlayer to display the command usage to
     * @param command The command to display usage for
     */
    static void printUsageWithDescription(PPlayer pplayer, CommandModule command) {
        if (command.getArguments().length() == 0) {
            LangManager.sendSimpleMessage(pplayer, Lang.COMMAND_DESCRIPTIONS_HELP_1, command.getName(), LangManager.getText(command.getDescription()));
        } else {
            LangManager.sendSimpleMessage(pplayer, Lang.COMMAND_DESCRIPTIONS_HELP_2, command.getName(), command.getArguments(), LangManager.getText(command.getDescription()));
        }
    }

}
