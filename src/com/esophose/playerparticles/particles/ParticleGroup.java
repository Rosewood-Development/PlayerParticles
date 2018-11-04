package com.esophose.playerparticles.particles;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import com.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.util.ParticleUtils;

public class ParticleGroup {
    
    public static final String DEFAULT_NAME = "active";
    private static HashMap<ParticleGroup, Material> presetGroups;

    private String name;
    private List<ParticlePair> particles;

    public ParticleGroup(String name, List<ParticlePair> particles) {
        this.name = name;
        this.particles = particles;
    }
    
    /**
     * Get the player-given name of this ParticleGroup
     * This will be null if it's the player's active ParticleGroup
     * 
     * @return The name of this group
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the List of ParticlePairs in this group
     * 
     * @return The particles in this group
     */
    public List<ParticlePair> getParticles() {
        return this.particles;
    }
    
    /**
     * Checks if a Player can use this ParticleGroup
     * 
     * @param player The Player
     * @return True if the Player can use this ParticleGroup
     */
    public boolean canPlayerUse(Player player) {
        if (PermissionManager.getMaxParticlesAllowed(player) < this.particles.size()) return false;
        for (ParticlePair particle : this.particles) {
            if (!PermissionManager.hasEffectPermission(player, particle.getEffect())) return false;
            if (!PermissionManager.hasStylePermission(player, particle.getStyle())) return false;
        }
        return true;
    }

    /**
     * Gets an empty ParticleGroup
     * 
     * @return The default empty active ParticleGroup
     */
    public static ParticleGroup getDefaultGroup() {
        return new ParticleGroup(DEFAULT_NAME, new ArrayList<ParticlePair>());
    }
    
    /**
     * Loads the preset groups from the groups.yml file
     * 
     * @param pluginDataFolder
     */
    public static void reload() {
        presetGroups = new HashMap<ParticleGroup, Material>();
        
        File pluginDataFolder = PlayerParticles.getPlugin().getDataFolder();
        File groupsFile = new File(pluginDataFolder.getAbsolutePath() + File.separator + "groups.yml");
        
        // Create the file if it doesn't exist
        if (!groupsFile.exists()) {
            try (InputStream inStream = PlayerParticles.getPlugin().getResource("groups.yml")) {
                Files.copy(inStream, Paths.get(groupsFile.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        // Parse groups.yml file
        YamlConfiguration groupsYaml = YamlConfiguration.loadConfiguration(groupsFile);
        Set<String> groupNames = groupsYaml.getKeys(false);
        for (String groupName : groupNames) {
            try {
                List<ParticlePair> particles = new ArrayList<ParticlePair>();
                Material iconMaterial = null;
                ConfigurationSection groupSection = groupsYaml.getConfigurationSection(groupName);
                
                Set<String> particleKeys = groupSection.getKeys(false);
                for (String stringId : particleKeys) {
                    if (stringId.equalsIgnoreCase("icon")) {
                        iconMaterial = Material.valueOf(groupSection.getString("icon"));
                        continue;
                    }
                    
                    ConfigurationSection particleSection = groupSection.getConfigurationSection(stringId);
                    
                    int id = Integer.parseInt(stringId);
                    ParticleEffect effect = ParticleEffect.fromName(particleSection.getString("effect"));
                    ParticleStyle style = ParticleStyle.fromName(particleSection.getString("style"));
                    
                    if (effect == null) {
                        PlayerParticles.getPlugin().getLogger().severe("Invalid effect name: '" + particleSection.getString("effect") + "'!");
                        throw new Exception();
                    }
                    
                    if (style == null) {
                        PlayerParticles.getPlugin().getLogger().severe("Invalid style name: '" + particleSection.getString("style") + "'!");
                        throw new Exception();
                    }
                    
                    Material itemData = null;
                    Material blockData = null;
                    OrdinaryColor colorData = null;
                    NoteColor noteColorData = null;
                    
                    String dataString = particleSection.getString("data");
                    if (!dataString.isEmpty()) {
                        String[] args = dataString.split(" ");
                        
                        if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                            if (effect == ParticleEffect.NOTE) {
                                if (args[0].equalsIgnoreCase("rainbow")) {
                                    noteColorData = new NoteColor(99);
                                } else {
                                    int note = -1;
                                    try {
                                        note = Integer.parseInt(args[0]);
                                    } catch (Exception e) {
                                        PlayerParticles.getPlugin().getLogger().severe("Invalid note: '" + args[0] + "'!");
                                        throw new Exception();
                                    }

                                    if (note < 0 || note > 23) {
                                        PlayerParticles.getPlugin().getLogger().severe("Invalid note: '" + args[0] + "'!");
                                        throw new Exception();
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
                                        PlayerParticles.getPlugin().getLogger().severe("Invalid color: '" + args[0] + " " + args[1] + " " + args[2] + "'!");
                                        throw new Exception();
                                    }

                                    if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                                        PlayerParticles.getPlugin().getLogger().severe("Invalid color: '" + args[0] + " " + args[1] + " " + args[2] + "'!");
                                        throw new Exception();
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
                                    PlayerParticles.getPlugin().getLogger().severe("Invalid block: '" + args[0] + "'!");
                                    throw new Exception();
                                }
                            } else if (effect == ParticleEffect.ITEM) {
                                try {
                                    itemData = ParticleUtils.closestMatch(args[0]);
                                    if (itemData == null || itemData.isBlock()) throw new Exception();
                                } catch (Exception e) {
                                    PlayerParticles.getPlugin().getLogger().severe("Invalid item: '" + args[0] + "'!");
                                    throw new Exception();
                                }
                            }
                        }
                    }
                    
                    particles.add(new ParticlePair(null, id, effect, style, blockData, blockData, colorData, noteColorData));
                }
                
                if (iconMaterial == null) {
                    
                }
                
                presetGroups.put(new ParticleGroup(groupName, particles), iconMaterial);
            } catch (Exception ex) {
                PlayerParticles.getPlugin().getLogger().severe("An error occurred while parsing the groups.yml file!");
            }
        }
    }
    
    public static Set<Entry<ParticleGroup, Material>> getPresetGroupsForGUIForPlayer(Player player) {
        Set<Entry<ParticleGroup, Material>> groups = new HashSet<Entry<ParticleGroup, Material>>();
        for (Entry<ParticleGroup, Material> entry : presetGroups.entrySet())
            if (entry.getKey().canPlayerUse(player))
                groups.add(entry);
        return groups;
    }
    
    /**
     * Gets all the preset ParticleGroups that a player can use
     * 
     * @param player The player
     * @return a List of preset ParticleGroups the player can use
     */
    public static List<ParticleGroup> getPresetGroupsForPlayer(Player player) {
        return presetGroups.keySet().stream().filter(x -> x.canPlayerUse(player)).collect(Collectors.toList());
    }
    
    /**
     * Gets a preset ParticleGroup by its name
     * 
     * @param groupName The ParticleGroup name
     * @return The preset ParticleGroup if it exists, otherwise null 
     */
    public static ParticleGroup getPresetGroup(String groupName) {
        for (ParticleGroup group : presetGroups.keySet())
            if (group.getName().equalsIgnoreCase(groupName))
                return group;
        return null;
    }

}
