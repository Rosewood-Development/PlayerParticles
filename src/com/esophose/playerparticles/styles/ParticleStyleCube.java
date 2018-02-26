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

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.util.VectorUtils;

/**
 * Credit goes to Slikey who made all this logic for drawing a cube out of particles
 * The project this is from is called EffectLib and can be found here:
 * https://github.com/Slikey/EffectLib
 */
public class ParticleStyleCube implements ParticleStyle {

    private float edgeLength = 2;
    private double angularVelocityX = (Math.PI / 200) / 5;
    private double angularVelocityY = (Math.PI / 170) / 5;
    private double angularVelocityZ = (Math.PI / 155) / 5;
    private int particles = 7;
    private int step = 0;
    private boolean skipNextStep = false; // Only spawn every 2 ticks

    public PParticle[] getParticles(PPlayer pplayer, Location location) {
        List<PParticle> pparticles = new ArrayList<PParticle>();

        if (!skipNextStep) {
            double xRotation = 0, yRotation = 0, zRotation = 0;
            xRotation = step * angularVelocityX;
            yRotation = step * angularVelocityY;
            zRotation = step * angularVelocityZ;
            float a = edgeLength / 2;
            double angleX, angleY;
            Vector v = new Vector();
            for (int i = 0; i < 4; i++) {
                angleY = i * Math.PI / 2;
                for (int j = 0; j < 2; j++) {
                    angleX = j * Math.PI;
                    for (int p = 0; p <= particles; p++) {
                        v.setX(a).setY(a);
                        v.setZ(edgeLength * p / particles - a);
                        VectorUtils.rotateAroundAxisX(v, angleX);
                        VectorUtils.rotateAroundAxisY(v, angleY);
                        VectorUtils.rotateVector(v, xRotation, yRotation, zRotation);
                        pparticles.add(new PParticle(location.clone().add(v)));
                    }
                }
                for (int p = 0; p <= particles; p++) {
                    v.setX(a).setZ(a);
                    v.setY(edgeLength * p / particles - a);
                    VectorUtils.rotateAroundAxisY(v, angleY);
                    VectorUtils.rotateVector(v, xRotation, yRotation, zRotation);
                    pparticles.add(new PParticle(location.clone().add(v)));
                }
            }
        }

        return pparticles.toArray(new PParticle[pparticles.size()]);
    }

    public void updateTimers() {
        skipNextStep = !skipNextStep;
        step++;
    }

    public String getName() {
        return "cube";
    }

    public boolean canBeFixed() {
        return true;
    }

}
