package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticleGroup;
import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import com.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.util.ParticleUtils;

public class EditCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        if (args.length < 3) {
            CommandModule.printUsage(pplayer, this);
            return;
        }
        
        int id = -1;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            LangManager.sendMessage(pplayer, Lang.ID_INVALID);
            return;
        }
        
        if (id <= 0) {
            LangManager.sendMessage(pplayer, Lang.ID_INVALID);
            return;
        }
        
        if (pplayer.getActiveParticle(id) == null) {
            LangManager.sendMessage(pplayer, Lang.ID_UNKNOWN, id);
            return;
        }
        
        String[] cmdArgs = new String[args.length - 2];
        for (int i = 2; i < args.length; i++) {
            cmdArgs[i - 2] = args[i];
        }
        
        switch (args[1].toLowerCase()) {
        case "effect":
            editEffect(pplayer, id, cmdArgs);
            break;
        case "style":
            editStyle(pplayer, id, cmdArgs);
            break;
        case "data":
            editData(pplayer, id, cmdArgs);
            break;
        default:
            LangManager.sendMessage(pplayer, Lang.EDIT_INVALID_PROPERTY, args[1]);
            break;
        }
    }
    
    /**
     * Executes the effect subcommand
     * 
     * @param pplayer The PPlayer executing the command
     * @param id The target particle ID
     * @param args The rest of the args
     */
    private void editEffect(PPlayer pplayer, int id, String[] args) {
        ParticleEffect effect = ParticleEffect.fromName(args[0]);
        if (effect == null) {
            LangManager.sendMessage(pplayer, Lang.EFFECT_INVALID, args[0]);
            return;
        } else if (!PermissionManager.hasEffectPermission(pplayer.getPlayer(), effect)) {
            LangManager.sendMessage(pplayer, Lang.EFFECT_NO_PERMISSION, effect.getName());
            return;
        }
        
        ParticleGroup group = pplayer.getActiveParticleGroup();
        for (ParticlePair particle : group.getParticles()) {
            if (particle.getId() == id) {
                particle.setEffect(effect);
                break;
            }
        }
        
        DataManager.saveParticleGroup(pplayer.getUniqueId(), group);
        LangManager.sendMessage(pplayer, Lang.EDIT_SUCCESS_EFFECT, id, effect.getName());
    }
    
    /**
     * Executes the style subcommand
     * 
     * @param pplayer The PPlayer executing the command
     * @param id The target particle ID
     * @param args The rest of the args
     */
    private void editStyle(PPlayer pplayer, int id, String[] args) {
        ParticleStyle style = ParticleStyle.fromName(args[0]);
        if (style == null) {
            LangManager.sendMessage(pplayer, Lang.STYLE_INVALID, args[0]);
            return;
        } else if (!PermissionManager.hasStylePermission(pplayer.getPlayer(), style)) {
            LangManager.sendMessage(pplayer, Lang.STYLE_NO_PERMISSION, style.getName());
            return;
        }
        
        ParticleGroup group = pplayer.getActiveParticleGroup();
        for (ParticlePair particle : group.getParticles()) {
            if (particle.getId() == id) {
                particle.setStyle(style);
                break;
            }
        }
        
        DataManager.saveParticleGroup(pplayer.getUniqueId(), group);
        LangManager.sendMessage(pplayer, Lang.EDIT_SUCCESS_STYLE, id, style.getName());
    }
    
    /**
     * Executes the data subcommand
     * 
     * @param pplayer The PPlayer executing the command
     * @param id The target particle ID
     * @param args The rest of the args
     */
    private void editData(PPlayer pplayer, int id, String[] args) {
        Material itemData = null;
        Material blockData = null;
        OrdinaryColor colorData = null;
        NoteColor noteColorData = null;
        
        ParticleEffect effect = pplayer.getActiveParticle(id).getEffect();

        if (effect.hasProperty(ParticleProperty.COLORABLE)) {
            if (effect == ParticleEffect.NOTE) {
                if (args[0].equalsIgnoreCase("rainbow")) {
                    noteColorData = new NoteColor(99);
                } else {
                    int note = -1;
                    try {
                        note = Integer.parseInt(args[0]);
                    } catch (Exception e) {
                        LangManager.sendMessage(pplayer, Lang.DATA_INVALID_NOTE);
                        return;
                    }

                    if (note < 0 || note > 23) {
                        LangManager.sendMessage(pplayer, Lang.DATA_INVALID_NOTE);
                        return;
                    }

                    noteColorData = new NoteColor(note);
                }
            } else {
                if (args[0].equalsIgnoreCase("rainbow")) {
                    colorData = new OrdinaryColor(999, 999, 999);
                } else {
                    int r = -1;
                    int g = -1;
                    int b = -1;

                    try {
                        r = Integer.parseInt(args[0]);
                        g = Integer.parseInt(args[1]);
                        b = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        LangManager.sendMessage(pplayer, Lang.DATA_INVALID_COLOR);
                        return;
                    }

                    if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                        LangManager.sendMessage(pplayer, Lang.DATA_INVALID_COLOR);
                        return;
                    }

                    colorData = new OrdinaryColor(r, g, b);
                }
            }
        } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
            if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST) {
                try {
                    blockData = ParticleUtils.closestMatch(args[0]);
                    if (blockData == null || !blockData.isBlock()) throw new Exception();
                } catch (Exception e) {
                    LangManager.sendMessage(pplayer, Lang.DATA_INVALID_BLOCK);
                    return;
                }
            } else if (effect == ParticleEffect.ITEM) {
                try {
                    itemData = ParticleUtils.closestMatch(args[0]);
                    if (itemData == null || itemData.isBlock()) throw new Exception();
                } catch (Exception e) {
                    LangManager.sendMessage(pplayer, Lang.DATA_INVALID_ITEM);
                    return;
                }
            }
        }
        
        String updatedDataString = null;
        ParticleGroup group = pplayer.getActiveParticleGroup();
        for (ParticlePair particle : group.getParticles()) {
            if (particle.getId() == id) {
                if (itemData != null) particle.setItemMaterial(itemData);
                if (blockData != null) particle.setBlockMaterial(blockData);
                if (colorData != null) particle.setColor(colorData);
                if (noteColorData != null) particle.setNoteColor(noteColorData);
                updatedDataString = particle.getDataString();
                break;
            }
        }
        
        DataManager.saveParticleGroup(pplayer.getUniqueId(), group);
        LangManager.sendMessage(pplayer, Lang.EDIT_SUCCESS_DATA, id, updatedDataString);
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        Player p = pplayer.getPlayer();
        List<String> matches = new ArrayList<String>();
        List<String> ids = new ArrayList<String>();
        
        for (ParticlePair particles : pplayer.getActiveParticles())
            ids.add(String.valueOf(particles.getId()));
        
        if (args.length == 0) {
            matches = ids;  
        } else if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], ids, matches);
        }
        
        int id = -1;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) { }
        
        if (pplayer.getActiveParticle(id) != null) {
            if (args.length == 2) {
                List<String> possibleValues = new ArrayList<String>();
                possibleValues.add("effect");
                possibleValues.add("style");
                possibleValues.add("data");
                StringUtil.copyPartialMatches(args[1], possibleValues, matches);
            } else if (args.length >= 3) {
                switch (args[1].toLowerCase()) {
                case "effect":
                    if (args.length == 3)
                        StringUtil.copyPartialMatches(args[2], PermissionManager.getEffectsUserHasPermissionFor(p), matches);
                    break;
                case "style":
                    if (args.length == 3)
                        StringUtil.copyPartialMatches(args[2], PermissionManager.getStylesUserHasPermissionFor(p), matches);
                    break;
                case "data":
                    ParticleEffect effect = pplayer.getActiveParticle(id).getEffect();
                    if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                        List<String> possibleValues = new ArrayList<String>();
                        if (effect == ParticleEffect.NOTE) { // Note data
                            if (args.length == 3) {
                                possibleValues.add("<0-23>");
                                possibleValues.add("rainbow");
                            }
                        } else { // Color data
                            if (args.length <= 5 && !args[2].equalsIgnoreCase("rainbow")) {
                                possibleValues.add("<0-255>");
                            }
                            if (args.length <= 4 && !args[2].equalsIgnoreCase("rainbow")) {
                                possibleValues.add("<0-255> <0-255>");
                            }
                            if (args.length <= 3) {
                                possibleValues.add("<0-255> <0-255> <0-255>");
                                possibleValues.add("rainbow");
                            }
                        }
                        StringUtil.copyPartialMatches(args[args.length - 1], possibleValues, matches);
                    } else if (args.length == 3 && effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                        if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST) { // Block material
                            matches = StringUtil.copyPartialMatches(args[2], ParticleUtils.getAllBlockMaterials(), matches);
                        } else if (effect == ParticleEffect.ITEM) { // Item material
                            matches = StringUtil.copyPartialMatches(args[2], ParticleUtils.getAllItemMaterials(), matches);
                        }
                    }
                    break;
                }
            }
        }
        
        return matches;
    }

    public String getName() {
        return "edit";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_EDIT;
    }

    public String getArguments() {
        return "<id> <effect>|<style>|<data> <args>";
    }

    public boolean requiresEffects() {
        return true;
    }

}
