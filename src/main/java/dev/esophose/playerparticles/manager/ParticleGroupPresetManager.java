package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.gui.GuiInventory.BorderColor;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.preset.ParticleGroupPreset;
import dev.esophose.playerparticles.particles.preset.ParticleGroupPresetPage;
import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.util.HexUtils;
import dev.esophose.playerparticles.util.inputparser.InputParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
    
    public static final String FILE_NAME = "preset_groups.yml";
    
    private Map<Integer, ParticleGroupPresetPage> presetGroupPages;

    public ParticleGroupPresetManager(PlayerParticles playerParticles) {
        super(playerParticles);
    }

    /**
     * Loads the preset groups from the preset_groups.yml file
     */
    @Override
    public void reload() {
        this.presetGroupPages = new HashMap<>();
        
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
        int pageNumber = 1;
        for (String pageKey: groupsYaml.getKeys(false)) {
            ConfigurationSection pageSection = groupsYaml.getConfigurationSection(pageKey);
            String title = HexUtils.colorify(pageSection.getString("title"));
            ConfigurationSection presetsSection = pageSection.getConfigurationSection("presets");
            List<ParticleGroupPreset> presets = new ArrayList<>();
            for (String groupName : presetsSection.getKeys(false)) {
                Map<Integer, ParticlePair> particles = new HashMap<>();
                String displayName = "";
                Material guiIcon = Material.ENDER_CHEST;
                int guiSlot = -1;
                String permission = "";
                boolean allowPermissionOverride = false;
                ConfigurationSection groupSection = presetsSection.getConfigurationSection(groupName);

                Set<String> particleKeys = groupSection.getKeys(false);
                for (String stringId : particleKeys) {
                    if (stringId.equalsIgnoreCase("display-name")) {
                        displayName = groupSection.getString(stringId);
                        continue;
                    }

                    if (stringId.equalsIgnoreCase("gui-icon")) {
                        guiIcon = Material.matchMaterial(groupSection.getString(stringId));
                        continue;
                    }

                    if (stringId.equalsIgnoreCase("gui-slot")) {
                        guiSlot = groupSection.getInt(stringId);
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
                        continue;
                    }

                    if (style == null) {
                        PlayerParticles.getInstance().getLogger().severe("Invalid style name: '" + particleSection.getString("style") + "'!");
                        continue;
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
                                    continue;
                                }
                            } else {
                                colorData = inputParser.next(OrdinaryColor.class);
                                if (colorData == null) {
                                    PlayerParticles.getInstance().getLogger().severe("Invalid color: '" + dataString + "'!");
                                    continue;
                                }
                            }
                        } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                            if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST) {
                                blockData = inputParser.next(Material.class);
                                if (blockData == null || !blockData.isBlock()) {
                                    PlayerParticles.getInstance().getLogger().severe("Invalid block: '" + dataString + "'!");
                                    continue;
                                }
                            } else if (effect == ParticleEffect.ITEM) {
                                itemData = inputParser.next(Material.class);
                                if (itemData == null || itemData.isBlock()) {
                                    PlayerParticles.getInstance().getLogger().severe("Invalid item: '" + dataString + "'!");
                                    continue;
                                }
                            }
                        }
                    }

                    particles.put(id, new ParticlePair(null, id, effect, style, itemData, blockData, colorData, noteColorData));
                }

                presets.add(new ParticleGroupPreset(HexUtils.colorify(displayName), guiIcon, guiSlot, permission, allowPermissionOverride, new ParticleGroup(groupName, particles)));
            }

            Map<Integer, BorderColor> extra = new HashMap<>();
            ConfigurationSection extraSection = pageSection.getConfigurationSection("extra");
            if (extraSection != null) {
                for (String slots : extraSection.getKeys(false)) {
                    BorderColor borderColor = BorderColor.valueOf(extraSection.getString(slots));
                    try {
                        if (slots.contains("-")) {
                            String[] split = slots.split("-");
                            int start = Integer.parseInt(split[0]);
                            int end = Integer.parseInt(split[1]);
                            for (int i = start; i <= end; i++)
                                extra.put(i, borderColor);
                        } else {
                            extra.put(Integer.parseInt(slots), borderColor);
                        }
                    } catch (NumberFormatException ignored) { }
                }
            }

            this.presetGroupPages.put(pageNumber++, new ParticleGroupPresetPage(title, presets, extra));
        }
    }

    @Override
    public void disable() {

    }

    /**
     * @return a Map of page numbers to preset pages
     */
    public Map<Integer, ParticleGroupPresetPage> getPresetGroupPages() {
        return Collections.unmodifiableMap(this.presetGroupPages);
    }

    /**
     * @return the max page number for the preset groups
     */
    public int getMaxPageNumber() {
        return this.presetGroupPages.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(1);
    }
    
    /**
     * Gets all the preset ParticleGroups that a player can use
     * 
     * @param player The player
     * @return a List of preset ParticleGroups the player can use
     */
    public List<ParticleGroupPreset> getPresetGroupsForPlayer(PPlayer player) {
        return this.getAllPresets().stream()
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
        return this.getAllPresets().stream()
                .filter(x -> x.getGroup().getName().equalsIgnoreCase(groupName))
                .findFirst()
                .orElse(null);
    }

    /**
     * @return a list of all preset groups
     */
    private List<ParticleGroupPreset> getAllPresets() {
        List<ParticleGroupPreset> allPresets = new ArrayList<>();
        for (ParticleGroupPresetPage page : this.presetGroupPages.values())
            allPresets.addAll(page.getPresets());
        return allPresets;
    }

}
