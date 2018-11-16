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
package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleCompanion implements ParticleStyle {

    private int numParticles = 150;
    private int particlesPerIteration = 5;
    private double size = 1.25;
    private double xFactor = 1.0, yFactor = 1.5, zFactor = 1.0;
    private double xOffset = 0.0, yOffset = -0.75, zOffset = 0.0;
    private int step = 0;

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();

        Vector vector = new Vector();
        
        double t = (Math.PI / numParticles) * step;
        double r = Math.sin(t) * size;
        double s = 2 * Math.PI * t;

        vector.setX(xFactor * r * Math.cos(s) + xOffset);
        vector.setZ(zFactor * r * Math.sin(s) + zOffset);
        vector.setY(yFactor * size * Math.cos(t) + yOffset);

        for (int i = 0; i < particlesPerIteration; i++) {
            particles.add(new PParticle(location.clone().subtract(vector)));
        }

        return particles;
    }

    public void updateTimers() {
        step++;
    }

    public String getName() {
        return "companion";
    }

    public boolean canBeFixed() {
        return true;
    }
    
    public boolean canToggleWithMovement() {
        return false;
    }

}
