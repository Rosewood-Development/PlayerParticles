package dev.esophose.playerparticles.pack;

import org.bukkit.configuration.file.FileConfiguration;

public class ParticlePackDescription {

    private final String name;
    private final String version;

    public ParticlePackDescription(FileConfiguration packFileConfig) {
        this.name = packFileConfig.getString("name");
        this.version = packFileConfig.getString("version");
    }

    /**
     * @return the name of the pack
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the version of the pack
     */
    public String getVersion() {
        return this.version;
    }

}
