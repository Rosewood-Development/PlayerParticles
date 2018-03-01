/**
 * Copyright Esophose 2018
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.esophose.playerparticles.gui.PlayerParticlesGui;
import com.esophose.playerparticles.gui.PlayerParticlesGui.GuiState;
import com.esophose.playerparticles.manager.ConfigManager;
import com.esophose.playerparticles.manager.MessageManager;
import com.esophose.playerparticles.manager.MessageManager.MessageType;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.particles.FixedParticleEffect;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticleEffect.BlockData;
import com.esophose.playerparticles.particles.ParticleEffect.ItemData;
import com.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import com.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.styles.DefaultStyles;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.styles.api.ParticleStyleManager;
import com.esophose.playerparticles.util.ParticleUtils;

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
            onGUI(p, true);
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
            case "fixed":
                onFixed(p, cmdArgs);
                break;
            case "reset":
                onReset(p, cmdArgs);
                break;
            case "gui":
                onGUI(p, false);
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
        ParticleEffect effect = ConfigManager.getInstance().getPPlayer(p.getUniqueId(), true).getParticleEffect();
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
                    material = ParticleUtils.closestMatch(args[0]);
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
                    material = ParticleUtils.closestMatch(args[0]);
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
     * Can be executed for another player by having their name as the final command argument
     * 
     * @param p The player who used the command
     * @param args Additional arguments
     */
    private void onReset(Player p, String[] args) {
        if (args.length >= 1) {
            String altPlayerName = args[0];
            if (!PermissionManager.canUseForceReset(p)) {
                MessageManager.sendMessage(p, MessageType.FAILED_EXECUTE_NO_PERMISSION, altPlayerName);
            } else {
                Player altPlayer = getOnlinePlayerByName(altPlayerName);
                if (altPlayer == null) {
                    MessageManager.sendMessage(p, MessageType.FAILED_EXECUTE_NOT_FOUND, altPlayerName);
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
     */
    private void onEffect(Player p, String[] args) {
        if (args.length == 0) {
            MessageManager.sendMessage(p, MessageType.INVALID_TYPE);
            return;
        }
        String argument = args[0].replace("_", "");
        if (ParticleManager.effectFromString(argument) != null) {
            ParticleEffect effect = ParticleManager.effectFromString(argument);
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

    /**
     * Called when a player uses /pp fixed
     * 
     * @param p The player who used the command
     * @param args The arguments for the command
     */
    private void onFixed(Player p, String[] args) {
        if (!PermissionManager.canUseFixedEffects(p)) {
            MessageManager.sendMessage(p, MessageType.NO_PERMISSION_FIXED);
            return;
        }

        if (args.length == 0) { // General information on command
            MessageManager.sendMessage(p, MessageType.INVALID_FIXED_COMMAND);
            MessageManager.sendMessage(p, MessageType.FIXED_COMMAND_DESC_CREATE);
            MessageManager.sendMessage(p, MessageType.FIXED_COMMAND_DESC_REMOVE);
            MessageManager.sendMessage(p, MessageType.FIXED_COMMAND_DESC_LIST);
            MessageManager.sendMessage(p, MessageType.FIXED_COMMAND_DESC_INFO);
            if (p.hasPermission("playerparticles.fixed.clear")) MessageManager.sendMessage(p, MessageType.FIXED_COMMAND_DESC_CLEAR);
            return;
        }

        String cmd = args[0];

        String[] cmdArgs = new String[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            cmdArgs[i - 1] = args[i];
        }
        args = cmdArgs;

        if (cmd.equalsIgnoreCase("create")) {
            if (ConfigManager.getInstance().hasPlayerReachedMaxFixedEffects(p.getUniqueId())) {
                MessageManager.sendMessage(p, MessageType.MAX_FIXED_EFFECTS_REACHED);
                return;
            }

            if (args.length < 5) {
                MessageManager.sendMessage(p, MessageType.CREATE_FIXED_MISSING_ARGS, (5 - args.length) + "");
                return;
            }

            double xPos = -1, yPos = -1, zPos = -1;
            try {
                if (args[0].startsWith("~")) {
                    if (args[0].equals("~")) xPos = p.getLocation().getX();
                    else xPos = p.getLocation().getX() + Double.parseDouble(args[0].substring(1));
                } else {
                    xPos = Double.parseDouble(args[0]);
                }

                if (args[1].startsWith("~")) {
                    if (args[1].equals("~")) yPos = p.getLocation().getY() + 1;
                    else yPos = p.getLocation().getY() + 1 + Double.parseDouble(args[1].substring(1));
                } else {
                    yPos = Double.parseDouble(args[1]);
                }

                if (args[2].startsWith("~")) {
                    if (args[2].equals("~")) zPos = p.getLocation().getZ();
                    else zPos = p.getLocation().getZ() + Double.parseDouble(args[2].substring(1));
                } else {
                    zPos = Double.parseDouble(args[2]);
                }
            } catch (Exception e) {
                MessageManager.sendMessage(p, MessageType.CREATE_FIXED_INVALID_COORDS);
                return;
            }

            double distanceFromEffect = p.getLocation().distance(new Location(p.getWorld(), xPos, yPos, zPos));
            int maxCreationDistance = ConfigManager.getInstance().getMaxFixedEffectCreationDistance();
            if (maxCreationDistance != 0 && distanceFromEffect > maxCreationDistance) {
                MessageManager.sendMessage(p, MessageType.CREATE_FIXED_OUT_OF_RANGE, maxCreationDistance + "");
                return;
            }

            ParticleEffect effect = ParticleManager.effectFromString(args[3]);
            if (effect == null) {
                MessageManager.sendMessage(p, MessageType.CREATE_FIXED_INVALID_EFFECT, args[3]);
                return;
            } else if (!PermissionManager.hasEffectPermission(p, effect)) {
                MessageManager.sendMessage(p, MessageType.CREATE_FIXED_NO_PERMISSION_EFFECT, effect.getName());
                return;
            }

            ParticleStyle style = ParticleStyleManager.styleFromString(args[4]);
            if (style == null) {
                MessageManager.sendMessage(p, MessageType.CREATE_FIXED_INVALID_STYLE, args[4]);
                return;
            } else if (!PermissionManager.hasStylePermission(p, style)) {
                MessageManager.sendMessage(p, MessageType.CREATE_FIXED_NO_PERMISSION_STYLE, args[4]);
                return;
            }

            if (!style.canBeFixed()) {
                MessageManager.sendMessage(p, MessageType.CREATE_FIXED_NON_FIXABLE_STYLE, style.getName());
                return;
            }

            ItemData itemData = null;
            BlockData blockData = null;
            OrdinaryColor colorData = null;
            NoteColor noteColorData = null;

            if (args.length > 5) {
                if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                    if (effect == ParticleEffect.NOTE) {
                        if (args[5].equalsIgnoreCase("rainbow")) {
                            noteColorData = new NoteColor(99);
                        } else {
                            int note = -1;
                            try {
                                note = Integer.parseInt(args[5]);
                            } catch (Exception e) {
                                MessageManager.sendMessage(p, MessageType.CREATE_FIXED_DATA_ERROR, "note");
                                return;
                            }

                            if (note < 0 || note > 23) {
                                MessageManager.sendMessage(p, MessageType.CREATE_FIXED_DATA_ERROR, "note");
                                return;
                            }

                            noteColorData = new NoteColor(note);
                        }
                    } else {
                        if (args[5].equalsIgnoreCase("rainbow")) {
                            colorData = new OrdinaryColor(999, 999, 999);
                        } else {
                            int r = -1;
                            int g = -1;
                            int b = -1;

                            try {
                                r = Integer.parseInt(args[5]);
                                g = Integer.parseInt(args[6]);
                                b = Integer.parseInt(args[7]);
                            } catch (Exception e) {
                                MessageManager.sendMessage(p, MessageType.CREATE_FIXED_DATA_ERROR, "color");
                                return;
                            }

                            if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                                MessageManager.sendMessage(p, MessageType.CREATE_FIXED_DATA_ERROR, "color");
                                return;
                            }

                            colorData = new OrdinaryColor(r, g, b);
                        }
                    }
                } else if (effect.hasProperty(ParticleProperty.REQUIRES_DATA)) {
                    if (effect == ParticleEffect.BLOCK_CRACK || effect == ParticleEffect.BLOCK_DUST || effect == ParticleEffect.FALLING_DUST) {
                        Material material = null;
                        int data = -1;

                        try {
                            material = ParticleUtils.closestMatch(args[5]);
                            if (material == null) material = Material.matchMaterial(args[5]);
                            if (material == null) throw new Exception();
                        } catch (Exception e) {
                            MessageManager.sendMessage(p, MessageType.CREATE_FIXED_DATA_ERROR, "block");
                            return;
                        }

                        try {
                            data = Integer.parseInt(args[6]);
                        } catch (Exception e) {
                            MessageManager.sendMessage(p, MessageType.CREATE_FIXED_DATA_ERROR, "block");
                            return;
                        }

                        if (data < 0 || data > 15 || !material.isBlock()) {
                            MessageManager.sendMessage(p, MessageType.CREATE_FIXED_DATA_ERROR, "block");
                            return;
                        }

                        blockData = new BlockData(material, (byte) data);
                    } else if (effect == ParticleEffect.ITEM_CRACK) {
                        Material material = null;
                        int data = -1;

                        try {
                            material = ParticleUtils.closestMatch(args[5]);
                            if (material == null) material = Material.matchMaterial(args[5]);
                            if (material == null) throw new Exception();
                        } catch (Exception e) {
                            MessageManager.sendMessage(p, MessageType.CREATE_FIXED_DATA_ERROR, "item");
                            return;
                        }

                        try {
                            data = Integer.parseInt(args[6]);
                        } catch (Exception e) {
                            MessageManager.sendMessage(p, MessageType.CREATE_FIXED_DATA_ERROR, "item");
                            return;
                        }

                        if (data < 0 || data > 15 || material.isBlock()) {
                            MessageManager.sendMessage(p, MessageType.CREATE_FIXED_DATA_ERROR, "item");
                            return;
                        }

                        itemData = new ItemData(material, (byte) data);
                    }
                }
            }
            
            FixedParticleEffect fixedEffect = new FixedParticleEffect(p.getUniqueId(), // @formatter:off
																	  ConfigManager.getInstance().getNextFixedEffectId(p.getUniqueId()), 
																	  p.getLocation().getWorld().getName(), xPos, yPos, zPos, 
																	  effect, style, itemData, blockData, colorData, noteColorData); // @formatter:on

            MessageManager.sendMessage(p, MessageType.CREATE_FIXED_SUCCESS);
            ConfigManager.getInstance().saveFixedEffect(fixedEffect);
        } else if (cmd.equalsIgnoreCase("remove")) {
            if (args.length < 1) {
                MessageManager.sendMessage(p, MessageType.REMOVE_FIXED_NO_ARGS);
                return;
            }

            int id = -1;
            try {
                id = Integer.parseInt(args[0]);
            } catch (Exception e) {
                MessageManager.sendMessage(p, MessageType.REMOVE_FIXED_INVALID_ARGS);
                return;
            }

            if (ConfigManager.getInstance().removeFixedEffect(p.getUniqueId(), id)) {
                MessageManager.sendMessage(p, MessageType.REMOVE_FIXED_SUCCESS, id + "");
            } else {
                MessageManager.sendMessage(p, MessageType.REMOVE_FIXED_NONEXISTANT, id + "");
            }
        } else if (cmd.equalsIgnoreCase("list")) {
            List<Integer> ids = ConfigManager.getInstance().getFixedEffectIdsForPlayer(p.getUniqueId());
            Collections.sort(ids);

            if (ids.isEmpty()) {
                MessageManager.sendMessage(p, MessageType.LIST_FIXED_NONE);
                return;
            }

            String msg = MessageType.LIST_FIXED_SUCCESS.getMessage();
            boolean first = true;
            for (int id : ids) {
                if (!first) msg += ", ";
                else first = false;
                msg += id;
            }

            MessageManager.sendCustomMessage(p, msg);
        } else if (cmd.equalsIgnoreCase("info")) {
            if (args.length < 1) {
                MessageManager.sendMessage(p, MessageType.INFO_FIXED_NO_ARGS);
                return;
            }

            int id = -1;
            try {
                id = Integer.parseInt(args[0]);
            } catch (Exception e) {
                MessageManager.sendMessage(p, MessageType.INFO_FIXED_INVALID_ARGS);
                return;
            }

            FixedParticleEffect fixedEffect = ConfigManager.getInstance().getFixedEffectForPlayerById(p.getUniqueId(), id);

            if (fixedEffect == null) {
                MessageManager.sendMessage(p, MessageType.INFO_FIXED_NONEXISTANT, id + "");
                return;
            }

            DecimalFormat df = new DecimalFormat("0.##"); // Decimal formatter so the coords aren't super long
            String listMessage = MessageType.INFO_FIXED_INFO.getMessage() // @formatter:off
								 .replaceAll("\\{0\\}", fixedEffect.getId() + "")
								 .replaceAll("\\{1\\}", fixedEffect.getLocation().getWorld().getName())
								 .replaceAll("\\{2\\}", df.format(fixedEffect.getLocation().getX()) + "")
								 .replaceAll("\\{3\\}", df.format(fixedEffect.getLocation().getY()) + "")
								 .replaceAll("\\{4\\}", df.format(fixedEffect.getLocation().getZ()) + "")
								 .replaceAll("\\{5\\}", fixedEffect.getParticleEffect().getName())
								 .replaceAll("\\{6\\}", fixedEffect.getParticleStyle().getName())
								 .replaceAll("\\{7\\}", fixedEffect.getParticleDataString()); // @formatter:on
            MessageManager.sendCustomMessage(p, listMessage);
        } else if (cmd.equalsIgnoreCase("clear")) {
            if (!p.hasPermission("playerparticles.fixed.clear")) {
                MessageManager.sendMessage(p, MessageType.CLEAR_FIXED_NO_PERMISSION);
                return;
            }

            if (args.length < 1) {
                MessageManager.sendMessage(p, MessageType.CLEAR_FIXED_NO_ARGS);
                return;
            }

            int radius = -1;
            try {
                radius = Math.abs(Integer.parseInt(args[0]));
            } catch (Exception e) {
                MessageManager.sendMessage(p, MessageType.CLEAR_FIXED_INVALID_ARGS);
                return;
            }

            ArrayList<FixedParticleEffect> fixedEffectsToRemove = new ArrayList<FixedParticleEffect>();

            for (FixedParticleEffect fixedEffect : ParticleManager.fixedParticleEffects)
                if (fixedEffect.getLocation().getWorld() == p.getLocation().getWorld() && fixedEffect.getLocation().distance(p.getLocation()) <= radius) fixedEffectsToRemove.add(fixedEffect);

            for (FixedParticleEffect fixedEffect : fixedEffectsToRemove)
                ConfigManager.getInstance().removeFixedEffect(fixedEffect.getOwnerUniqueId(), fixedEffect.getId());

            String clearMessage = MessageType.CLEAR_FIXED_SUCCESS.getMessage() // @formatter:off
								  .replaceAll("\\{0\\}", fixedEffectsToRemove.size() + "")
								  .replaceAll("\\{1\\}", radius + ""); // @formatter:on
            MessageManager.sendCustomMessage(p, clearMessage);
            return;
        } else {
            MessageManager.sendMessage(p, MessageType.INVALID_FIXED_COMMAND);
            MessageManager.sendMessage(p, MessageType.FIXED_COMMAND_DESC_CREATE);
            MessageManager.sendMessage(p, MessageType.FIXED_COMMAND_DESC_REMOVE);
            MessageManager.sendMessage(p, MessageType.FIXED_COMMAND_DESC_LIST);
            MessageManager.sendMessage(p, MessageType.FIXED_COMMAND_DESC_INFO);
            if (p.hasPermission("playerparticles.fixed.clear")) MessageManager.sendMessage(p, MessageType.FIXED_COMMAND_DESC_CLEAR);
        }
    }

    private void onGUI(Player p, boolean byDefault) {
        if (PlayerParticlesGui.isGuiDisabled()) {
            if (byDefault) {
                onHelp(p);
            } else {
                MessageManager.sendMessage(p, MessageType.GUI_DISABLED);
            }
            return;
        }

        if (PermissionManager.getEffectsUserHasPermissionFor(p).size() == 1) {
            if (byDefault) {
                onHelp(p);
            } else {
                MessageManager.sendMessage(p, MessageType.NO_PARTICLES);
            }
            return;
        }

        if (byDefault) {
            MessageManager.sendMessage(p, MessageType.GUI_BY_DEFAULT);
        }

        PlayerParticlesGui.changeState(ConfigManager.getInstance().getPPlayer(p.getUniqueId(), true), GuiState.DEFAULT);
    }

}
