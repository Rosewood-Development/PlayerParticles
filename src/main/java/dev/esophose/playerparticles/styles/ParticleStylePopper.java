package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleStylePopper extends DefaultParticleStyle {

    private int step = 0;

    private double radius;
    private double grow;
    private double radials;
    private int helices;
    private int maxStep;
    private int popParticleAmount;
    private double popSpread;
    private double popSpeed;
    private double popOffset;

    protected ParticleStylePopper() {
        super("popper", true, true, 0.5);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        double radius = this.radius * (1 - (double) this.step / this.maxStep);
        for (int i = 0; i < this.helices; i++) {
            double angle = this.step * this.radials + (2 * Math.PI * i / this.helices);
            Vector v = new Vector(MathL.cos(angle) * radius, this.step * this.grow - 1, MathL.sin(angle) * radius);

            particles.add(new PParticle(location.clone().add(v)));
        }

        if (this.step == this.maxStep - 1)
            for (int i = 0; i < this.popParticleAmount; i++)
                particles.add(new PParticle(location.clone().add(0, this.popOffset, 0), this.popSpread, this.popSpread, this.popSpread, this.popSpeed));

        return particles;
    }

    @Override
    public void updateTimers() {
        this.step = (this.step + 1) % this.maxStep;
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("POPPED_CHORUS_FRUIT", "CHORUS_FRUIT_POPPED", "PUMPKIN_SEEDS");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("radius", 1.0, "The radius at the bottom of the vortex");
        this.setIfNotExists("grow", 0.08, "How much to change the height per particle");
        this.setIfNotExists("radials", 16, "The steepness of how fast the particles grow upwards", "More = faster/taller growth");
        this.setIfNotExists("helices", 2, "The number of orbs spinning around the player");
        this.setIfNotExists("step-amount", 32, "How many steps it takes to reach the highest point");
        this.setIfNotExists("pop-particle-amount", 10, "How many particles to spawn when the highest point is reached");
        this.setIfNotExists("pop-particle-spread", 0.5, "How much to spread out the popped particles");
        this.setIfNotExists("pop-particle-speed", 0.03, "How much speed to apply to the popped particles");
        this.setIfNotExists("pop-particle-offset", 1.5, "How far to vertically offset the pop particles location from the player");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.radius = config.getDouble("radius");
        this.grow = config.getDouble("grow");
        this.radials = Math.PI / config.getInt("radials");
        this.helices = config.getInt("helices");
        this.maxStep = config.getInt("step-amount");
        this.popParticleAmount = config.getInt("pop-particle-amount");
        this.popSpread = config.getDouble("pop-particle-spread");
        this.popSpeed = config.getDouble("pop-particle-speed");
        this.popOffset = config.getDouble("pop-particle-offset");
    }

}
