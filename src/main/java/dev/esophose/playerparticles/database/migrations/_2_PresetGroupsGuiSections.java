package dev.esophose.playerparticles.database.migrations;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedConfigurationSection;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.database.DataMigration;
import dev.esophose.playerparticles.database.DatabaseConnector;
import dev.esophose.playerparticles.manager.ParticleGroupPresetManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class _2_PresetGroupsGuiSections extends DataMigration {

    private final PlayerParticles playerParticles;

    public _2_PresetGroupsGuiSections(PlayerParticles playerParticles) {
        super(2);

        this.playerParticles = playerParticles;
    }

    @Override
    public void migrate(DatabaseConnector connector, Connection connection, String tablePrefix) {
        File presetsFile = new File(this.playerParticles.getDataFolder(), ParticleGroupPresetManager.FILE_NAME);
        if (!presetsFile.exists())
            return;

        // Convert existing preset_groups.yml into the new format
        CommentedFileConfiguration presetsConfig = CommentedFileConfiguration.loadConfiguration(this.playerParticles, presetsFile);
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
