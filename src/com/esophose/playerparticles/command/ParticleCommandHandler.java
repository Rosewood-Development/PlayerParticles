package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.PermissionManager;

public class ParticleCommandHandler implements CommandExecutor, TabCompleter {

    /**
     * A list of all commands
     */
    private static List<CommandModule> commands;

    static {
        commands = new ArrayList<CommandModule>();

        commands.add(new AddCommandModule());
        commands.add(new DataCommandModule());
        commands.add(new DefaultCommandModule());
        commands.add(new EditCommandModule());
        commands.add(new EffectCommandModule());
        commands.add(new EffectsCommandModule());
        commands.add(new FixedCommandModule());
        commands.add(new GroupCommandModule());
        commands.add(new GUICommandModule());
        commands.add(new HelpCommandModule());
        commands.add(new ListCommandModule());
        commands.add(new ReloadCommandModule());
        commands.add(new RemoveCommandModule());
        commands.add(new ResetCommandModule());
        commands.add(new StyleCommandModule());
        commands.add(new StylesCommandModule());
        commands.add(new VersionCommandModule());
        commands.add(new WorldsCommandModule());
    }

    /**
     * Finds a matching CommandModule by its name
     * 
     * @param commandName The command name
     * @return The found CommandModule, otherwise null
     */
    public static CommandModule findMatchingCommand(String commandName) {
        for (CommandModule commandModule : commands)
            if (commandModule.getName().equalsIgnoreCase(commandName)) 
                return commandModule;
        return null;
    }

    /**
     * Get a list of all available commands
     * 
     * @return A List of all CommandModules registered
     */
    public static List<CommandModule> getCommands() {
        return commands;
    }

    /**
     * Get all available command names
     * 
     * @return All available command names
     */
    public static List<String> getCommandNames() {
        List<String> commandNames = new ArrayList<String>();
        for (CommandModule cmd : commands)
            commandNames.add(cmd.getName());
        return commandNames;
    }

    /**
     * Called when a player executes a /pp command
     * Checks what /pp command it is and calls the corresponding module
     * 
     * @param sender Who executed the command
     * @param cmd The command
     * @param label The command label
     * @param args The arguments following the command
     * @return true
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        DataManager.getPPlayer(p.getUniqueId(), (pplayer) -> {
            String commandName = args.length > 0 ? args[0] : "";
            CommandModule commandModule = findMatchingCommand(commandName);

            if (commandModule != null) {
                if (commandModule.requiresEffects() && PermissionManager.getEffectsUserHasPermissionFor(p).isEmpty()) {
                    LangManager.sendMessage(p, Lang.COMMAND_ERROR_NO_EFFECTS);
                } else {
                    String[] cmdArgs = new String[0];
                    if (args.length > 1) cmdArgs = Arrays.copyOfRange(args, 1, args.length);
                    commandModule.onCommandExecute(pplayer, cmdArgs);
                }
            } else {
                LangManager.sendMessage(p, Lang.COMMAND_ERROR_UNKNOWN);
            }
        });

        return true;
    }

    /**
     * Activated when a user pushes tab in chat prefixed with /pp
     * 
     * @param sender The sender that hit tab, should always be a player
     * @param cmd The command the player is executing
     * @param alias The possible alias for the command
     * @param args All arguments following the command
     * @return A list of commands available to the sender
     */
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (!(sender instanceof Player)) return new ArrayList<String>();

        if (cmd.getName().equalsIgnoreCase("pp")) {
            if (args.length <= 1) {
                CommandModule commandModule = findMatchingCommand(""); // Get the default command module
                return commandModule.onTabComplete(DataManager.getPPlayer(((Player) sender).getUniqueId()), args);
            } else if (args.length >= 2) {
                CommandModule commandModule = findMatchingCommand(args[0]);
                String[] cmdArgs = Arrays.copyOfRange(args, 1, args.length);
                if (commandModule != null) {
                    return commandModule.onTabComplete(DataManager.getPPlayer(((Player) sender).getUniqueId()), cmdArgs);
                }
            }
        }
        return new ArrayList<String>();
    }

}
