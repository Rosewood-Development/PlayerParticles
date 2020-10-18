package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.CommandManager;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.OtherPPlayer;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class OtherCommandModule implements CommandModuleSecondary {

    @Override
    public void onCommandExecute(CommandSender sender, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);

        if (!permissionManager.canOverride(sender)) {
            localeManager.sendMessage(sender, "other-no-permission");
            return;
        }
        
        if (args.length < 2) {
            localeManager.sendMessage(sender, "other-missing-args");
            return;
        }
        
        Player other = Bukkit.getPlayer(args[0]);
        if (other == null) {
            localeManager.sendMessage(sender, "other-unknown-player", StringPlaceholders.single("player", args[0]));
            return;
        }
        
        CommandModule commandModule = PlayerParticles.getInstance().getManager(CommandManager.class).findMatchingCommand(args[1]);
        if (commandModule == null) {
            localeManager.sendMessage(sender, "other-unknown-command", StringPlaceholders.single("cmd", args[1]));
            return;
        }
        
        PlayerParticles.getInstance().getManager(DataManager.class).getPPlayer(other.getUniqueId(), (pplayer) -> {
            OtherPPlayer otherPPlayer = new OtherPPlayer(sender, pplayer);

            if (commandModule.requiresEffectsAndStyles() && (permissionManager.getEffectsUserHasPermissionFor(otherPPlayer).isEmpty() || permissionManager.getStylesUserHasPermissionFor(otherPPlayer).isEmpty())) {
                localeManager.sendMessage(sender, "other-success", StringPlaceholders.single("player", other.getName()));
                localeManager.sendMessage(sender, "command-error-missing-effects-or-styles");
                return;
            }

            localeManager.sendMessage(sender, "other-success", StringPlaceholders.single("player", other.getName()));
            
            String[] cmdArgs = Arrays.copyOfRange(args, 2, args.length);
            commandModule.onCommandExecute(otherPPlayer, cmdArgs);
        });
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length < 2) {
            List<String> playerNames = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) 
                playerNames.add(player.getName());
            
            if (args.length == 0) completions = playerNames;
            else StringUtil.copyPartialMatches(args[0], playerNames, completions);
        } else if (args.length == 2) {
            List<String> commandNames = PlayerParticles.getInstance().getManager(CommandManager.class).getCommandNames();
            StringUtil.copyPartialMatches(args[1], commandNames, completions);
        } else {
            Player otherPlayer = Bukkit.getPlayer(args[0]);
            if (otherPlayer != null) {
                PPlayer other = PlayerParticles.getInstance().getManager(DataManager.class).getPPlayer(otherPlayer.getUniqueId());
                if (other != null) {
                    CommandModule commandModule = PlayerParticles.getInstance().getManager(CommandManager.class).findMatchingCommand(args[1]);
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
