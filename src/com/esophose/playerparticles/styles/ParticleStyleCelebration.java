package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleCelebration implements ParticleStyle {

    private int step = 0;
    private final int spawnTime = 15;

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        return null;
    }

    /**
     * Spawns fireworks every spawnTime number of ticks
     * This style uses two different effects, one is always 'firework'
     */
    public void updateTimers() {
        step++;
        if (step == spawnTime) {
            step = 0;
            
            final Random random = new Random();
            for (PPlayer pplayer : ParticleManager.getPPlayers()) {
                final Player player = pplayer.getPlayer();
                
                for (ParticlePair particle : pplayer.getActiveParticles()) {
                    if (particle.getStyle() != this) continue;
                    
                    double angle = random.nextDouble() * Math.PI * 2;
                    double distanceFrom = 1.25 + random.nextDouble() * 1.5;
                    double dx = Math.sin(angle) * distanceFrom;
                    double dz = Math.cos(angle) * distanceFrom;
                    final Location loc = player.getLocation().clone().add(dx, 1, dz);
                    final int fuse = 3 + random.nextInt(3);

                    new BukkitRunnable() {
                        private Location location = loc;
                        private int fuseLength = fuse;
                        private int fuseTimer = 0;

                        public void run() {
                            if (this.fuseTimer < this.fuseLength) {
                                ParticlePair trail = ParticlePair.getNextDefault(pplayer);
                                trail.setEffect(ParticleEffect.FIREWORK);
                                trail.setStyle(DefaultStyles.CELEBRATION);
                                
                                ParticleManager.displayParticles(trail, Collections.singletonList(new PParticle(this.location)));
                                
                                this.location.add(0, 0.25, 0);
                            } else {
                                List<PParticle> particles = new ArrayList<PParticle>();
                                for (int i = 0; i < 40; i++) { // Copied directly from PlayerParticles source
                                    double radius = 0.6 + random.nextDouble() * 0.2;
                                    double u = random.nextDouble();
                                    double v = random.nextDouble();
                                    double theta = 2 * Math.PI * u;
                                    double phi = Math.acos(2 * v - 1);
                                    double dx = radius * Math.sin(phi) * Math.cos(theta);
                                    double dy = radius * Math.sin(phi) * Math.sin(theta);
                                    double dz = radius * Math.cos(phi);
                                    
                                    particles.add(new PParticle(this.location.clone().add(dx, dy, dz)));
                                }
                                ParticleManager.displayParticles(particle, particles);
                                
                                this.cancel();
                            }
                            this.fuseTimer++;
                        }
                    }.runTaskTimer(PlayerParticles.getPlugin(), 0, 1);
                }
            }
        }
    }

    public String getName() {
        return "celebration";
    }

    public boolean canBeFixed() {
        return true;
    }

    public boolean canToggleWithMovement() {
        return true;
    }

}
