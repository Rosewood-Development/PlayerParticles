/**
 * Copyright Esophose 2018
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.esophose.playerparticles.manager.MessageManager.MessageType;
import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.util.ParticleUtils;

public class ParticleCommandCompleter implements TabCompleter {

    private static final String[] COMMANDS = { "help", "gui", "effect", "effects", "style", "styles", "data", "fixed", "reset", "worlds", "version" };
    private static final String[] FIXED_COMMANDS = { "create", "remove", "list", "info" };
    private static final List<String> BLOCK_MATERIALS = ParticleUtils.getAllBlockMaterials();
    private static final List<String> ITEM_MATERIALS = ParticleUtils.getAllItemMaterials();

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

        List<String> completions = new ArrayList<String>();
        if (cmd.getName().equalsIgnoreCase("pp")) {
            if (args.length > 1) {
                if (args[0].equalsIgnoreCase("effect") && args.length == 2) {
                    List<String> commands = PermissionManager.getEffectsUserHasPermissionFor((Player) sender);
                    StringUtil.copyPartialMatches(args[1], commands, completions);
                } else if (args[0].equalsIgnoreCase("style") && args.length == 2) {
                    List<String> commands = PermissionManager.getStylesUserHasPermissionFor((Player) sender);
                    StringUtil.copyPartialMatches(args[1], commands, completions);
                } else if (args[0].equalsIgnoreCase("fixed") && args.length > 1) {
                    if (args.length == 2) {
                        List<String> commands = Arrays.asList(FIXED_COMMANDS);
                        StringUtil.copyPartialMatches(args[1], commands, completions);
                    } else if (args[1].equalsIgnoreCase("create")) {
                        completions.add("<x> <y> <z> <effect> <style> [data]");
                    } else if ((args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("info")) && args.length == 3) {
                        completions.add("<id>");
                    } 
                } else if (args[0].equalsIgnoreCase("data")) {
                    PPlayer pplayer = DataManager.getInstance().getPPlayer(((Player) sender).getUniqueId());
                    if (pplayer == null) {
                        completions.add(ChatColor.stripColor(MessageType.NO_DATA_USAGE.getMessage()));
                    } else if (pplayer.getParticleEffect().hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA) && args.length == 2) {
                        if (pplayer.getParticleEffect() == ParticleEffect.ITEM) {
                            StringUtil.copyPartialMatches(args[1], ITEM_MATERIALS, completions);
                        } else {
                            StringUtil.copyPartialMatches(args[1], BLOCK_MATERIALS, completions);
                        }
                    } else if (pplayer.getParticleEffect().hasProperty(ParticleProperty.COLORABLE)) {
                        if (pplayer.getParticleEffect() == ParticleEffect.NOTE && args.length == 2) {
                            completions.add("<0-23>");
                            StringUtil.copyPartialMatches(args[args.length - 1], Arrays.asList(new String[] { "rainbow" }), completions);
                        } else if (pplayer.getParticleEffect() != ParticleEffect.NOTE && args.length > 1 && args.length < 5) {
                            completions.add("<0-255>");
                            if (args.length == 2) {
                                StringUtil.copyPartialMatches(args[args.length - 1], Arrays.asList(new String[] { "rainbow" }), completions);
                            }
                        }
                    } else if (args.length == 2) {
                        completions.add(ChatColor.stripColor(MessageType.NO_DATA_USAGE.getMessage()));
                    }
                }
            } else {
                List<String> commands = new ArrayList<String>(Arrays.asList(COMMANDS));
                StringUtil.copyPartialMatches(args[0], commands, completions);
            }
        }
        return completions;
    }

    public static String[] getCommandsList() {
        return COMMANDS;
    }

}
