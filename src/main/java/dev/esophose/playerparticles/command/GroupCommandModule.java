package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.LangManager;
import dev.esophose.playerparticles.manager.LangManager.Lang;
import dev.esophose.playerparticles.manager.ParticleGroupPresetManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticleGroupPreset;
import dev.esophose.playerparticles.particles.ParticlePair;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GroupCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        List<String> validCommands = Arrays.asList("save", "load", "remove", "info", "list");
        if (args.length == 0 || !validCommands.contains(args[0])) {
            LangManager.sendMessage(pplayer, Lang.COMMAND_DESCRIPTION_GROUP_SAVE);
            LangManager.sendMessage(pplayer, Lang.COMMAND_DESCRIPTION_GROUP_LOAD);
            LangManager.sendMessage(pplayer, Lang.COMMAND_DESCRIPTION_GROUP_REMOVE);
            LangManager.sendMessage(pplayer, Lang.COMMAND_DESCRIPTION_GROUP_INFO);
            LangManager.sendMessage(pplayer, Lang.COMMAND_DESCRIPTION_GROUP_LIST);
            return;
        }
        
        if (args.length == 1 && !args[0].equalsIgnoreCase("list")) {
            LangManager.sendMessage(pplayer, Lang.GROUP_NO_NAME, args[0].toLowerCase());
            return;
        }

        switch (args[0].toLowerCase()) {
        case "save":
            this.onSave(pplayer, args[1].toLowerCase());
            break;
        case "load":
            this.onLoad(pplayer, args[1].toLowerCase());
            break;
        case "remove":
            this.onRemove(pplayer, args[1].toLowerCase());
            break;
        case "info":
            this.onInfo(pplayer, args[1].toLowerCase());
            break;
        case "list":
            this.onList(pplayer);
            break;
        default:
            LangManager.sendMessage(pplayer, Lang.COMMAND_DESCRIPTION_GROUP_SAVE);
            LangManager.sendMessage(pplayer, Lang.COMMAND_DESCRIPTION_GROUP_LOAD);
            LangManager.sendMessage(pplayer, Lang.COMMAND_DESCRIPTION_GROUP_REMOVE);
            LangManager.sendMessage(pplayer, Lang.COMMAND_DESCRIPTION_GROUP_INFO);
            LangManager.sendMessage(pplayer, Lang.COMMAND_DESCRIPTION_GROUP_LIST);
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
        if (pplayer.getParticleGroupByName(groupName) == null && PlayerParticles.getInstance().getManager(PermissionManager.class).hasPlayerReachedMaxGroups(pplayer)) {
            LangManager.sendMessage(pplayer, Lang.GROUP_SAVE_REACHED_MAX);
            return;
        }
        
        // Use the existing group if available, otherwise create a new one
        ParticleGroup group = pplayer.getParticleGroupByName(groupName);
        boolean groupUpdated = false;
        if (group == null) {
            List<ParticlePair> particles = new ArrayList<>();
            for (ParticlePair particle : pplayer.getActiveParticles())
                particles.add(particle.clone()); // Make sure the ParticlePairs aren't the same references in both the active and saved group
            group = new ParticleGroup(groupName, particles);
        } else {
            groupUpdated = true;
        }
        
        // Apply changes and notify player
        PlayerParticles.getInstance().getManager(DataManager.class).saveParticleGroup(pplayer.getUniqueId(), group);
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
            ParticleGroupPreset presetGroup = PlayerParticles.getInstance().getManager(ParticleGroupPresetManager.class).getPresetGroup(groupName);
            if (presetGroup == null) {
                LangManager.sendMessage(pplayer, Lang.GROUP_INVALID, groupName);
                return;
            }
            
            if (!presetGroup.canPlayerUse(pplayer.getPlayer())) {
                LangManager.sendMessage(pplayer, Lang.GROUP_PRESET_NO_PERMISSION, groupName);
                return;
            }
            
            group = presetGroup.getGroup();
            isPreset = true;
        }

        if (!group.canPlayerUse(pplayer.getPlayer())) {
            LangManager.sendMessage(pplayer, Lang.GROUP_NO_PERMISSION, groupName);
            return;
        }
        
        // Empty out the active group and fill it with clones from the target group
        ParticleGroup activeGroup = pplayer.getActiveParticleGroup();
        activeGroup.getParticles().clear();
        for (ParticlePair particle : group.getParticles())
            activeGroup.getParticles().add(particle.clone());
        
        // Update group and notify player
        PlayerParticles.getInstance().getManager(DataManager.class).saveParticleGroup(pplayer.getUniqueId(), activeGroup);
        
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
            ParticleGroupPreset presetGroup = PlayerParticles.getInstance().getManager(ParticleGroupPresetManager.class).getPresetGroup(groupName);
            
            if (presetGroup == null) {
                LangManager.sendMessage(pplayer, Lang.GROUP_INVALID, groupName);
            } else {
                LangManager.sendMessage(pplayer, Lang.GROUP_REMOVE_PRESET);
            }
            return;
        }
        
        // Delete the group and notify player
        PlayerParticles.getInstance().getManager(DataManager.class).removeParticleGroup(pplayer.getUniqueId(), group);
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
            ParticleGroupPreset presetGroup = PlayerParticles.getInstance().getManager(ParticleGroupPresetManager.class).getPresetGroup(groupName);
            if (presetGroup == null) {
                LangManager.sendMessage(pplayer, Lang.GROUP_INVALID, groupName);
                return;
            }
            
            if (!presetGroup.canPlayerUse(pplayer.getPlayer())) {
                LangManager.sendMessage(pplayer, Lang.GROUP_PRESET_NO_PERMISSION, groupName);
                return;
            }
            
            group = presetGroup.getGroup();
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

        Player player = pplayer.getPlayer();
        StringBuilder groupsList = new StringBuilder();
        for (ParticleGroup group : groups)
            if (!group.getName().equals(ParticleGroup.DEFAULT_NAME))
                groupsList.append(group.getName()).append(", ");
        
        if (groupsList.toString().endsWith(", "))
            groupsList = new StringBuilder(groupsList.substring(0, groupsList.length() - 2));
        
        StringBuilder presetsList = new StringBuilder();
        for (ParticleGroupPreset group : PlayerParticles.getInstance().getManager(ParticleGroupPresetManager.class).getPresetGroupsForPlayer(pplayer.getPlayer()))
            presetsList.append(group.getGroup().getName()).append(", ");
        
        if (presetsList.toString().endsWith(", "))
            presetsList = new StringBuilder(presetsList.substring(0, presetsList.length() - 2));
        
        if ((groupsList.length() == 0) && (presetsList.length() == 0)) {
            LangManager.sendMessage(pplayer, Lang.GROUP_LIST_NONE);
            return;
        }
        
        if (groupsList.length() > 0) {
            LangManager.sendMessage(pplayer, Lang.GROUP_LIST_OUTPUT, groupsList.toString());
        }
        
        if (presetsList.length() > 0) {
            LangManager.sendMessage(pplayer, Lang.GROUP_LIST_PRESETS, presetsList.toString());
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        List<String> matches = new ArrayList<>();
        List<String> subCommands = Arrays.asList("save", "load", "remove", "info", "list");
        
        if (args.length <= 1) {
            if (args.length == 0) matches = subCommands;
            else StringUtil.copyPartialMatches(args[0], subCommands, matches);
        } else if (args.length == 2 && !args[0].equalsIgnoreCase("list")) {
            if (args[0].equalsIgnoreCase("save")) {
                matches.add("<groupName>");
            } else {
                List<String> groupNames = new ArrayList<>();
                for (ParticleGroup group : pplayer.getParticleGroups())
                    if (!group.getName().equals(ParticleGroup.DEFAULT_NAME))
                        groupNames.add(group.getName());
                if (!args[0].equals("remove"))
                    for (ParticleGroupPreset group : PlayerParticles.getInstance().getManager(ParticleGroupPresetManager.class).getPresetGroupsForPlayer(pplayer.getPlayer()))
                        groupNames.add(group.getGroup().getName());
                StringUtil.copyPartialMatches(args[1], groupNames, matches);
            }
        }
        
        return matches;
    }

    public String getName() {
        return "group";
    }

    public String getDescriptionKey() {
        return "command-description-group";
    }

    public String getArguments() {
        return "<sub-command>";
    }

    public boolean requiresEffects() {
        return false;
    }

    public boolean canConsoleExecute() {
        return false;
    }

}
