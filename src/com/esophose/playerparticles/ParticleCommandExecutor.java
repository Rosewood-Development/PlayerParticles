/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.esophose.playerparticles.library.ParticleEffect;
import com.esophose.playerparticles.library.ParticleEffect.BlockData;
import com.esophose.playerparticles.library.ParticleEffect.ItemData;
import com.esophose.playerparticles.library.ParticleEffect.NoteColor;
import com.esophose.playerparticles.library.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.library.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.manager.ConfigManager;
import com.esophose.playerparticles.manager.MessageManager;
import com.esophose.playerparticles.manager.MessageManager.MessageType;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.styles.DefaultStyles;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.styles.api.ParticleStyleManager;

public class ParticleCommandExecutor implements CommandExecutor {

	/**
	 * Called when a player executes a /pp command
	 * Checks what /pp command it is and calls the correct method
	 * 
	 * @param sender Who executed the command
	 * @param cmd The command
	 * @param label The command label
	 * @param args The arguments following the command
	 * @return True if everything went as planned (should always be true)
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return true;
		Player p = (Player) sender;

		if (args.length == 0) {
			MessageManager.sendMessage(p, MessageType.INVALID_ARGUMENTS);
			return true;
		} else {
			String[] cmdArgs = new String[0];
			if (args.length >= 2) cmdArgs = Arrays.copyOfRange(args, 1, args.length);

			switch (args[0].toLowerCase()) {
			case "help":
				onHelp(p);
				break;
			case "worlds":
				onWorlds(p);
				break;
			case "version":
				onVersion(p);
				break;
			case "effect":
				onEffect(p, cmdArgs);
				break;
			case "effects":
				onEffects(p);
				break;
			case "style":
				onStyle(p, cmdArgs);
				break;
			case "styles":
				onStyles(p);
				break;
			case "data":
				onData(p, cmdArgs);
				break;
			case "reset":
				onReset(p, cmdArgs);
				break;
			default:
				MessageManager.sendMessage(p, MessageType.INVALID_ARGUMENTS);
			}
			return true;
		}
	}

	/**
	 * Gets an online player by their username if they exist
	 * 
	 * @param playerName The player's username to lookup
	 * @return The player, if they exist
	 */
	private Player getOnlinePlayerByName(String playerName) {
		for (Player p : Bukkit.getOnlinePlayers())
			if (p.getName().toLowerCase().contains(playerName.toLowerCase())) return p;
		return null;
	}

	/**
	 * Called when a player uses /pp help
	 * 
	 * @param p The player who used the command
	 */
	private void onHelp(Player p) {
		MessageManager.sendMessage(p, MessageType.AVAILABLE_COMMANDS);
		MessageManager.sendMessage(p, MessageType.COMMAND_USAGE);
	}

	/**
	 * Called when a player uses /pp worlds
	 * 
	 * @param p The player who used the command
	 */
	private void onWorlds(Player p) {
		if (ConfigManager.getInstance().getDisabledWorlds() == null || ConfigManager.getInstance().getDisabledWorlds().isEmpty()) {
			MessageManager.sendMessage(p, MessageType.DISABLED_WORLDS_NONE);
			return;
		}

		String worlds = "";
		for (String s : ConfigManager.getInstance().getDisabledWorlds()) {
			worlds += s + ", ";
		}
		if (worlds.length() > 2) worlds = worlds.substring(0, worlds.length() - 2);

		MessageManager.sendCustomMessage(p, MessageType.DISABLED_WORLDS.getMessage() + " " + worlds);
	}

	/**
	 * Called when a player uses /pp version
	 * 
	 * @param p The player who used the command
	 */
	private void onVersion(Player p) {
		MessageManager.sendCustomMessage(p, ChatColor.GOLD + "Running PlayerParticles v" + PlayerParticles.getPlugin().getDescription().getVersion());
		MessageManager.sendCustomMessage(p, ChatColor.GOLD + "Plugin created by: Esophose");
	}

	/**
	 * Called when a player uses /pp data
	 * 
	 * @param p The player who used the command
	 * @param args The arguments for the command
	 */
	private void onData(Player p, String[] args) {
		ParticleEffect effect = ConfigManager.getInstance().getPPlayer(p.getUniqueId()).getParticleEffect();
		if ((!effect.hasProperty(ParticleProperty.REQUIRES_DATA) && !effect.hasProperty(ParticleProperty.COLORABLE)) || args.length == 0) {
			if (effect.hasProperty(ParticleProperty.COLORABLE)) {
				if (effect == ParticleEffect.NOTE) {
					MessageManager.sendMessage(p, MessageType.DATA_USAGE, "note");
					MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.NOTE_DATA_USAGE.getMessage());
				} else {
					MessageManager.sendMessage(p, MessageType.DATA_USAGE, "color");
					MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.COLOR_DATA_USAGE.getMessage());
				}
			} else if (effect.hasProperty(ParticleProperty.REQUIRES_DATA)) {
				if (effect == ParticleEffect.ITEM_CRACK) {
					MessageManager.sendMessage(p, MessageType.DATA_USAGE, "item");
					MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.ITEM_DATA_USAGE.getMessage());
				} else {
					MessageManager.sendMessage(p, MessageType.DATA_USAGE, "block");
					MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.BLOCK_DATA_USAGE.getMessage());
				}
			} else {
				MessageManager.sendMessage(p, MessageType.NO_DATA_USAGE);
			}
			return;
		}
		if (effect.hasProperty(ParticleProperty.COLORABLE)) {
			if (effect == ParticleEffect.NOTE) {
				if (args[0].equalsIgnoreCase("rainbow")) {
					ConfigManager.getInstance().savePPlayer(p.getUniqueId(), new NoteColor(99));
					MessageManager.sendMessage(p, MessageType.DATA_APPLIED, "note");
					return;
				}
				
				int note = -1;
				try {
					note = Integer.parseInt(args[0]);
				} catch (Exception e) {
					MessageManager.sendMessage(p, MessageType.DATA_INVALID_ARGUMENTS, "note");
					MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.NOTE_DATA_USAGE.getMessage());
					return;
				}

				if (note < 0 || note > 23) {
					MessageManager.sendMessage(p, MessageType.DATA_INVALID_ARGUMENTS, "note");
					MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.NOTE_DATA_USAGE.getMessage());
					return;
				}

				ConfigManager.getInstance().savePPlayer(p.getUniqueId(), new NoteColor(note));
				MessageManager.sendMessage(p, MessageType.DATA_APPLIED, "note");
			} else {
				if (args[0].equalsIgnoreCase("rainbow")) {
					ConfigManager.getInstance().savePPlayer(p.getUniqueId(), new OrdinaryColor(999, 999, 999));
					MessageManager.sendMessage(p, MessageType.DATA_APPLIED, "color");
				} else if (args.length >= 3) {
					int r = -1;
					int g = -1;
					int b = -1;

					try {
						r = Integer.parseInt(args[0]);
						g = Integer.parseInt(args[1]);
						b = Integer.parseInt(args[2]);
					} catch (Exception e) {
						MessageManager.sendMessage(p, MessageType.DATA_INVALID_ARGUMENTS, "color");
						MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.COLOR_DATA_USAGE.getMessage());
						return;
					}

					if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
						MessageManager.sendMessage(p, MessageType.DATA_INVALID_ARGUMENTS, "color");
						MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.COLOR_DATA_USAGE.getMessage());
						return;
					}

					ConfigManager.getInstance().savePPlayer(p.getUniqueId(), new OrdinaryColor(r, g, b));
					MessageManager.sendMessage(p, MessageType.DATA_APPLIED, "color");
				} else {
					MessageManager.sendMessage(p, MessageType.DATA_INVALID_ARGUMENTS, "color");
					MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.COLOR_DATA_USAGE.getMessage());
				}
			}
		} else if (effect.hasProperty(ParticleProperty.REQUIRES_DATA)) {
			if (effect == ParticleEffect.ITEM_CRACK) {
				Material material = null;
				int data = -1;

				try {
					material = ParticlesUtil.closestMatch(args[0]);
					if (material == null) material = Material.matchMaterial(args[0]);
					if (material == null) throw new Exception();
				} catch (Exception e) {
					MessageManager.sendMessage(p, MessageType.DATA_MATERIAL_UNKNOWN, "item");
					MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.ITEM_DATA_USAGE.getMessage());
					return;
				}

				try {
					data = Integer.parseInt(args[1]);
				} catch (Exception e) {
					MessageManager.sendMessage(p, MessageType.DATA_INVALID_ARGUMENTS, "item");
					MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.ITEM_DATA_USAGE.getMessage());
					return;
				}

				if (material.isBlock()) {
					MessageManager.sendMessage(p, MessageType.DATA_MATERIAL_MISMATCH, "item");
					MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.ITEM_DATA_USAGE.getMessage());
					return;
				}

				if (data < 0 || data > 15) {
					MessageManager.sendMessage(p, MessageType.DATA_INVALID_ARGUMENTS, "item");
					MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.ITEM_DATA_USAGE.getMessage());
					return;
				}

				ConfigManager.getInstance().savePPlayer(p.getUniqueId(), new ItemData(material, (byte) data));
				MessageManager.sendMessage(p, MessageType.DATA_APPLIED, "item");
			} else {
				Material material = null;
				int data = -1;

				try {
					material = ParticlesUtil.closestMatch(args[0]);
					if (material == null) material = Material.matchMaterial(args[0]);
					if (material == null) throw new Exception();
				} catch (Exception e) {
					MessageManager.sendMessage(p, MessageType.DATA_MATERIAL_UNKNOWN, "block");
					MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.BLOCK_DATA_USAGE.getMessage());
					return;
				}

				try {
					data = Integer.parseInt(args[1]);
				} catch (Exception e) {
					MessageManager.sendMessage(p, MessageType.DATA_INVALID_ARGUMENTS, "block");
					MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.BLOCK_DATA_USAGE.getMessage());
					return;
				}

				if (!material.isBlock()) {
					MessageManager.sendMessage(p, MessageType.DATA_MATERIAL_MISMATCH, "block");
					MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.BLOCK_DATA_USAGE.getMessage());
					return;
				}

				if (data < 0 || data > 15) {
					MessageManager.sendMessage(p, MessageType.DATA_INVALID_ARGUMENTS, "block");
					MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.BLOCK_DATA_USAGE.getMessage());
					return;
				}

				ConfigManager.getInstance().savePPlayer(p.getUniqueId(), new BlockData(material, (byte) data));
				MessageManager.sendMessage(p, MessageType.DATA_APPLIED, "block");
			}
		}
	}

	/**
	 * Called when a player uses /pp reset
	 * 
	 * @param p The player who used the command
	 * @param altPlayer The alternate player to reset
	 */
	private void onReset(Player p, String[] args) { // TODO: Apply this to effects, styles, and data(?)
		if (args.length >= 1) {
			String altPlayerName = args[0];
			if (!PermissionManager.canExecuteForOthers(p)) {
				MessageManager.sendMessage(p, MessageType.FAILED_EXECUTE_NO_PERMISSION, altPlayerName);
			} else {
				Player altPlayer = getOnlinePlayerByName(altPlayerName);
				if (altPlayer == null) {
					MessageManager.sendMessage(p, MessageType.FAILED_EXECUTE_NOT_FOUND);
				} else {
					ConfigManager.getInstance().resetPPlayer(altPlayer.getUniqueId());
					MessageManager.sendMessage(altPlayer, MessageType.RESET);
					
					MessageManager.sendMessage(p, MessageType.EXECUTED_FOR_PLAYER, altPlayer.getName());
				}
			}
		} else {
			ConfigManager.getInstance().resetPPlayer(p.getUniqueId());
			MessageManager.sendMessage(p, MessageType.RESET);
		}
	}

	/**
	 * Called when a player uses /pp effect
	 * 
	 * @param p The player who used the command
	 * @param args The arguments for the command
	 * @param altPlayer The alternate player to give the effect
	 */
	private void onEffect(Player p, String[] args) {
		if (args.length == 0) {
			MessageManager.sendMessage(p, MessageType.INVALID_TYPE);
			return;
		}
		String argument = args[0].replace("_", "");
		if (ParticleCreator.particleFromString(argument) != null) {
			ParticleEffect effect = ParticleCreator.particleFromString(argument);
			if (!PermissionManager.hasEffectPermission(p, effect)) {
				MessageManager.sendMessage(p, MessageType.NO_PERMISSION, effect.getName().toLowerCase());
				return;
			}
			ConfigManager.getInstance().savePPlayer(p.getUniqueId(), effect);
			if (effect != ParticleEffect.NONE) {
				MessageManager.sendMessage(p, MessageType.NOW_USING, effect.getName().toLowerCase());
			} else {
				MessageManager.sendMessage(p, MessageType.CLEARED_PARTICLES);
			}
			return;
		}
		MessageManager.sendMessage(p, MessageType.INVALID_TYPE);
	}

	/**
	 * Called when a player uses /pp effects
	 * 
	 * @param p The player who used the command
	 */
	private void onEffects(Player p) {
		String toSend = MessageType.USE.getMessage() + " ";
		for (ParticleEffect effect : ParticleEffect.getSupportedEffects()) {
			if (PermissionManager.hasEffectPermission(p, effect)) {
				toSend += effect.getName().toLowerCase().replace("_", "") + ", ";
				continue;
			}
		}
		if (toSend.endsWith(", ")) {
			toSend = toSend.substring(0, toSend.length() - 2);
		}
		if (toSend.equals(MessageType.USE.getMessage() + " " + ParticleEffect.NONE.getName())) {
			MessageManager.sendMessage(p, MessageType.NO_PARTICLES);
			return;
		}
		MessageManager.sendCustomMessage(p, toSend);
		MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.PARTICLE_USAGE.getMessage());
	}

	/**
	 * Called when a player uses /pp style
	 * 
	 * @param p The player who used the command
	 * @param args The arguments for the command
	 * @param altPlayer The alternate player to give the style
	 */
	private void onStyle(Player p, String[] args) {
		if (args.length == 0) {
			MessageManager.sendMessage(p, MessageType.INVALID_TYPE_STYLE);
			return;
		}
		String argument = args[0].replace("_", "");
		if (ParticleStyleManager.styleFromString(argument) != null) {
			ParticleStyle style = ParticleStyleManager.styleFromString(argument);
			if (!PermissionManager.hasStylePermission(p, style)) {
				MessageManager.sendMessage(p, MessageType.NO_PERMISSION_STYLE, style.getName().toLowerCase());
				return;
			}
			ConfigManager.getInstance().savePPlayer(p.getUniqueId(), style);
			if (style != DefaultStyles.NONE) {
				MessageManager.sendMessage(p, MessageType.NOW_USING_STYLE, style.getName().toLowerCase());
			} else {
				MessageManager.sendMessage(p, MessageType.CLEARED_STYLE);
			}
			return;
		}
		MessageManager.sendMessage(p, MessageType.INVALID_TYPE_STYLE);
	}

	/**
	 * Called when a player uses /pp styles
	 * 
	 * @param p The player who used the command
	 */
	private void onStyles(Player p) {
		String toSend = MessageType.USE.getMessage() + " ";
		for (ParticleStyle style : ParticleStyleManager.getStyles()) {
			if (PermissionManager.hasStylePermission(p, style)) {
				toSend += style.getName().toLowerCase();
				toSend += ", ";
			}
		}
		if (toSend.endsWith(", ")) {
			toSend = toSend.substring(0, toSend.length() - 2);
		}
		if (toSend.equals(MessageType.USE.getMessage() + " " + DefaultStyles.NONE.getName().toLowerCase())) {
			MessageManager.sendMessage(p, MessageType.NO_STYLES);
			return;
		}
		MessageManager.sendCustomMessage(p, toSend);
		MessageManager.sendCustomMessage(p, MessageType.USAGE.getMessage() + " " + MessageType.STYLE_USAGE.getMessage());
	}

}
