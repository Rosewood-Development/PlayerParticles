package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.particles.OtherPPlayer;
import com.esophose.playerparticles.particles.PPlayer;

public class OtherCommandModule implements CommandModuleSecondary {

    public void onCommandExecute(CommandSender sender, String[] args) {
        if (!PermissionManager.canOverride(sender)) {
            LangManager.sendCommandSenderMessage(sender, Lang.OTHER_NO_PERMISSION);
            return;
        }
        
        if (args.length < 2) {
            LangManager.sendCommandSenderMessage(sender, Lang.OTHER_MISSING_ARGS);
            return;
        }
        
        Player other = Bukkit.getPlayer(args[0]);
        if (other == null) {
            LangManager.sendCommandSenderMessage(sender, Lang.OTHER_UNKNOWN_PLAYER, args[0]);
            return;
        }
        
        CommandModule commandModule = ParticleCommandHandler.findMatchingCommand(args[1]);
        if (commandModule == null) {
            LangManager.sendCommandSenderMessage(sender, Lang.OTHER_UNKNOWN_COMMAND, args[1]);
            return;
        }
        
        if (commandModule.requiresEffects() && PermissionManager.getEffectNamesUserHasPermissionFor(other).isEmpty()) {
            LangManager.sendCommandSenderMessage(sender, Lang.OTHER_SUCCESS, other.getName());
            LangManager.sendCommandSenderMessage(sender, Lang.COMMAND_ERROR_NO_EFFECTS);
            return;
        }
        
        DataManager.getPPlayer(other.getUniqueId(), (pplayer) -> {
            OtherPPlayer otherPPlayer = new OtherPPlayer(sender, pplayer);
            
            LangManager.sendCommandSenderMessage(sender, Lang.OTHER_SUCCESS, other.getName());
            
            String[] cmdArgs = Arrays.copyOfRange(args, 2, args.length);
            commandModule.onCommandExecute(otherPPlayer, cmdArgs);
        });
    }

    public List<String> onTabComplete(CommandSender sender, String[] args) {
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
                    CommandModule commandModule = ParticleCommandHandler.findMatchingCommand(args[1]);
                    if (commandModule != null) {
                        String[] cmdArgs = Arrays.copyOfRange(args, 2, args.length);
                        completions = commandModule.onTabComplete(other, cmdArgs);
                    }
                }
            }
        }
        
        return completions;
    }

}
