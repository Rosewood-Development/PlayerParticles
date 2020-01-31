package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.List;

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
     * Gets the locale description key of this command
     * 
     * @return The locale description key of this command
     */
    String getDescriptionKey();

    /**
     * Gets any arguments this command has
     * 
     * @return The arguments this command has
     */
    String getArguments();

    /**
     * True if this command requires the player to have any effects and styles
     * 
     * @return If the player must have effects and styles to use this command
     */
    boolean requiresEffectsAndStyles();

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
        StringPlaceholders placeholders = StringPlaceholders.builder("cmd", command.getName()).addPlaceholder("args", command.getArguments()).build();
        PlayerParticles.getInstance().getManager(LocaleManager.class).sendMessage(pplayer, "command-descriptions-usage", placeholders);
    }
    
    /**
     * Displays a command's usage (with its description) to the player
     * 
     * @param pplayer The PPlayer to display the command usage to
     * @param command The command to display usage for
     */
    static void printUsageWithDescription(PPlayer pplayer, CommandModule command) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        if (command.getArguments().length() == 0) {
            StringPlaceholders placeholders = StringPlaceholders.builder("cmd", command.getName())
                    .addPlaceholder("desc", localeManager.getLocaleMessage(command.getDescriptionKey()))
                    .build();
            localeManager.sendSimpleMessage(pplayer, "command-descriptions-help-1", placeholders);
        } else {
            StringPlaceholders placeholders = StringPlaceholders.builder("cmd", command.getName())
                    .addPlaceholder("args", command.getArguments())
                    .addPlaceholder("desc", localeManager.getLocaleMessage(command.getDescriptionKey()))
                    .build();
            localeManager.sendSimpleMessage(pplayer, "command-descriptions-help-2", placeholders);
        }
    }

}
