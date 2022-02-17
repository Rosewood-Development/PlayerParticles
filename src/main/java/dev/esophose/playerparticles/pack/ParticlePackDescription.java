package dev.esophose.playerparticles.pack;

import org.bukkit.configuration.file.FileConfiguration;

public class ParticlePackDescription {

    private final String name;

    public ParticlePackDescription(FileConfiguration packFileConfig) {
        this.name = packFileConfig.getString("name");
    }

    public String getName() {
        return this.name;
    }

}
