/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.esophose.playerparticles.manager.PermissionManager;

public class ParticleCommandCompleter implements TabCompleter {

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
		if(cmd.getName().equalsIgnoreCase("pp")) {
			if(args.length == 1) {
				List<String> list = PermissionManager.getParticlesUserHasPermissionFor((Player)sender);
				list.add("list");
				list.add("styles");
				list.add("style");
				list.add("version");
				list.add("worlds");
				list.add("help");
				return list;
			}
			if(args.length == 2) return PermissionManager.getStylesUserHasPermissionFor((Player)sender);
		}
		return null;
	}

}
