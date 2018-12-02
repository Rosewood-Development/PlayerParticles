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

public class ParticleStyleVortex implements ParticleStyle {
    
    private double grow = .05f;
    private double radials = Math.PI / 16;
    private int helices = 4;
    private int step = 0;
    private int maxStep = 70;

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();

        double radius = 2 * (1 - (double)step / maxStep);
        for (int i = 0; i < helices; i++) {
            double angle = step * radials + (2 * Math.PI * i / helices);
            Vector v = new Vector(Math.cos(angle) * radius, step * grow - 1, Math.sin(angle) * radius);

            particles.add(new PParticle(location.clone().add(v)));
        }
        
        return particles;
    }

    public void updateTimers() {
        step = (step + 1) % maxStep;
    }

    public String getName() {
        return "vortex";
    }

    public boolean canBeFixed() {
        return true;
    }
    
    public boolean canToggleWithMovement() {
        return true;
    }
    
    public double getFixedEffectOffset() {
        return 0.5;
    }

}
