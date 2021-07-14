package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;

public class ParticleStylePoint extends DefaultParticleStyle {

    private double offset;

    protected ParticleStylePoint() {
        super("point", true, false, -0.5);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        return Collections.singletonList(new PParticle(location.clone().add(0.0, this.offset, 0.0)));
    }

    @Override
    public void updateTimers() {

    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("STONE_BUTTON");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("player-offset", 1.5, "How far to offset the player location vertically");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.offset = config.getDouble("player-offset");
    }

}
