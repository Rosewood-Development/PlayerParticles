package dev.esophose.playerparticles.nms.wrapper;

import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.version.VersionMapping;
import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class ParticleHandler {

    protected VersionMapping versionMapping;

    public ParticleHandler(VersionMapping versionMapping) {
        this.versionMapping = versionMapping;
    }

    public abstract void spawnParticle(ParticleEffect particleEffect, Collection<Player> players, Location location, int count, float offsetX, float offsetY, float offsetZ, float extra, Object data);

}
