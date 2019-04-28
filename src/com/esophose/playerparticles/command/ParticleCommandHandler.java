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
import com.esophose.playerparticles.particles.PPlayer;

import net.md_5.bungee.api.ChatColor;

public class ParticleCommandHandler implements CommandExecutor, TabCompleter {

    /**
     * A list of all commands
     */
    private static List<CommandModule> commands;
    private static CommandModuleSecondary ppoCommand;

    static {
        commands = new ArrayList<>();

        commands.add(new AddCommandModule());
        commands.add(new DataCommandModule());
        commands.add(new DefaultCommandModule());
        commands.add(new EditCommandModule());
        commands.add(new EffectsCommandModule());
        commands.add(new FixedCommandModule());
        commands.add(new GroupCommandModule());
        commands.add(new GUICommandModule());
        commands.add(new HelpCommandModule());
        commands.add(new ListCommandModule());
        commands.add(new ReloadCommandModule());
        commands.add(new RemoveCommandModule());
        commands.add(new ResetCommandModule());
        commands.add(new StylesCommandModule());
        commands.add(new ToggleCommandModule());
        commands.add(new VersionCommandModule());
        commands.add(new WorldsCommandModule());
        
        ppoCommand = new OtherCommandModule();
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
        List<String> commandNames = new ArrayList<>();
        for (CommandModule cmd : commands)
            commandNames.add(cmd.getName());
        return commandNames;
    }

    /**
     * Called when a player executes a PlayerParticles command
     * Checks what PlayerParticles command it is and calls the corresponding module
     * 
     * @param sender Who executed the command
     * @param cmd The command
     * @param label The command label
     * @param args The arguments following the command
     * @return true
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pp")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Error: This command can only be executed by a player.");
                return true;
            }
            
            Player p = (Player) sender;

            DataManager.getPPlayer(p.getUniqueId(), (pplayer) -> {
                String commandName = args.length > 0 ? args[0] : "";
                CommandModule commandModule = findMatchingCommand(commandName);

                if (commandModule != null) {
                    if (commandModule.requiresEffects() && PermissionManager.getEffectNamesUserHasPermissionFor(p).isEmpty()) {
                        LangManager.sendMessage(pplayer, Lang.COMMAND_ERROR_NO_EFFECTS);
                    } else {
                        String[] cmdArgs = new String[0];
                        if (args.length > 1) cmdArgs = Arrays.copyOfRange(args, 1, args.length);
                        commandModule.onCommandExecute(pplayer, cmdArgs);
                    }
                } else {
                    LangManager.sendMessage(pplayer, Lang.COMMAND_ERROR_UNKNOWN);
                }
            });
        } else if (cmd.getName().equalsIgnoreCase("ppo")) {
            ppoCommand.onCommandExecute(sender, args);
        }
        
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
        if (cmd.getName().equalsIgnoreCase("pp")) {
            if (!(sender instanceof Player)) return new ArrayList<>();
            
            PPlayer pplayer = DataManager.getPPlayer(((Player) sender).getUniqueId());
            if (pplayer == null) return new ArrayList<>();
            
            if (args.length <= 1) {
                CommandModule commandModule = findMatchingCommand(""); // Get the default command module
                return commandModule.onTabComplete(pplayer, args);
            } else {
                CommandModule commandModule = findMatchingCommand(args[0]);
                if (commandModule != null) {
                    String[] cmdArgs = Arrays.copyOfRange(args, 1, args.length);
                    return commandModule.onTabComplete(pplayer, cmdArgs);
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("ppo")) {
            return ppoCommand.onTabComplete(sender, args);
        }
        
        return new ArrayList<>();
    }

}
