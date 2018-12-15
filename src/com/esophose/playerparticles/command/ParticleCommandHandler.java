package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.particles.OtherPPlayer;
import com.esophose.playerparticles.particles.PPlayer;

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
                sender.sendMessage("Error: PlayerParticles only supports players executing commands.");
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
            if (!PermissionManager.canOverride(sender)) {
                LangManager.sendCommandSenderMessage(sender, Lang.OTHER_NO_PERMISSION);
                return true;
            }
            
            if (args.length < 2) {
                LangManager.sendCommandSenderMessage(sender, Lang.OTHER_MISSING_ARGS);
                return true;
            }
            
            Player other = Bukkit.getPlayer(args[0]);
            if (other == null) {
                LangManager.sendCommandSenderMessage(sender, Lang.OTHER_UNKNOWN_PLAYER, args[0]);
                return true;
            }
            
            CommandModule commandModule = findMatchingCommand(args[1]);
            if (commandModule == null) {
                LangManager.sendCommandSenderMessage(sender, Lang.OTHER_UNKNOWN_COMMAND, args[1]);
                return true;
            }
            
            if (commandModule.requiresEffects() && PermissionManager.getEffectNamesUserHasPermissionFor(other).isEmpty()) {
                LangManager.sendCommandSenderMessage(sender, Lang.COMMAND_ERROR_NO_EFFECTS);
                return true;
            }
            
            DataManager.getPPlayer(other.getUniqueId(), (pplayer) -> {
                OtherPPlayer otherPPlayer = new OtherPPlayer(sender, pplayer);
                
                LangManager.sendCommandSenderMessage(sender, Lang.OTHER_SUCCESS, other.getName());
                
                String[] cmdArgs = Arrays.copyOfRange(args, 2, args.length);
                commandModule.onCommandExecute(otherPPlayer, cmdArgs);
            });
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
            if (!(sender instanceof Player)) return new ArrayList<String>();
            
            PPlayer pplayer = DataManager.getPPlayer(((Player) sender).getUniqueId());
            if (pplayer == null) return new ArrayList<String>();
            
            if (args.length <= 1) {
                CommandModule commandModule = findMatchingCommand(""); // Get the default command module
                return commandModule.onTabComplete(pplayer, args);
            } else if (args.length >= 2) {
                CommandModule commandModule = findMatchingCommand(args[0]);
                if (commandModule != null) {
                    String[] cmdArgs = Arrays.copyOfRange(args, 1, args.length);
                    return commandModule.onTabComplete(pplayer, cmdArgs);
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("ppo")) {
            List<String> completions = new ArrayList<String>();
            
            if (args.length < 2) {
                List<String> playerNames = new ArrayList<String>();
                for (Player player : Bukkit.getOnlinePlayers()) 
                    playerNames.add(player.getName());
                
                if (args.length == 0) completions = playerNames;
                else StringUtil.copyPartialMatches(args[0], playerNames, completions);
            } else if (args.length == 2) {
                List<String> commandNames = ParticleCommandHandler.getCommandNames();
                StringUtil.copyPartialMatches(args[1], commandNames, completions);
            } else {
                Player otherPlayer = Bukkit.getPlayer(args[0]);
                if (otherPlayer != null) {
                    PPlayer other = DataManager.getPPlayer(otherPlayer.getUniqueId());
                    if (other != null) {
                        CommandModule commandModule = findMatchingCommand(args[1]);
                        if (commandModule != null) {
                            String[] cmdArgs = Arrays.copyOfRange(args, 2, args.length);
                            completions = commandModule.onTabComplete(other, cmdArgs);
                        }
                    }
                }
            }
            
            return completions;
        }
        
        return new ArrayList<String>();
    }

}
