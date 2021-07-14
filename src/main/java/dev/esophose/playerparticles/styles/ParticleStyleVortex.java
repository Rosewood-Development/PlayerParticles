/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2014 Slikey
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleStyleVortex extends DefaultParticleStyle {

    private int step = 0;

    private double radius;
    private double grow;
    private double radials;
    private int helices;
    private int maxStep;

    protected ParticleStyleVortex() {
        super("vortex", true, true, 0.5);
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

        return particles;
    }

    @Override
    public void updateTimers() {
        this.step = (this.step + 1) % this.maxStep;
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("GLOWSTONE_DUST");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("radius", 2.0, "The bottom radius of the vortex");
        this.setIfNotExists("grow", 0.05, "How much to change the height per particle");
        this.setIfNotExists("radials", 16, "The steepness of how fast the particles grow upwards", "More = faster/taller growth");
        this.setIfNotExists("helices", 4, "The number of orbs spinning around the player");
        this.setIfNotExists("step-amount", 70, "How many steps it takes to reach the highest point");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.radius = config.getDouble("radius");
        this.grow = config.getDouble("grow");
        this.radials = Math.PI / config.getInt("radials");
        this.helices = config.getInt("helices");
        this.maxStep = config.getInt("step-amount");
    }

}
