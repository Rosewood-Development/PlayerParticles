/**
 * Copyright Esophose 2017
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.esophose.playerparticles.manager.PermissionManager;

public class ParticleCommandCompleter implements TabCompleter {

	private final String[] COMMANDS = { "help", "effect", "effects", "style", "styles", "worlds", "version", "fixed", "reset" };
	private final String[] FIXED_COMMANDS = { "create", "remove", "list", "info" };

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
		List<String> completions = new ArrayList<String>();
		if (cmd.getName().equalsIgnoreCase("pp")) {
			if (args.length > 1) {
				if (args[0].equalsIgnoreCase("effect")) {
					List<String> commands = PermissionManager.getParticlesUserHasPermissionFor((Player) sender);
					StringUtil.copyPartialMatches(args[1], commands, completions);
					return completions;
				} else if (args[0].equalsIgnoreCase("style")) {
					List<String> commands = PermissionManager.getStylesUserHasPermissionFor((Player) sender);
					StringUtil.copyPartialMatches(args[1], commands, completions);
					return completions;
				} else if (args[0].equalsIgnoreCase("fixed")) {
					List<String> commands = Arrays.asList(FIXED_COMMANDS);
					StringUtil.copyPartialMatches(args[1], commands, completions);
				}
			} else {
				List<String> commands = new ArrayList<String>(Arrays.asList(COMMANDS));
				StringUtil.copyPartialMatches(args[0], commands, completions);
			}
		}
		return completions;
	}

}
