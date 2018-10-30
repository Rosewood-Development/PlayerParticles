package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import com.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.particles.ParticleGroup;
import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.styles.api.ParticleStyleManager;
import com.esophose.playerparticles.util.ParticleUtils;

public class AddCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        if (args.length < 2) {
            CommandModule.printUsage(pplayer, this);
            return;
        }
        
        int maxParticlesAllowed = PermissionManager.getMaxParticlesAllowed(pplayer.getPlayer());
        if (pplayer.getActiveParticles().size() >= maxParticlesAllowed) {
            LangManager.sendMessage(pplayer, Lang.ADD_REACHED_MAX, maxParticlesAllowed);
            return;
        }
        
        ParticleEffect effect = ParticleEffect.fromName(args[0]);
        if (effect == null) {
            LangManager.sendMessage(pplayer, Lang.EFFECT_INVALID, args[0]);
            return;
        } else if (!PermissionManager.hasEffectPermission(pplayer.getPlayer(), effect)) {
            LangManager.sendMessage(pplayer, Lang.EFFECT_NO_PERMISSION, effect.getName());
            return;
        }

        ParticleStyle style = ParticleStyle.fromName(args[1]);
        if (style == null) {
            LangManager.sendMessage(pplayer, Lang.STYLE_INVALID, args[1]);
            return;
        } else if (!PermissionManager.hasStylePermission(pplayer.getPlayer(), style)) {
            LangManager.sendMessage(pplayer, Lang.STYLE_NO_PERMISSION, args[1]);
            return;
        }

        Material itemData = null;
        Material blockData = null;
        OrdinaryColor colorData = null;
        NoteColor noteColorData = null;

        if (args.length > 2) {
            if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                if (effect == ParticleEffect.NOTE) {
                    if (args[2].equalsIgnoreCase("rainbow")) {
                        noteColorData = new NoteColor(99);
                    } else {
                        int note = -1;
                        try {
                            note = Integer.parseInt(args[2]);
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
                    if (args[2].equalsIgnoreCase("rainbow")) {
                        colorData = new OrdinaryColor(999, 999, 999);
                    } else {
                        int r = -1;
                        int g = -1;
                        int b = -1;

                        try {
                            r = Integer.parseInt(args[2]);
                            g = Integer.parseInt(args[3]);
                            b = Integer.parseInt(args[4]);
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
                        blockData = ParticleUtils.closestMatch(args[2]);
                        if (blockData == null || !blockData.isBlock()) throw new Exception();
                    } catch (Exception e) {
                        LangManager.sendMessage(pplayer, Lang.DATA_INVALID_BLOCK);
                        return;
                    }
                } else if (effect == ParticleEffect.ITEM) {
                    try {
                        itemData = ParticleUtils.closestMatch(args[2]);
                        if (itemData == null || itemData.isBlock()) throw new Exception();
                    } catch (Exception e) {
                        LangManager.sendMessage(pplayer, Lang.DATA_INVALID_ITEM);
                        return;
                    }
                }
            }
        }
        
        ParticleGroup group = pplayer.getActiveParticleGroup();
        ParticlePair newParticle = new ParticlePair(pplayer.getUniqueId(), pplayer.getNextActiveParticleId(), effect, style, blockData, blockData, colorData, noteColorData);
        group.getParticles().add(newParticle);
        DataManager.saveParticleGroup(pplayer.getUniqueId(), group);
        
        LangManager.sendMessage(pplayer, Lang.ADD_PARTICLE_APPLIED, newParticle.getEffect().getName(), newParticle.getStyle().getName(), newParticle.getDataString());
        
        if (ParticleStyleManager.isCustomHandled(newParticle.getStyle())) {
            LangManager.sendMessage(pplayer, Lang.STYLE_EVENT_SPAWNING_INFO, newParticle.getStyle().getName());
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        Player p = pplayer.getPlayer();
        List<String> matches = new ArrayList<String>();

        if (args.length <= 1) { // Effect name
            if (args.length == 0) matches = PermissionManager.getEffectsUserHasPermissionFor(p);
            else StringUtil.copyPartialMatches(args[0], PermissionManager.getEffectsUserHasPermissionFor(p), matches);
        } else if (args.length == 2) { // Style name
            StringUtil.copyPartialMatches(args[1], PermissionManager.getStylesUserHasPermissionFor(p), matches);
        } else if (args.length >= 3) { // Data
            ParticleEffect effect = ParticleEffect.fromName(args[0]);
            if (effect != null) {
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
            }
        }
        
        return matches;
    }

    public String getName() {
        return "add";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_ADD;
    }

    public String getArguments() {
        return "<effect> <style> [data]";
    }

    public boolean requiresEffects() {
        return true;
    }

}
