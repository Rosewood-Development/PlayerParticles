/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Slikey
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
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

public class ParticleStyleCompanion extends DefaultParticleStyle {

    private int step = 0;

    private int numParticles;
    private int particlesPerIteration;
    private double size;
    private double xFactor, yFactor, zFactor;
    private double xOffset, yOffset, zOffset;

    protected ParticleStyleCompanion() {
        super("companion", true, false, 1);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        Vector vector = new Vector();
        
        double t = (Math.PI / this.numParticles) * this.step;
        double r = MathL.sin(t) * this.size;
        double s = 2 * Math.PI * t;

        vector.setX(this.xFactor * r * MathL.cos(s) + this.xOffset);
        vector.setZ(this.zFactor * r * MathL.sin(s) + this.zOffset);
        vector.setY(this.yFactor * this.size * MathL.cos(t) + this.yOffset);

        for (int i = 0; i < this.particlesPerIteration; i++)
            particles.add(new PParticle(location.clone().subtract(vector)));

        return particles;
    }

    @Override
    public void updateTimers() {
        this.step++;
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("NAME_TAG");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("particle-amount", 150, "The number of total particles in the animation cycle");
        this.setIfNotExists("particles-per-iteration", 5, "The number of particles spawned per iteration");
        this.setIfNotExists("size", 1.25, "The size of the animation");
        this.setIfNotExists("x-factor", 1.0, "The multiplier for the x-axis");
        this.setIfNotExists("y-factor", 1.5, "The multiplier for the y-axis");
        this.setIfNotExists("z-factor", 1.0, "The multiplier for the z-axis");
        this.setIfNotExists("x-offset", 0.0, "The offset for the x-axis");
        this.setIfNotExists("y-offset", -0.75, "The offset for the y-axis");
        this.setIfNotExists("z-offset", 0.0, "The offset for the x-axis");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.numParticles = config.getInt("particle-amount");
        this.particlesPerIteration = config.getInt("particles-per-iteration");
        this.size = config.getDouble("size");
        this.xFactor = config.getDouble("x-factor");
        this.yFactor = config.getDouble("y-factor");
        this.zFactor = config.getDouble("z-factor");
        this.xOffset = config.getDouble("x-offset");
        this.yOffset = config.getDouble("y-offset");
        this.zOffset = config.getDouble("z-offset");
    }

}
