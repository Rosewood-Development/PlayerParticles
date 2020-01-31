package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import dev.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticleGroupPreset;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.util.inputparser.InputParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ParticleGroupPresetManager extends Manager {
    
    private static final String FILE_NAME = "preset_groups.yml";
    
    private List<ParticleGroupPreset> presetGroups;

    public ParticleGroupPresetManager(PlayerParticles playerParticles) {
        super(playerParticles);
    }

    /**
     * Loads the preset groups from the preset_groups.yml file
     */
    @Override
    public void reload() {
        this.presetGroups = new ArrayList<>();
        
        File pluginDataFolder = PlayerParticles.getInstance().getDataFolder();
        if (!pluginDataFolder.exists())
            pluginDataFolder.mkdir();

        File groupsFile = new File(pluginDataFolder, FILE_NAME);
        
        // Create the file if it doesn't exist
        if (!groupsFile.exists()) {
            try (InputStream inStream = PlayerParticles.getInstance().getResource(FILE_NAME)) {
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
                Map<Integer, ParticlePair> particles = new HashMap<>();
                String displayName = "";
                Material guiIcon = Material.ENDER_CHEST;
                String permission = "";
                boolean allowPermissionOverride = false;
                ConfigurationSection groupSection = groupsYaml.getConfigurationSection(groupName);
                
                Set<String> particleKeys = groupSection.getKeys(false);
                for (String stringId : particleKeys) {
                    if (stringId.equalsIgnoreCase("display-name")) {
                        displayName = groupSection.getString(stringId);
                        continue;
                    }

                    if (stringId.equalsIgnoreCase("gui-icon")) {
                        guiIcon = Material.valueOf(groupSection.getString(stringId));
                        continue;
                    }
                    
                    if (stringId.equalsIgnoreCase("permission")) {
                        permission = groupSection.getString(stringId);
                        continue;
                    }
                    
                    if (stringId.equalsIgnoreCase("allow-permission-override")) {
                        allowPermissionOverride = groupSection.getBoolean(stringId);
                        continue;
                    }
                    
                    ConfigurationSection particleSection = groupSection.getConfigurationSection(stringId);
                    
                    int id = Integer.parseInt(stringId);
                    ParticleEffect effect = new InputParser(null, new String[] { particleSection.getString("effect") }).next(ParticleEffect.class);
                    ParticleStyle style = new InputParser(null, new String[] { particleSection.getString("style") }).next(ParticleStyle.class);
                    
                    if (effect == null) {
                        PlayerParticles.getInstance().getLogger().severe("Invalid effect name: '" + particleSection.getString("effect") + "'!");
                        throw new Exception();
                    }
                    
                    if (style == null) {
                        PlayerParticles.getInstance().getLogger().severe("Invalid style name: '" + particleSection.getString("style") + "'!");
                        throw new Exception();
                    }
                    
                    Material itemData = null;
                    Material blockData = null;
                    OrdinaryColor colorData = null;
                    NoteColor noteColorData = null;
                    
                    String dataString = particleSection.getString("data");
                    if (dataString != null && !dataString.isEmpty()) {
                        String[] args = dataString.split(" ");
                        InputParser inputParser = new InputParser(null, args);
                        
                        if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                            if (effect == ParticleEffect.NOTE) {
                                noteColorData = inputParser.next(NoteColor.class);
                                if (noteColorData == null) {
                                    PlayerParticles.getInstance().getLogger().severe("Invalid note: '" + dataString + "'!");
                                    throw new Exception();
                                }
                            } else {
                                colorData = inputParser.next(OrdinaryColor.class);
                                if (colorData == null) {
                                    PlayerParticles.getInstance().getLogger().severe("Invalid color: '" + dataString + "'!");
                                    throw new Exception();
                                }
                            }
                        } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                            if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST) {
                                blockData = inputParser.next(Material.class);
                                if (blockData == null || !blockData.isBlock()) {
                                    PlayerParticles.getInstance().getLogger().severe("Invalid block: '" + dataString + "'!");
                                    throw new Exception();
                                }
                            } else if (effect == ParticleEffect.ITEM) {
                                itemData = inputParser.next(Material.class);
                                if (itemData == null || itemData.isBlock()) {
                                    PlayerParticles.getInstance().getLogger().severe("Invalid item: '" + dataString + "'!");
                                    throw new Exception();
                                }
                            }
                        }
                    }
                    
                    particles.put(id, new ParticlePair(null, id, effect, style, itemData, blockData, colorData, noteColorData));
                }

                this.presetGroups.add(new ParticleGroupPreset(displayName, guiIcon, permission, allowPermissionOverride, new ParticleGroup(groupName, particles)));
            } catch (Exception ex) {
                PlayerParticles.getInstance().getLogger().severe("An error occurred while parsing the " + FILE_NAME + " file!");
            }
        }
    }

    @Override
    public void disable() {

    }
    
    /**
     * Gets all the preset ParticleGroups that a player can use
     * 
     * @param player The player
     * @return a List of preset ParticleGroups the player can use
     */
    public List<ParticleGroupPreset> getPresetGroupsForPlayer(PPlayer player) {
        return this.presetGroups.stream()
                .filter(x -> x.canPlayerUse(player))
                .sorted(Comparator.comparing(ParticleGroupPreset::getDisplayName))
                .collect(Collectors.toList());
    }
    
    /**
     * Gets a preset ParticleGroup by its name
     * 
     * @param groupName The ParticleGroup name
     * @return The preset ParticleGroup if it exists, otherwise null 
     */
    public ParticleGroupPreset getPresetGroup(String groupName) {
        for (ParticleGroupPreset group : this.presetGroups)
            if (group.getGroup().getName().equalsIgnoreCase(groupName))
                return group;
        return null;
    }

}
