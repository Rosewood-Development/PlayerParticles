package com.esophose.playerparticles;

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
			MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-invalid-arguments", null) + ChatColor.GREEN + " /pp help", ChatColor.RED);
			return true;
		} else {
			switch (args[0].toLowerCase()) {
			case "help":
				onHelp(p, args);
				break;
			case "worlds":
				onWorlds(p, args);
				break;
			case "version":
				onVersion(p, args);
				break;
			case "effect":
				onEffect(p, args);
				break;
			case "effects":
				onEffects(p, args);
				break;
			case "style":
				onStyle(p, args);
				break;
			case "styles":
				onStyles(p, args);
				break;
			case "data":
				onData(p, args);
				break;
			case "reset":
				onReset(p, args);
				break;
			default:
				MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-invalid-arguments", null) + ChatColor.GREEN + " /pp help", ChatColor.RED);
			}
			return true;
		}
	}

	/**
	 * Called when a player uses /pp help
	 * 
	 * @param p The player who used the command
	 * @param args The arguments for the command
	 */
	private void onHelp(Player p, String[] args) {
		MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-available-commands", null), ChatColor.GREEN);
		MessageManager.getInstance().sendMessage(p, "effect, effects, style, styles, data, reset, worlds, version, help", ChatColor.AQUA);
		MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp <command>", ChatColor.YELLOW);
	}

	/**
	 * Called when a player uses /pp worlds
	 * 
	 * @param p The player who used the command
	 * @param args The arguments for the command
	 */
	private void onWorlds(Player p, String[] args) {
		String worlds = "";
		if (ConfigManager.getInstance().getDisabledWorlds() == null) {
			MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-disabled-worlds-none", null), ChatColor.GREEN);
		}
		for (String s : ConfigManager.getInstance().getDisabledWorlds()) {
			worlds += s + ", ";
		}
		if (worlds.length() > 2) worlds = worlds.substring(0, worlds.length() - 2);
		if (worlds.equals("")) {
			worlds = MessageManager.getMessageFromConfig("message-disabled-worlds-none", null);
		} else {
			worlds = MessageManager.getMessageFromConfig("message-disabled-worlds", null) + " " + ChatColor.AQUA + worlds;
		}
		MessageManager.getInstance().sendMessage(p, worlds, ChatColor.GREEN);
	}

	/**
	 * Called when a player uses /pp version
	 * 
	 * @param p The player who used the command
	 * @param args The arguments for the command
	 */
	private void onVersion(Player p, String[] args) {
		MessageManager.getInstance().sendMessage(p, "Running PlayerParticles v" + PlayerParticles.getPlugin().getDescription().getVersion(), ChatColor.GOLD);
		MessageManager.getInstance().sendMessage(p, "Plugin created by: Esophose", ChatColor.GOLD);
	}

	/**
	 * Called when a player uses /pp data
	 * 
	 * @param p The player who used the command
	 * @param args The arguments for the command
	 */
	private void onData(Player p, String[] args) {
		ParticleEffect effect = ConfigManager.getInstance().getPPlayer(p.getUniqueId()).getParticleEffect();
<<<<<<< HEAD
		if ((!effect.hasProperty(ParticleProperty.REQUIRES_DATA) && !effect.hasProperty(ParticleProperty.COLORABLE)) || args.length == 1) {
=======
		if (args.length == 1) {
>>>>>>> refs/remotes/origin/master
			if (effect.hasProperty(ParticleProperty.COLORABLE)) {
				if (effect == ParticleEffect.NOTE) {
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-note-data-usage", null), ChatColor.YELLOW);
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <0-23>", ChatColor.YELLOW);
				} else {
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-color-data-usage", null), ChatColor.YELLOW);
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <0-255> <0-255> <0-255>", ChatColor.YELLOW);
				}
			} else if (effect.hasProperty(ParticleProperty.REQUIRES_DATA)) {
				if (effect == ParticleEffect.ITEM_CRACK) {
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-item-data-usage", null), ChatColor.YELLOW);
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <itemName> <0-15>", ChatColor.YELLOW);
				} else {
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-block-data-usage", null), ChatColor.YELLOW);
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <blockName> <0-15>", ChatColor.YELLOW);
				}
			} else {
				MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-no-data-usage", null), ChatColor.YELLOW);
			}
			return;
		}
		if (effect.hasProperty(ParticleProperty.COLORABLE)) {
			if (effect == ParticleEffect.NOTE) {
				if (args.length >= 2) {
					int note = -1;
					try {
						note = Integer.parseInt(args[1]);
					} catch (Exception e) {
						MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-note-data-invalid-arguments", null), ChatColor.RED);
						MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <0-23>", ChatColor.YELLOW);
						return;
					}
					
					if (note < 0 || note > 23) {
						MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-note-data-invalid-arguments", null), ChatColor.RED);
						MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <0-23>", ChatColor.YELLOW);
						return;
					}
					
					ConfigManager.getInstance().savePPlayer(p.getUniqueId(), new NoteColor(note));
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-note-data-applied", null), ChatColor.GREEN);
				} else {
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-note-data-invalid-arguments", null), ChatColor.RED);
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <0-23>", ChatColor.YELLOW);
				}
			} else {
				if (args.length >= 4) {
					int r = -1;
					int g = -1;
					int b = -1;

					try {
						r = Integer.parseInt(args[1]);
						g = Integer.parseInt(args[2]);
						b = Integer.parseInt(args[3]);
					} catch (Exception e) {
						MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-color-data-invalid-arguments", null), ChatColor.RED);
						MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <0-255> <0-255> <0-255>", ChatColor.YELLOW);
						return;
					}
					
					if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
						MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-color-data-invalid-arguments", null), ChatColor.RED);
						MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <0-255> <0-255> <0-255>", ChatColor.YELLOW);
						return;
					}
					
					ConfigManager.getInstance().savePPlayer(p.getUniqueId(), new OrdinaryColor(r, g, b));
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-color-data-applied", null), ChatColor.GREEN);
				} else {
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-color-data-invalid-arguments", null), ChatColor.RED);
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <0-255> <0-255> <0-255>", ChatColor.YELLOW);
				}
			}
		} else if (effect.hasProperty(ParticleProperty.REQUIRES_DATA)) {
			if (effect == ParticleEffect.ITEM_CRACK) {
				Material material = null;
				int data = -1;

				try {
<<<<<<< HEAD
					material = ParticlesUtil.closestMatch(args[1]);
					if (material == null) material = Material.matchMaterial(args[1]);
					if (material == null) throw new Exception();
				} catch (Exception e) {
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-item-data-unknown", args[1]), ChatColor.RED);
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <itemName> <0-15>", ChatColor.YELLOW);
=======
					material = Material.matchMaterial(args[1]);
				} catch (Exception e) {
					// Unknown item
>>>>>>> refs/remotes/origin/master
					return;
				}

				try {
					data = Integer.parseInt(args[2]);
				} catch (Exception e) {
<<<<<<< HEAD
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-item-data-usage", null), ChatColor.RED);
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <itemName> <0-15>", ChatColor.YELLOW);
=======
					// Invalid data
>>>>>>> refs/remotes/origin/master
					return;
				}

				if (material.isBlock()) {
<<<<<<< HEAD
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-item-data-mismatch", material.name()), ChatColor.RED);
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <itemName> <0-15>", ChatColor.YELLOW);
=======
					// Material must be an item
>>>>>>> refs/remotes/origin/master
					return;
				}
				
				if (data < 0 || data > 15) {
<<<<<<< HEAD
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-item-data-invalid-arguments", null), ChatColor.RED);
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <itemName> <0-15>", ChatColor.YELLOW);
=======
					// Error data range must be between 0-15
>>>>>>> refs/remotes/origin/master
					return;
				}
				
				ConfigManager.getInstance().savePPlayer(p.getUniqueId(), new ItemData(material, (byte) data));
				MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-item-data-applied", null), ChatColor.GREEN);
			} else {
				Material material = null;
				int data = -1;

				try {
<<<<<<< HEAD
					material = ParticlesUtil.closestMatch(args[1]);
					if (material == null) material = Material.matchMaterial(args[1]);
					if (material == null) throw new Exception();
				} catch (Exception e) {
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-block-data-unknown", args[1]), ChatColor.RED);
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <blockName> <0-15>", ChatColor.YELLOW);
=======
					material = Material.matchMaterial(args[1]);
				} catch (Exception e) {
					// Unknown block
>>>>>>> refs/remotes/origin/master
					return;
				}

				try {
					data = Integer.parseInt(args[2]);
				} catch (Exception e) {
<<<<<<< HEAD
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-block-data-usage", null), ChatColor.RED);
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <blockName> <0-15>", ChatColor.YELLOW);
					return;
				}

				if (!material.isBlock()) {
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-block-data-mismatch", material.name()), ChatColor.RED);
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <blockName> <0-15>", ChatColor.YELLOW);
=======
					// Invalid data
					return;
				}

				if (material.isBlock()) {
					// Material must be a block
>>>>>>> refs/remotes/origin/master
					return;
				}
				
				if (data < 0 || data > 15) {
<<<<<<< HEAD
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-block-data-invalid-arguments", null), ChatColor.RED);
					MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp data <blockName> <0-15>", ChatColor.YELLOW);
=======
					// Error data range must be between 0-15
>>>>>>> refs/remotes/origin/master
					return;
				}
				
				ConfigManager.getInstance().savePPlayer(p.getUniqueId(), new BlockData(material, (byte) data));
				MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-block-data-applied", null), ChatColor.GREEN);
			}
		}
	}

	/**
	 * Called when a player uses /pp reset
	 * 
	 * @param p The player who used the command
	 * @param args The arguments for the command
	 */
	private void onReset(Player p, String[] args) {
<<<<<<< HEAD
		ConfigManager.getInstance().resetPPlayer(p.getUniqueId());
=======
		ConfigManager.getInstance().saveEntirePPlayer(PPlayer.getNewPPlayer(p.getUniqueId()));
>>>>>>> refs/remotes/origin/master
		MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-reset", null), ChatColor.GREEN);
	}

	/**
	 * Called when a player uses /pp effect
	 * 
	 * @param p The player who used the command
	 * @param args The arguments for the command
	 */
	private void onEffect(Player p, String[] args) {
		if (args.length == 1) {
			MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-invalid-type", null) + ChatColor.GREEN + " /pp effects | /pp effect <type>", ChatColor.RED);
			return;
		}
		String argument = args[1].replace("_", "");
		if (ParticleCreator.particleFromString(argument) != null) {
			ParticleEffect effect = ParticleCreator.particleFromString(argument);
			if (!PermissionManager.hasEffectPermission(p, effect)) {
				MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-no-permission", ChatColor.AQUA + effect.getName().toLowerCase() + ChatColor.RED), ChatColor.RED);
				return;
			}
			ConfigManager.getInstance().savePPlayer(p.getUniqueId(), effect);
			if (effect != ParticleEffect.NONE) {
				MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-now-using", ChatColor.AQUA + effect.getName().toLowerCase() + ChatColor.GREEN), ChatColor.GREEN);
			} else {
				MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-cleared-particles", null), ChatColor.GREEN);
			}
			return;
		}
		MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-invalid-type", null) + ChatColor.GREEN + " /pp effects", ChatColor.RED);
	}

	/**
	 * Called when a player uses /pp effects
	 * 
	 * @param p The player who used the command
	 * @param args The arguments for the command
	 */
	private void onEffects(Player p, String[] args) {
		String toSend = MessageManager.getMessageFromConfig("message-use", null) + " ";
		for (ParticleEffect effect : ParticleEffect.getSupportedEffects()) {
			if (PermissionManager.hasEffectPermission(p, effect)) {
				toSend += effect.getName().toLowerCase().replace("_", "") + ", ";
				continue;
			}
		}
		if (toSend.equals(MessageManager.getMessageFromConfig("message-use", null) + " ")) {
			MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-no-particles", null), ChatColor.RED);
			return;
		}
		toSend = toSend + "clear";
		MessageManager.getInstance().sendMessage(p, toSend, ChatColor.GREEN);
		MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp effect <type>", ChatColor.YELLOW);
	}

	/**
	 * Called when a player uses /pp style
	 * 
	 * @param p The player who used the command
	 * @param args The arguments for the command
	 */
	private void onStyle(Player p, String[] args) {
		if (args.length == 1) {
			MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp style <type>", ChatColor.YELLOW);
			return;
		}
		String argument = args[1].replace("_", "");
		if (ParticleStyleManager.styleFromString(argument) != null) {
			ParticleStyle style = ParticleStyleManager.styleFromString(argument);
			if (!PermissionManager.hasStylePermission(p, style)) {
				MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-no-permission-style", ChatColor.AQUA + style.getName().toLowerCase() + ChatColor.RED), ChatColor.RED);
				return;
			}
			ConfigManager.getInstance().savePPlayer(p.getUniqueId(), style);
			if (style != DefaultStyles.NONE) {
				MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-now-using-style", ChatColor.AQUA + style.getName().toLowerCase() + ChatColor.GREEN), ChatColor.GREEN);
			} else {
				MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-cleared-style", null), ChatColor.GREEN);
			}
			return;
		}
		MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-invalid-type-style", null) + ChatColor.GREEN + " /pp styles", ChatColor.RED);
	}

	/**
	 * Called when a player uses /pp styles
	 * 
	 * @param p The player who used the command
	 * @param args The arguments for the command
	 */
	private void onStyles(Player p, String[] args) {
		String toSend = MessageManager.getMessageFromConfig("message-use-style", null) + " ";
		for (ParticleStyle style : ParticleStyleManager.getStyles()) {
			if (PermissionManager.hasStylePermission(p, style)) {
				toSend += style.getName().toLowerCase();
				toSend += ", ";
			}
		}
		if (toSend.endsWith(", ")) {
			toSend = toSend.substring(0, toSend.length() - 2);
		}
		if (toSend.equals(MessageManager.getMessageFromConfig("message-use-style", null) + " " + DefaultStyles.NONE.getName().toLowerCase())) {
			MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-no-styles", null), ChatColor.RED);
			return;
		}
		MessageManager.getInstance().sendMessage(p, toSend, ChatColor.GREEN);
		MessageManager.getInstance().sendMessage(p, MessageManager.getMessageFromConfig("message-usage", null) + ChatColor.AQUA + " /pp style <type>", ChatColor.YELLOW);
	}

}
