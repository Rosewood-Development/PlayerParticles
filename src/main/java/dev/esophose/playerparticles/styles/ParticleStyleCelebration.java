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
import dev.esophose.playerparticles.util.MathL;
import dev.esophose.playerparticles.util.NMSUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleStyleCelebration extends DefaultParticleStyle {

    private int step = 0;

    private int spawnFrequency;
    private int burstAmount;
    private double baseBurstSize;
    private double burstSizeRandomizer;
    private int baseFuseLength;
    private int fuseLengthRandomizer;
    private double baseDistanceFrom;
    private double distanceFromRandomizer;
    private double fuseSpacing;
    private ParticleEffect fuseEffect;

    protected ParticleStyleCelebration() {
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
        if (this.step == this.spawnFrequency) {
            this.step = 0;
            
            Random random = new Random();
            for (PPlayer pplayer : particleManager.getPPlayers()) {
                Player player = pplayer.getPlayer();
                if (player != null && (NMSUtil.getVersionNumber() < 8 || player.getGameMode() != GameMode.SPECTATOR) && permissionManager.isWorldEnabled(player.getWorld().getName()))
                    for (ParticlePair particle : pplayer.getActiveParticles())
                        if (particle.getStyle() == this)
                            this.spawnFirework(player.getLocation(), pplayer, pplayer.getPlayer(), particle, random);
                
                for (FixedParticleEffect fixedEffect : pplayer.getFixedParticles())
                    if (fixedEffect.getParticlePair().getStyle() == this && permissionManager.isWorldEnabled(fixedEffect.getLocation().getWorld().getName()))
                        this.spawnFirework(fixedEffect.getLocation(), pplayer, null, fixedEffect.getParticlePair(), random);
            }
        }
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("FIREWORK_ROCKET", "FIREWORK");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("spawn-frequency", 15, "How many ticks to wait between spawns");
        this.setIfNotExists("burst-amount", 40, "How many particles to spawn per burst");
        this.setIfNotExists("base-burst-size", 0.6, "The minimum size of the particle burst");
        this.setIfNotExists("burst-size-randomizer", 0.2, "The maximum size to add to the base of the particle burst");
        this.setIfNotExists("base-fuse-length", 4, "The minimum fuse length");
        this.setIfNotExists("fuse-length-randomizer", 3, "The max length to add to the base of the fuse");
        this.setIfNotExists("base-distance-from", 1.25, "The minimum distance to spawn from the player");
        this.setIfNotExists("distance-from-randomizer", 1.5, "The max distance to add to the base of the distance");
        this.setIfNotExists("fuse-spacing", 0.25, "The vertical distance between fuse particles");
        this.setIfNotExists("fuse-effect", "firework", "The effect type to use for the fuse particles");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.spawnFrequency = config.getInt("spawn-frequency");
        this.burstAmount = config.getInt("burst-amount");
        this.baseBurstSize = config.getDouble("base-burst-size");
        this.burstSizeRandomizer = config.getDouble("burst-size-randomizer");
        this.baseFuseLength = config.getInt("base-fuse-length");
        this.fuseLengthRandomizer = config.getInt("fuse-length-randomizer");
        this.baseDistanceFrom = config.getDouble("base-distance-from");
        this.distanceFromRandomizer = config.getDouble("distance-from-randomizer");
        this.fuseSpacing = config.getDouble("fuse-spacing");
        this.fuseEffect = ParticleEffect.fromInternalName(config.getString("fuse-effect"));

        if (this.fuseEffect == null)
            this.fuseEffect = ParticleEffect.FIREWORK;
    }
    
    private void spawnFirework(final Location location, final PPlayer pplayer, final Player player, final ParticlePair particle, final Random random) {
        double angle = random.nextDouble() * Math.PI * 2;
        double distanceFrom = this.baseDistanceFrom + random.nextDouble() * this.distanceFromRandomizer;
        double dx = MathL.sin(angle) * distanceFrom;
        double dz = MathL.cos(angle) * distanceFrom;
        final Location loc = location.clone().add(dx, 1, dz);
        final int fuse = this.baseFuseLength + random.nextInt(this.fuseLengthRandomizer);
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);

        new BukkitRunnable() {
            private Location location = loc;
            private int fuseLength = fuse;
            private int fuseTimer = 0;

            public void run() {
                if (this.fuseTimer < this.fuseLength) {
                    ParticlePair trail = ParticlePair.getNextDefault(pplayer);
                    trail.setEffect(ParticleStyleCelebration.this.fuseEffect);
                    trail.setStyle(DefaultStyles.CELEBRATION);

                    particleManager.displayParticles(pplayer, this.location.getWorld(), trail, Collections.singletonList(new PParticle(this.location)), true);
                    
                    this.location.add(0, ParticleStyleCelebration.this.fuseSpacing, 0);
                } else {
                    List<PParticle> particles = new ArrayList<>();
                    for (int i = 0; i < ParticleStyleCelebration.this.burstAmount; i++) {
                        double radius = ParticleStyleCelebration.this.baseBurstSize + random.nextDouble() * ParticleStyleCelebration.this.burstSizeRandomizer;
                        double u = random.nextDouble();
                        double v = random.nextDouble();
                        double theta = 2 * Math.PI * u;
                        double phi = Math.acos(2 * v - 1);
                        double dx = radius * MathL.sin(phi) * MathL.cos(theta);
                        double dy = radius * MathL.sin(phi) * MathL.sin(theta);
                        double dz = radius * MathL.cos(phi);
                        
                        particles.add(new PParticle(this.location.clone().add(dx, dy, dz)));
                    }
                    particleManager.displayParticles(pplayer, this.location.getWorld(), particle, particles, true);
                    
                    this.cancel();
                }
                this.fuseTimer++;
            }
        }.runTaskTimer(PlayerParticles.getInstance(), 0, 1);
    }

}
