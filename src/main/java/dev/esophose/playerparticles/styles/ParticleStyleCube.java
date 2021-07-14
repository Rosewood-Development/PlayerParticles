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
import dev.esophose.playerparticles.util.VectorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Credit goes to Slikey who made all this logic for drawing a cube out of particles
 * The project this is from is called EffectLib and can be found here:
 * https://github.com/Slikey/EffectLib
 */
public class ParticleStyleCube extends DefaultParticleStyle {

    private int step = 0;
    private boolean skipNextStep = false; // Only spawn every 2 ticks

    private double edgeLength;
    private double angularVelocityX;
    private double angularVelocityY;
    private double angularVelocityZ;
    private int particlesPerEdge;

    protected ParticleStyleCube() {
        super("cube", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> pparticles = new ArrayList<>();

        if (this.skipNextStep)
            return pparticles;

        double xRotation = this.step * this.angularVelocityX;
        double yRotation = this.step * this.angularVelocityY;
        double zRotation = this.step * this.angularVelocityZ;
        double a = this.edgeLength / 2;
        double angleX, angleY;
        Vector v = new Vector();
        for (int i = 0; i < 4; i++) {
            angleY = i * Math.PI / 2;
            for (int j = 0; j < 2; j++) {
                angleX = j * Math.PI;
                for (int p = 0; p <= this.particlesPerEdge; p++) {
                    v.setX(a).setY(a);
                    v.setZ(this.edgeLength * p / this.particlesPerEdge - a);
                    VectorUtils.rotateAroundAxisX(v, angleX);
                    VectorUtils.rotateAroundAxisY(v, angleY);
                    VectorUtils.rotateVector(v, xRotation, yRotation, zRotation);
                    pparticles.add(new PParticle(location.clone().add(v)));
                }
            }
            for (int p = 0; p <= this.particlesPerEdge; p++) {
                v.setX(a).setZ(a);
                v.setY(this.edgeLength * p / this.particlesPerEdge - a);
                VectorUtils.rotateAroundAxisY(v, angleY);
                VectorUtils.rotateVector(v, xRotation, yRotation, zRotation);
                pparticles.add(new PParticle(location.clone().add(v)));
            }
        }

        return pparticles;
    }

    @Override
    public void updateTimers() {
        this.skipNextStep = !this.skipNextStep;
        this.step++;
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("STONE");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("edge-length", 2.0, "The length (in blocks) of the edges of the cube");
        this.setIfNotExists("angular-velocity-x", 0.00314159265, "The angular velocity on the x-axis");
        this.setIfNotExists("angular-velocity-y", 0.00369599135, "The angular velocity on the y-axis");
        this.setIfNotExists("angular-velocity-z", 0.00405366794, "The angular velocity on the z-axis");
        this.setIfNotExists("particles-per-edge", 7, "The number of particles to spawn per edge of the cube");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.edgeLength = config.getDouble("edge-length");
        this.angularVelocityX = config.getDouble("angular-velocity-x");
        this.angularVelocityY = config.getDouble("angular-velocity-y");
        this.angularVelocityZ = config.getDouble("angular-velocity-z");
        this.particlesPerEdge = config.getInt("particles-per-edge");
    }

}
