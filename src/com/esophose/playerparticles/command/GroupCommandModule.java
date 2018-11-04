package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleGroup;
import com.esophose.playerparticles.particles.ParticlePair;

public class GroupCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        Player p = pplayer.getPlayer();
        
        List<String> validCommands = Arrays.asList(new String[] { "save", "load", "remove", "info", "list" });
        if (args.length == 0 || !validCommands.contains(args[0])) {
            LangManager.sendMessage(p, Lang.COMMAND_DESCRIPTION_GROUP_SAVE);
            LangManager.sendMessage(p, Lang.COMMAND_DESCRIPTION_GROUP_LOAD);
            LangManager.sendMessage(p, Lang.COMMAND_DESCRIPTION_GROUP_REMOVE);
            LangManager.sendMessage(p, Lang.COMMAND_DESCRIPTION_GROUP_INFO);
            LangManager.sendMessage(p, Lang.COMMAND_DESCRIPTION_GROUP_LIST);
            return;
        }
        
        if (args.length == 1 && !args[0].equalsIgnoreCase("list")) {
            LangManager.sendMessage(p, Lang.GROUP_NO_NAME, args[0].toLowerCase());
            return;
        }

        switch (args[0].toLowerCase()) {
        case "save":
            onSave(pplayer, args[1].toLowerCase());
            break;
        case "load":
            onLoad(pplayer, args[1].toLowerCase());
            break;
        case "remove":
            onRemove(pplayer, args[1].toLowerCase());
            break;
        case "info":
            onInfo(pplayer, args[1].toLowerCase());
            break;
        case "list":
            onList(pplayer);
            break;
        default:
            LangManager.sendMessage(p, Lang.COMMAND_DESCRIPTION_GROUP_SAVE);
            LangManager.sendMessage(p, Lang.COMMAND_DESCRIPTION_GROUP_LOAD);
            LangManager.sendMessage(p, Lang.COMMAND_DESCRIPTION_GROUP_REMOVE);
            LangManager.sendMessage(p, Lang.COMMAND_DESCRIPTION_GROUP_INFO);
            LangManager.sendMessage(p, Lang.COMMAND_DESCRIPTION_GROUP_LIST);
            break;
        }
    }
    
    /**
     * Handles the command /pp group save
     * 
     * @param pplayer The PPlayer
     * @param groupName The target group name
     */
    private void onSave(PPlayer pplayer, String groupName) {
        // Check that the groupName isn't the reserved name
        if (groupName.equalsIgnoreCase(ParticleGroup.DEFAULT_NAME)) {
            LangManager.sendMessage(pplayer, Lang.GROUP_RESERVED);
            return;
        }
        
        // Check if the player actually has any particles
        if (pplayer.getActiveParticles().size() == 0) {
            LangManager.sendMessage(pplayer, Lang.GROUP_SAVE_NO_PARTICLES);
            return;
        }
        
        // The database column can only hold up to 100 characters, cut it off there
        if (groupName.length() >= 100) {
            groupName = groupName.substring(0, 100);
        }
        
        // Check if they are creating a new group, if they are, check that they haven't gone over their limit
        if (pplayer.getParticleGroupByName(groupName) == null && PermissionManager.hasPlayerReachedMaxGroups(pplayer)) {
            LangManager.sendMessage(pplayer, Lang.GROUP_SAVE_REACHED_MAX);
            return;
        }
        
        // Use the existing group if available, otherwise create a new one
        ParticleGroup group = pplayer.getParticleGroupByName(groupName);
        boolean groupUpdated = false;
        if (group == null) {
            List<ParticlePair> particles = new ArrayList<ParticlePair>();
            for (ParticlePair particle : pplayer.getActiveParticles())
                particles.add(particle.clone()); // Make sure the ParticlePairs aren't the same references in both the active and saved group
            group = new ParticleGroup(groupName, particles);
        } else {
            groupUpdated = true;
        }
        
        // Apply changes and notify player
        DataManager.saveParticleGroup(pplayer.getUniqueId(), group);
        if (groupUpdated) {
            LangManager.sendMessage(pplayer, Lang.GROUP_SAVE_SUCCESS_OVERWRITE, groupName);
        } else {
            LangManager.sendMessage(pplayer, Lang.GROUP_SAVE_SUCCESS, groupName);
        }
    }
    
    /**
     * Handles the command /pp group load
     * 
     * @param pplayer The PPlayer
     * @param groupName The target group name
     */
    private void onLoad(PPlayer pplayer, String groupName) {
        // Check that the groupName isn't the reserved name
        if (groupName.equalsIgnoreCase(ParticleGroup.DEFAULT_NAME)) {
            LangManager.sendMessage(pplayer, Lang.GROUP_RESERVED);
            return;
        }
        
        // Get the group
        boolean isPreset = false;
        ParticleGroup group = pplayer.getParticleGroupByName(groupName);
        if (group == null) {
            // Didn't find a saved group, look at the presets
            group = ParticleGroup.getPresetGroup(groupName);
            if (group == null) {
                LangManager.sendMessage(pplayer, Lang.GROUP_INVALID, groupName);
                return;
            }
            
            if (!group.canPlayerUse(pplayer.getPlayer())) {
                LangManager.sendMessage(pplayer, Lang.GROUP_PRESET_NO_PERMISSION, groupName);
                return;
            }
            
            isPreset = true;
        }
        
        // Empty out the active group and fill it with clones from the target group
        ParticleGroup activeGroup = pplayer.getActiveParticleGroup();
        activeGroup.getParticles().clear();
        for (ParticlePair particle : group.getParticles())
            activeGroup.getParticles().add(particle.clone());
        
        // Update group and notify player
        DataManager.saveParticleGroup(pplayer.getUniqueId(), activeGroup);
        
        if (!isPreset)
            LangManager.sendMessage(pplayer, Lang.GROUP_LOAD_SUCCESS, activeGroup.getParticles().size(), groupName);
        else
            LangManager.sendMessage(pplayer, Lang.GROUP_LOAD_PRESET_SUCCESS, activeGroup.getParticles().size(), groupName);
    }
    
    /**
     * Handles the command /pp group remove
     * 
     * @param pplayer The PPlayer
     * @param groupName The target group name
     */
    private void onRemove(PPlayer pplayer, String groupName) {
        // Check that the groupName isn't the reserved name
        if (groupName.equalsIgnoreCase(ParticleGroup.DEFAULT_NAME)) {
            LangManager.sendMessage(pplayer, Lang.GROUP_RESERVED);
            return;
        }
        
        ParticleGroup group = pplayer.getParticleGroupByName(groupName);
        if (group == null) {
            // Didn't find a saved group, look at the presets
            group = ParticleGroup.getPresetGroup(groupName);
            if (group != null) {
                LangManager.sendMessage(pplayer, Lang.GROUP_REMOVE_PRESET);
                return;
            }
        }
        
        // Delete the group and notify player
        DataManager.removeParticleGroup(pplayer.getUniqueId(), group);
        LangManager.sendMessage(pplayer, Lang.GROUP_REMOVE_SUCCESS, groupName);
    }
    
    /**
     * Handles the command /pp group info
     * 
     * @param pplayer The PPlayer
     * @param groupName The target group name
     */
    private void onInfo(PPlayer pplayer, String groupName) {
        // Check that the groupName isn't the reserved name
        if (groupName.equalsIgnoreCase(ParticleGroup.DEFAULT_NAME)) {
            LangManager.sendMessage(pplayer, Lang.GROUP_RESERVED);
            return;
        }
        
        ParticleGroup group = pplayer.getParticleGroupByName(groupName);
        if (group == null) {
            // Didn't find a saved group, look at the presets
            group = ParticleGroup.getPresetGroup(groupName);
            if (group == null) {
                LangManager.sendMessage(pplayer, Lang.GROUP_INVALID, groupName);
                return;
            }
            
            if (!group.canPlayerUse(pplayer.getPlayer())) {
                LangManager.sendMessage(pplayer, Lang.GROUP_PRESET_NO_PERMISSION, groupName);
                return;
            }
        }
        
        List<ParticlePair> particles = group.getParticles();
        particles.sort(Comparator.comparingInt(ParticlePair::getId));
        
        LangManager.sendMessage(pplayer, Lang.GROUP_INFO_HEADER, groupName);
        for (ParticlePair particle : particles)
            LangManager.sendMessage(pplayer, Lang.LIST_OUTPUT, particle.getId(), particle.getEffect().getName(), particle.getStyle().getName(), particle.getDataString());
    }
    
    /**
     * Handles the command /pp group list
     * 
     * @param pplayer The PPlayer
     */
    private void onList(PPlayer pplayer) {
        List<ParticleGroup> groups = pplayer.getParticleGroups();
        groups.sort(Comparator.comparing(ParticleGroup::getName));
        
        String groupsList = "";
        for (ParticleGroup group : groups)
            if (!group.getName().equals(ParticleGroup.DEFAULT_NAME))
                groupsList += group.getName() + ", ";
        
        if (groupsList.endsWith(", ")) 
            groupsList = groupsList.substring(0, groupsList.length() - 2);
        
        String presetsList = "";
        for (ParticleGroup group : ParticleGroup.getPresetGroupsForPlayer(pplayer.getPlayer()))
            presetsList += group.getName() + ", ";
        
        if (presetsList.endsWith(", "))
            presetsList = presetsList.substring(0, presetsList.length() - 2);
        
        if (groupsList.isEmpty() && presetsList.isEmpty()) {
            LangManager.sendMessage(pplayer, Lang.GROUP_LIST_NONE);
            return;
        }
        
        if (!groupsList.isEmpty()) {
            LangManager.sendMessage(pplayer, Lang.GROUP_LIST_OUTPUT, groupsList);
        }
        
        if (!presetsList.isEmpty()) {
            LangManager.sendMessage(pplayer, Lang.GROUP_LIST_PRESETS, presetsList);
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        List<String> matches = new ArrayList<String>();
        List<String> subCommands = Arrays.asList(new String[] { "save", "load", "remove", "info", "list" });
        
        if (args.length <= 1) {
            if (args.length == 0) matches = subCommands;
            else StringUtil.copyPartialMatches(args[0], subCommands, matches);
        } else if (args.length == 2 && !args[0].equalsIgnoreCase("list")) {
            if (args[0].equalsIgnoreCase("save")) {
                matches.add("<groupName>");
            } else {
                List<String> groupNames = new ArrayList<String>();
                for (ParticleGroup group : pplayer.getParticleGroups())
                    if (!group.getName().equals(ParticleGroup.DEFAULT_NAME))
                        groupNames.add(group.getName());
                if (!args[0].equals("remove"))
                    for (ParticleGroup group : ParticleGroup.getPresetGroupsForPlayer(pplayer.getPlayer()))
                        groupNames.add(group.getName());
                StringUtil.copyPartialMatches(args[1], groupNames, matches);
            }
        }
        
        return matches;
    }

    public String getName() {
        return "group";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_GROUP;
    }

    public String getArguments() {
        return "<sub-command>";
    }

    public boolean requiresEffects() {
        return true;
    }

}
