package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.FixedParticleEffect;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleStyleCelebration extends DefaultParticleStyle {

    private int step = 0;
    private final int spawnTime = 15;

    public ParticleStyleCelebration() {
        super("celebration", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        return new ArrayList<>();
    }

    /**
     * Spawns fireworks every spawnTime number of ticks
     * This style uses two different effects, one is always 'firework'
     */
    @Override
    public void updateTimers() {
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);

        this.step++;
        if (this.step == this.spawnTime) {
            this.step = 0;
            
            Random random = new Random();
            for (PPlayer pplayer : particleManager.getPPlayers()) {
                Player player = pplayer.getPlayer();
                if (player != null && player.getGameMode() != GameMode.SPECTATOR && permissionManager.isWorldEnabled(player.getWorld().getName()))
                    for (ParticlePair particle : pplayer.getActiveParticles())
                        if (particle.getStyle() == this)
                            this.spawnFirework(player.getLocation(), pplayer, particle, random);
                
                for (FixedParticleEffect fixedEffect : pplayer.getFixedParticles())
                    if (fixedEffect.getParticlePair().getStyle() == this && permissionManager.isWorldEnabled(fixedEffect.getLocation().getWorld().getName()))
                        this.spawnFirework(fixedEffect.getLocation(), pplayer, fixedEffect.getParticlePair(), random);
            }
        }
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }
    
    private void spawnFirework(final Location location, final PPlayer pplayer, final ParticlePair particle, final Random random) {
        double angle = random.nextDouble() * Math.PI * 2;
        double distanceFrom = 1.25 + random.nextDouble() * 1.5;
        double dx = Math.sin(angle) * distanceFrom;
        double dz = Math.cos(angle) * distanceFrom;
        final Location loc = location.clone().add(dx, 1, dz);
        final int fuse = 3 + random.nextInt(3);
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);

        new BukkitRunnable() {
            private Location location = loc;
            private int fuseLength = fuse;
            private int fuseTimer = 0;

            public void run() {
                if (this.fuseTimer < this.fuseLength) {
                    ParticlePair trail = ParticlePair.getNextDefault(pplayer);
                    trail.setEffect(ParticleEffect.FIREWORK);
                    trail.setStyle(DefaultStyles.CELEBRATION);

                    particleManager.displayParticles(trail, Collections.singletonList(new PParticle(this.location)));
                    
                    this.location.add(0, 0.25, 0);
                } else {
                    List<PParticle> particles = new ArrayList<>();
                    for (int i = 0; i < 40; i++) {
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
                    particleManager.displayParticles(particle, particles);
                    
                    this.cancel();
                }
                this.fuseTimer++;
            }
        }.runTaskTimer(PlayerParticles.getInstance(), 0, 1);
    }

}
