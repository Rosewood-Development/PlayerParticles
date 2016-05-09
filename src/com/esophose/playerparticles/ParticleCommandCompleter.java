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

public class ParticleCommandCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("pp")) {
			
			if(args.length == 1) {
				
				List<String> list = PermissionHandler.getParticlesUserHasPermissionFor((Player)sender);
				if(PermissionHandler.canReload((Player)sender)) list.add("reload");
				list.add("list");
				list.add("styles");
				list.add("style");
				list.add("version");
				list.add("worlds");
				list.add("help");
				return list;
				
			}
			
			if(args.length == 2) {
				
				return PermissionHandler.getStylesUserHasPermissionFor((Player)sender);
				
			}
			
		}
		
		return null;
	}

}
