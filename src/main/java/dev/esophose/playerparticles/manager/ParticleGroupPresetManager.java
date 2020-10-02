package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedConfigurationSection;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

        File presetsFile = new File(pluginDataFolder, FILE_NAME);
        
        // Create the file if it doesn't exist
        if (!presetsFile.exists()) {
            try (InputStream inStream = PlayerParticles.getInstance().getResource(FILE_NAME)) {
                Files.copy(inStream, Paths.get(presetsFile.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.tryMigrateOld(presetsFile);
        }
        
        // Parse groups.yml file
        try {
            YamlConfiguration groupsYaml = YamlConfiguration.loadConfiguration(presetsFile);
            int pageNumber = 1;
            for (String pageKey: groupsYaml.getKeys(false)) {
                ConfigurationSection pageSection = groupsYaml.getConfigurationSection(pageKey);
                String title = HexUtils.colorify(pageSection.getString("title"));
                ConfigurationSection presetsSection = pageSection.getConfigurationSection("presets");
                List<ParticleGroupPreset> presets = new ArrayList<>();
                if (presetsSection != null) {
                    for (String groupName : presetsSection.getKeys(false)) {
                        Map<Integer, ParticlePair> particles = new HashMap<>();
                        String displayName = "";
                        Material guiIcon = Material.ENDER_CHEST;
                        int guiSlot = -1;
                        List<String> lore = new ArrayList<>();
                        String permission = "";
                        boolean allowPermissionOverride = false;
                        ConfigurationSection groupSection = presetsSection.getConfigurationSection(groupName);

                        Set<String> particleKeys = groupSection.getKeys(false);
                        for (String stringId : particleKeys) {
                            if (stringId.equalsIgnoreCase("display-name")) {
                                displayName = HexUtils.colorify(groupSection.getString(stringId));
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

                            if (stringId.equalsIgnoreCase("lore")) {
                                lore = groupSection.getStringList("lore");
                                lore.replaceAll(HexUtils::colorify);
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

                        presets.add(new ParticleGroupPreset(displayName, guiIcon, guiSlot, lore, permission, allowPermissionOverride, new ParticleGroup(groupName, particles)));
                    }
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
        } catch (Exception ex) {
            ex.printStackTrace();
            this.playerParticles.getLogger().severe("An error occurred while parsing the preset_groups.yml file!");
        }
    }

    @Override
    public void disable() {

    }

    /**
     * Gets a map of page numbers to preset pages for a PPlayer
     *
     * @param pplayer The PPlayer
     * @return a Map of page numbers to preset pages for a PPlayer
     */
    public Map<Integer, ParticleGroupPresetPage> getPresetGroupPages(PPlayer pplayer) {
        // Hide pages the player doesn't have access to anything for
        Map<Integer, ParticleGroupPresetPage> presetGroupPages = new HashMap<>();

        List<Integer> pageNumbers = this.presetGroupPages.keySet().stream().sorted().collect(Collectors.toList());
        int pageNumber = 1;
        for (int key : pageNumbers) {
            ParticleGroupPresetPage page = this.presetGroupPages.get(key);
            if (page.getPresets().stream().noneMatch(x -> x.canPlayerUse(pplayer)))
                continue;

            presetGroupPages.put(pageNumber++, page);
        }

        // Use a default page
        if (presetGroupPages.isEmpty()) {
            LocaleManager localeManager = this.playerParticles.getManager(LocaleManager.class);
            Map<Integer, BorderColor> extraBorder = new HashMap<>();
            IntStream.range(0, 9).forEach(x -> extraBorder.put(x, BorderColor.GREEN));
            IntStream.range(45, 54).forEach(x -> extraBorder.put(x, BorderColor.GREEN));
            Arrays.asList(9, 18, 27, 36, 17, 26, 35, 44).forEach(x -> extraBorder.put(x, BorderColor.GREEN));
            presetGroupPages.put(1, new ParticleGroupPresetPage(localeManager.getLocaleMessage("gui-load-a-preset-group"), Collections.emptyList(), extraBorder));
        }

        return Collections.unmodifiableMap(presetGroupPages);
    }

    /**
     * Gets the max page number for preset groups for a PPlayer
     *
     * @param pplayer The PPlayer
     * @return the max page number for the preset groups for a PPlayer
     */
    public int getMaxPageNumber(PPlayer pplayer) {
        return this.getPresetGroupPages(pplayer).keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(1);
    }
    
    /**
     * Gets all the preset ParticleGroups that a player can use
     * 
     * @param player The PPlayer
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

    private void tryMigrateOld(File presetsFile) {
        CommentedFileConfiguration presetsConfig = CommentedFileConfiguration.loadConfiguration(this.playerParticles, presetsFile);

        // Check all keys to see if we have any that match both "presets" and "extra", that should be good enough
        boolean title = false;
        boolean extras = false;
        boolean presets = false;
        for (String key : presetsConfig.getKeys(true)) {
            if (!title && key.contains("title"))
                title = true;
            if (!extras && key.contains("extra"))
                extras = true;
            if (!presets && key.contains("presets"))
                presets = true;
        }

        if (title && extras && presets)
            return;

        // Convert existing preset_groups.yml into the new format
        List<PresetData> existingPresets = new ArrayList<>();
        for (String groupName : presetsConfig.getKeys(false)) {
            CommentedConfigurationSection groupSection = presetsConfig.getConfigurationSection(groupName);
            String displayName = groupSection.getString("display-name");
            String guiIcon = groupSection.getString("gui-icon");
            String permission = groupSection.getString("permission");
            boolean allowPermissionOverride = groupSection.getBoolean("allow-permission-override");
            List<ParticleData> particleData = new ArrayList<>();
            for (String key : groupSection.getKeys(false)) {
                try {
                    int id = Integer.parseInt(key);
                    CommentedConfigurationSection particleGroup = groupSection.getConfigurationSection(key);
                    String effect = particleGroup.getString("effect");
                    String style = particleGroup.getString("style");
                    String data = particleGroup.getString("data");
                    particleData.add(new ParticleData(id, effect, style, data));
                } catch (NumberFormatException ignored) { }
            }
            existingPresets.add(new PresetData(groupName, displayName, guiIcon, permission, allowPermissionOverride, particleData));
        }

        // Delete existing file, going to overwrite it all
        presetsFile.delete();

        // Copy over the new file
        try (InputStream inStream = PlayerParticles.getInstance().getResource(ParticleGroupPresetManager.FILE_NAME)) {
            Files.copy(inStream, Paths.get(presetsFile.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Reload the config
        presetsConfig.reloadConfig();

        // Delete everything in the presets section
        presetsConfig.set("page1.presets", null);

        // Recreate presets section
        CommentedConfigurationSection presetsSection = presetsConfig.createSection("page1.presets");

        // Fill the data back in
        int slot = 10;
        int nextWrap = 17;
        int maxSlot = 43;

        for (PresetData presetData : existingPresets) {
            CommentedConfigurationSection presetSection = presetsSection.createSection(presetData.groupName);
            presetSection.set("display-name", presetData.displayName);
            presetSection.set("gui-icon", presetData.guiIcon);
            presetSection.set("gui-slot", slot);
            presetSection.set("permission", presetData.permission);
            presetSection.set("allow-permission-override", presetData.allowPermissionOverride);

            for (ParticleData particleData : presetData.particleData) {
                CommentedConfigurationSection particleSection = presetSection.createSection(String.valueOf(particleData.id));
                particleSection.set("effect", particleData.effect);
                particleSection.set("style", particleData.style);
                particleSection.set("data", particleData.data);
            }

            slot++;
            if (slot == nextWrap) { // Loop around border
                nextWrap += 9;
                slot += 2;
            }
            // Overflowed the available space
            if (slot > maxSlot)
                slot = maxSlot;
        }

        presetsConfig.save();
    }

    private static class PresetData {
        private final String groupName, displayName, guiIcon, permission;
        private final boolean allowPermissionOverride;
        private final List<ParticleData> particleData;

        private PresetData(String groupName, String displayName, String guiIcon, String permission, boolean allowPermissionOverride, List<ParticleData> particleData) {
            this.groupName = groupName;
            this.displayName = displayName;
            this.guiIcon = guiIcon;
            this.permission = permission;
            this.allowPermissionOverride = allowPermissionOverride;
            this.particleData = particleData;
        }
    }

    private static class ParticleData {
        private final int id;
        private final String effect, style, data;

        private ParticleData(int id, String effect, String style, String data) {
            this.id = id;
            this.effect = effect;
            this.style = style;
            this.data = data;
        }
    }

}
