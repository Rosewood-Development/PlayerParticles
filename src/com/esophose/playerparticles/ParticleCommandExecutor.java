package com.esophose.playerparticles;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.esophose.playerparticles.libraries.particles.ParticleEffect.ParticleType;
import com.esophose.playerparticles.manager.ConfigManager;
import com.esophose.playerparticles.manager.MessageManager;
import com.esophose.playerparticles.manager.PermissionManager;

public class ParticleCommandExecutor implements CommandExecutor {

	/**
	 * Called when a player does a command and continues if the command is /pp
	 * Executes all the commands and methods 
	 * Does some sorcery 
	 * 
	 * @param sender Who executed the command
	 * @param cmd The command
	 * @param label The command label
	 * @param args The arguments following the command
	 * @return True if everything went as planned (should always be true)
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return true;
		Player p = (Player) sender;
		if(args.length == 1 && args[0].equalsIgnoreCase("worlds")) {
			String worlds = "";
			if(ConfigManager.getInstance().getDisabledWorlds() == null) {
				MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-disabled-worlds-none", null, null), ChatColor.GREEN);
			}
			for(String s : ConfigManager.getInstance().getDisabledWorlds()) {
				worlds += s + ", ";
			}
			if(worlds.length() > 2) worlds = worlds.substring(0, worlds.length() - 2);
			if(worlds.equals("")) {
				worlds = MessageManager.getMessageFromConfig("message-disabled-worlds-none", null, null);
			}else{
				worlds = MessageManager.getMessageFromConfig("message-disabled-worlds", null, null) + " " + ChatColor.AQUA + worlds;
			}
			MessageManager.getInstance().sendMessage(p, worlds, ChatColor.GREEN);
			return true;
		}
		if(args.length > 1 && args[0].equalsIgnoreCase("style")) {
			String argument = args[1].replace("_", "");
			if(ParticleStyle.styleFromString(argument) != null){
				ParticleStyle style = ParticleStyle.styleFromString(argument);
				if(!PermissionManager.hasStylePermission(p, style)) {
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-no-permission-style", null, ChatColor.AQUA + style.toString().toLowerCase() + ChatColor.RED), ChatColor.RED);
					return true;
				}
				ConfigManager.getStyleInstance().setStyle(style, p);
				ParticleCreator.addStyleMap(p, style);
				MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-now-using-style", null, ChatColor.AQUA + style.toString().toLowerCase() + ChatColor.GREEN), ChatColor.GREEN);
				return true;
			}
			MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-invalid-type-style", null, null) + ChatColor.GREEN + " /pp styles", ChatColor.RED);
			return true;
		}
		if(args.length != 1){
			MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-invalid-arguments", null, null) + ChatColor.GREEN + " /pp list", ChatColor.RED);
			return true;
		}
		String argument = args[0].replace("_", "");
		if(ParticleCreator.particleFromString(argument) != null){
			ParticleType effect = ParticleCreator.particleFromString(argument);
			if(!PermissionManager.hasPermission(p, effect)){
				MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-no-permission", ChatColor.AQUA + (effect.equals(ParticleType.RAINBOW) ? "rainbow" : effect.getName().toLowerCase() + ChatColor.RED), null), ChatColor.RED);
				return true;
			}
			ConfigManager.getInstance().setParticle(effect, p);
			ParticleCreator.addMap(p, effect);
			MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-now-using", ChatColor.AQUA + (effect.equals(ParticleType.RAINBOW) ? "rainbow" : effect.getName().toLowerCase() + ChatColor.GREEN), null), ChatColor.GREEN);
			return true;
		}
		if(argument.equalsIgnoreCase("clear")) {
			ConfigManager.getInstance().resetParticle(p);
			ParticleCreator.removeMap(p);
			MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-cleared-particles", null, null), ChatColor.GREEN);
			return true;
		}
		if(argument.equalsIgnoreCase("version")) {
			MessageManager.getInstance().sendMessage(p, "Running PlayerParticles v" + PlayerParticles.getPlugin().getDescription().getVersion(), ChatColor.GOLD);
			MessageManager.getInstance().sendMessage(p, "Plugin created by: Esophose", ChatColor.GOLD);
			return true;
		}
		if(argument.equalsIgnoreCase("help")) {
			MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-available-commands", null, null), ChatColor.GREEN);
			MessageManager.getInstance().sendMessage(p, "list, styles, style, worlds, version, help", ChatColor.AQUA);
			MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null, null) + ChatColor.AQUA + " /pp <Command>", ChatColor.YELLOW);
			return true;
		}
		if(argument.equalsIgnoreCase("list")) {
			String toSend = MessageManager.getMessageFromConfig("message-use", null, null) + " ";
			for(ParticleType effect : ParticleType.values()){
				if(PermissionManager.hasPermission(p, effect)){
					toSend = toSend + (effect.equals(ParticleType.RAINBOW) ? "rainbow" : effect.getName().toLowerCase()) + ", ";
					continue;
				}
			}
			if(toSend.equals(MessageManager.getMessageFromConfig("message-use", null, null) + " ")) {
				MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-no-particles", null, null), ChatColor.RED);
				return true;
			}
			toSend = toSend + "clear";
			MessageManager.getInstance().sendMessage(p, toSend, ChatColor.GREEN);
			MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null, null) + ChatColor.AQUA + " /pp <Type>", ChatColor.YELLOW);
			return true;
		}
		if(argument.equalsIgnoreCase("style")) {
			MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-invalid-type-style", null, null) + ChatColor.GREEN + " /pp styles", ChatColor.RED);
			return true;
		}
		if(argument.equalsIgnoreCase("styles")) {
			String toSend = MessageManager.getMessageFromConfig("message-use-style", null, null) + " ";
			for(ParticleStyle style : ParticleStyle.values()){
				if(PermissionManager.hasStylePermission(p, style)){
					toSend = toSend + style.toString().toLowerCase();
					toSend += ", ";
				}
			}
			if(toSend.endsWith(", ")) {
				toSend = toSend.substring(0, toSend.length() - 2);
			}
			if(toSend.equals(MessageManager.getMessageFromConfig("message-use-style", null, null) + " " + ParticleStyle.NONE.toString().toLowerCase())) {
				MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-no-styles", null, null), ChatColor.RED);
				return true;
			}
			MessageManager.getInstance().sendMessage(p, toSend, ChatColor.GREEN);
			MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null, null) + ChatColor.AQUA + " /pp style <Type>", ChatColor.YELLOW);
			return true;
		}
		
		MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-invalid-type", null, null) + ChatColor.GREEN + " /pp list", ChatColor.RED);
		
		return true;
	}

}
