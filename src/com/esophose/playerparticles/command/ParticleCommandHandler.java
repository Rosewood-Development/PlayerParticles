/**
 * Copyright Esophose 2018
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.command;

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
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.gui.PlayerParticlesGui;
import com.esophose.playerparticles.gui.PlayerParticlesGui.GuiState;
import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.particles.FixedParticleEffect;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import com.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.styles.DefaultStyles;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.styles.api.ParticleStyleManager;
import com.esophose.playerparticles.util.ParticleUtils;

public class ParticleCommandHandler implements CommandExecutor, TabCompleter {
    
    /**
     * A list of all commands
     */
    private static List<CommandModule> commands;
    
    static {
        commands.add(new AddCommandModule());
        commands.add(new DataCommandModule());
        commands.add(new DefaultCommandModule());
        commands.add(new EditCommandModule());
        commands.add(new EffectCommandModule());
        commands.add(new EffectsCommandModule());
        commands.add(new FixedCommandModule());
        commands.add(new GroupCommandModule());
        commands.add(new GUICommandModule());
        commands.add(new HelpCommandModule());
        commands.add(new InfoCommandModule());
        commands.add(new ListCommandModule());
        commands.add(new RemoveCommandModule());
        commands.add(new ResetCommandModule());
        commands.add(new StyleCommandModule());
        commands.add(new StylesCommandModule());
        commands.add(new VersionCommandModule());
        commands.add(new WorldsCommandModule());
    }
    
    public static CommandModule findMatchingCommand(String commandName) {
    	for (CommandModule commandModule : commands)
    		if (commandModule.getName().equalsIgnoreCase(commandName))
    			return commandModule;
    	return null;
    }

    /**
     * Called when a player executes a /pp command
     * Checks what /pp command it is and calls the corresponding module
     * 
     * @param sender Who executed the command
     * @param cmd The command
     * @param label The command label
     * @param args The arguments following the command
     * @return true
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        
        DataManager.getPPlayer(p.getUniqueId(), (pplayer) -> {
        	String commandName = args.length > 0 ? args[0] : "";
        	CommandModule commandModule = findMatchingCommand(commandName);
        	
        	if (commandModule != null) {
        		if (commandModule.requiresEffects() && PermissionManager.getEffectsUserHasPermissionFor(p).size() == 1) {
        			LangManager.sendMessage(p, Lang.NO_PARTICLES); // TODO: Rename to NO_EFFECTS
        		} else {
        			String[] cmdArgs = new String[0];
                    if (args.length > 1) cmdArgs = Arrays.copyOfRange(args, 1, args.length);
            		commandModule.onCommandExecute(pplayer, cmdArgs);
        		}
        	} else {
        		LangManager.sendMessage(p, Lang.INVALID_ARGUMENTS); // TODO: Rename to UNKNOWN_COMMAND
        	}
        });
        
        return true;
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
        LangManager.sendMessage(p, Lang.AVAILABLE_COMMANDS);
        LangManager.sendMessage(p, Lang.COMMAND_USAGE);
    }

    /**
     * Called when a player uses /pp data
     * 
     * @param p The player who used the command
     * @param args The arguments for the command
     */
    private void onData(Player p, String[] args) {
        ParticleEffect effect = DataManager.getInstance().getPPlayer(p.getUniqueId()).getParticleEffect();
        if ((!effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA) && !effect.hasProperty(ParticleProperty.COLORABLE)) || args.length == 0) {
            if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                if (effect == ParticleEffect.NOTE) {
                    LangManager.sendMessage(p, Lang.DATA_USAGE, "note");
                    LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.NOTE_DATA_USAGE.get());
                } else {
                    LangManager.sendMessage(p, Lang.DATA_USAGE, "color");
                    LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.COLOR_DATA_USAGE.get());
                }
            } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                if (effect == ParticleEffect.ITEM) {
                    LangManager.sendMessage(p, Lang.DATA_USAGE, "item");
                    LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.ITEM_DATA_USAGE.get());
                } else {
                    LangManager.sendMessage(p, Lang.DATA_USAGE, "block");
                    LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.BLOCK_DATA_USAGE.get());
                }
            } else {
                LangManager.sendMessage(p, Lang.NO_DATA_USAGE);
            }
            return;
        }
        if (effect.hasProperty(ParticleProperty.COLORABLE)) {
            if (effect == ParticleEffect.NOTE) {
                if (args[0].equalsIgnoreCase("rainbow")) {
                    DataManager.getInstance().savePPlayer(p.getUniqueId(), new NoteColor(99));
                    LangManager.sendMessage(p, Lang.DATA_APPLIED, "note");
                    return;
                }

                int note = -1;
                try {
                    note = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    LangManager.sendMessage(p, Lang.DATA_INVALID_ARGUMENTS, "note");
                    LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.NOTE_DATA_USAGE.get());
                    return;
                }

                if (note < 0 || note > 23) {
                    LangManager.sendMessage(p, Lang.DATA_INVALID_ARGUMENTS, "note");
                    LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.NOTE_DATA_USAGE.get());
                    return;
                }

                DataManager.getInstance().savePPlayer(p.getUniqueId(), new NoteColor(note));
                LangManager.sendMessage(p, Lang.DATA_APPLIED, "note");
            } else {
                if (args[0].equalsIgnoreCase("rainbow")) {
                    DataManager.getInstance().savePPlayer(p.getUniqueId(), new OrdinaryColor(999, 999, 999));
                    LangManager.sendMessage(p, Lang.DATA_APPLIED, "color");
                } else if (args.length >= 3) {
                    int r = -1;
                    int g = -1;
                    int b = -1;

                    try {
                        r = Integer.parseInt(args[0]);
                        g = Integer.parseInt(args[1]);
                        b = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        LangManager.sendMessage(p, Lang.DATA_INVALID_ARGUMENTS, "color");
                        LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.COLOR_DATA_USAGE.get());
                        return;
                    }

                    if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                        LangManager.sendMessage(p, Lang.DATA_INVALID_ARGUMENTS, "color");
                        LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.COLOR_DATA_USAGE.get());
                        return;
                    }

                    DataManager.getInstance().savePPlayer(p.getUniqueId(), new OrdinaryColor(r, g, b));
                    LangManager.sendMessage(p, Lang.DATA_APPLIED, "color");
                } else {
                    LangManager.sendMessage(p, Lang.DATA_INVALID_ARGUMENTS, "color");
                    LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.COLOR_DATA_USAGE.get());
                }
            }
        } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
            if (effect == ParticleEffect.ITEM) {
                Material material = null;
                try {
                    material = ParticleUtils.closestMatch(args[0]);
                    if (material == null) material = Material.matchMaterial(args[0]);
                    if (material == null) throw new Exception();
                } catch (Exception e) {
                    LangManager.sendMessage(p, Lang.DATA_MATERIAL_UNKNOWN, "item");
                    LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.ITEM_DATA_USAGE.get());
                    return;
                }

                if (material.isBlock()) {
                    LangManager.sendMessage(p, Lang.DATA_MATERIAL_MISMATCH, "item");
                    LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.ITEM_DATA_USAGE.get());
                    return;
                }

                DataManager.getInstance().savePPlayer(p.getUniqueId(), new ItemData(material));
                LangManager.sendMessage(p, Lang.DATA_APPLIED, "item");
            } else {
                Material material = null;
                try {
                    material = ParticleUtils.closestMatch(args[0]);
                    if (material == null) material = Material.matchMaterial(args[0]);
                    if (material == null) throw new Exception();
                } catch (Exception e) {
                    LangManager.sendMessage(p, Lang.DATA_MATERIAL_UNKNOWN, "block");
                    LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.BLOCK_DATA_USAGE.get());
                    return;
                }

                if (!material.isBlock()) {
                    LangManager.sendMessage(p, Lang.DATA_MATERIAL_MISMATCH, "block");
                    LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.BLOCK_DATA_USAGE.get());
                    return;
                }

                DataManager.getInstance().savePPlayer(p.getUniqueId(), new BlockData(material));
                LangManager.sendMessage(p, Lang.DATA_APPLIED, "block");
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
                LangManager.sendMessage(p, Lang.FAILED_EXECUTE_NO_PERMISSION, altPlayerName);
            } else {
                Player altPlayer = getOnlinePlayerByName(altPlayerName);
                if (altPlayer == null) {
                    LangManager.sendMessage(p, Lang.FAILED_EXECUTE_NOT_FOUND, altPlayerName);
                } else {
                    DataManager.getInstance().resetPPlayer(altPlayer.getUniqueId());
                    LangManager.sendMessage(altPlayer, Lang.RESET);

                    LangManager.sendMessage(p, Lang.EXECUTED_FOR_PLAYER, altPlayer.getName());
                }
            }
        } else {
            DataManager.getInstance().resetPPlayer(p.getUniqueId());
            LangManager.sendMessage(p, Lang.RESET);
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
            LangManager.sendMessage(p, Lang.INVALID_TYPE);
            return;
        }
        String argument = args[0];
        if (ParticleManager.effectFromString(argument) != null) {
            ParticleEffect effect = ParticleManager.effectFromString(argument);
            if (!PermissionManager.hasEffectPermission(p, effect)) {
                LangManager.sendMessage(p, Lang.NO_PERMISSION, effect.getName());
                return;
            }
            DataManager.getInstance().savePPlayer(p.getUniqueId(), effect);
            if (effect != ParticleEffect.NONE) {
                LangManager.sendMessage(p, Lang.NOW_USING, effect.getName());
            } else {
                LangManager.sendMessage(p, Lang.CLEARED_PARTICLES);
            }
            return;
        }
        LangManager.sendMessage(p, Lang.INVALID_TYPE);
    }

    /**
     * Called when a player uses /pp effects
     * 
     * @param p The player who used the command
     */
    private void onEffects(Player p) {
        if (PermissionManager.getEffectsUserHasPermissionFor(p).size() == 1) {
            LangManager.sendMessage(p, Lang.NO_PARTICLES);
            return;
        }
        
        String toSend = Lang.USE.get() + " ";
        for (ParticleEffect effect : ParticleEffect.getSupportedEffects()) {
            if (PermissionManager.hasEffectPermission(p, effect)) {
                toSend += effect.getName() + ", ";
                continue;
            }
        }
        if (toSend.endsWith(", ")) {
            toSend = toSend.substring(0, toSend.length() - 2);
        }
        
        LangManager.sendCustomMessage(p, toSend);
        LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.PARTICLE_USAGE.get());
    }

    /**
     * Called when a player uses /pp style
     * 
     * @param p The player who used the command
     * @param args The arguments for the command
     */
    private void onStyle(Player p, String[] args) {
        if (PermissionManager.getStylesUserHasPermissionFor(p).size() == 1) {
            LangManager.sendMessage(p, Lang.NO_STYLES);
            return;
        }
        if (args.length == 0) {
            LangManager.sendMessage(p, Lang.INVALID_TYPE_STYLE);
            return;
        }
        String argument = args[0];
        if (ParticleStyleManager.styleFromString(argument) != null) {
            ParticleStyle style = ParticleStyleManager.styleFromString(argument);
            if (!PermissionManager.hasStylePermission(p, style)) {
                LangManager.sendMessage(p, Lang.NO_PERMISSION_STYLE, style.getName());
                return;
            }
            DataManager.getInstance().savePPlayer(p.getUniqueId(), style);
            if (style != DefaultStyles.NONE) {
                LangManager.sendMessage(p, Lang.NOW_USING_STYLE, style.getName());
            } else {
                LangManager.sendMessage(p, Lang.CLEARED_STYLE);
            }
            return;
        }
        LangManager.sendMessage(p, Lang.INVALID_TYPE_STYLE);
    }

    /**
     * Called when a player uses /pp styles
     * 
     * @param p The player who used the command
     */
    private void onStyles(Player p) {
        if (PermissionManager.getStylesUserHasPermissionFor(p).size() == 1) {
            LangManager.sendMessage(p, Lang.NO_STYLES);
            return;
        }
        
        String toSend = Lang.USE.get() + " ";
        for (ParticleStyle style : ParticleStyleManager.getStyles()) {
            if (PermissionManager.hasStylePermission(p, style)) {
                toSend += style.getName();
                toSend += ", ";
            }
        }
        if (toSend.endsWith(", ")) {
            toSend = toSend.substring(0, toSend.length() - 2);
        }
        
        LangManager.sendCustomMessage(p, toSend);
        LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.STYLE_USAGE.get());
    }

    /**
     * Called when a player uses /pp fixed
     * 
     * @param p The player who used the command
     * @param args The arguments for the command
     */
    private void onFixed(Player p, String[] args) {
        if (!PermissionManager.canUseFixedEffects(p)) {
            LangManager.sendMessage(p, Lang.NO_PERMISSION_FIXED);
            return;
        }

        if (args.length == 0) { // General information on command
            LangManager.sendMessage(p, Lang.INVALID_FIXED_COMMAND);
            LangManager.sendMessage(p, Lang.FIXED_COMMAND_DESC_CREATE);
            LangManager.sendMessage(p, Lang.FIXED_COMMAND_DESC_REMOVE);
            LangManager.sendMessage(p, Lang.FIXED_COMMAND_DESC_LIST);
            LangManager.sendMessage(p, Lang.FIXED_COMMAND_DESC_INFO);
            if (p.hasPermission("playerparticles.fixed.clear")) LangManager.sendMessage(p, Lang.FIXED_COMMAND_DESC_CLEAR);
            return;
        }

        String cmd = args[0];

        String[] cmdArgs = new String[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            cmdArgs[i - 1] = args[i];
        }
        args = cmdArgs;

        if (cmd.equalsIgnoreCase("create")) {
            final String[] f_args = args;
            DataManager.getInstance().hasPlayerReachedMaxFixedEffects(p.getUniqueId(), (reachedMax) -> {
                if (reachedMax) {
                    LangManager.sendMessage(p, Lang.MAX_FIXED_EFFECTS_REACHED);
                    return;
                }
                
                if (f_args.length < 5) {
                    LangManager.sendMessage(p, Lang.CREATE_FIXED_MISSING_ARGS, (5 - f_args.length) + "");
                    return;
                }

                double xPos = -1, yPos = -1, zPos = -1;
                try {
                    if (f_args[0].startsWith("~")) {
                        if (f_args[0].equals("~")) xPos = p.getLocation().getX();
                        else xPos = p.getLocation().getX() + Double.parseDouble(f_args[0].substring(1));
                    } else {
                        xPos = Double.parseDouble(f_args[0]);
                    }

                    if (f_args[1].startsWith("~")) {
                        if (f_args[1].equals("~")) yPos = p.getLocation().getY() + 1;
                        else yPos = p.getLocation().getY() + 1 + Double.parseDouble(f_args[1].substring(1));
                    } else {
                        yPos = Double.parseDouble(f_args[1]);
                    }

                    if (f_args[2].startsWith("~")) {
                        if (f_args[2].equals("~")) zPos = p.getLocation().getZ();
                        else zPos = p.getLocation().getZ() + Double.parseDouble(f_args[2].substring(1));
                    } else {
                        zPos = Double.parseDouble(f_args[2]);
                    }
                } catch (Exception e) {
                    LangManager.sendMessage(p, Lang.CREATE_FIXED_INVALID_COORDS);
                    return;
                }

                double distanceFromEffect = p.getLocation().distance(new Location(p.getWorld(), xPos, yPos, zPos));
                int maxCreationDistance = DataManager.getInstance().getMaxFixedEffectCreationDistance();
                if (maxCreationDistance != 0 && distanceFromEffect > maxCreationDistance) {
                    LangManager.sendMessage(p, Lang.CREATE_FIXED_OUT_OF_RANGE, maxCreationDistance + "");
                    return;
                }

                ParticleEffect effect = ParticleManager.effectFromString(f_args[3]);
                if (effect == null) {
                    LangManager.sendMessage(p, Lang.CREATE_FIXED_INVALID_EFFECT, f_args[3]);
                    return;
                } else if (!PermissionManager.hasEffectPermission(p, effect)) {
                    LangManager.sendMessage(p, Lang.CREATE_FIXED_NO_PERMISSION_EFFECT, effect.getName());
                    return;
                }

                ParticleStyle style = ParticleStyleManager.styleFromString(f_args[4]);
                if (style == null) {
                    LangManager.sendMessage(p, Lang.CREATE_FIXED_INVALID_STYLE, f_args[4]);
                    return;
                } else if (!PermissionManager.hasStylePermission(p, style)) {
                    LangManager.sendMessage(p, Lang.CREATE_FIXED_NO_PERMISSION_STYLE, f_args[4]);
                    return;
                }

                if (!style.canBeFixed()) {
                    LangManager.sendMessage(p, Lang.CREATE_FIXED_NON_FIXABLE_STYLE, style.getName());
                    return;
                }

                ItemData itemData = null;
                BlockData blockData = null;
                OrdinaryColor colorData = null;
                NoteColor noteColorData = null;

                if (f_args.length > 5) {
                    if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                        if (effect == ParticleEffect.NOTE) {
                            if (f_args[5].equalsIgnoreCase("rainbow")) {
                                noteColorData = new NoteColor(99);
                            } else {
                                int note = -1;
                                try {
                                    note = Integer.parseInt(f_args[5]);
                                } catch (Exception e) {
                                    LangManager.sendMessage(p, Lang.CREATE_FIXED_DATA_ERROR, "note");
                                    return;
                                }

                                if (note < 0 || note > 23) {
                                    LangManager.sendMessage(p, Lang.CREATE_FIXED_DATA_ERROR, "note");
                                    return;
                                }

                                noteColorData = new NoteColor(note);
                            }
                        } else {
                            if (f_args[5].equalsIgnoreCase("rainbow")) {
                                colorData = new OrdinaryColor(999, 999, 999);
                            } else {
                                int r = -1;
                                int g = -1;
                                int b = -1;

                                try {
                                    r = Integer.parseInt(f_args[5]);
                                    g = Integer.parseInt(f_args[6]);
                                    b = Integer.parseInt(f_args[7]);
                                } catch (Exception e) {
                                    LangManager.sendMessage(p, Lang.CREATE_FIXED_DATA_ERROR, "color");
                                    return;
                                }

                                if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                                    LangManager.sendMessage(p, Lang.CREATE_FIXED_DATA_ERROR, "color");
                                    return;
                                }

                                colorData = new OrdinaryColor(r, g, b);
                            }
                        }
                    } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                        if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST) {
                            Material material = null;
                            try {
                                material = ParticleUtils.closestMatch(f_args[5]);
                                if (material == null) material = Material.matchMaterial(f_args[5]);
                                if (material == null) throw new Exception();
                            } catch (Exception e) {
                                LangManager.sendMessage(p, Lang.CREATE_FIXED_DATA_ERROR, "block");
                                return;
                            }

                            blockData = new BlockData(material);
                        } else if (effect == ParticleEffect.ITEM) {
                            Material material = null;
                            try {
                                material = ParticleUtils.closestMatch(f_args[5]);
                                if (material == null) material = Material.matchMaterial(f_args[5]);
                                if (material == null) throw new Exception();
                            } catch (Exception e) {
                                LangManager.sendMessage(p, Lang.CREATE_FIXED_DATA_ERROR, "item");
                                return;
                            }

                            itemData = new ItemData(material);
                        }
                    }
                }
                
                // If somebody knows how to avoid this, please create a pull request
                final double f_xPos = xPos, f_yPos = yPos, f_zPos = zPos;
                final ItemData f_itemData = itemData;
                final BlockData f_blockData = blockData;
                final OrdinaryColor f_colorData = colorData;
                final NoteColor f_noteData = noteColorData;
                
                DataManager.getInstance().getNextFixedEffectId(p.getUniqueId(), (nextFixedEffectId) -> {
                    FixedParticleEffect fixedEffect = new FixedParticleEffect(p.getUniqueId(), // @formatter:off
                                                                              nextFixedEffectId, 
                                                                              p.getLocation().getWorld().getName(), f_xPos, f_yPos, f_zPos, 
                                                                              effect, style, f_itemData, f_blockData, f_colorData, f_noteData); // @formatter:on

                    LangManager.sendMessage(p, Lang.CREATE_FIXED_SUCCESS);
                    DataManager.getInstance().saveFixedEffect(fixedEffect);
                });
            });
        } else if (cmd.equalsIgnoreCase("remove")) {
            if (args.length < 1) {
                LangManager.sendMessage(p, Lang.REMOVE_FIXED_NO_ARGS);
                return;
            }

            int id = -1;
            try {
                id = Integer.parseInt(args[0]);
            } catch (Exception e) {
                LangManager.sendMessage(p, Lang.REMOVE_FIXED_INVALID_ARGS);
                return;
            }

            final int f_id = id;
            DataManager.getInstance().removeFixedEffect(p.getUniqueId(), id, (successful) -> {
                if (successful) {
                    LangManager.sendMessage(p, Lang.REMOVE_FIXED_SUCCESS, f_id + "");
                } else {
                    LangManager.sendMessage(p, Lang.REMOVE_FIXED_NONEXISTANT, f_id + "");
                }
            });
        } else if (cmd.equalsIgnoreCase("list")) {
            DataManager.getInstance().getFixedEffectIdsForPlayer(p.getUniqueId(), (ids) -> {
                Collections.sort(ids);

                if (ids.isEmpty()) {
                    LangManager.sendMessage(p, Lang.LIST_FIXED_NONE);
                    return;
                }

                String msg = Lang.LIST_FIXED_SUCCESS.get();
                boolean first = true;
                for (int id : ids) {
                    if (!first) msg += ", ";
                    else first = false;
                    msg += id;
                }

                LangManager.sendCustomMessage(p, msg);
            });
        } else if (cmd.equalsIgnoreCase("info")) {
            if (args.length < 1) {
                LangManager.sendMessage(p, Lang.INFO_FIXED_NO_ARGS);
                return;
            }

            int id = -1;
            try {
                id = Integer.parseInt(args[0]);
            } catch (Exception e) {
                LangManager.sendMessage(p, Lang.INFO_FIXED_INVALID_ARGS);
                return;
            }
            
            
            final int f_id = id;
            DataManager.getInstance().getFixedEffectForPlayerById(p.getUniqueId(), id, (fixedEffect) -> {
                if (fixedEffect == null) {
                    LangManager.sendMessage(p, Lang.INFO_FIXED_NONEXISTANT, f_id + "");
                    return;
                }

                DecimalFormat df = new DecimalFormat("0.##"); // Decimal formatter so the coords aren't super long
                String listMessage = Lang.INFO_FIXED_INFO.get() // @formatter:off
                                     .replaceAll("\\{0\\}", fixedEffect.getId() + "")
                                     .replaceAll("\\{1\\}", fixedEffect.getLocation().getWorld().getName())
                                     .replaceAll("\\{2\\}", df.format(fixedEffect.getLocation().getX()) + "")
                                     .replaceAll("\\{3\\}", df.format(fixedEffect.getLocation().getY()) + "")
                                     .replaceAll("\\{4\\}", df.format(fixedEffect.getLocation().getZ()) + "")
                                     .replaceAll("\\{5\\}", fixedEffect.getParticleEffect().getName())
                                     .replaceAll("\\{6\\}", fixedEffect.getParticleStyle().getName())
                                     .replaceAll("\\{7\\}", fixedEffect.getDataString()); // @formatter:on
                LangManager.sendCustomMessage(p, listMessage);
            });
        } else if (cmd.equalsIgnoreCase("clear")) {
            if (!p.hasPermission("playerparticles.fixed.clear")) {
                LangManager.sendMessage(p, Lang.CLEAR_FIXED_NO_PERMISSION);
                return;
            }

            if (args.length < 1) {
                LangManager.sendMessage(p, Lang.CLEAR_FIXED_NO_ARGS);
                return;
            }

            int radius = -1;
            try {
                radius = Math.abs(Integer.parseInt(args[0]));
            } catch (Exception e) {
                LangManager.sendMessage(p, Lang.CLEAR_FIXED_INVALID_ARGS);
                return;
            }

            ArrayList<FixedParticleEffect> fixedEffectsToRemove = new ArrayList<FixedParticleEffect>();

            for (FixedParticleEffect fixedEffect : ParticleManager.fixedParticleEffects)
                if (fixedEffect.getLocation().getWorld() == p.getLocation().getWorld() && fixedEffect.getLocation().distance(p.getLocation()) <= radius) 
                    fixedEffectsToRemove.add(fixedEffect);

            for (FixedParticleEffect fixedEffect : fixedEffectsToRemove) 
                DataManager.getInstance().removeFixedEffect(fixedEffect.getOwnerUniqueId(), fixedEffect.getId(), (successful) -> { });

            String clearMessage = Lang.CLEAR_FIXED_SUCCESS.get() // @formatter:off
								  .replaceAll("\\{0\\}", fixedEffectsToRemove.size() + "")
								  .replaceAll("\\{1\\}", radius + ""); // @formatter:on
            LangManager.sendCustomMessage(p, clearMessage);
            return;
        } else {
            LangManager.sendMessage(p, Lang.INVALID_FIXED_COMMAND);
            LangManager.sendMessage(p, Lang.FIXED_COMMAND_DESC_CREATE);
            LangManager.sendMessage(p, Lang.FIXED_COMMAND_DESC_REMOVE);
            LangManager.sendMessage(p, Lang.FIXED_COMMAND_DESC_LIST);
            LangManager.sendMessage(p, Lang.FIXED_COMMAND_DESC_INFO);
            if (p.hasPermission("playerparticles.fixed.clear")) LangManager.sendMessage(p, Lang.FIXED_COMMAND_DESC_CLEAR);
        }
    }
    
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

        if (cmd.getName().equalsIgnoreCase("pp")) {
            if (args.length > 1) {
            	CommandModule commandModule = findMatchingCommand(args[0]);
            	if (commandModule != null) {
            		String[] cmdArgs = Arrays.copyOfRange(args, 1, args.length);
            		return commandModule.onTabComplete(DataManager.getPPlayer(((Player) sender).getUniqueId()), cmdArgs);
            	}
            	
            	// TODO: Move to correct CommandModules
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
                    PPlayer pplayer = DataManager.getPPlayer(((Player) sender).getUniqueId());
                    if (pplayer == null) {
                        completions.add(ChatColor.stripColor(Lang.NO_DATA_USAGE.get()));
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
                        completions.add(ChatColor.stripColor(Lang.NO_DATA_USAGE.get()));
                    }
                }
            } else {
                List<String> commands = new ArrayList<String>(Arrays.asList(COMMANDS));
                StringUtil.copyPartialMatches(args[0], commands, completions);
            }
        }
        return null;
    }

    public static String[] getCommandsList() {
        return COMMANDS;
    }

}
